package fileformats.genBankIO;

//import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
//import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import fileformats.genBankIO.elements.Feature;
import fileformats.genBankIO.elements.GenBankHeader;
import fileformats.genBankIO.elements.Origin;
import fileformats.genBankIO.parsers.FeaturesParser;
import fileformats.genBankIO.parsers.GenBankHeaderParser;
import fileformats.genBankIO.parsers.OriginParser;

final public class GenBankReaderAsync  implements Runnable{

    private ObjectOutputStream outputStream = null;
    
	private PipedInputStream pipe;
	
	private ObjectInputStream inputstream;
	
	private BufferedReader source;
	
	private Queue<Exception> exceptions; 
	
	
	///////////////////////
	// Constructor
	
	/**
	 * Creates a new GenBankReaderAsync.
	 * Reads a source containing one or more GenBank records.
	 * 
	 * The input is a buffered reader for two reasons:
	 * <ol>
	 * <li>BufferedReader can read complete lines of text.</li>
	 * <li>BufferedReader can be on top of FileReaders, InputStreamReaders, Standard Input and other stuffs.</li>
	 * </ol>
	 * @param gbSource a <code>BufferedReader</code> with the GenBank data.
	 */
	public GenBankReaderAsync(BufferedReader gbSource) throws IOException {
		
		this.source = new BufferedReader(gbSource);
		
		this.pipe = new PipedInputStream(2048);

        this.outputStream = new ObjectOutputStream(new PipedOutputStream(pipe));
        
		this.inputstream = new ObjectInputStream(pipe);

		this.exceptions = new PriorityQueue<Exception>();
		
		Thread readingThread = new Thread(this);

		readingThread.start();
		
	}
	
	// Public Interface
	/**
	 * <br>
	 * 
	 * @param filein is the input file.
	 * @throws Exception 
	 * @throws FileNotFoundException if the file doesn't exists.
	 * @throws GenBankFormatException if the GenBank record is not well formed.
	 */
//	public List<GenBankRecord> readFile(File filein) throws GenBankFormatException, FileNotFoundException {
//		return this.readGenBank(new BufferedReader(new FileReader(filein)));
//	}
	

	public GenBankRecord readGenBankRecord() throws Exception {
	
		GenBankRecord result = null;
    	
    	try {

    		result = (GenBankRecord) inputstream.readObject();

    		
		} catch (EOFException e) {
			
			result = null;
			
		} catch (Exception e1) {
			
			System.err.println(e1.getMessage());
			
		}
	
		while(!this.exceptions.isEmpty()) {
			
			throw this.exceptions.poll();
			
		}
		
    	return result;
    	
	}


	/**
	 * Parses asynchronously genbank data. Multiple adjacents records can be used as input.
	 * If there is a format error in one of the GenBank records, the previous to the 
	 * error could be parsed and returned with some types of errors. The others will no be parsed.<br>
	 * 
	 * This method is used by Thread Class and shouldn't be called anywhere else. 
	 * Use readGenBankRecord instead.
	 */
	@Override
	public void run() {
		
		StringBuilder headerpart = null;
		StringBuilder featurespart = null;
		StringBuilder originpart = null;
		int partCounter = 0;

		try {
			while(this.source.ready()) {
				
				String currentline = this.source.readLine();
				
				switch (partCounter) {
					case 3: {
					// Reading origin
						if (currentline.toUpperCase().trim().startsWith("//")) {
							// Change to part zero
							partCounter=0;
							
							GenBankHeader gbh = new GenBankHeader();
							List<Feature> feat = new ArrayList<Feature>();
							Origin ori = new Origin();
							
							gbh = (new GenBankHeaderParser()).parse(headerpart.toString());
							feat = (new FeaturesParser()).parse(featurespart.toString());
							ori = (new OriginParser()).parse(originpart.toString());

							outputStream.writeObject(new GenBankRecord(gbh, feat, ori));
							
							outputStream.flush();
							
						} else {
							if (currentline.toUpperCase().trim() != "") {
								originpart.append(currentline);
								originpart.append("\n");
							}
						}

					}
					break;
					case 2: {
					// reading features
						if (currentline.toUpperCase().trim().startsWith("ORIGIN")) {
							// Change to part three
							partCounter=3;
						} else {
							if (currentline.toUpperCase().trim() != "") {
								featurespart.append(currentline);
								featurespart.append("\n");
							}
						}
					}
					break;
					case 1: {
					// Reading header
						if (currentline.toUpperCase().trim().startsWith("FEATURES")) {
							// Change to part two
							partCounter=2;
							featurespart.append(currentline.toUpperCase().trim());
							featurespart.append("\n");
						} else {
							if (currentline.toUpperCase().trim() != "") {
								headerpart.append(currentline);
								headerpart.append("\n");
							}
						}
						
					}
					break;
					case 0: {
					// before start
						
						if (currentline.toUpperCase().trim().startsWith("LOCUS")) {
							// Change to part one
							partCounter = 1;
							
							headerpart = new StringBuilder();
							featurespart = new StringBuilder();
							originpart = new StringBuilder();
							
							headerpart.append(currentline.toUpperCase().trim());
							headerpart.append("\n");
						} else {
							if (!currentline.toUpperCase().trim().equals("") ){
								throw(new GenBankFormatException("Error Reading: " + currentline + "\nThe KeyWord 'LOCUS' was expected"));
							}
						}
				
					}
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (GenBankFormatException e) {
			
            System.err.println("Error Parsing GenBank Record. " + e.getMessage());
            
		} finally {

			try {
				outputStream.flush();
				
				outputStream.close();
				
			} catch (IOException e) {

				System.err.println("There was an error closing the output stream" + e.getMessage());
				
			}
			
		}
		
		if (partCounter != 0) {
			
			this.exceptions.add(new GenBankFormatException("An Incomplete GenBank Record Was Found."));
			
		}
		
	}
	
	// Main Executable Example
	/**
	 * Example of use of the GenBankIO package.
	 */
	public static void main(String[] args) {
		
		GenBankReaderAsync gbrd = null;
		String fileName = "test.gb";

		try {
			gbrd = new GenBankReaderAsync(new BufferedReader(new FileReader(fileName)));
		} catch (FileNotFoundException e) {

			System.err.println("The file " + fileName + " was not found.");

			System.err.println("Exit.");

			System.exit(1);

		} catch (IOException e) {
			
			System.err.println("There was an error reading file " + fileName + ".");

			System.err.println("Exit.");

			System.exit(1);
			
		}
		
		GenBankRecord record;
		int counter = 0;
		try {
			while ( ( record = gbrd.readGenBankRecord()) != null) {
				
				counter++;
				
				System.out.println(counter + ": " + record.getHeader().getDefinition());
				
				System.out.println(counter + ": " + record.getOrigin().getSequence().substring(0,100));			
				
			}
		} catch (Exception e) {
			
			System.err.println(e.getMessage());
			
		}
		
	}



	
}
