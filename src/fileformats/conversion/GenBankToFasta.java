package fileformats.conversion;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import cmdGA2.CommandLine;
import cmdGA2.NoArgumentOption;
import cmdGA2.OptionsFactory;
import cmdGA2.SingleArgumentOption;
import cmdGA2.returnvalues.InfileValue;
import fileformats.genBankIO.GenBankFormatException;
import fileformats.genBankIO.GenBankReader;
import fileformats.genBankIO.GenBankReaderAsync;
import fileformats.genBankIO.GenBankRecord;

public class GenBankToFasta {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

//		Parser parser = new Parser();
		////////////////////////////////////////////////////////////////////////
		// Step One : Create the parser
		CommandLine cmd = new CommandLine();
		////////////////////////////////////////////////////////////////////////
		
		////////////////////////////////////////////////////////////////////////
		// Step Two : Add Command Line Options
		SingleArgumentOption<File> inputFileOpt = 
				new SingleArgumentOption<File>(cmd, "--infile", 
						                       new InfileValue(), null);

		SingleArgumentOption<PrintStream> printStreamOpt = 
				OptionsFactory.createBasicPrintStreamArgument(cmd);		

		NoArgumentOption countOpt = new NoArgumentOption(cmd, "--count");
		
		NoArgumentOption giOpt = new NoArgumentOption(cmd, "--desc_gi");
		////////////////////////////////////////////////////////////////////////

		////////////////////////////////////////////////////////////////////////
		// Step Three : Try to parse the command line
		cmd.readAndExitOnError(args);
		////////////////////////////////////////////////////////////////////////

		////////////////////////////////////////////////////////////////////////
		// Get values from command line
		File in = inputFileOpt.getValue();
		PrintStream out = printStreamOpt.getValue();
		boolean countIsPresent = countOpt.isPresent();
		boolean giIsPresent = giOpt.isPresent();
		////////////////////////////////////////////////////////////////////////
		
		////////////////////////////////////////////////////////////////////////
		// Validate input file
		if (in == null || !in.exists()) System.exit(1);
		////////////////////////////////////////////////////////////////////////
		
		////////////////////////////////////////////////////////////////////////
		// select tasks
		if(countIsPresent) {
			printOutCount(in, out);
			System.exit(0);
		} else {
			try {
				printOutFasta(in, out, giIsPresent);
				System.exit(0);
			} catch (Exception e) {
				System.err.println("There was an error: " + e.getMessage());
				System.exit(1);	
			}
		}
		////////////////////////////////////////////////////////////////////////
	}

	////////////////////////////////////////////////////////////////////////////
	// Class methods
	/**
	 * Print origin sequences in genbank reacord into a printstream in fasta 
	 * format
	 * @param in Input file in genbank format
	 * @param out printstream to export sequences in fasta format.
	 * @param giIsPresent if true, prints GI codes as description of fasta files
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws Exception
	 */
	private static void printOutFasta(File in, PrintStream out,
			boolean giIsPresent) throws FileNotFoundException, IOException,
			Exception {
		BufferedReader br = new BufferedReader(new FileReader(in));
		
		GenBankReaderAsync gbreader = new GenBankReaderAsync(br);
		
		GenBankRecord record = null;
		
		while((record = gbreader.readGenBankRecord())!=null) {
			
			if (giIsPresent) {
				
				out.println(">" + record.getHeader().getGi());
				
			} else {
				
				out.println(">" + record.getHeader().getDefinition());
				
			}
			
			out.println(record.getOrigin().getSequence());

		}
	}
 
    /**
     * Counts an prints out the number of genbank record in an input file.
     * @param in Input file with GenBank records
     * @param out PrintStream to export the result.
     */
	private static void printOutCount(File in, PrintStream out) {
		try {
			List<GenBankRecord> a = GenBankReader.readFile(in);
			out.println(a.size());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (GenBankFormatException e) {
			e.printStackTrace();
		}
	}
	////////////////////////////////////////////////////////////////////////////

}
