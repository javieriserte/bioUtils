package fileformats.genBankIO.elements;

/**
 * A class to store the origin data of a GenBank record.
 * 
 * @author Javier Iserte (jiserte@unq.edu.ar)
 *
 */
public class Origin {
	// PRIVATE INSTANCE VARIABLES
	private String sequence;
	
	// CONSTRUCTOR
	public Origin(String sequence) {
		super();
		this.sequence = sequence;
	}

	public Origin() {
		super();
	}

	// GETTERS AND SETTERS
	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
		
	}
	
	
	
}
