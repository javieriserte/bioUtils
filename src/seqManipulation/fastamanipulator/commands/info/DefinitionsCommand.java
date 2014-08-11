package seqManipulation.fastamanipulator.commands.info;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import cmdGA2.NoArgumentOption;
import pair.Pair;
import seqManipulation.fastamanipulator.commands.FastaCommand;

/**
 * Command to get the descriptions of a fasta file with its row number.
 * The row number starts with one.
 * 
 * @author javier Iserte
 */
public class DefinitionsCommand extends FastaCommand<NoArgumentOption> {

	public DefinitionsCommand(InputStream inputstream, PrintStream output, 	NoArgumentOption option) {
		super(inputstream, output, option);
	}

	@Override
	protected List<String> performAction() {
		
		List<Pair<String, String>> pairs = this.getSequences();
		
		List<String> results = new ArrayList<String>();

		for (Pair<String, String> pair : pairs) {
			
			int index = pairs.indexOf(pair);
			
			results.add((index+1) + "\t" + pair.getFirst());
			
		}
	
		return results;
		
	}

}
