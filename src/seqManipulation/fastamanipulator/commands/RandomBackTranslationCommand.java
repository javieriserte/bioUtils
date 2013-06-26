package seqManipulation.fastamanipulator.commands;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import seqManipulation.orf.Translate;

import cmdGA.NoOption;
import fileformats.fastaIO.Pair;

/**
 * Perform a back-translation of a protein sequence into a nucleotide sequence.
 * To avoid degeneration, one of the possible codons is selected randomly for each amino acid.
 * 
 * @author javier iserte
 *
 */
public class RandomBackTranslationCommand extends FastaCommand<NoOption> {

	public RandomBackTranslationCommand(InputStream inputstream, PrintStream output, NoOption option) {
		
		super(inputstream, output, option);

	}

	@Override
	protected List<String> performAction() {
		
		List<String> results = new ArrayList<String>();
		
		List<Pair<String, String>> sequences = this.getSequences();
		
		Map<String,String> geneticCode = Translate.getInstance().getStandardGeneticCode();
		
		Map<String,List<String>> geneticCodeRev = new HashMap<String, List<String>>();
		
		for (String codon : geneticCode.keySet()) {
			
			String aa = geneticCode.get(codon);
			
			if (geneticCodeRev.containsKey(aa)) {
				
				geneticCodeRev.get(aa).add(codon);
				
			} else {
				
				List<String> codons = new ArrayList<String>();
				
				codons.add(codon);
				
				geneticCodeRev.put(aa, codons);
				
			}
			
		}
		
		for (Pair<String, String> pair : sequences) {
			
			results.add(">" + pair.getFirst());
			
			StringBuilder newSeq = new StringBuilder();
			
			for (int i=0;i<pair.getSecond().length();i++) {
				
				String current = pair.getSecond().substring(i, i+1);
				
				if (geneticCodeRev.containsKey(current)) {
				
					List<String> list = geneticCodeRev.get(current); 
				
					int randomIndex = ((Double)(Math.random()*list.size())).intValue();
					
					newSeq.append(list.get(randomIndex));
				
				}
				
			}
			
			results.add(newSeq.toString());
			
		}
		
		return results;
	}

}
