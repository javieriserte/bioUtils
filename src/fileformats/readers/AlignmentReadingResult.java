package fileformats.readers;

import java.util.List;

import fileformats.readers.rules.AlignmentRule;
import pair.Pair;

/**
 * Contains the result of attempting to read a sequence alignment.
 * Is the reading was successful, the alignment can be retrieved, if not
 * an AlignmentRule object containing the first error in the input data can be 
 * retrieved. 
 * @see #successfulRead()
 * @see #getAlignment()
 * @see #getUnmetRule()
 * @author javier
 */
public class AlignmentReadingResult {

	//////////////////////////////
	// Instance varibles 
	private List<Pair<String,String>> alignment = null;
	private AlignmentRule             unmetRule = null;
	
	
	///////////////////////////////
	// Public Interface
	/**
	 * Verifies that the input alignment was successfully read.
	 * 
	 * @return <b>true</b> if the alignment was completely read.<br>
	 *         <b>false</b> otherwise
	 */
	public boolean successfulRead() {
		
		return this.getAlignment() != null && this.getUnmetRule() ==null;
		
	}
	
	///////////////////////////////
	// Getters And Setters
	public List<Pair<String, String>> getAlignment() {
		return alignment;
	}
	public void setAlignment(List<Pair<String, String>> alignment) {
		this.alignment = alignment;
	}
	public AlignmentRule getUnmetRule() {
		return unmetRule;
	}
	public void setUnmetRule(AlignmentRule unmetRule) {
		this.unmetRule = unmetRule;
	}
	
	
	
}
