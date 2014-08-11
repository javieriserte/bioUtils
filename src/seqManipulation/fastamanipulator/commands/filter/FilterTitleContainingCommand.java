package seqManipulation.fastamanipulator.commands.filter;

import java.io.InputStream;
import java.io.PrintStream;

import seqManipulation.filtersequences.FilterSequence;
import seqManipulation.filtersequences.FilterSequenceContainingInTitle;
import cmdGA2.NoArgumentOption;
import cmdGA2.SingleArgumentOption;

/**
 * Filter sequences that contains a given text in the title
 * 
 * @author javier
 *
 */
public class FilterTitleContainingCommand extends FilterCommand<SingleArgumentOption<String>> {
	
	public FilterTitleContainingCommand(InputStream inputstream, PrintStream output, SingleArgumentOption<String> option, NoArgumentOption invertFiler) {
		
		super(inputstream, output, option, invertFiler);
		
		
	}
	
	public void getFilter() {

		String value = this.getOption().getValue();
		
		if (value!=null) {
			
			FilterSequence filter = new FilterSequenceContainingInTitle(value);
			
			this.filter = filter;
			
		} else {
			
			this.filter = null;
			// TODO create a neutral Filter that do not eliminates anything.
			
		}

		
	}



}
