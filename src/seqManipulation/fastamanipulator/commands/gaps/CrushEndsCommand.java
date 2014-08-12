package seqManipulation.fastamanipulator.commands.gaps;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import pair.Pair;
import cmdGA2.NoArgumentOption;
import seqManipulation.fastamanipulator.commands.FastaCommand;

public class CrushEndsCommand extends FastaCommand<NoArgumentOption> {

	public CrushEndsCommand(InputStream inputstream, PrintStream output,
			NoArgumentOption option) {
		super(inputstream, output, option);
	}

	@Override
	protected List<String> performAction() {
		List<String> results = new ArrayList<String>();
		
		List<Pair<String, String>> seqs = this.getSequences();
		
		int min = 0;
		
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
			
			for (int i = currentSeq.length()-1; i>= 0;i--) {
				
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
			
			results.add(pair.getSecond().substring(min, max+1));
			
		}
		
		return results;
		
	}

}
