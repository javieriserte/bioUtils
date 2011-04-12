package distanceMatrix;

public class ImposibleNumberRepresentationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6592454525458484373L;

	public ImposibleNumberRepresentationException() {
		super("No es posible representar este número como un string con los digitos de la mantisa y decimales pedidos");
	}
}
