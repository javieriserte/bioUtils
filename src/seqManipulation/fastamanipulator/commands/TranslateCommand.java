package seqManipulation.fastamanipulator.commands;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import seqManipulation.orf.Translate;

import cmdGA.NoOption;
import pair.Pair;

/**
 * Translate each sequence of an alignment into an amino acid sequence
 * 
 * @author Javier Iserte
 *
 */
public class TranslateCommand extends FastaCommand<NoOption> {

	public TranslateCommand(InputStream inputstream, PrintStream output, NoOption option) {

		super(inputstream, output, option);

	}

	@Override
	protected List<String> performAction() {

		Translate translator = Translate.getInstance();
		
		List<Pair<String, String>> seqs = this.getSequences();
		
		List<String> results = new ArrayList<String>();
		
		for (Pair<String, String> pair : seqs) {
			
			results.add(">"+ pair.getFirst());
			
			results.add(translator.translateSeq(pair.getSecond()));
			
		}
		
		return results;
		
	}

}
