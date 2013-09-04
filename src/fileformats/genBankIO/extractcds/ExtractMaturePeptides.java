package fileformats.genBankIO.extractcds;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import cmdGA2.CommandLine;
import cmdGA2.OptionsFactory;
import cmdGA2.SingleArgumentOption;
import fileformats.genBankIO.GenBankReaderAsync;
import fileformats.genBankIO.GenBankRecord;
import fileformats.genBankIO.elements.Feature;

public class ExtractMaturePeptides {

	public static void main(String[] args) {
		
		//////////////////////////////
		// Create commandline
		CommandLine cmdline = new CommandLine();
		
		/////////////////////////////
		// Add commandLine options
		SingleArgumentOption<InputStream> inOpt = OptionsFactory.createBasicInputStreamArgument(cmdline);
		
		SingleArgumentOption<PrintStream> outOpt = OptionsFactory.createBasicPrintStreamArgument(cmdline);
		
		////////////////////////////
		// Parse Commandline
		cmdline.readAndExitOnError(args);
		
		//////////////////////////////
		// Get values from command line
		InputStream in = inOpt.getValue();
		
		PrintStream out = outOpt.getValue();

		///////////////////////////////
		// Read GenBank Records from 
		// File
		BufferedReader br = new BufferedReader(new InputStreamReader(in));

		try {
			
			GenBankReaderAsync gbr = new GenBankReaderAsync(br);
			
			GenBankRecord record = null;
			
			while((record = gbr.readGenBankRecord())!= null) {

				List<Feature> features = record.getFeatures();
				
				List<String> mat_peptide_features = new ArrayList<>();
				
				
				/////////////////////////////////////////
				// Checks if features is a Mature Peptide 
				// description
				for (Feature feature : features) {
					
					if (feature.name().equalsIgnoreCase("mat_peptide")) {
						
						mat_peptide_features.add( feature.getQualifierValue("product") + "\t" + feature.getRegion());
						
					}
					
				}
				
				/////////////////////////////////////////
				// Generate Output
				
				StringBuilder sb = new StringBuilder();
				
				sb.append(record.getHeader().getGi());
				
				for (String string : mat_peptide_features) {

					sb.append("\t" + string);
					
				}
				
				out.println(sb.toString()); 
				
			}
			
		} catch (Exception e) {
			System.err.println("There was an error: "+ e.getMessage());
			
			System.exit(1);
		}
		
		
		
		
	}
	
}
