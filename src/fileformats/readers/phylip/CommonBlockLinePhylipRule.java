package fileformats.readers.phylip;

import fileformats.readers.rules.AlignmentRule;

public class CommonBlockLinePhylipRule extends AlignmentRule {

	public CommonBlockLinePhylipRule() {
		super();
		this.setMessage("Sequence from data blocks other than the first must contain the 10 first characters as white spaces, and the the sequence.");
	}

	
	
}
