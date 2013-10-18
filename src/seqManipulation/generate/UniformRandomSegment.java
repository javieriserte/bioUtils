package seqManipulation.generate;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * A sequence generator segment that generates a random 
 * sequence from a given set of symbols. Each symbol is 
 * selected from a uniform probability distribution.
 * 
 * @author Javier Iserte
 *
 */
public class UniformRandomSegment implements Segment {

	///////////////////////////
	// Instance variables
	private boolean gapsUsed;
	// Flag to indicate thar gaps will be introduced into the generated
	// Sequence.

	private Set<Character> chars;
	// Set of symbols that will be sampled to generate the sequence
	
	private int length;
	// The desired length of the new generated sequence 

	////////////////////////////
	// Constructor
	/**
	 * Creates a new UniformRandomSegment.
	 * The length of the desired sequence is required ny this constructor.
	 * Gaps are set to not be included, if gaps are wanted then use 
	 * setGapsUsed method.
	 * 
	 * @param length is the desired length of the generated sequence.
	 */
	public UniformRandomSegment(int length) {
		super();
		this.setGapsUsed(false);
		this.setLength(length);
	}

	/**
	 * Creates a new UniformRandomSegment.
	 * The length of the desired sequence is set zero. 
	 * To change this property use the setLength method. 
	 * Gaps are set to not be included, if gaps are wanted then use 
	 * setGapsUsed method.
	 */
	public UniformRandomSegment() {
		super();
		this.setGapsUsed(false);
		this.setLength(0);
	}

	
	////////////////////////////
	// Public Interface
	@Override
	public String generate() {

		int counter = 0;
		// variable to enumerate from 0 to N-1 the symbols
		// to be used. N is the symbol alphabet size.
		
		Map<Integer,Character> symbolMap = new HashMap<>();
		// Map data structure to access to any symbol with 
		// a value from 0 to N-1. N is the symbol alphabet size.
		
 
		for (Character symbol : this.getChars()) {
		// Fills the map...			
			
			symbolMap.put(counter, symbol);
			// associate each symbol with a 
			// number.

			counter ++;
			// update the counter to add the next value.
		}
		
		if (this.areGapsUsed()) {
		// If gaps are included...
			symbolMap.put(counter, '-');
			// ... adds them to the symbol map too.
			
		}
			
		StringBuilder sequenceBuilder = new StringBuilder();
		// Variable to store the growing sequence
		
		int positionsRemaining = this.getLength();
		// varaible to store the number of symbols
		// that are left to to be added in the new sequence.
		// Initially set to the desired length of the sequnce.
		
		while (positionsRemaining > 0) {
		// if there symbols left to be included...
			
			int randomInt = (int)(Math.random()* symbolMap.size());
			// select a random number from 0 to N-1.
			// N is the symbol alphabet size.
			
			sequenceBuilder.append(symbolMap.get(randomInt));
			// adds the new symbol to the growing sequence.
			
			positionsRemaining--;
			// updates the number of symbols left.
			
		}
		
		return sequenceBuilder.toString();
		
	}

	/**
	 * Indicates the set of symbols to be sampled to generate the sequence.
	 * If other symbols where set before, thay will be erased.
	 * Is no needed to add a gap symbol if gaps are desired to be included.
	 * 
	 * @param chars a string formed by all the characters to be included
	 */
	public void setCharacters(String chars) {
		
		char[] symbols = chars.toCharArray();
		
		Set<Character> symbolSet = new HashSet<>();
		
		for (char c : symbols) {

			symbolSet.add(c);
		}
		
		this.setCharacters(symbolSet);
		
	}
	
	/**
	 * Indicates the set of symbols to be sampled to generate the sequence.
	 * If other symbols where set before, thay will be erased.
	 * Is no needed to add a gap symbol if gaps are desired to be included.
	 * 
	 * @param chars a collection of the chars to be included
	 */
	public void setCharacters(Collection<Character> chars) {
		
		this.setChars(new HashSet<Character>());
		
		this.getChars().addAll(chars);
		
		if (chars.contains('-')) {
			
			this.getChars().remove('-');
			
		}
		
	}
	/**
	 * Indicates that the set of symbols to be sampled to generate the sequence
	 * are the 20 standard amino acids.
	 */
	public void setProteinCharacters() {
		
		this.setCharacters("QWERTYIPASDFGHKLCVNM");
		
	}

	/**
	 * Indicates that the set of symbols to be sampled to generate the sequence
	 * are the four DNA bases.
	 */
	public void setDNACharacters() {
		
		this.setCharacters("ACTG");
		
	}
	
	///////////////////////////////
	// Getters and Setters
	protected Set<Character> getChars() {
		return chars;
	}

	protected void setChars(Set<Character> chars) {
		this.chars = chars;
	}
	
	public boolean areGapsUsed() {
		return gapsUsed;
	}

	public void setGapsUsed(boolean useGaps) {
		this.gapsUsed = useGaps;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}
	
	
}
