package seqManipulation.fastamanipulator.commands;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import cmdGA.NoOption;
import pair.Pair;

/**
 * Eliminate gaps from a sequence alignment.
 * 
 * @author javier iserte
 *
 */
public class DegapCommand extends FastaCommand<NoOption> {

	public DegapCommand(InputStream inputstream, PrintStream output, NoOption option) {
		
		super(inputstream, output, option);

	}

	@Override
	protected List<String> performAction() {

		List<Pair<String, String>> sequences = this.getSequences();
		
		List<String> results = new ArrayList<String>();
		
		for (Pair<String, String> pair : sequences) {

			String newSeq = pair.getSecond().replaceAll("-", "");
			
			results.add(">" + pair.getFirst());

			results.add(newSeq);
			
		}
		
		return results;
		
	}

}
