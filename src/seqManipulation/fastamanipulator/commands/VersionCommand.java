package seqManipulation.fastamanipulator.commands;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import seqManipulation.fastamanipulator.FastaAlignmentManipulator;

import cmdGA.NoOption;

/**
 * Shows the Version number of FastaAlignmentManipulator
 * @author javier
 *
 */
public class VersionCommand extends FastaCommand<NoOption> {

	public VersionCommand(InputStream inputstream, PrintStream output, 	NoOption option) {
		
		super(inputstream, output, option);
		
	}

	@Override
	protected List<String> performAction() {
		
		List<String> results = new ArrayList<String>();
		
		results.add(FastaAlignmentManipulator.VERSION);
		
		return results;
		
	}

}
