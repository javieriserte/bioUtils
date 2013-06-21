package seqManipulation.fastamanipulator.commands;

import java.io.InputStream;
import java.io.PrintStream;

import seqManipulation.filtersequences.FilterSequence;
import seqManipulation.filtersequences.FilterSequenceSmallerThan;

import cmdGA.NoOption;
import cmdGA.SingleOption;

public class FilterSizeSmEqThanCommand extends FilterCommand<SingleOption> {

	public FilterSizeSmEqThanCommand(InputStream inputstream, PrintStream output, SingleOption option, NoOption invertFiler) {
		
		super(inputstream, output, option, invertFiler);
		
		Integer size = (Integer) this.getOption().getValue();
		
		if (size != null) {
		
			FilterSequence filter = new FilterSequenceSmallerThan(size);
			
			this.filter = filter;
		
		} else {
			
			this.filter = null;
			
		}

	}


}
