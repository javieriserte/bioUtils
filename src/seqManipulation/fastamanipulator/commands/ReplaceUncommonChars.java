package seqManipulation.fastamanipulator.commands;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import cmdGA.MultipleOption;
import fileformats.fastaIO.Pair;

	

/**
 * Replaces uncommon chars in a sequence (ie. not amino acid or not bases or not degenerated bases and gaps with a given char)
 * 
 * @author javier
 *
 */
public class ReplaceUncommonChars extends FastaCommand<MultipleOption> {

	static String permited_bases = "ACTG\\-actg";
	
	static String permited_deg_bases = "ACTGWSRYMKVBHDN\\-actgwsrymkvbhdn";
	
	static String permited_aa = "QWERTYIPASDFGHKLCVNM\\-qwertyipasdfghklcvnm";
	
	public ReplaceUncommonChars(InputStream inputstream, PrintStream output, MultipleOption option) {

		super(inputstream, output, option);
		
	}

	@Override
	protected List<String> performAction() {
		
		String type = (String) this.getOption().getValue(0); 
		
		String replacement = (String) this.getOption().getValue(1);
		
		List<Pair<String, String>> seqs = this.getSequences();
		
		List<String> results = new ArrayList<String>();
		
		for (Pair<String, String> pair : seqs) {
			
			results.add(">" + pair.getFirst());
			
			String newSeq = pair.getSecond();
			
			String query = "";
			
			switch (type) {
			case "b":
				query = permited_bases;
				break;

			case "a":
				query = permited_aa;
				break;
				
			case "d":
				query = permited_deg_bases;
				break;
				
			default:
				query = ".";
			}
			
			newSeq = newSeq.replaceAll("[^"+ query +"]", replacement);
			
			results.add(newSeq);
			
		}
		
		return results;
		
	}

}
