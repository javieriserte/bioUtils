package seqManipulation.fastamanipulator.commands;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import seqManipulation.orf.Translate;

import cmdGA.SingleOption;
import fileformats.fastaIO.Pair;

/**
 * Translate each sequence into amino acid positions assuming a given genetic code. 
 * 
 * @author javier Iserte
 *
 */
public class TranslateWithCommand extends FastaCommand<SingleOption> {

	public TranslateWithCommand(InputStream inputstream, PrintStream output, SingleOption option) {
		
		super(inputstream, output, option);

	}

	@Override
	protected List<String> performAction() {

		Map<String, String> geneticCode=null;
		
		InputStreamReader gcISR = (InputStreamReader) this.getOption().getValue();
		
		BufferedReader infile = new BufferedReader(gcISR);
		
		geneticCode = Translate.readGeneticCode(infile);

		Translate translator = Translate.getInstance();
		
		List<Pair<String, String>> seqs = this.getSequences();
		
		List<String> results = new ArrayList<String>();
		
		for (Pair<String, String> pair : seqs) {
			
			results.add(">"+ pair.getFirst());
			
			results.add(translator.translateSeq(pair.getFirst(),geneticCode));
			
		}
		
		return results;

	}

}
