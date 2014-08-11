package seqManipulation.fastamanipulator.commands.filter;

import java.io.InputStream;
import java.io.PrintStream;

import seqManipulation.filtersequences.FilterSequence;
import seqManipulation.filtersequences.FilterSequenceStartingWith;
import cmdGA2.NoArgumentOption;
import cmdGA2.SingleArgumentOption;

/**
 * Filter sequences that starts with a given subsequence.
 * 
 * @author javier
 *
 */
public class FilterSequenceStartingWithCommand extends FilterCommand<SingleArgumentOption<String>> {

	public FilterSequenceStartingWithCommand(InputStream inputstream, 	PrintStream output, SingleArgumentOption<String> option, NoArgumentOption invertFiler) {
		
		super(inputstream, output, option, invertFiler);
		
	}

	public void getFilter() {
		String string = this.getOption().getValue();
		
		if (string != null) {
			
			FilterSequence filter = new FilterSequenceStartingWith(string);
			
			this.filter = filter;
		
		} else {
			
			this.filter = null;
			
		}
	}

}
