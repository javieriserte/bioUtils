package seqManipulation.fastamanipulator.commands.extraction;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pair.Pair;
import seqManipulation.fastamanipulator.commands.FastaCommand;
import cmdGA2.MultipleArgumentOption;

public class ExtractByTitleCommand extends FastaCommand<MultipleArgumentOption<String>> {

	public ExtractByTitleCommand(InputStream inputstream, PrintStream output, MultipleArgumentOption<String> option) {
		
		super(inputstream, output, option);
		
	}

	@Override
	protected List<String> performAction() {
		
		List<Pair<String, String>> pairs = this.getSequences();
		
		Map<String,String> sequenceMap = new HashMap<String, String>();
		
		for (Pair<String, String> pair : pairs) {
			
			sequenceMap.put(pair.getFirst(), pair.getSecond());
			
		}
		
		List<String> results = new ArrayList<String>();
		
		List<String> queryDescriptions = this.getOption().getValues();
		
		for (String query : queryDescriptions) {
			
			if (sequenceMap.containsKey(query)) {
			
				results.add(">" + query);
				
				results.add(sequenceMap.get(query));
			
			}
			
		}
		
		return results;
	}

}
