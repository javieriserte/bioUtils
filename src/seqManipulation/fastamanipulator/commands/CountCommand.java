package seqManipulation.fastamanipulator.commands;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import cmdGA.NoOption;
import fileformats.fastaIO.Pair;

/***
 * Count the number of sequences in a fasta file.
 * 
 * @author Javier Iserte
 *
 */
public class CountCommand extends FastaCommand<NoOption> {

	public CountCommand(InputStream inputstream, PrintStream output, NoOption option) {
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
