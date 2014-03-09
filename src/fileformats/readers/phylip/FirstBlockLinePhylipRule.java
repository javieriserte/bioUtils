package fileformats.readers.phylip;

import fileformats.readers.rules.AlignmentRule;

public class FirstBlockLinePhylipRule extends AlignmentRule {

	public FirstBlockLinePhylipRule() {

		super();
		
		this.setMessage("Sequences in the first block of data must have a description of 10 characters and then the sequence.");
		
	}
	
	

}
