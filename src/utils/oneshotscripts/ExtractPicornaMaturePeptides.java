package utils.oneshotscripts;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import cmdGA.Parser;
import cmdGA.SingleOption;
import cmdGA.exceptions.IncorrectParameterTypeException;
import cmdGA.parameterType.InputStreamParameter;

import fileformats.genBankIO.GenBankReaderAsync;
import fileformats.genBankIO.GenBankRecord;
import fileformats.genBankIO.elements.Feature;

public class ExtractPicornaMaturePeptides {
	
	public static void main(String[] args) throws Exception {
		
		Parser parser =  new Parser();
		
		SingleOption listOpt = new SingleOption(parser, null, "-in", InputStreamParameter.getParameter());
		
		try {
			
			parser.parseEx(args);
			
		} catch (IncorrectParameterTypeException e) {
			
			System.err.println("There was an error parsing "+ args);
			
			System.err.println(e.getMessage());
			
		}
		
		if (listOpt.isPresent()) {
			
			// First 
			// printPeptides(listOpt);
			
			// Second
			keepGroupOsElevenPeptides(listOpt);
					
		}
		
	}
	

	private static void keepGroupOsElevenPeptides(SingleOption listOpt)
			throws IOException, Exception {
		@SuppressWarnings("unused")
		ExtractPicornaMaturePeptides ex = new ExtractPicornaMaturePeptides();
		
		InputStream in = (InputStream) listOpt.getValue();
		
		BufferedReader bf = new BufferedReader(new InputStreamReader(in));

		GenBankReaderAsync gbreader = new GenBankReaderAsync(bf);
		
		GenBankRecord record ;

		while ((record = gbreader.readGenBankRecord())!=null) {
			
			List<Feature> features = record.getFeatures();

			String sequence = record.getOrigin().getSequence();
			
			int counter = 0;
			
			List<Feature> group = new ArrayList<Feature>();
			
			for (Feature feature : features) {
				
				if (feature.name().equals("MAT_PEPTIDE")) {
					
					String value = feature.getRegion().trim();
					
					if(!value.startsWith("join")) {
						
						group.add(feature);
						
					}
					
				}
				
			}
			
			if (group.size()==11) {
				
				StringBuilder sb = new StringBuilder();
				
				sb.append(record.getHeader().getGi());
				
				for (Feature feature : group) {

				String[] data = feature.getRegion().trim().split("[^0-9]+");
				
				String seq = sequence.substring(Integer.valueOf(data[0])-1,Integer.valueOf(data[1]));
			
				counter++;
			
				System.out.println(">"+record.getHeader().getGi()+"|ORF" + counter);
			
				System.out.println(seq.toUpperCase());
				
				}
				
			}
			
		}
		
	}

	@SuppressWarnings("unused")
	private static void printPeptides(SingleOption listOpt)
			throws IOException, Exception {
		ExtractPicornaMaturePeptides ex = new ExtractPicornaMaturePeptides();
		
		InputStream in = (InputStream) listOpt.getValue();
		
		BufferedReader bf = new BufferedReader(new InputStreamReader(in));

		GenBankReaderAsync gbreader = new GenBankReaderAsync(bf);
		
		GenBankRecord record ;

		while ((record = gbreader.readGenBankRecord())!=null) {
			
//				if (record.getHeader().getGi().equals("21363123")) {
//				
//					@SuppressWarnings("unused")
//					int a = 1;
//				
//				}
		
			List<Feature> features = record.getFeatures();

			String sequence = record.getOrigin().getSequence();
			
			int counter = 0;
			
			for (Feature feature : features) {
				
				if (feature.name().equals("MAT_PEPTIDE")) {
					
					String value = feature.getRegion().trim();
					
					if(!value.startsWith("join")) {
						
//							String[] data = value.split("\\.\\.");

						String[] data = value.split("[^0-9]+");
//							System.out.println(value);
						String seq = sequence.substring(Integer.valueOf(data[0])-1,Integer.valueOf(data[1]));
						
						counter++;
						
						System.out.println(">"+record.getHeader().getGi()+"|ORF" + counter);
						
						System.out.println(seq.toUpperCase());
						
					}
					
				}
				
			}
			
		}
		
	}
	
}