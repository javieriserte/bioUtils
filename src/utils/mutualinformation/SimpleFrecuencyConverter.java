package utils.mutualinformation;

import java.util.Map;

public class SimpleFrecuencyConverter extends FrequencyConverter {

	/**
	 * Given a Map of Characters to Integers that counts the number of repetitions repetitions 
	 * for a character, converts it to frequencies dividing each value for the total number of elements.
	 * 
	 * @param freq
	 * @param numberOfElements
	 */
	public void convertToFrequencies(Map<Object, Double> freq, int numberOfElements) {
		
		for (Object element : freq.keySet()) {
			
			freq.put(element, Double.valueOf(freq.get(element)/numberOfElements));
			
		}
		
	}
	
}
