package fileformats.genBankIO.extractcds;

import io.onelinelister.OneLineListReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.io.InputStream;

import cmdGA2.CommandLine;
import cmdGA2.OptionsFactory;
import cmdGA2.SingleArgumentOption;
import cmdGA2.returnvalues.InfileValue;
import fileformats.genBankIO.GenBankReaderAsync;
import fileformats.genBankIO.GenBankRecord;
import fileformats.genBankIO.elements.Feature;

/**
 * Extract all translation sequences in CDS Features of a GenBank file.
 * 
 * @author Javier Iserte
 *
 */
public class ExtractCDS {
	
	public static void main(String[] args) {
		
		//////////////////////////////////////
		// Create the command line 
		CommandLine cmd = new CommandLine();
		// Add options to the command line
		SingleArgumentOption<File> listOpt = new SingleArgumentOption<File>(cmd, "--list", new InfileValue(),null);
		SingleArgumentOption<InputStream> inOpt = OptionsFactory.createBasicInputStreamArgument(cmd);
		SingleArgumentOption<PrintStream> outOpt = OptionsFactory.createBasicPrintStreamArgument(cmd);
		// Parse Command Line Options
		cmd.readAndExitOnError(args);
		//////////////////////////////////////
		
		//////////////////////////////////////
		// Get Values from command line arguments
		// Get output print stream
		PrintStream output = outOpt.getValue();
		// Get input Files
		List<BufferedReader> inputs = new ArrayList<>();
		inputs.addAll(getInputFileList(listOpt));
		// Get source input stream
		inputs.addAll(getGenBankInputStream(inOpt));
		////////////////////////////////////////////////
		
		////////////////////////////////////////////////
		// Extract CDS features
		ExtractCDS extractor = new ExtractCDS();
		//
		for (BufferedReader bufferedReader : inputs) {
		
			try {

				GenBankReaderAsync gbreader = new GenBankReaderAsync(bufferedReader);
						
				GenBankRecord currentRecord = null;
						
				while ((currentRecord=gbreader.readGenBankRecord())!=null) {
							
					List<String> aas = extractor.getCDS(currentRecord);

					String gi = currentRecord.getHeader().getGi();
							
					for (String string : aas) {
						
						extractor.printOut(string, output, new String[]{gi});
						
					}
					
				}
		
			} catch (Exception e) {
				e.printStackTrace();
		    }
		
		}
	
	}


	private static List<BufferedReader> getGenBankInputStream( SingleArgumentOption<InputStream> inOpt) {
		
		List<BufferedReader> inputs = new ArrayList<BufferedReader>();
		
		if (inOpt.isPresent()) {
			inputs.add(new BufferedReader(new InputStreamReader(inOpt.getValue())));
		}
		return inputs;
	}


	private static List<BufferedReader> getInputFileList(
			SingleArgumentOption<File> listOpt) {
		List<BufferedReader> inputs = new ArrayList<>();
		
		if (listOpt.isPresent() ) {
		
			File listFile = listOpt.getValue();
			
			List<String> listOfFiles = OneLineListReader.createOneLineListReaderForString().read(listFile); 
			
			for (String file : listOfFiles) {
			
				try {
					
					inputs.add(new BufferedReader(new FileReader(new File(file))));
					
				} catch (FileNotFoundException e) {
					
					e.printStackTrace();
					
				}
				
			}
			
		}
		return inputs;
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
