package seqManipulation.fastamanipulator;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;

import seqManipulation.fastamanipulator.commands.FastaCommand;
import cmdGA2.ArgOption;

public class FastaAlignmentManipulator {
	public static final String VERSION = "0.2.1";
	/**
	 * A simple program to manipulate Alignments given in fasta format.
	 * 
	 * @param args
	 */
	public static void main(String[] args)  {

		long time0 = System.currentTimeMillis();

		////////////////////////////////////////////////////////////////////////
		// Read Command line
		CommandLineMaganerModule manager = new CommandLineMaganerModule();
		manager.dealWithCommandLine(args);
		////////////////////////////////////////////////////////////////////////
		
		if (manager.isDebugFlagSet()) {
			
			System.err.println("Parsing arguments after: "+ (System.currentTimeMillis() - time0) + "ms.");
			
		}
		
		////////////////////////////////////////////////////////////////////////
		// Get command line values
		List<FastaCommand<? extends ArgOption<?>>> uniqueCommands = manager.getUniqueCommand();
		int parametersUsed = getNumberOfParametersUsed(uniqueCommands);
		PrintStream out = manager.getOut();
		InputStream input = manager.getIn();
		////////////////////////////////////////////////////////////////////////
		
		////////////////////////////////////////////////////////////////////////
		// Check For Help
		if (parametersUsed==0 || manager.isHelpFlagSet()) {
			
			manager.getHelpCommand().setOutput(out);
			
			manager.getHelpCommand().execute();

			System.exit(0);
			
		} else 	if (parametersUsed>1) {
			
			System.err.println("Only one option is expected");
			
			System.exit(0);
			
		}
		////////////////////////////////////////////////////////////////////////
		
		////////////////////////////////////////////////////////////////////////
		// Select The command for execution
		FastaCommand<? extends ArgOption<?>> selectedCommand = getSelectedCommand(uniqueCommands);
		selectedCommand.setInputstream(input);
		selectedCommand.setOutput(out);
		////////////////////////////////////////////////////////////////////////

		if (manager.isDebugFlagSet()) {
			
			System.err.println("Starting execute option after: "+ (System.currentTimeMillis() - time0) + "ms.");
			
		}
		
		////////////////////////////////////////////////////////////////////////
		// Execute the command
		selectedCommand.execute();
		////////////////////////////////////////////////////////////////////////
		
		if (manager.isDebugFlagSet()) {
			
			System.err.println("Ending execute option after: "+ (System.currentTimeMillis() - time0) + "ms.");
			
		}
		
		if (manager.isDebugFlagSet()) {
			
			System.err.println("End Program after: "+ (System.currentTimeMillis() - time0) + "ms.");
			
		}

	}

	////////////////////////////////////////////////////////////////////////////
	// Private Methods
	private static FastaCommand<? extends ArgOption<?>> getSelectedCommand( List<FastaCommand<? extends ArgOption<?>>> uniqueCommands) {
		
		for (FastaCommand<? extends ArgOption<?>> fastaCommand : uniqueCommands) {

			if (fastaCommand.getOption().isPresent()) {
				
				return fastaCommand;
				
			}
			
		}
		
		return null;
		
	}

	private static int getNumberOfParametersUsed(
			List<FastaCommand<? extends ArgOption<?>>> uniqueCommands) {
		int parametersUsed = 0;
		
		for (FastaCommand<? extends ArgOption<?>> command : uniqueCommands) { 		
			if (command.getOption().isPresent()) parametersUsed++; 
		}
		return parametersUsed;
	}
	////////////////////////////////////////////////////////////////////////////
	
}
