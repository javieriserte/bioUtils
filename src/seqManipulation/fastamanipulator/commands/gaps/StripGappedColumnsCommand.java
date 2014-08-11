package seqManipulation.fastamanipulator.commands.gaps;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import cmdGA2.NoArgumentOption;
import pair.Pair;
import seqManipulation.fastamanipulator.commands.FastaCommand;

public class StripGappedColumnsCommand extends FastaCommand<NoArgumentOption> {

	public StripGappedColumnsCommand(InputStream inputstream, PrintStream output, NoArgumentOption option) {
		
		super(inputstream, output, option);
		
	}

	@Override
	protected List<String> performAction() {
		
		List<String> results = new ArrayList<String>();
		
		List<Pair<String, String>> seqs = this.getSequences();
		// removes the columns of the alignment that contain a gap
		boolean[] keeper = new boolean[seqs.get(0).getSecond().length()];
		
		for (int i=0; i<keeper.length; i++) {
			keeper[i] = true;
		}
		
		for (Pair<String, String> seq : seqs) {
			
			for (int j = 0; j< seq.getSecond().length();j++) {
				
				keeper[j] = keeper[j] && !(seq.getSecond().charAt(j) == '-');
				
			}
			
		}
		
		for (Pair<String, String> seq : seqs) {
			
			StringBuilder nseq = new StringBuilder();
			
			String oldSeq = seq.getSecond();
			
			for (int j = 0; j< oldSeq.length();j++) {
				
				if (keeper[j]) nseq.append(oldSeq.charAt(j)); 
				
			}
			
			results.add(">" + seq.getFirst());
			
			results.add(nseq.toString());
			
		}
		
		return results;
		
	}

	
	
}
