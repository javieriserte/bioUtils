package seqManipulation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fileformats.fastaIO.Pair;
/**
 * Calculates a Identity Matrix from a sequence alignment.
 * The matrix returned is implemented as a map from pair(a,b) to double precision numbers.
 * All values are in the [0,1] range. 
 * 
 * @author javier iserte
 *
 */
public class IndentityMatrixCalculator {

	///////////////////////////
	// Public Class Interface

	/**
	 * Retrieves the identity Matrix
	 * @param alignment
	 * @return
	 */
	public static Map<Pair<Integer,Integer>,Double> calculateIdentityMatrix(List<Pair<String,String>> alignment) {
		
		Map<Pair<Integer,Integer>,Double> result =  new HashMap<Pair<Integer,Integer>,Double>();
		
		for (int i=0;i<alignment.size()-1;i++) {
			
			for (int j=i+1; j<alignment.size();j++) {
				
				result.put(new Pair<Integer,Integer>(i,j),calculateIdentity(alignment.get(i).getSecond(),alignment.get(j).getSecond()));
				
			}
			
		}
		
		return result;
	}

	/**
	 * Retrieve a list of the values in the matrix, discarding the row and column information 
	 * 
	 * @param alignment
	 * @return
	 */
	public static List<Double> listOfIdentityValues(List<Pair<String,String>> alignment) {
		
		
		List<Double> result = new ArrayList<Double>();
		
		Map<Pair<Integer, Integer>, Double> a = calculateIdentityMatrix(alignment);

		int matrixSize = alignment.size();
		
		for(int i=0; i<matrixSize-1 ; i++) {
			
			for (int j=i+1; j<matrixSize; j++) {
				
				result.add(a.get(new Pair<Integer,Integer>(i,j)));
				
			}
			
		}
		
		return result;
		
	}
	
	///////////////////////////
	// Private Class Methods

	/**
	 * Performs the calculation of identity between two sequences.
	 * 
	 * @param s1
	 * @param s2
	 * @return
	 */
	private static double calculateIdentity(String s1, String s2) {

		double result = 0;
		
		if (s1.length() == s2.length()) {
			
			int counter =0;
			
			int total = 0;
			
			for (int i =0; i < s1.length(); i++) {
				
				char char_1 = Character.toUpperCase(s1.charAt(i));
				
				char char_2 = Character.toUpperCase(s2.charAt(i));
				
				// If both chars are equal and different from a gap it is an identity.
				boolean condition = (char_1 == char_2) && char_1 != '-';
				
				counter += condition ? 1 : 0;

				// If both chars are equal to a gap, this column must not be taken into account.
				total += ((char_1 == char_2) && char_1 == '-') ? 0:1;
				
			}
			
			result = ((double) counter) / total;
			
		}
		
		return result;
	}
	
}
