package fileformats.readers.nexus;

import fileformats.readers.faults.AlignmentReadingFault;

public class NexusBeginBlockFault extends AlignmentReadingFault {

	public NexusBeginBlockFault() {
		super();
		this.setMessage("The beginning of a nexus block is expected.");
	}
	

}
