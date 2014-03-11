package fileformats.readers.faults;

public class BlankAlignmentFault extends AlignmentReadingFault {

	public BlankAlignmentFault() {
		super();
		
		this.setMessage("Empty input data");
	}

	
	
}
