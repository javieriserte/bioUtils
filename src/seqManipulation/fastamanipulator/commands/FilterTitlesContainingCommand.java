package seqManipulation.fastamanipulator.commands;

import java.io.InputStream;
import java.io.PrintStream;

import seqManipulation.filtersequences.FilterSequence;
import seqManipulation.filtersequences.FilterSequenceBooleanOR;
import seqManipulation.filtersequences.FilterSequenceContainingInTitle;

import cmdGA.MultipleOption;
import cmdGA.NoOption;

/**
 * Filter sequences that contains at least one of a set of given texts.
 * 
 * @author javier iserte
 *
 */
public class FilterTitlesContainingCommand extends FilterCommand<MultipleOption> {

	public FilterTitlesContainingCommand(InputStream inputstream, PrintStream output, MultipleOption option, NoOption invertFilterOpt) {
		
		super(inputstream, output, option, invertFilterOpt);
		
	}

	public void getFilter() {
		Object[] values = this.getOption().getValues();
		
		FilterSequence filter = new FilterSequenceContainingInTitle((String) values[0]);
		
		if (values!=null) {
		
			for(int i=1;i<values.length; i++) {
			
				filter = new FilterSequenceBooleanOR(filter, new FilterSequenceContainingInTitle((String) values[i]));
			
			}
		
			this.filter = filter;
		
		}
	}

	
	
	
}
