package fileformats.genBankIO.elements;

import java.io.Serializable;

/**
 * A class to store the origin data of a GenBank record.
 * 
 * @author Javier Iserte (jiserte@unq.edu.ar)
 *
 */
public class Origin implements Serializable {
	
	/////////////////////////////
	// PRIVATE INSTANCE VARIABLES
	private String sequence;
	private static final long serialVersionUID = -8242477689620564368L;
	
	/////////////////////////////
	// CONSTRUCTOR
	public Origin(String sequence) {
		super();
		this.sequence = sequence;
	}

	public Origin() {
		super();
	}

	//////////////////////////////
	// GETTERS AND SETTERS
	public String getSequence() {
		return this.sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
		
	}
	
	
	
}
