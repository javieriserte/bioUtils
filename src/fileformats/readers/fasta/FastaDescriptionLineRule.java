package fileformats.readers.fasta;

import fileformats.readers.rules.AlignmentRule;

public class FastaDescriptionLineRule extends AlignmentRule {

	public FastaDescriptionLineRule() {
		
		super();
		
		this.setMessage("Fasta description lines starts with '>' symbol.");
		
	}
	
	

}
