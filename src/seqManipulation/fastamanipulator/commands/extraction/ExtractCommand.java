package seqManipulation.fastamanipulator.commands.extraction;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import cmdGA2.MultipleArgumentOption;
import pair.Pair;
import seqManipulation.fastamanipulator.commands.FastaCommand;

/**
 * Extract some sequences from the alignment, given a set of indexes.
 * First Index is 1, no 0.
 * 
 * @author Javier Iserte
 *
 */
public class ExtractCommand extends FastaCommand<MultipleArgumentOption<Integer>> {

	//////////////////////////
	// Constructor
	public ExtractCommand(InputStream inputstream, PrintStream output, 	MultipleArgumentOption<Integer> option) {
		
		super(inputstream, output, option);
		
	}

	//////////////////////////
	// Protected methods
	@Override
	protected List<String> performAction() {
		
		List<Pair<String, String>> pairs = this.getSequences();
		
		List<String> results = new ArrayList<String>();
		
		List<Integer> queryIndexes = this.getOption().getValues();
		
		for (Integer queryIndex : queryIndexes) {
			
			if (queryIndex>0 && queryIndex<=pairs.size()) {

				results.add(">" + pairs.get(queryIndex-1).getFirst());
				
				results.add(pairs.get(queryIndex-1).getSecond());
				
			}
			
		}
		
		return results;
		
	}

}
