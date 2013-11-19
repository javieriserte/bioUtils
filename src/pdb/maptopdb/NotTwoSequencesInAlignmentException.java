package pdb.maptopdb;

public class NotTwoSequencesInAlignmentException extends Exception {

	private static final long serialVersionUID = 2703449776829898879L;
	
	public NotTwoSequencesInAlignmentException() {

		super("Alignment must contain just two sequences");
		
	}
	
}
