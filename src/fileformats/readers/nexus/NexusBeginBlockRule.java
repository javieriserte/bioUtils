package fileformats.readers.nexus;

import fileformats.readers.rules.AlignmentRule;

public class NexusBeginBlockRule extends AlignmentRule {

	public NexusBeginBlockRule() {
		super();
		this.setMessage("The beginning of a nexus block is expected.");
	}
	

}
