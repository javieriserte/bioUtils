package fileformats.genBankIO;

//import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
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
//import java.util.Vector;

import fileformats.genBankIO.elements.Feature;
import fileformats.genBankIO.elements.GenBankHeader;
import fileformats.genBankIO.elements.Origin;
import fileformats.genBankIO.parsers.FeaturesParser;
import fileformats.genBankIO.parsers.GenBankHeaderParser;
import fileformats.genBankIO.parsers.OriginParser;

final public class GenBankReader {

	
	
	// Public Interface
	/**
	 * Reads a file containing one or more GenBank records.<br>
	 * 
	 * @param filein is the input file.
	 * @throws FileNotFoundException if the file doesn't exists.
	 * @throws GenBankFormatException if the GenBank record is not well formed.
	 */
	static public List<GenBankRecord> readFile(File filein) throws GenBankFormatException, FileNotFoundException {
		return GenBankReader.readGenBank(new BufferedReader(new FileReader(filein)));
	}
	
	
	// Private Methods
	/**
	 * Parses genbank data. Multiple adjacents records can be used as input.
	 * If there is a format error in one of the GenBank records, the previous to the 
	 * error could be parsed and returned with some types of errors. The others will no be parsed.<br>
	 * 
	 * The input is a buffered reader for two reasons:
	 * <ol>
	 * <li>BufferedReader can read complete lines of text.</li>
	 * <li>BufferedReader can be on top of FileReaders, InputStreamReaders, Standard Input and other stuffs.</li>
	 * </ol>
	 * @param in a <code>BufferedReader</code> with the GenBank data.
	 * @return a <code>List&lt;GenBankRecord></code>.
	 */
	static private List<GenBankRecord> readGenBank(BufferedReader in) throws GenBankFormatException {

		@SuppressWarnings({ "unused", "resource" })
		PipedOutputStream po = new PipedOutputStream();
		
		PipedInputStream pi = new PipedInputStream();
		
		
		
		try {
			@SuppressWarnings({ "unused", "resource" })
			ObjectInputStream a = new ObjectInputStream(pi);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		List<GenBankRecord> result = new ArrayList<GenBankRecord>();
		StringBuilder headerpart = null;
		StringBuilder featurespart = null;
		StringBuilder originpart = null;
		int partCounter = 0;

		try {
			while(in.ready()) {
				
				String currentline = in.readLine();
				
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

							result.add(new GenBankRecord(gbh, feat, ori));
							
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
		}
		
		if (partCounter != 0) {
			throw (new GenBankFormatException("An Incomplete GenBank Record Was Found."));
		}
		
		return result;
		
	}
	
	@SuppressWarnings("resource")
	static public ObjectInputStream getObjectInputStream(final BufferedReader in) {
		
		try {
			
			final PipedOutputStream out = new PipedOutputStream();
			

			final PipedInputStream inp = new PipedInputStream(out);

			
			
			

			Thread ti = new Thread(new Runnable() {
				
				@Override
				public void run() {
					ObjectInputStream ois = null;
					try {
						ois = new ObjectInputStream(inp);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					};
					

					GenBankRecord readObject;
					try {
						while( (readObject = (GenBankRecord) ois.readObject()) != null){
							System.out.println(readObject);
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
						
					} catch (IOException e) {
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
			});

			
			
			Thread to = new Thread(new Runnable() {
				
				@Override
				public void run() {
					
					try {
						GenBankReader.bufferedReadGenBank(in, out);
					} catch (GenBankFormatException e) {
						e.printStackTrace();
					}
					
				}
			});
			

			System.out.println("beforeRun");

			to.start();
			
			ti.start();
			

			
			
			System.out.println("afterRun");
			
			return null;			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return null;
		
	}
	
	static private void bufferedReadGenBank(BufferedReader in, PipedOutputStream po) throws GenBankFormatException {

		ObjectOutputStream oout = null;
		
		try {
			oout = new ObjectOutputStream(po);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		StringBuilder headerpart = null;
		StringBuilder featurespart = null;
		StringBuilder originpart = null;
		int partCounter = 0;

		try {
			while(in.ready()) {
				
				String currentline = in.readLine();
				
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

//							result.add(new GenBankRecord(gbh, feat, ori));
							
							oout.writeObject(new GenBankRecord(gbh, feat, ori));
							Thread.sleep(1000);
							
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
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (partCounter != 0) {
			throw (new GenBankFormatException("An Incomplete GenBank Record Was Found."));
		}
		
		
		try {
			po.close();
			oout.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
	}
	
	// Main Executable Example
	/**
	 * Example of use of the GenBankIO package.
	 */
	public static void main(String[] args) {
//		List<GenBankRecord> gbrd = null;
//		
//		try {
//			gbrd = GenBankReader.readGenBank(new BufferedReader(new FileReader("test.gb")));
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (GenBankFormatException e) {
//			
//		}
//		
//		
//		for (GenBankRecord record : gbrd) {		
//			List<Feature> cds = new Vector<Feature>();
//		
//			for (Feature f : record.getFeatures()) {
//				if (f.name().toUpperCase().equals("CDS")) {
//					cds.add(f);
//				}
//			}
//		
//			for(Feature f : cds) {
//				System.out.println(f.getQualifierNames());
//				if (f.getQualifierNames().contains("TRANSLATION")) {
//					System.out.println(f.getQualifierValue("TRANSLATION"));
//				}
//			}
//		}
		
		ObjectInputStream gbrd = null;
		
		try {
			
			BufferedReader in = new BufferedReader(new FileReader("test.gb"));
			gbrd = GenBankReader.getObjectInputStream(in);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 

		GenBankRecord gbr;
		
		try {
			while ( (gbr = ((GenBankRecord) gbrd.readObject())) != null) {
				System.out.println(gbr.getOrigin());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		
		
	}
	
}
