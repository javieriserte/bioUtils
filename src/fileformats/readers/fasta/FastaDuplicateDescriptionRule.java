package fileformats.readers.fasta;

import fileformats.readers.rules.AlignmentRule;

public class FastaDuplicateDescriptionRule extends AlignmentRule {

	public FastaDuplicateDescriptionRule() {
		
		super();
		
		this.setMessage("Duplicate id are not allowed in fasta format.");
		
	}
	
	

}
