package seqManipulation.gapstripper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import clustering.HobohmClusteringM1;
import pair.Pair;

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
	 * for a given alignment without perform clustering before.
	 * 
	 * @param sequences a map of sequences and descriptions
	 * @return an array of the length of the alignment. In each position 
	 *          of the array is the maximum frequency for one column of 
	 *          the alignment. 
	 */
	public double[] calculateProfileWithoutClustering(Map<String, String> sequences) {

		Map<String,Double> clusterWeigth = new HashMap<>();
		
		for (String description : sequences.keySet()) {
			
			clusterWeigth.put(description, 1d);
			
		}
		
		return calculateProfile(sequences,clusterWeigth);

	}
	
	/**
	 * Calculates the maximum frequency profile
	 * for a given alignment performing clustering before.
	 * Clustering gives a weight to every sequence in the alignment as:
	 * <pre>
	 * w = 1 / (number of sequences in the cluster)  
	 * </pre>
	 * 
	 * @param sequences a map of sequences and descriptions
	 * @return an array of the length of the alignment. In each position 
	 *          of the array is the maximum frequency for one column of 
	 *          the alignment. 
	 */
	public double[] calculateProfileUsingClustering(Map<String, String> sequences, double thresholdId) {
		

		Map<String,Double> clusterWeigth = new HashMap<>();
		
		HobohmClusteringM1 clusterer = new HobohmClusteringM1();
		
		List<Pair<String,String>> sequenceList = new ArrayList<>();
		
		for (String  description : sequences.keySet()) {
			
			sequenceList.add(new Pair<String, String>(description, sequences.get(description)));
			
		}
		
		Set<List<Pair<String, String>>> clusters = clusterer.clusterize(sequenceList, thresholdId);

		for (List<Pair<String, String>> list : clusters) {
			
			double currentClusterWeight = list.size();
			
			for (Pair<String, String> pair : list) {
				
				clusterWeigth.put(pair.getFirst(), 1/currentClusterWeight);
				
			}
			
		}
		
		return calculateProfile(sequences,clusterWeigth);
		
	}

	/////////////////////////////////
	// Private Methods
	
	/**
	 * Calculates the maximum frequency profile
	 * for a given alignment
	 *  
	 * @param sequences is a Map representing an alignment.
	 * @return a double array with the profile values.
	 */
	private double[] calculateProfile(Map<String, String> sequences, Map<String,Double> clusterWeigth) {
		
		Set<String> descriptions = sequences.keySet();
		// Retrieves the description key of sequences

		int AlignmentLength = sequences.get(descriptions.iterator().next()).length();
		// Retrieves the length of the alignment
		// Assumes that all sequences has the same length. 

		double[] profile = new double[AlignmentLength];
		// Creates a data structure to hold and return the profile.

		for (int i = 0; i<AlignmentLength; i++) {
		// iterates over each column of the alignment
		
			getColumnMaxFreq(sequences, descriptions, profile, i, clusterWeigth);
			
		}

		return profile;
	}
	
	private void getColumnMaxFreq(Map<String, String> sequences,
			Set<String> descriptions, double[] profile, int i, Map<String,Double> clusterWeigth) {
		
		Map<Character,Double> frequencies = new HashMap<>();
		// Creates a temporary data structure to accumulate
		// frequencies of each symbol of the alphabet
		
		double counter = 0;
		// Counter to store the number of non-gap characters
		// or weights if clustering is used
		
		for (String currentSeq : descriptions) {
		// Iterates over each position
		// of the current column
			
			char currentChar = sequences.get(currentSeq).charAt(i);
			
			if (currentChar != MaximumFrequencyProfiler.gap) {
			
				if (!frequencies.containsKey(currentChar)) {
					// Initializes the frequencies map
					// for a given new char
					
					frequencies.put(currentChar, 0d);
					
				}
				
				frequencies.put(currentChar, frequencies.get(currentChar) + clusterWeigth.get(currentSeq) );
				// Adds one to the count
				// of this character
				
				counter = counter + clusterWeigth.get(currentSeq);
				// Also, adds one the the counter of all non-gap chars.
				
			}
			
		}
		
		double maxCount = getMaxCount(frequencies);
		// Search for the symbol with maximum 
		// frequency
		
		profile[i] = maxCount / counter;
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
