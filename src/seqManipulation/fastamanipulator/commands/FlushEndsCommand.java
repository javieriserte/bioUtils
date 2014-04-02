package seqManipulation.fastamanipulator.commands;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import cmdGA.NoOption;
import pair.Pair;

/***
 * Removes columns from the beginning and the end of an alignment while at least one of them doesn't started with the sequence (i.e. contain gaps).
 * <pre> 
 * Example:
 * 
 * ------AAAAAA---AAAAA-----
 * --C---AAAAAA-C-AAAAA----- 
 * ----TGAATATA---AAAAA-C---
 * -----TAATAAAT--TATAT-T---
 * 
 * returns
 *
 * ----AAAAAA---AAAAA--   
 * C---AAAAAA-C-AAAAA--    
 * --TGAATATA---AAAAA-C   
 * ---TAATAAAT--TATAT-T   
 * 
 * </pre>
 * @author javier
 *
 */
public class FlushEndsCommand extends FastaCommand<NoOption> {

	public FlushEndsCommand(InputStream inputstream, PrintStream output, NoOption option) {
		
		super(inputstream, output, option);
		
	}

	@Override
	protected List<String> performAction() {

		List<String> results = new ArrayList<String>();
		
		List<Pair<String, String>> seqs = this.getSequences();
		
		int min=0;
		
		int max = seqs.get(0).getSecond().length();
		
		for (Pair<String, String> pair : seqs) {
			
			String currentSeq = pair.getSecond();
			
			int currentMin = 0;
			
			int currentMax = 0;
			
			for (int i = 0; i < currentSeq.length();i++) {
				
				if (currentSeq.charAt(i)!='-') {
					
					currentMin = i;
					
					break;
					
				}
				
		}
			
			for (int i = currentSeq.length()-1; 1>= 0;i--) {
				
				if (currentSeq.charAt(i)!='-') {
					
					currentMax = i;
					
					break;
					
				}
				
			}
			
			min = Math.max(min, currentMin);
			
			max = Math.min(max, currentMax);
			
		}
		
		for (Pair<String, String> pair : seqs) {
			
			results.add(">" + pair.getFirst());
			
			results.add(pair.getSecond().substring(min, max));
			
		}
		
		return results;
		
	}

}
