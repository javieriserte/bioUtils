package seqManipulation.fastamanipulator.commands.filter;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;

import seqManipulation.filtersequences.FilterSequence;
import seqManipulation.filtersequences.FilterSequenceBooleanOR;
import seqManipulation.filtersequences.FilterSequenceContainingInTitle;
import cmdGA2.MultipleArgumentOption;
import cmdGA2.NoArgumentOption;

/**
 * Filter sequences that contains at least one of a set of given texts.
 * 
 * @author javier iserte
 *
 */
public class FilterTitlesContainingCommand extends FilterCommand<MultipleArgumentOption<String>> {

	public FilterTitlesContainingCommand(InputStream inputstream, PrintStream output, MultipleArgumentOption<String> option, NoArgumentOption invertFilterOpt) {
		
		super(inputstream, output, option, invertFilterOpt);
		
	}

	public void getFilter() {
		
		List<String> values = this.getOption().getValues();
		
		FilterSequence filter = new FilterSequenceContainingInTitle(values.get(0));
		
		if (values!=null) {
		
			for(int i=1;i<values.size(); i++) {
			
				filter = new FilterSequenceBooleanOR(filter, new FilterSequenceContainingInTitle( values.get(i)));
			
			}
		
			this.filter = filter;
		
		}
	}

	
	
	
}
