package seqManipulation.fastamanipulator.commands;

import java.io.InputStream;
import java.io.PrintStream;

import seqManipulation.filtersequences.FilterSequence;
import seqManipulation.filtersequences.FilterSequenceGreaterThan;

import cmdGA.NoOption;
import cmdGA.SingleOption;

/**
 * Filter sequence by its size.
 * 
 * @author javier iserte
 *
 */
public class FilterSizeGrEqThanCommand extends FilterCommand<SingleOption> {

	public FilterSizeGrEqThanCommand(InputStream inputstream, PrintStream output, SingleOption option, NoOption invertFiler) {
		
		super(inputstream, output, option, invertFiler);

		Integer value = (Integer) this.getOption().getValue();
		
		if (value!=null) {
			
			FilterSequence filter = new FilterSequenceGreaterThan(value);
			
			this.filter = filter;
			
		} else {
			
			this.filter = null;
			// TODO create a neutral Filter that do not eliminates anything.
			
		}
		
	}
	
}
