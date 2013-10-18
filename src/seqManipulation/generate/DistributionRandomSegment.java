package seqManipulation.generate;

import java.util.HashMap;
import java.util.Map;

/**
 * Sequence generator segment that creates a new sequenc from a 
 * set of symbols, each one with a particular probability to appear.
 * 
 * 
 * @author Javier
 *
 */
public class DistributionRandomSegment implements Segment {

	/////////////////////////
	// Instance Variables
	private Map<Integer,Character> symbolMap;
	// Map data structure to associate each symbol  
	// to be used to generate the new sequence with a 
	// number.
	
	private double[] probabilities;
    // Data structure to contains the probabilities of each 
	// symbol (accumulated probabilities are stored, in the first
	// position is the probability of symbol one, in the second 
	// position is the probability of symbol one + probability of
	// symbol two. This is strategy to simplify the recovery a symbol
	// index from the output of a uniforam random distribution).
	// The positions in the array correspond to the symbol associated
	// with index number in the symbolMap map. 

	private int length;
	// desired length of the generated sequence. 

	///////////////////////////////////
	// Constructor
	public DistributionRandomSegment(int length) {
		super();
		this.setLength(length);
	}
	
	public DistributionRandomSegment() {
		super();
		this.setLength(0);
	}


	///////////////////////////////////
	// Public Interface
	/**
	 * Generates the new sequence with a given symbol
	 * distribution probability.
	 */
	@Override
	public String generate() {

		StringBuilder sequenceBuilder = new StringBuilder();
		
		int positionsRemaining = this.getLength();
		
		while (positionsRemaining>0) {
			
			double randomDouble = Math.random();
			
			int index = this.getSymbolIndex(randomDouble);
			
			sequenceBuilder.append(this.symbolMap.get(index));
			
			positionsRemaining--;
			
		}
		
		return sequenceBuilder.toString();
		
	}

	/**
	 * Sets amino acido frequenccy composition 
	 * using the values of UniProtKB db.
	 * Release 2013_10 of 16-Oct-13 of UniProtKB/Swiss-Prot 
	 * contains 541561 sequence entries,
	 * comprising 192480382 amino acids abstracted from 
	 * 223284 references. 
	 * Composition in percent for the complete database	

	 */
	public void setSymbolDistributionUniProtKBProteins(){
   
	   Map<Character,Double> symbolProbabilities = new HashMap<>();
			
		symbolProbabilities.put('A',8.25);
		symbolProbabilities.put('R',5.53);
		symbolProbabilities.put('N',4.06);
		symbolProbabilities.put('D',5.45);
		symbolProbabilities.put('C',1.37);
		symbolProbabilities.put('E',6.75);
		symbolProbabilities.put('Q',3.93);
		symbolProbabilities.put('G',7.07);
		symbolProbabilities.put('H',2.27);
		symbolProbabilities.put('I',5.95);
		symbolProbabilities.put('L',9.66);
		symbolProbabilities.put('K',5.84);
		symbolProbabilities.put('M',2.42);
		symbolProbabilities.put('F',3.86);
		symbolProbabilities.put('P',4.70);
		symbolProbabilities.put('S',6.57);
		symbolProbabilities.put('T',5.34);
		symbolProbabilities.put('W',1.08);
		symbolProbabilities.put('Y',2.92);
		symbolProbabilities.put('V',6.87);
	
		this.setSymbolDistribution(symbolProbabilities);
		
	}
	


	/**
	 * 
	 * @param probabilities
	 */
	public void setSymbolDistribution(Map<Character, Double> probabilities) {

		this.setSymbolMap(new HashMap<Integer, Character>());
		
		this.setProbabilities(new double[probabilities.size()]);

		int symbolCounter = 0;
		
		double accumulatedProbability = 0;
		
		for (Character symbol : probabilities.keySet()) {
		
			accumulatedProbability += probabilities.get(symbol);
			
			this.getProbabilities()[symbolCounter] = accumulatedProbability;
			
			this.getSymbolMap().put(symbolCounter, symbol);
			
			symbolCounter++;
			
		}
		
		
		for (int index = 0; index < this.getProbabilities().length; index++) {

			this.getProbabilities()[index] /= accumulatedProbability;
			// Normalize the probabilities to [0 , 1] range. 
			
		}
		
		
		
	}

	//////////////////////////////
	// Getters and Setters
	protected Map<Integer, Character> getSymbolMap() {
		return symbolMap;
	}

	protected void setSymbolMap(Map<Integer, Character> symbolMap) {
		this.symbolMap = symbolMap;
	}

	protected double[] getProbabilities() {
		return probabilities;
	}

	protected void setProbabilities(double[] probabilities) {
		this.probabilities = probabilities;
	}

	private void setLength(int length) {
		this.length = length;
		
	}
	protected int getLength() {
		return length;
	}

	/////////////////////////////////
	// protected methods
	
	protected int getSymbolIndex(double probability) {
		
		int indexCounter = 0;
		
		while (indexCounter < this.getProbabilities().length && 
			   this.getProbabilities()[indexCounter]<probability) {
			
			indexCounter++;
			
		}
		
		return indexCounter;
		
	}
	
}
