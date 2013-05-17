package fileformats.genBankIO.extractcds;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import cmdGA.Parser;
import cmdGA.SingleOption;
import cmdGA.exceptions.IncorrectParameterTypeException;
import cmdGA.parameterType.InFileParameter;

import fileformats.genBankIO.GenBankReaderAsync;
import fileformats.genBankIO.GenBankRecord;
import fileformats.genBankIO.elements.Feature;

public class ExtractCDS {
	
	public static void main(String[] args) {
		
		Parser parser =  new Parser();
		
		SingleOption listOpt = new SingleOption(parser, null, "-list", InFileParameter.getParameter());
		
		try {
			
			parser.parseEx(args);
			
		} catch (IncorrectParameterTypeException e) {
			
			System.err.println("There was an error parsing "+ args);
			
			System.err.println(e.getMessage());
			
		}
		
		if (listOpt.isPresent()) {
			
			ExtractCDS ex = new ExtractCDS();
			
			File in = (File) listOpt.getValue();
			
			BufferedReader bf;
			
			try {
				bf = new BufferedReader(new FileReader(in));
				
				String line = null;
				
				while ((line=bf.readLine())!=null) {
					
					String[] fields = line.split("\t");
					
					File file = new File(fields[0]);
					
					if (file.exists()) {
						
						BufferedReader bff = new BufferedReader(new FileReader(file));
					
						GenBankReaderAsync gbreader = new GenBankReaderAsync(bff);
						
						GenBankRecord r = null;
						
						while ((r=gbreader.readGenBankRecord())!=null) {
							
							List<String> aas = ex.getCDS(r);
							
							int counter=0;
							
							for (String string : aas) {
								
								counter++;
								
								ex.printOut(string, System.out, new String[]{fields[1],fields[2],String.valueOf(counter)});
								
							}
							
						}
					
					}
					
				}
				
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
		}
		
	}
	

	public List<String> getCDS(GenBankRecord record) {
		
		List<String> result = new ArrayList<String>();
		
		
		for (Feature f : record.getFeatures()) {
			
			
			if (f.name().equals("CDS")) {
				
				String translation = f.getQualifierValue("TRANSLATION");
				
				if (translation!=null) {
					
					result.add(translation);
				}
				
			}
			
		}
		
		return result;
		
	}
	
	public void printOut(String seqs, PrintStream out, String[] fieldsDesc) {
		
		StringBuilder desc = new StringBuilder();
		
		for (String string : fieldsDesc) {
			
			desc.append(string);
			
			desc.append('|');
			
		}
		
		
		desc.deleteCharAt(desc.length()-1);
		
		out.println(">" + desc.toString());
		
		out.println(seqs);
		
	}

}
