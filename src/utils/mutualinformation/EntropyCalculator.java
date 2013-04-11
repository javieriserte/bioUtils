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
	
	private static char gapChar = '-' ;

	public static double calculateEntropy(Character[] characters, int alphabetSize, boolean countGaps) {
		
		// a Map to store the frequencies
		Map<Character,Integer> freq = EntropyCalculator.countCharacters(characters);
		
		// The total number of non-gapped characters in the data
		int numberOfElements = characters.length;
		
		if (countGaps) {numberOfElements -= freq.get(gapChar)		;};
	
		// Converts the counts to frequencies
		EntropyCalculator.convertToFrequencies(freq, numberOfElements);
		
		// The entropy sum
		double entropy = EntropyCalculator.calculateEntropy(freq, alphabetSize) ;
		
		return 0;
		
		
	}

	
	private static double calculateEntropy(Map<Character, Integer> freq, 	int alphabetSize) {
		// The entropy
		double entropy = 0;
		
		for (Character character : freq.keySet()) {
			
			if (character != gapChar) {
				
				entropy -= freq.get(character) * Math.log10(freq.get(character));
				
			}
			
		}
		
		
		
		return entropy;
	}


	/**
	 * Given a Map of Characters to Integers that counts the number of repetitions repetitions 
	 * for a character, converts it to frequencies dividing each value for the total number of elements.
	 * 
	 * @param freq
	 * @param numberOfElements
	 */
	private static void convertToFrequencies(Map<Character, Integer> freq, int numberOfElements) {
		
		for (Character character : freq.keySet()) {
			
			freq.put(character, freq.get(character)/numberOfElements);
			
		}
		
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
	public static Map<Character, Integer> countCharacters(Character[] characters) {
		
		Map<Character, Integer> counter = new HashMap<Character, Integer>();
		
		for (Character c : characters) {
			
			if (counter.containsKey(c)) {
				
				counter.put(c, 1+counter.get(c));
			
			} else {
			
				counter.put(c, 1);
			
			}

			
		}
		
		return counter;
	}
	
}
