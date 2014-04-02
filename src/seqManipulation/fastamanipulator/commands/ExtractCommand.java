package seqManipulation.fastamanipulator.commands;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import cmdGA.MultipleOption;
import pair.Pair;

/**
 * Extract some sequences from the alignment, given a set of indexes.
 * First Index is 1, no 0.
 * 
 * @author Javier Iserte
 *
 */
public class ExtractCommand extends FastaCommand<MultipleOption> {

	//////////////////////////
	// Constructor
	public ExtractCommand(InputStream inputstream, PrintStream output, 	MultipleOption option) {
		
		super(inputstream, output, option);
		
	}

	//////////////////////////
	// Protected methods
	@Override
	protected List<String> performAction() {
		
		List<Pair<String, String>> pairs = this.getSequences();
		
		List<String> results = new ArrayList<String>();
		
		Object[] queryIndexes = this.getOption().getValues();
		
		for (Object queryIndex : queryIndexes) {
			
			int queryIndexInteger = (Integer) queryIndex;
			
			if (queryIndexInteger>0 && queryIndexInteger<=pairs.size()) {

				results.add(">" + pairs.get(queryIndexInteger-1).getFirst());
				
				results.add(pairs.get(queryIndexInteger-1).getSecond());
				
			}
			
		}
		
		return results;
		
	}

}
