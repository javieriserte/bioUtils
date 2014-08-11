package seqManipulation.fastamanipulator.commands.modify;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import cmdGA2.MultipleArgumentOption;
import pair.Pair;
import seqManipulation.fastamanipulator.commands.FastaCommand;

	

/**
 * Replaces uncommon chars in a sequence (ie. not amino acid or not bases or not degenerated bases and gaps with a given char)
 * 
 * @author javier
 *
 */
public class ReplaceUncommonChars extends FastaCommand<MultipleArgumentOption<String>> {

	static String permited_bases = "ACTG\\-actg";
	
	static String permited_deg_bases = "ACTGWSRYMKVBHDN\\-actgwsrymkvbhdn";
	
	static String permited_aa = "QWERTYIPASDFGHKLCVNM\\-qwertyipasdfghklcvnm";
	
	public ReplaceUncommonChars(InputStream inputstream, PrintStream output, MultipleArgumentOption<String> option) {

		super(inputstream, output, option);
		
	}

	@Override
	protected List<String> performAction() {
		
		String type = this.getOption().getValues().get(0).toLowerCase(); 
		
		String replacement = this.getOption().getValues().get(1);
		
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
