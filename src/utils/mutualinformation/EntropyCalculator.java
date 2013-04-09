package utils.mutualinformation;

import java.util.HashMap;
import java.util.Map;

/**
 * Calculates the Shannon Information Entropy for a list of data character.
 * Its purpose is to be used for calculate entropy for Multiple Sequence Alignment 
 * 
 * 
 * @author Javier Iserte
 *
 */
public class EntropyCalculator {

	public static double calculateEntropy(Character[] characters, int alphabetSize, boolean countGaps) {
		
		// a Map to store the frequencies
		Map<Character,Integer> freq = new HashMap<Character, Integer>();
		
		// The total number of non-gapped characters in the data
		
		int numberOfElements = countCharacters(characters, countGaps, freq);
		
		
		for (Character character : freq.keySet()) {
			
			freq.put(character, freq.get(character)/numberOfElements);
			
			
		}
		
		
		
		
		
		return 0;
		
		
	}

	/**
	 * Counts the total number of characters of a list of characters, including or not the gaps.
	 * Also, counts the number repetitions of each particular character. 
	 * 
	 * @param characters
	 * @param countGaps
	 * @param freq
	 * @return
	 */
	private static int countCharacters(Character[] characters, boolean countGaps, Map<Character, Integer> freq) {
		
		int numberOfElements = 0;
		
		for (Character c : characters) {
			
			if (!c.equals('-') && !countGaps) {
			
				numberOfElements =+ 1;
				
				if (freq.containsKey(c)) {
					
					freq.put(c, 1+freq.get(c));
				
				} else {
				
					freq.put(c, 1);
				
				}
			
			}
			
		}
		return numberOfElements;
	}
	
}
