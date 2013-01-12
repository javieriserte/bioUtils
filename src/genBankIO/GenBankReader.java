package genBankIO;

import java.io.BufferedReader;
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

public class GenBankReader {
	
	
	
	public GenBankReader() {
		super();
		// TODO Auto-generated constructor stub
	}

	public GenBankRecord readGenBank(BufferedReader in) {
		
		StringBuilder headerpart = new StringBuilder();
		StringBuilder featurespart = new StringBuilder();
		StringBuilder originpart = new StringBuilder();
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
							headerpart.append(currentline.toUpperCase().trim());
							headerpart.append("\n");
						} else {
							if (currentline.toUpperCase().trim() != "") {
								// TODO raise an exception 
							}
						}
				
					}
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} ;
		
		System.out.println(headerpart.toString());
		GenBankHeader gbh = new GenBankHeader();
		List<Feature> feat = new ArrayList<Feature>();
		Origin ori = new Origin();
		
		gbh = (new GenBankHeaderParser()).parse(headerpart.toString());
		feat = (new FeaturesParser()).parse(featurespart.toString());
		ori = (new OriginParser()).parse(originpart.toString());
		
		return new GenBankRecord(gbh, feat, ori);
		
	}
	
	public static void main(String[] args) {
		GenBankReader gbr = new GenBankReader();
		GenBankRecord gbrd = null;
		
		try {
			gbrd = gbr.readGenBank(new BufferedReader(new FileReader("test.gb")));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		List<Feature> cds = new Vector<Feature>();
		
		for (Feature f : gbrd.getFeatures()) {
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
