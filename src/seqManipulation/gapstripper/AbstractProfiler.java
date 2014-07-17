package seqManipulation.gapstripper;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class AbstractProfiler {

	////////////////////////////////////////////////////////////////////////////
	// Class Constants
	static final char gap = '-';
    ////////////////////////////////////////////////////////////////////////////

	////////////////////////////////////////////////////////////////////////////
	// Abstract Methods
	protected abstract void calculateProfileFromFrequencies(double[] profile, int i, Map<Character, Double> frequencies, double counter);
	////////////////////////////////////////////////////////////////////////////
	
	////////////////////////////////////////////////////////////////////////////
	// public interface
	/**
	 * Calculates the profile for a given alignment
	 *  
	 * @param sequences is a Map representing an alignment.
	 * @param weighter is an object that weights differentially each sequence 
	 *        in the alignment.
	 * @return a double array with the profile values.
	 */
	public double[] calculateProfile(Map<String, String> sequences, SequenceWeighter weighter) {
		
		Set<String> descriptions = sequences.keySet();
		// Retrieves the description key of sequences

		int AlignmentLength = sequences.get(descriptions.iterator().next()).length();
		// Retrieves the length of the alignment
		// Assumes that all sequences has the same length. 

		double[] profile = new double[AlignmentLength];
		// Creates a data structure to hold and return the profile.

		for (int i = 0; i<AlignmentLength; i++) {
		// iterates over each column of the alignment
		
			this.getColumnProfileValue(sequences, descriptions, profile, i, weighter);
			
		}

		return profile;
	}
	////////////////////////////////////////////////////////////////////////////

	////////////////////////////////////////////////////////////////////////////
	// Protected methods
	/**
	 * Calculates the value for a given position according to the given 
	 * profiler object. Uses a template method design pattern.
	 * 
	 * @param sequences
	 * @param descriptions
	 * @param profile
	 * @param currentPosition
	 * @param weighter
	 */
	protected void getColumnProfileValue(
			Map<String, String> sequences, 
			Set<String> descriptions,
			double[] profile, 
			int currentPosition, 
			SequenceWeighter weighter) {
				
		Map<Character,Double> frequencies = new HashMap<>();
		// Creates a temporary data structure to accumulate
		// frequencies of each symbol of the alphabet
		
		double totalWeight = this.calculateFrequencies(sequences, descriptions, currentPosition, weighter, frequencies);
		// Frequencies for the current column of the alignment is stored in 
		// 'frequencies' map. The above function returns the sum of all weights
		// in the column. The totalWeight may differ from the expected value
		// if the column contain gaps.
		
		this.calculateProfileFromFrequencies(profile, currentPosition, frequencies, totalWeight);
		// The calculated profile value for the current position is stored on
		// 'profile' array.
		
	}
	////////////////////////////////////////////////////////////////////////////

	protected double calculateFrequencies(Map<String, String> sequences,
			Set<String> descriptions, int currentPosition,
			SequenceWeighter weighter, Map<Character, Double> frequencies) {
		double totalWeight = 0;
		// Counter to store the number of non-gap characters
		// or weights if clustering is used
		
		for (String currentSeq : descriptions) {
		// Iterates over each position
		// of the current column
			
			char currentChar = sequences.get(currentSeq).charAt(currentPosition);
			
			if (currentChar != AbstractProfiler.gap) {
			
				if (!frequencies.containsKey(currentChar)) {
					// Initializes the frequencies map
					// for a given new char
					
					frequencies.put(currentChar, 0d);
					
				}
				
				frequencies.put(currentChar, frequencies.get(currentChar) + weighter.getWeight(currentSeq) );
				// Adds one to the count
				// of this character
				
				totalWeight = totalWeight + weighter.getWeight(currentSeq);
				// Also, adds one the the counter of all non-gap chars.
				
			}
			
		}
		return totalWeight;
	}
	
}
