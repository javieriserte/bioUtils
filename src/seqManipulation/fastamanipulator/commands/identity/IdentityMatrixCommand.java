package seqManipulation.fastamanipulator.commands.identity;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import seqManipulation.fastamanipulator.commands.FastaCommand;
import seqManipulation.identity.IndentityMatrixCalculator;
import cmdGA2.NoArgumentOption;
import pair.Pair;

/**
 * Retrieves an squeare matrix with identity values for each pair of sequences in a MSA.
 * 
 * 
 * @author javier
 *
 */
public class IdentityMatrixCommand extends FastaCommand<NoArgumentOption> {

	public IdentityMatrixCommand(InputStream inputstream, PrintStream output, NoArgumentOption option) {

		super(inputstream, output, option);

	}

	@Override
	protected List<String> performAction() {
		
		List<Pair<String, String>> seqs;

		seqs = this.getSequences();

		List<String> results = new ArrayList<String>();

		Map<Pair<Integer, Integer>, Double> matrix = IndentityMatrixCalculator.calculateIdentityMatrix(seqs);
		
		int len = seqs.size();
		
		for (int i=0;i<len;i++) {
			
			StringBuilder sb = new StringBuilder();
			
			for (int j=0;j<len;j++) {
				
				double value = 1;
				
				int pi = Math.min(i, j);
				
				int pj = Math.max(i, j);
				
				Pair<Integer,Integer> pair = new Pair<Integer, Integer>(pi, pj);
				
				if (matrix.containsKey(pair)) {
					
					value = matrix.get(pair) ;
					
				}
				
				sb.append(value+ ";");
				
			}
			
			sb.deleteCharAt(sb.length()-1);

			results.add(sb.toString());
			
		}
		
		return results;
		
	}

}
