package utils.mutualinformation;

import java.util.HashMap;
import java.util.Map;

/**
 * Calculates the Shannon Information Entropy for a list of data character.
 * Its purpose is to be used for calculate entropy for Multiple Sequence Alignment 
 * 
 * 
 * @author Javier Iserte (jiserte@unq.edu.ar, jiserte@leloir.org.ar)
 *
 */
public class EntropyCalculator {
	
	private static char gapChar = '-' ;
	
	/**
	 * Calculates the shannon entropy for a group of characters.
	 * The characters are supposed to correspond to a column of a 
	 * Multiple Sequence Alignment of DNA or Protein. 
	 * 
	 * @param characters is the group of character whose entropy will be calculated
	 * @param alphabetSize is size of the set from which the character could be chosen. 
	 * 	       This value is 4 for DNA and 20 for proteins.
	 * @param countGaps decides if gap characters ('-') will be taken into account for 
	 *         frequency calculus.
	 * @return the Shannon informative entropy for this group of chars.
	 */
	public static double calculateEntropy(Character[] characters, int alphabetSize, boolean countGaps) {
		
		// a Map to store the frequencies
		Map<Object, Double> freq = EntropyCalculator.countCharacters(characters);
		
		// The total number of non-gapped characters in the data
		int numberOfElements = characters.length;
		
		if (!countGaps && freq.containsKey(gapChar)) {
			numberOfElements -= freq.get(gapChar);
			};
	
		// Converts the counts to frequencies
		EntropyCalculator.convertToFrequencies(freq, numberOfElements);
		
		// The entropy sum
		double entropy = EntropyCalculator.calculateEntropy(freq, alphabetSize) ;
		
		return entropy;
		
	}

	
	/**
	 * Calculate the entropy sum from frequencies.
	 * <pre>
	 * Entropy = SUM_x ( fr(x) * log_alphabetsize ( fr(x) ) ) 
	 * </pre>
	 * 
	 * @param freq a Map from characters to frequencies.
	 * @param alphabetSize the size of the set from which the characters can be chosen.
	 * @return
	 */
	public static double calculateEntropy(Map<Object, Double> freq, 	int alphabetSize) {
		// The entropy sum
		double entropy = 0;
		
		for (Object element : freq.keySet()) {
			
			// Only look for non-gapped rows
			if (String.valueOf(element).indexOf(gapChar) < 0) {
				
				entropy -= freq.get(element) * Math.log(freq.get(element)) / Math.log(alphabetSize);
				
			}
			
		}
		
		return entropy;
	}


	/**
	 * Calculates the shannon entropy for a group of characters.
	 * The characters are supposed to correspond to a column of a 
	 * Multiple Sequence Alignment of DNA or Protein. 
	 * 
	 * @param characters is the group of character whose entropy will be calculated
	 * @param alphabetSize is size of the set from which the character could be chosen. 
	 * 	       This value is 4 for DNA and 20 for proteins.
	 * @param countGaps decides if gap characters ('-') will be taken into account for 
	 *         frequency calculus.
	 * @return the Shannon informative entropy for this group of chars.
	 * @throws CharGroupSizeException 
	 */
	public static double calculateEntropy(Character[] characters_a, Character[] characters_b, int alphabetSize, boolean countGaps) throws CharGroupSizeException {
		
		if (characters_a.length != characters_b.length) {throw new CharGroupSizeException();}
		
		// a Map to store the frequencies
		Map<Object, Double> freq = EntropyCalculator.countDiCharacters(characters_a,characters_b);
		
		// The total number of non-gapped characters in the data
		int numberOfElements = characters_a.length;
		
		if (!countGaps && freq.containsKey(gapChar)) {
			numberOfElements -= freq.get(gapChar);
			};
	
		// Converts the counts to frequencies
		EntropyCalculator.convertToFrequencies(freq, numberOfElements);
		
		// The entropy sum
		double entropy = EntropyCalculator.calculateEntropy(freq, alphabetSize) ;
		
		return entropy;
		
	}
	



	/**
	 * Given a Map of Characters to Integers that counts the number of repetitions repetitions 
	 * for a character, converts it to frequencies dividing each value for the total number of elements.
	 * 
	 * @param freq
	 * @param numberOfElements
	 */
	public static void convertToFrequencies(Map<Object, Double> freq, int numberOfElements) {
		
		for (Object element : freq.keySet()) {
			
			freq.put(element, Double.valueOf(freq.get(element)/numberOfElements));
			
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
	public static Map<Object, Double> countCharacters(Character[] characters) {
		
		return countElements(characters);
		
	}

	/**
	 * Count the number of different pairs of char between two list of chars.
	 * The two list are assumed to have the same length, and dicharacters are count
	 * following its order in both list, (i.e. first dicharacter is formed by the 
	 * first char of the first list and the first char of the second list. The second 
	 * dicharacter is formed by the second char of the first list and the second char 
	 * of the second list.) 
	 * 
	 * @param characters_a 
	 * @param characters_b
	 * @return
	 */
	private static Map<Object, Double> countDiCharacters( 	Character[] characters_a, Character[] characters_b) {
		
		String[] dichars = new String[characters_a.length];
		
		for (int i=0; i<dichars.length;i++) {
			
			dichars[i] = new String(new char[]{characters_a[i],characters_b[i]});
			
		}
		
		return EntropyCalculator.countElements(dichars);
		
	}

	/**
	 * Count elements
	 * 
	 * @param elements
	 * @return
	 */
	private static Map<Object, Double> countElements(Object[] elements) {
		
		Map<Object, Double> counter = new HashMap<Object, Double>();
		
		for (Object c : elements) {
			
			if (counter.containsKey(c)) {
				
				counter.put(c, 1+counter.get(c));
			
			} else {
			
				counter.put(c, 1d);
			
			}
			
		}
		
		return counter;
	}
	
}
