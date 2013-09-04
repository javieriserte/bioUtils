package fileformats.genBankIO.extractcds;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import cmdGA2.CommandLine;
import cmdGA2.NoArgumentOption;
import cmdGA2.OptionsFactory;
import cmdGA2.SingleArgumentOption;
import fileformats.genBankIO.GenBankReaderAsync;
import fileformats.genBankIO.GenBankRecord;

public class ExtractFromGB {

	public static void main (String[] args) {
		//////////////////////////////
		// Create commandline
		CommandLine cmdline = new CommandLine();
		
		/////////////////////////////
		// Add commandLine options
		SingleArgumentOption<InputStream> inOpt = OptionsFactory.createBasicInputStreamArgument(cmdline);
		
		SingleArgumentOption<PrintStream> outOpt = OptionsFactory.createBasicPrintStreamArgument(cmdline);
		
		NoArgumentOption organismOpt = new NoArgumentOption(cmdline, "-org");
		
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
				
				/////////////////////////////////////////
				// Generate Output
				
				StringBuilder sb = new StringBuilder();

				sb.append(record.getHeader().getGi());

				if (organismOpt.isPresent()) {

					sb.append("\t");

					sb.append(record.getHeader().getOrganism());
				
				}
				
				out.println(sb.toString()); 
				
			}
			
		} catch (Exception e) {
			System.err.println("There was an error: "+ e.getMessage());
			
			System.exit(1);
		}
		
		
		
		
	}
	
}
