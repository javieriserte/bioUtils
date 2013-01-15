package fileformats.genBankIO;
/**
 * This es an <code>Exception</code> Object to inform that 
 * the GenBank record data is not well formed. 
 * 
 * @author Javier Iserte (jiserte@unq.edu.ar)
 *
 */
public final class GenBankFormatException extends Exception {

	private static final long serialVersionUID = 1L;

	// CONSTRUCTORS
	/**
	 * Creates a new GenBankFormatException with the default error message.
	 */
	public GenBankFormatException() {
		super("GenBank Record is not well formed.");
	}
	/**
	 * Creates a new GenBankFormatException with the custom error message.
	 * @param message is the custom error message.
	 */
	public GenBankFormatException(String message) {
		super(message);
	}
	
	
}
