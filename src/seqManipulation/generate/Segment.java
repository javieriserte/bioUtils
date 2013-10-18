package seqManipulation.generate;

/**
 * Interface for objects the creates segments of new sequences. 
 * 
 * @author Javier Iserte
 *
 */
public interface Segment {

	/**
	 * The only method in the public interface required 
	 * is <code>generate</code>. Each <code>Segment</code> 
	 * implementatin must have methods to explicit sequene 
	 * lengths, and other parameters.
	 *  
	 * @return a new sequence
	 */
	public String generate();
	
}
