package genBankIO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import genBankIO.elements.Feature;
import genBankIO.elements.GenBankHeader;
import genBankIO.elements.GenBankRecord;
import genBankIO.elements.Origin;

final public class GenBankReader {

	
	
	// Public Interface
	/**
	 * Reads de file containing one or more GenBank records.<br>
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
	 * Parses genbank data.
	 * 
	 * The input is a buffered reader for two reasons:
	 * 	1 - BufferedReader can be read complete lines of text.
	 *  2 - BufferedReader can be on top of FileReaders, InputStreamReaders, Standard Input and other stuffs.
	 * @param in
	 * @return
	 */
	static private List<GenBankRecord> readGenBank(BufferedReader in) throws GenBankFormatException {
		
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
							if (currentline.toUpperCase().trim() != "") {
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
	
	// Main Executable Example
	public static void main(String[] args) {
		List<GenBankRecord> gbrd = null;
		
		try {
			gbrd = GenBankReader.readGenBank(new BufferedReader(new FileReader("test.gb")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (GenBankFormatException e) {
			
		}
		
		
		for (GenBankRecord record : gbrd) {		
			List<Feature> cds = new Vector<Feature>();
		
			for (Feature f : record.getFeatures()) {
				if (f.name().toUpperCase().equals("CDS")) {
					cds.add(f);
				}
			}
		
			for(Feature f : cds) {
				System.out.println(f.getQualifierNames());
				if (f.getQualifierNames().contains("TRANSLATION")) {
					System.out.println(f.getQualifierValue("TRANSLATION"));
				}
			}
		}
		
	}
	
}
