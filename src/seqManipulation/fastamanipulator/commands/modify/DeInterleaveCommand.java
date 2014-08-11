package seqManipulation.fastamanipulator.commands.modify;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import cmdGA2.NoArgumentOption;
import pair.Pair;
import seqManipulation.fastamanipulator.commands.FastaCommand;

/**
 * Deinterleaves a multiple fasta alignment
 * 
 * @author javier iserte
 *
 */
public class DeInterleaveCommand extends FastaCommand<NoArgumentOption> {

	public DeInterleaveCommand(InputStream inputstream, PrintStream output, NoArgumentOption option) {
		
		super(inputstream, output, option);
		
	}

	@Override
	protected List<String> performAction() {
		
		List<Pair<String, String>> seqs = this.getSequences();
				
		List<String> results = new ArrayList<String>();
		
		for (Pair<String, String> pair : seqs) {
			
			results.add(">" + pair.getFirst());
			
			results.add(pair.getSecond());
			
		}
		
		return results;
		
	}
	
}
