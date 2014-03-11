package fileformats.readers.clustal;

import fileformats.readers.faults.AlignmentReadingFault;

public class SequenceOrConservationClustalFault extends AlignmentReadingFault {

	public SequenceOrConservationClustalFault() {
		
		super();
		
		this.setMessage("Line content do not match a sequence line or a conservation line.");
		
	}

	
	
}
