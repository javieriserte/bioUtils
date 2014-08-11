package seqManipulation.fastamanipulator.commands.misc;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import seqManipulation.dottedalignment.ReconstructDottedAlignment;
import seqManipulation.fastamanipulator.commands.FastaCommand;
import cmdGA2.SingleArgumentOption;
import pair.Pair;

/**
 * Reconstruct a full alignment from a dotted alignment.
 * 
 * For the reconstruction the index of a reference sequence must be provided.
 * If 0 is provided as index, then the reference sequence is guessed.
 * The index of the first sequence is 1.  
 * 
 * @author javier iserte
 *
 */
public class ReconstructDottedAlignmentCommand extends FastaCommand<SingleArgumentOption<Integer>> {

	public ReconstructDottedAlignmentCommand(InputStream inputstream, PrintStream output, SingleArgumentOption<Integer> option) {

		super(inputstream, output, option);

	}

	@Override
	protected List<String> performAction() {
		
		Integer referenceIndex = (Integer)this.getOption().getValue();
		
		List<Pair<String, String>> seqs = this.getSequences();
		
		List<String> results = new ArrayList<String>();
		
		List<Pair<String,String>> rec = ReconstructDottedAlignment.reconstruct(seqs, referenceIndex-1); 
		
		for (Pair<String, String> pair : rec) {
			
			results.add(">" + pair.getFirst());
			
			results.add(pair.getSecond());
			
		}
		
		return results;
		
	}

}
