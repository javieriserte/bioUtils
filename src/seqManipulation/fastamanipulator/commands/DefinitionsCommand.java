package seqManipulation.fastamanipulator.commands;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import cmdGA.NoOption;
import fileformats.fastaIO.Pair;

/**
 * Command to get the descriptions of a fasta file
 * 
 * @author javier Iserte
 */
public class DefinitionsCommand extends FastaCommand<NoOption> {

	public DefinitionsCommand(InputStream inputstream, PrintStream output, 	NoOption option) {
		super(inputstream, output, option);
	}

	@Override
	protected List<String> performAction() {
		
		List<Pair<String, String>> pairs = this.getSequences();
		
		List<String> results = new ArrayList<String>();

		for (Pair<String, String> pair : pairs) {
			
			int index = pairs.indexOf(pair);
			
			results.add(index + ": " + pair.getFirst());
			
		}
	
		return results;
		
	}

}
