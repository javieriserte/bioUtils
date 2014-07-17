package seqManipulation.gapstripper;

import java.util.Map;
/**
 * Creates a profile with the entropy frequency for each 
 * column of an alignment.
 * 
 * @author Javier Iserte
 *
 */
public class EntropyProfiler extends AbstractProfiler {
	
	@Override
	protected void calculateProfileFromFrequencies(
			double[] profile, 
			int i,
			Map<Character, Double> frequencies, 
			double counter) {
	
		double entropy = 0;
		
		double correctingConstant = Math.log(20);
		
		for (char symbol : frequencies.keySet()) {
			
			double frequency = frequencies.get(symbol) / counter;
			
			entropy = entropy - Math.log(frequency) * ( frequency / correctingConstant);
			
		}
		
		profile[i] = entropy;
	}
	
}
