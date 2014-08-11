package seqManipulation.fastamanipulator.commands.modify;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import cmdGA2.MultipleArgumentOption;
import pair.Pair;
import seqManipulation.fastamanipulator.commands.FastaCommand;

/**
 * Gets a subsection of an alignment given the first and last column to be retrieved.
 * The first column is the number 1, no 0.
 * 
 * @author javier iserte
 *
 */
public class SliceCommand extends FastaCommand<MultipleArgumentOption<Integer>> {

	public SliceCommand(InputStream inputstream, PrintStream output, MultipleArgumentOption<Integer> option) {

		super(inputstream, output, option);
		
	}

	@Override
	protected List<String> performAction() {
		
		List<Pair<String, String>> sequences = this.getSequences();
		
		List<String> results = new ArrayList<String>();
				
		List<Integer> values = this.getOption().getValues();
		if (values.size()==2) {
				
			// This option requires exactly two parameters

			int from = values.get(0)-1;

			int to = values.get(1);

			int len = sequences.get(0).getSecond().length();
				
			if (to <= len && from < to && from >=0) {
				
				for (Pair<String, String> pair : sequences) {
						
					results.add(">" + pair.getFirst());
						
					results.add(pair.getSecond().substring(from, to));
						
				}
					
			}
				
				
		} else {
				
			System.err.println("This option requires exactly two parameters.");
				
		}
			
		return results;

	}
	
}
