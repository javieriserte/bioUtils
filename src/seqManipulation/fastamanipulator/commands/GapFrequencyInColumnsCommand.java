package seqManipulation.fastamanipulator.commands;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import pair.Pair;
import cmdGA.NoOption;

public class GapFrequencyInColumnsCommand extends FastaCommand<NoOption> {

	public GapFrequencyInColumnsCommand(InputStream inputstream, PrintStream output, NoOption option) {
		super(inputstream, output, option);
	}

	@Override
	protected List<String> performAction() {
		List<Pair<String, String>> seq = this.getSequences();
		ArrayList<String> result = new ArrayList<String>();
		
		if (seq.size()>0) {
			
			double[] gap_freq = new double[seq.get(0).getSecond().length()];
		
			for (Pair<String, String> pair : seq) {
				
				String s = pair.getSecond();
				
				for (int i = 0; i<s.length(); i++) {
					
					gap_freq[i] += s.charAt(i)=='-'?1:0;
					
				}
				
			}
			
			for (int i = 0; i < gap_freq.length; i++) {
				
				double value = gap_freq[i] / (double) seq.size();
				
				result.add(String.valueOf(value));
				
			}
			
		}
		
		return result;
		
	}

}
