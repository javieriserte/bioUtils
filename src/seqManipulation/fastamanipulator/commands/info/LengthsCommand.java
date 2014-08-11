package seqManipulation.fastamanipulator.commands.info;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import cmdGA2.NoArgumentOption;
import pair.Pair;
import seqManipulation.fastamanipulator.commands.FastaCommand;

/**
 * Returns the lengths of each sequence in an alignment.
 * 
 * @author javier Iserte
 *
 */
public class LengthsCommand extends FastaCommand<NoArgumentOption> {

	public LengthsCommand(InputStream inputstream, PrintStream output, NoArgumentOption option) {
		
		super(inputstream, output, option);
		
	}

	@Override
	protected List<String> performAction() {
		
		List<Pair<String, String>> sequences = this.getSequences();
		
		List<String> results = new ArrayList<String>();
		
		for (Pair<String,String> pair : sequences) {

			results.add(String.valueOf(pair.getSecond().length()));

		}
		
		return results;
		
	}

}
