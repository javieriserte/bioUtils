package fileformats.readers.phylip;

import fileformats.readers.rules.AlignmentRule;

public class PhylipHeaderRule extends AlignmentRule {

	public PhylipHeaderRule() {
		super();
		
		this.setMessage("First line in Phylip format must contain two only two numbers: The number of sequences and length of alignment, in that order.");
		
	}
	
}
