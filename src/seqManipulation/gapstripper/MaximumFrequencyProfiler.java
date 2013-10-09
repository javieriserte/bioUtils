package seqManipulation.gapstripper;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Creates a profile with the maximum frequency for each 
 * column of an alignment.
 * 
 * @author Javier Iserte
 *
 */
// TODO join this profiler with profiler objects in 'utils.ConservationImage'
// package.
public class MaximumFrequencyProfiler {
	
	/////////////////////////////////
	// Class Constants
	private static final char gap = '-';

	/////////////////////////////////
	// Public Methods
	/**
	 * Calculates the maximum frequency profile
	 * for a given alignment
	 *  
	 * @param sequences is a Map representing an alignment.
	 * @return a double array with the profile values.
	 */
	public double[] calculateProfile(Map<String, String> sequences) {
		
		Set<String> descriptions = sequences.keySet();
		// Retrieves the description key of sequences

		int AlignmentLength = sequences.get(descriptions.iterator().next()).length();
		// Retrieves the length of the alignment
		// Assumes that all sequences has the same length. 

		double[] profile = new double[AlignmentLength];
		// Creates a data structure to hold and return the profile.

		for (int i = 0; i<AlignmentLength; i++) {
		// iterates over each column of the alignment
		
			getColumnMaxFreq(sequences, descriptions, profile, i);
			
		}

		return profile;
	}

	/////////////////////////////////
	// Private Methods
	private void getColumnMaxFreq(Map<String, String> sequences,
			Set<String> descriptions, double[] profile, int i) {
		
		Map<Character,Integer> frequencies = new HashMap<>();
		// Creates a temporary data structure to accumulate
		// frequencies of each symbol of the alphabet
		
		int counter = 0;
		// Counter to store the number of non-gap characters
		
		for (String currentSeq : descriptions) {
		// Iterates over each position
		// of the current column
			
			char currentChar = sequences.get(currentSeq).charAt(i);
			
			if (currentChar != MaximumFrequencyProfiler.gap) {
			
				if (!frequencies.containsKey(currentChar)) {
					// Initializes the frequencies map
					// for a given new char
					
					frequencies.put(currentChar, 0);
					
				}
				
				frequencies.put(currentChar, frequencies.get(currentChar) + 1 );
				// Adds one to the count
				// of this character
				
				counter++;
				// Also, adds one the the counter of all non-gap chars.
				
			}
			
		}
		
		int maxCount = getMaxCount(frequencies);
		// Search for the symbol with maximum 
		// frequency
		
		profile[i] = (double) maxCount / (double) counter;
		// Adds the max frequency value to the result array
	}

	/**
	 * Search for the alphabet symbol that is most frequent in a column
	 * of an alignment.
	 * 
	 * @param frequencies are the counts calculated for a given column
	 * @return the number of counts of the most common symbol.
	 */
	private int getMaxCount(Map<Character, Integer> frequencies) {
		
		int maxCount = 0;
		
		for (char alphabetSymbol : frequencies.keySet()) {
			
			maxCount = Math.max(maxCount, frequencies.get(alphabetSymbol));
			
		}
		
		return maxCount;
		
	}
	
}
