package seqManipulation.fastamanipulator.commands.help;

import io.resources.ResourceContentAsString;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import seqManipulation.fastamanipulator.FastaAlignmentManipulator;
import seqManipulation.fastamanipulator.commands.FastaCommand;
import cmdGA2.NoArgumentOption;

/**
 * Generates help to build a custom genetic code file.
 * 
 * @author javier iserte
 *
 */
public class GeneticCodeHelpCommand extends FastaCommand<NoArgumentOption> {

	public GeneticCodeHelpCommand(InputStream inputstream, PrintStream output, NoArgumentOption option) {
		
		super(inputstream, output, option);
		
	}

	@Override
	protected List<String> performAction() {
		
		String textHelp = new ResourceContentAsString().readContents("genCodeHelp", FastaAlignmentManipulator.class);
		List<String> results = new ArrayList<String>();
		results.add(textHelp);
		return results;
		
	}

}
