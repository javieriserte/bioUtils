package fileformats.readers.nexus;

import fileformats.readers.faults.AlignmentReadingFault;

public class NexusFirstLineFault extends AlignmentReadingFault {

	public NexusFirstLineFault() {
		super();
		this.setMessage("Nexus alignment must start with: '#NEXUS' string.");
	}
	
	

}
