package seqManipulation.fastamanipulator.commands.info;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import cmdGA2.NoArgumentOption;
import pair.Pair;
import seqManipulation.fastamanipulator.commands.FastaCommand;

/***
 * Count the number of sequences in a fasta file.
 * 
 * @author Javier Iserte
 *
 */
public class CountCommand extends FastaCommand<NoArgumentOption> {

	public CountCommand(InputStream inputstream, PrintStream output, NoArgumentOption option) {
		super(inputstream, output, option);
	}

	@Override
	protected List<String> performAction() {

		List<String> results = new ArrayList<String>();
		
		List<Pair<String,String>> pairs = this.getSequences();
		
		String sequencesCount = String.valueOf(pairs.size());
		
		results.add(sequencesCount);
		
		return results;
	}


}
