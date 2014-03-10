package fileformats.readers.nexus;

import fileformats.readers.rules.AlignmentRule;

public class NexusDataBlockHeaderRule extends AlignmentRule {

	public NexusDataBlockHeaderRule() {
		super();
		this.setMessage("A line starting with 'Dimension', 'Format' or 'matrix' was expected.");
	}
	
	

}
