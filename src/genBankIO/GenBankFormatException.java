package genBankIO;

public final class GenBankFormatException extends Exception {

	private static final long serialVersionUID = 1L;

	// CONSTRUCTORS
	/**
	 * Creates a new InvalidCommandLineException with the default error message.
	 */
	public GenBankFormatException() {
		super("GenBank Record is not well formed.");
	}
	/**
	 * Creates a new InvalidCommandLineException with the custom error message.
	 * @param message is the custom error message.
	 */
	public GenBankFormatException(String message) {
		super(message);
	}
	
	
}
