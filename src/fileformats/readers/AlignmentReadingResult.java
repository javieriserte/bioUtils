package fileformats.readers;

import java.util.List;

import fileformats.readers.rules.AlignmentRule;
import pair.Pair;

public class AlignmentReadingResult {

	//////////////////////////////
	// Instance varibles 
	private List<Pair<String,String>> alignment = null;
	private AlignmentRule             unmetRule = null;
	
	
	///////////////////////////////
	// Public Interface
	/**
	 * Verifies that the input alignment was succesfully read.
	 * 
	 * @return <b>true</b> if the alignem was completely read.<br>
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
