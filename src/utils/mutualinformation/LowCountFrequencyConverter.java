package utils.mutualinformation;

import java.util.Map;
import java.util.Set;

public class LowCountFrequencyConverter extends FrequencyConverter {

	
	private double lambda;
	private Object[] alphabet;
	private MolType type;
	
	@SuppressWarnings("unused")
	private LowCountFrequencyConverter() {
		super();
	}
	
	public LowCountFrequencyConverter(double lambda, MolType type) {
		super();
		this.lambda = lambda;
		this.type = type;
	}

	@Override
	public void convertToFrequencies(Map<Object, Double> freq, int numberOfElements) {

		this.convertToLowCountFrequencies(freq, numberOfElements, lambda, alphabet, numberOfColumns);
		
	}

	/**
	 * Given a Map of Characters to Integers that counts the number of repetitions repetitions 
	 * for a character, converts it to frequencies with a low count correction by Buslje:2009.
	 * 
	 * <pre>
	 * fr(a,b) = (lambda + N(a,b)) / Sum [(lambda + N(x,y))]
	 * </pre>
	 * 
	 * @param freq
	 * @param numberOfElements
	 */
	public void convertToLowCountFrequencies(Map<Object, Double> freq, int numberOfElements, double lambda, Object[] alphabet, int numberOfColumns) {
		
		Set<Object> keys = freq.keySet();
		
		for (Object element : alphabet) {
			
			if (keys.contains(element)) {

				freq.put(element, Double.valueOf( ( Math.pow(alphabet.length, 1 / numberOfColumns) * lambda + freq.get(element)) / (numberOfElements + alphabet.length * lambda)));
				
			} else {
				
				freq.put(element, Double.valueOf(  Math.pow(alphabet.length, 2 / numberOfColumns) * lambda / (numberOfElements + alphabet.length * lambda)));
				
			}
			
			
		}
		
	}
	
	// Getters & Setters
	
	public double getLambda() {
		return lambda;
	}

	public void setLambda(double lambda) {
		this.lambda = lambda;
	}

	public Object[] getAlphabet() {
		return alphabet;
	}

	public void setAlphabet(Object[] alphabet) {
		this.alphabet = alphabet;
	}
	
	@Override
	public void setNumberOfColumns(int numberOfColumns) {
		super.setNumberOfColumns(numberOfColumns);
		
		switch (this.type) {
		case Protein:

			Object[] a = new Object[]{'Q','W','E','R','T','Y','I','P','A','S','D','F','G','H','K','L','C','V','N','M'};
			
			this.combineArray(a, numberOfColumns);

			break;

		case Nucleic:
			
			break;

		}
		
		
//		alphabet
//		TODO
		
	}

	private void combineArray(Object[] a, int numberOfColumns) {
		
		int l = a.length ^ numberOfColumns;

		Object[] result = new Object[l];
		
		int counter;
	
		for (int i = 0; i<l; i++) {
			
			for (int j = 0; j<numberOfColumns; j++) {

				
			}
		
			StringBuilder sb = new StringBuilder();
			
			for (int j = 0; j<numberOfColumns; j++) {
				
				counter = i % (a.length^j);

				sb.append(a[counter]);
				
			}
			
			result[i] = sb.toString();
			
			
			
		}
		
	}
	
	
	
}
