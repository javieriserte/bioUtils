package seqManipulation.fastamanipulator.commands.filter;

import java.io.InputStream;
import java.io.PrintStream;

import seqManipulation.filtersequences.FilterSequence;
import seqManipulation.filtersequences.FilterSequenceGreaterThan;
import cmdGA2.NoArgumentOption;
import cmdGA2.SingleArgumentOption;

/**
 * Filter sequence by its size.
 * 
 * @author javier iserte
 *
 */
public class FilterSizeGrEqThanCommand extends FilterCommand<SingleArgumentOption<Integer>> {

	public FilterSizeGrEqThanCommand(InputStream inputstream, PrintStream output, SingleArgumentOption<Integer> option, NoArgumentOption invertFiler) {
		
		super(inputstream, output, option, invertFiler);
	
	}

	public void getFilter() {
		Integer value = this.getOption().getValue();
		
		if (value!=null) {
			
			FilterSequence filter = new FilterSequenceGreaterThan(value);
			
			this.filter = filter;
			
		} else {
			
			this.filter = null;
			// TODO create a neutral Filter that do not eliminates anything.
			
		}
	}
	
}
