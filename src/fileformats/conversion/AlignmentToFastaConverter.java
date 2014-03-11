package fileformats.conversion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.List;

import pair.Pair;
import cmdGA2.CommandLine;
import cmdGA2.NoArgumentOption;
import cmdGA2.OptionsFactory;
import cmdGA2.SingleArgumentOption;
import cmdGA2.returnvalues.PrintStreamValue;
import fileformats.readers.AlignmentReadingResult;
import fileformats.readers.GenericAlignmentReader;

/**
 * Executable program that tries to read a sequence alignment 
 * formatted with Phylip, Clustal, Nexus or Pir and to convert it
 * to Fasta format.
 * 
 * @author javier
 *
 */
public class AlignmentToFastaConverter {

	///////////////////////////////////////
	// Main Executable method
	public static void main(String[] args) {
		
		///////////////////////////////
		// Create Command line option
		CommandLine cmd = new CommandLine();
		///////////////////////////////
		
		///////////////////////////////
		// Add options to command line
		SingleArgumentOption<InputStream> inOpt = OptionsFactory.createBasicInputStreamArgument(cmd);
		SingleArgumentOption<PrintStream> outOpt = OptionsFactory.createBasicPrintStreamArgument(cmd);
		SingleArgumentOption<PrintStream> errOpt = new SingleArgumentOption<PrintStream>(cmd, "--err", new PrintStreamValue(), System.err);
		NoArgumentOption helpOpt = new NoArgumentOption(cmd, "--help");
		///////////////////////////////
		
		//////////////////////////////
		// Read Command line
		cmd.readAndExitOnError(args);
		//////////////////////////////
		
		//////////////////////////////
		// Get Values from command line
		InputStream in = inOpt.getValue();
		PrintStream out = outOpt.getValue();
		PrintStream err = errOpt.getValue();
		//////////////////////////////
		
		////////////////////////////////
		// Check for help flag
		if (helpOpt.isPresent()) {
			
			String helpText = AlignmentToFastaConverter.getHelp();
			
			out.println(helpText);
			try {
				in.close();
				out.close();
				err.close();
			} catch (IOException e) {
				System.exit(0);
			}
			System.exit(0);
			
		}
		////////////////////////////////
		
		///////////////////////////////
		// Reads the input data
		GenericAlignmentReader reader = new GenericAlignmentReader();
		List<AlignmentReadingResult> result = reader.read(new BufferedReader(new InputStreamReader(in)));
		///////////////////////////////
		
		///////////////////////////////
		// If reading was successful export fasta
		boolean anySuccessful = anySuccessfulReading(result);
		
		if (anySuccessful) {
			
			exportFirstSuccessfulReading(out, result);
			
		} else {
			// if reading was not ok,
			// export faults.
			exportFaultMessages(err, result);
			
		}
		///////////////////////////////
		
		///////////////////////////////
		// Try to close all buffers used
		// and exit
		try {
			err.close();
			out.close();
			in.close();
		} catch (IOException e) {
			System.exit(0);
		}
		System.exit(0);
		////////////////////////////////
	}
	// End of main Executable method
	////////////////////////////////////
	
	//////////////////////////////////////
	// Private and protected methods
	private static boolean anySuccessfulReading( List<AlignmentReadingResult> result) {
		boolean anySuccessful = false;
		for (AlignmentReadingResult alignmentReadingResult : result) {
			
			anySuccessful = anySuccessful || alignmentReadingResult.successfulRead();
			
		}
		return anySuccessful;
	}

	private static void exportFaultMessages(PrintStream err,
			List<AlignmentReadingResult> result) {
		for (AlignmentReadingResult alignmentReadingResult : result) {
			
			err.println(alignmentReadingResult.getFault().getDefaultMessage());
			
		}
	}

	private static void exportFirstSuccessfulReading(PrintStream out,
			List<AlignmentReadingResult> result) {
		AlignmentReadingResult first = result.get(0);
		
		for (Pair<String, String> pair: first.getAlignment()) {
			out.println(">"+pair.getFirst());
			out.println(pair.getSecond());
		}
	}
	private static String getHelp() {
		InputStream in = AlignmentToFastaConverter.class.getResourceAsStream("help");
		
		StringBuilder sb = new StringBuilder();
		
		try {
			int c;
			while ( (c = in.read()) != -1) {
				sb.append((char)c);	
			}
			return sb.toString();

		} catch (IOException e) {
			return sb.toString();
		}
		
	}
	// End of private and protected methods
	/////////////////////////////////////////////
	
}
