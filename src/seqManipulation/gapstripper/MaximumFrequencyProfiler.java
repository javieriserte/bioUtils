package seqManipulation.gapstripper;

import java.util.Map;

/**
 * Creates a profile with the maximum frequency for each 
 * column of an alignment.
 * 
 * @author Javier Iserte
 *
 */
public class MaximumFrequencyProfiler extends AbstractProfiler {
	/**
	 * Calculates the maximum frequency profile values for a given alignment. 
	 * Calculates the value for one position in each call.
	 *  
	 * @param profile is the array where the value is stored
	 * @param i is the current position of the profile being calculated
	 * @param frequencies is a map whose keys are amino acids and values are 
	 *        the number of occurrences of this amino acid in the i-th column 
	 *        of the alignment.
	 * @param numberOfSequences is the number of non-gap positions in the i-th
	 *        column of the alignment.
	 */
	@Override
	protected void calculateProfileFromFrequencies(
			double[] profile, 
			int i,
			Map<Character, Double> frequencies, 
			double numberOfSequences) {
		double maxCount = getMaxCount(frequencies);
		// Search for the symbol with maximum 
		// frequency
		
		profile[i] = maxCount / numberOfSequences;
		// Adds the max frequency value to the result array
	}

	/**
	 * Search for the alphabet symbol that is most frequent in a column
	 * of an alignment.
	 * 
	 * @param frequencies are the counts calculated for a given column
	 * @return the number of counts of the most common symbol.
	 */
	private double getMaxCount(Map<Character, Double> frequencies) {
		
		double maxCount = 0;
		
		for (char alphabetSymbol : frequencies.keySet()) {
			
			maxCount = Math.max(maxCount, frequencies.get(alphabetSymbol));
			
		}
		
		return maxCount;
		
	}

}
