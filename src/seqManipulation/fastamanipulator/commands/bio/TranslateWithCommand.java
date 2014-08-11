package seqManipulation.fastamanipulator.commands.bio;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import seqManipulation.fastamanipulator.commands.FastaCommand;
import seqManipulation.orf.Translate;
import cmdGA2.SingleArgumentOption;
import pair.Pair;

/**
 * Translate each sequence into amino acid positions assuming a given genetic code. 
 * 
 * @author javier Iserte
 *
 */
public class TranslateWithCommand extends FastaCommand<SingleArgumentOption<File>> {

	public TranslateWithCommand(InputStream inputstream, PrintStream output, SingleArgumentOption<File> option) {
		
		super(inputstream, output, option);

	}

	@Override
	protected List<String> performAction() {

		Map<String, String> geneticCode=null;
		
		BufferedReader infile=null;
		try {
			infile = new BufferedReader(new FileReader(this.getOption().getValue()));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
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
