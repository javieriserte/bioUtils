package fileformats.conversion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

import pair.Pair;
import cmdGA2.CommandLine;
import cmdGA2.OptionsFactory;
import cmdGA2.SingleArgumentOption;
import cmdGA2.returnvalues.PrintStreamValue;
import fileformats.readers.AlignmentReadingResult;
import fileformats.readers.GenericAlignmentReader;

public class AlignmentToFastaConverter {

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
		
		///////////////////////////////
		// Reads the input data
		GenericAlignmentReader reader = new GenericAlignmentReader();
		AlignmentReadingResult result = reader.read(new BufferedReader(new InputStreamReader(in)));
		///////////////////////////////
		
		///////////////////////////////
		// If reading was successful export fasta
		if (result.successfulRead()) {
			for (Pair<String, String> pair: result.getAlignment()) {
				out.println(">"+pair.getFirst());
				out.println(pair.getSecond());
			}
		} else {
		// if reading was not ok,
		// export unmet rule of the deepest alignment.
			err.println(result.getUnmetRule().getDefaultMessage());
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
	
}
