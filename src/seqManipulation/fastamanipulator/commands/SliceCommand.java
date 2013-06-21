package seqManipulation.fastamanipulator.commands;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import cmdGA.MultipleOption;

import fileformats.fastaIO.Pair;

/**
 * Gets a subsection of an alignment given the first and last column to be retrieved.
 * The first column is the number 1, no 0.
 * 
 * @author javier iserte
 *
 */
public class SliceCommand extends FastaCommand<MultipleOption> {

	public SliceCommand(InputStream inputstream, PrintStream output, MultipleOption option) {

		super(inputstream, output, option);
		
	}

	@Override
	protected List<String> performAction() {
		
		List<Pair<String, String>> sequences = this.getSequences();
		
		List<String> results = new ArrayList<String>();
				
		if (this.getOption().count()==2) {
				
			// This option requires exactly two parameters

			int from = (Integer) this.getOption().getValue(0)-1;

			int to = (Integer) this.getOption().getValue(1);

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
