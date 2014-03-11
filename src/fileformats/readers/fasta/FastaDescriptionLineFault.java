package fileformats.readers.fasta;

import fileformats.readers.faults.AlignmentReadingFault;

public class FastaDescriptionLineFault extends AlignmentReadingFault {

	public FastaDescriptionLineFault() {
		
		super();
		
		this.setMessage("Fasta description lines starts with '>' symbol.");
		
	}
	
	

}
