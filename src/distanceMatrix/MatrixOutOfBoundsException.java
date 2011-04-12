package distanceMatrix;

public class MatrixOutOfBoundsException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7221119892315375084L;

	public MatrixOutOfBoundsException() {
		super("Se ha intentado acceder a la matriz fuera de los limites permitidos");
	}
}
