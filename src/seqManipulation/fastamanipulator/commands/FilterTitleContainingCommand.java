package seqManipulation.fastamanipulator.commands;

import java.io.InputStream;
import java.io.PrintStream;
import seqManipulation.filtersequences.FilterSequence;
import seqManipulation.filtersequences.FilterSequenceContainingInTitle;
import cmdGA.NoOption;
import cmdGA.SingleOption;

/**
 * Filter sequences that contains a given text in the title
 * 
 * @author javier
 *
 */
public class FilterTitleContainingCommand extends FilterCommand<SingleOption> {
	
	public FilterTitleContainingCommand(InputStream inputstream, PrintStream output, SingleOption option, NoOption invertFiler) {
		
		super(inputstream, output, option, invertFiler);
		
		
	}
	
	public void getFilter() {

		String value = (String) this.getOption().getValue();
		
		if (value!=null) {
			
			FilterSequence filter = new FilterSequenceContainingInTitle(value);
			
			this.filter = filter;
			
		} else {
			
			this.filter = null;
			// TODO create a neutral Filter that do not eliminates anything.
			
		}

		
	}



}
