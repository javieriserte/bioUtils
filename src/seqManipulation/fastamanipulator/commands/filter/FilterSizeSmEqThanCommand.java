package seqManipulation.fastamanipulator.commands.filter;

import java.io.InputStream;
import java.io.PrintStream;

import seqManipulation.filtersequences.FilterSequence;
import seqManipulation.filtersequences.FilterSequenceSmallerThan;

import cmdGA2.NoArgumentOption;
import cmdGA2.SingleArgumentOption;

/**
 * Filter sequences whose lengths is smaller or equal than a threshold value.
 * 
 * @author javier iserte
 *
 */
public class FilterSizeSmEqThanCommand extends FilterCommand<SingleArgumentOption<Integer>> {

	public FilterSizeSmEqThanCommand(InputStream inputstream, PrintStream output, SingleArgumentOption<Integer> option, NoArgumentOption invertFiler) {
		
		super(inputstream, output, option, invertFiler);
		
	}

	public void getFilter() {
		Integer size = this.getOption().getValue();
		
		if (size != null) {
		
			FilterSequence filter = new FilterSequenceSmallerThan(size);
			
			this.filter = filter;
		
		} else {
			
			this.filter = null;
			
		}
	}


}
