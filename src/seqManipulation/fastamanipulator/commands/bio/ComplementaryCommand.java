package seqManipulation.fastamanipulator.commands.bio;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import seqManipulation.complementary.Complementary;
import seqManipulation.fastamanipulator.commands.FastaCommand;
import cmdGA2.NoArgumentOption;
import pair.Pair;

/**
 * Retrieves the reverse-complementary sequence of a given DNA sequence
 * 
 * @author javier
 *
 */
public class ComplementaryCommand extends FastaCommand<NoArgumentOption> {

	public ComplementaryCommand(InputStream inputstream, PrintStream output, NoArgumentOption option) {

		super(inputstream, output, option);
	
	}

	@Override
	protected List<String> performAction() {

		List<Pair<String, String>> sequences = this.getSequences();
		
		List<String> results = new ArrayList<String>();
		
		for (Pair<String, String> pair : sequences) {
			
			results.add(">" + pair.getFirst());
			
			results.add(Complementary.reverseComplementary(pair.getSecond()));
			
		}
		
		return results;
		
	}

	
	
}
