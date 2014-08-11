package seqManipulation.fastamanipulator.commands.filter;

import java.io.InputStream;
import java.io.PrintStream;

import seqManipulation.filtersequences.FilterSequenceContaining;
import cmdGA2.NoArgumentOption;
import cmdGA2.SingleArgumentOption;


/**
 * Filter Sequences that contains a given subsequence
 * 
 * @author javier
 *
 */
public class FilterSequenceContainingCommand extends FilterCommand<SingleArgumentOption<String>> {

	public FilterSequenceContainingCommand(InputStream inputstream, PrintStream output, SingleArgumentOption<String> option, NoArgumentOption invertFiler) {

		super(inputstream, output, option, invertFiler);
	
	}

	public void getFilter() {
		String value = this.getOption().getValue();
		
		if (value!=null) {
			
			this.filter = new FilterSequenceContaining(value);
			
		} else {
			
			this.filter = null;
		}
	}

}
