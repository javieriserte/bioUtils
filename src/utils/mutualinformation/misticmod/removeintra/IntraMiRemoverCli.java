package utils.mutualinformation.misticmod.removeintra;

import io.bufferreaders.UncommenterBufferedReader;
import io.resources.ResourceContentAsString;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.List;

import cmdGA2.CommandLine;
import cmdGA2.MultipleArgumentOption;
import cmdGA2.NoArgumentOption;
import cmdGA2.OptionsFactory;
import cmdGA2.SingleArgumentOption;
import cmdGA2.returnvalues.IntegerValue;

public class IntraMiRemoverCli {

	public static void main(String[] args) {
		
		////////////////////////////////////////////////////////////////////////
		// Create Command Line Parser
		CommandLine cmd = new CommandLine();
        ////////////////////////////////////////////////////////////////////////
		
        ////////////////////////////////////////////////////////////////////////
		// Define Commanda Line Arguments
		SingleArgumentOption<InputStream> inOpt = OptionsFactory.createBasicInputStreamArgument(cmd);
		
		MultipleArgumentOption<Integer> lenOpt= new MultipleArgumentOption<Integer>(cmd, "--lengths", ',', null, new IntegerValue());
		
		SingleArgumentOption<PrintStream> outOpt = OptionsFactory.createBasicPrintStreamArgument(cmd);
	
		NoArgumentOption helpOpt = new NoArgumentOption(cmd, "--help");
		
		NoArgumentOption removeOpt = new NoArgumentOption(cmd, "--remove");
        ////////////////////////////////////////////////////////////////////////
		
        ////////////////////////////////////////////////////////////////////////
		// Read Command line
		cmd.readAndExitOnError(args);
        ////////////////////////////////////////////////////////////////////////
			
        ////////////////////////////////////////////////////////////////////////
		// Read Command Line Arguments
		PrintStream out = outOpt.getValue();
		BufferedReader in = new UncommenterBufferedReader(new InputStreamReader( inOpt.getValue()));		
		List<Integer> lengths = lenOpt.getValues();
        ////////////////////////////////////////////////////////////////////////
		
        ////////////////////////////////////////////////////////////////////////
		// Check for Help
		if (helpOpt.isPresent()) {
			
			String helpText =  new ResourceContentAsString().readContents("help", IntraMiRemoverCli.class);
			System.err.println(helpText);
			System.exit(1);

		}
        ////////////////////////////////////////////////////////////////////////
			
	    ////////////////////////////////////////////////////////////////////////
		// Validate Command Line Arguments
		if (!lenOpt.isPresent()) {

			System.err.println("-lengths option is mandatory.");

			System.exit(1);

		}
	    ////////////////////////////////////////////////////////////////////////
			
	    ////////////////////////////////////////////////////////////////////////
		// Remove Connections
		IntraMiRemover remover = new IntraMiRemover();
		try {
			remover.removeConnections(out, in, lengths);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	    ////////////////////////////////////////////////////////////////////////
			
	}
	
}
