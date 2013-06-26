package seqManipulation.fastamanipulator.commands;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import cmdGA.NoOption;
import fileformats.fastaIO.Pair;

/**
 * Fills an alignment with gaps at the end of sequences that are smalles than the alignment itself.
 * 
 * @author javier
 *
 */
public class PadWithGapCommand extends FastaCommand<NoOption> {

	public PadWithGapCommand(InputStream inputstream, PrintStream output, NoOption option) {
		
		super(inputstream, output, option);

	}

	@Override
	protected List<String> performAction() {

		List<Pair<String, String>> sequences = this.getSequences();
		
		
		List<String> results = new ArrayList<String>();

		int maxlen = getMaxlength(sequences);

		for (Pair<String, String> pair : sequences) {
		
			results.add(">" + pair.getFirst());
			
			int length = pair.getSecond().length();
			
			if (length < maxlen) {
				
				int dif = maxlen - length; 
				
				StringBuilder sb = new StringBuilder(maxlen);
				
				sb.append(pair.getSecond());
				
				while(dif>0) {
					
					sb.append('-');
					
					dif--;
					
				}
				
				results.add(sb.toString());
				
			} else {
				
				results.add(pair.getSecond());
				
			}
			
		}
		
		return results;
		
	}

	public int getMaxlength(List<Pair<String, String>> sequences) {
		int maxlen = 0;
		
		for (Pair<String,String> pair: sequences) {
			
			maxlen = Math.max(pair.getSecond().length(),maxlen);
			
		}
		return maxlen;
	}

}
