package seqManipulation.fastamanipulator.commands;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import cmdGA.NoOption;

/**
 * Generates the Help for FastaAlignmentManipulator.
 * 
 * 
 * @author Javier Iserte
 *
 */
public class HelpCommand extends FastaCommand<NoOption> {

	public HelpCommand(InputStream inputstream, PrintStream output, NoOption option) {
		
		super(inputstream, output, option);

	}

	@Override
	protected List<String> performAction() {
		
		List<String> results = new ArrayList<String>();
		
		results.add("Hello");
		
		return results;
		
	}

}
