package utils.mutualinformation;

public class CharGroupSizeException extends Exception {

	private static final long serialVersionUID = 7206127054994911442L;
	
	public CharGroupSizeException() {
		super("Groups have differents sizes.");
	}

}
