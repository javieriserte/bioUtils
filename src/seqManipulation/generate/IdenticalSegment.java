package seqManipulation.generate;

/**
 * Is a simple sequence generetor segment that always return the same
 * sequence.
 * 
 * @author Javier
 *
 */
public class IdenticalSegment implements Segment {

	///////////////////////////////
	// Instance variables
	private String initalSequence;
	// variable to store the sequence to be 
	// returned
	
	///////////////////////////////
	// Constructor
	/**
	 * Creates a new IdenticalSequence.
	 * 
	 * @param initalSequence is the sequence that will be returned
	 *        after generate is called.
	 */
	public IdenticalSegment(String initalSequence) {
		super();
		this.setInitalSequence(initalSequence);
	}


	///////////////////////////////
	// Public interface
	@Override
	/**
	 * Return the given sequence.
	 */
	public String generate() {
		
		return this.getInitalSequence();
		
	}

	///////////////////////////////////
	// Getters and setters
	protected String getInitalSequence() {
		return initalSequence;
	}

	protected void setInitalSequence(String initalSequence) {
		this.initalSequence = initalSequence;
	}
	
	

}
