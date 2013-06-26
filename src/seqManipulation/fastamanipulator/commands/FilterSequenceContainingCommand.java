package seqManipulation.fastamanipulator.commands;

import java.io.InputStream;
import java.io.PrintStream;

import seqManipulation.filtersequences.FilterSequenceContaining;

import cmdGA.NoOption;
import cmdGA.SingleOption;


/**
 * Filter Sequences that contains a given subsequence
 * 
 * @author javier
 *
 */
public class FilterSequenceContainingCommand extends FilterCommand<SingleOption> {

	public FilterSequenceContainingCommand(InputStream inputstream, PrintStream output, SingleOption option, NoOption invertFiler) {

		super(inputstream, output, option, invertFiler);
	
	}

	public void getFilter() {
		String value = (String) this.getOption().getValue();
		
		if (value!=null) {
			
			this.filter = new FilterSequenceContaining(value);
			
		} else {
			
			this.filter = null;
		}
	}

}
