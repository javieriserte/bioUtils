package fileformats.readers.nexus;

import fileformats.readers.faults.AlignmentReadingFault;

public class NexusDataBlockHeaderFault extends AlignmentReadingFault {

	public NexusDataBlockHeaderFault() {
		super();
		this.setMessage("A line starting with 'Dimension', 'Format' or 'matrix' was expected.");
	}
	
	

}
