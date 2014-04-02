package seqManipulation.fastamanipulator.commands;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import seqManipulation.complementary.Complementary;

import cmdGA.NoOption;
import pair.Pair;

/**
 * Retrieves the reverse-complementary sequence of a given DNA sequence
 * 
 * @author javier
 *
 */
public class ComplementaryCommand extends FastaCommand<NoOption> {

	public ComplementaryCommand(InputStream inputstream, PrintStream output, NoOption option) {

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
