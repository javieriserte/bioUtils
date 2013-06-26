package seqManipulation.fastamanipulator.commands;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import cmdGA.NoOption;
import fileformats.fastaIO.Pair;

/**
 * Deinterleaves a multiple fasta alignment
 * 
 * @author javier iserte
 *
 */
public class DeInterleaveCommand extends FastaCommand<NoOption> {

	public DeInterleaveCommand(InputStream inputstream, PrintStream output, NoOption option) {
		
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
