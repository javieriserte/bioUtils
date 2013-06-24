package seqManipulation.fastamanipulator.commands;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import seqManipulation.identity.IndentityMatrixCalculator;

import cmdGA.NoOption;
import fileformats.fastaIO.Pair;

/**
 * Retrieves a list of indentity values for a given alignment.
 * Identity values are calculated as: 
 * <pre>
 * (Count of identities) / (total),
 * where 
 *       a Count of identity is when the two sequences has the same char at a given position and this char is not a gap.
 *       total is the number of positions that both sequences are not gaps.
 * </pre>
 * 
 * 
 * @author javier
 *
 */
public class IdentityValuesCommand extends FastaCommand<NoOption> {

	public IdentityValuesCommand(InputStream inputstream, PrintStream output, NoOption option) {
	
		super(inputstream, output, option);

	}

	@Override
	protected List<String> performAction() {

		List<String> results = new ArrayList<String>();;

		List<Pair<String, String>> seqs = this.getSequences();
		
		List<Double> values = IndentityMatrixCalculator.listOfIdentityValues(seqs);
		
		for (Double value : values) {

			results.add(String.valueOf(value));
			
		} 
		
		return results;
		
	}

}
