package seqManipulation.fastamanipulator.commands.gaps;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import cmdGA2.NoArgumentOption;
import pair.Pair;
import seqManipulation.fastamanipulator.commands.FastaCommand;

/**
 * Eliminate gaps from a sequence alignment.
 * 
 * @author javier iserte
 *
 */
public class DegapCommand extends FastaCommand<NoArgumentOption> {

	public DegapCommand(InputStream inputstream, PrintStream output, NoArgumentOption option) {
		
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
