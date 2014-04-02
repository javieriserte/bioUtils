package seqManipulation.fastamanipulator.commands;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import seqManipulation.AlignmentSequenceEditor;
import utils.mutualinformation.MICalculator;

import cmdGA.NoOption;
import pair.Pair;

/**
 * Calculates Mutual Information data for each pair of columns in a multiple sequence alignment.
 * 
 * @author javier iserte
 *
 */
public class CalculateMICommand extends FastaCommand<NoOption> {

	public CalculateMICommand(InputStream inputstream, PrintStream output, 	NoOption option) {
		
		super(inputstream, output, option);
		
	}

	@Override
	protected List<String> performAction() {
		
		List<Pair<String, String>> sequences = this.getSequences();
		
		List<String> results = new ArrayList<String>();
		
		AlignmentSequenceEditor ase = new AlignmentSequenceEditor(sequences);

		int length = ase.getRowsSize();
		
		Map<Pair<Integer, Integer>, Double> MI = MICalculator.calculateProteinMIMatrix(sequences);
			
		for (int i = 0; i <length;i++ ) {
			
			StringBuilder currentLine = new StringBuilder();

			for (int j = 0; j <length;j++ ) {
				
				int pa = Math.min(i, j);
				
				int pb = Math.max(i, j);
				
				if (pa==pb) {
					
					currentLine.append(0);
					
				} else {
					
					double value = MI.get(new Pair<Integer,Integer>(pa,pb));
					
					Locale defLocale = Locale.getDefault();
					
					currentLine.append(String.format(defLocale, "%.4f",value));
				}
				
				if (j<length-1)currentLine.append("; ");
				
			}

			results.add(currentLine.toString());
			
		}
		
		return results;
		
	}

}
