package seqManipulation.fastamanipulator.commands;

import java.io.InputStream;
import java.io.PrintStream;

import seqManipulation.filtersequences.FilterSequence;
import seqManipulation.filtersequences.FilterSequenceStartingWith;

import cmdGA.NoOption;
import cmdGA.SingleOption;

/**
 * Filter sequences that starts with a given subsequence.
 * 
 * @author javier
 *
 */
public class FilterSequenceStartingWithCommand extends FilterCommand<SingleOption> {

	public FilterSequenceStartingWithCommand(InputStream inputstream, 	PrintStream output, SingleOption option, NoOption invertFiler) {
		
		super(inputstream, output, option, invertFiler);
		
		String string = (String) this.getOption().getValue();
		
		if (string != null) {
			
			FilterSequence filter = new FilterSequenceStartingWith(string);
			
			this.filter = filter;
		
		} else {
			
			this.filter = null;
			
		}
		
	}

}
