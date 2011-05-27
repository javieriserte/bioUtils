package distanceMatrix;

public class IncorrectMegaFormatException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public IncorrectMegaFormatException() {
		super("El formato del archivo Mega no es correcto");
	}
	
	public IncorrectMegaFormatException(String message) {
		super(message);
	}
	
}
