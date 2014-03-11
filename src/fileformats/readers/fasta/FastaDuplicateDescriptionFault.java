package fileformats.readers.fasta;

import fileformats.readers.faults.AlignmentReadingFault;

public class FastaDuplicateDescriptionFault extends AlignmentReadingFault {

	public FastaDuplicateDescriptionFault() {
		
		super();
		
		this.setMessage("Duplicate id are not allowed in fasta format.");
		
	}
	
	

}
