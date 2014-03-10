package fileformats.readers.nexus;

import fileformats.readers.rules.AlignmentRule;

public class NexusFirstLineRule extends AlignmentRule {

	public NexusFirstLineRule() {
		super();
		this.setMessage("Nexus alignment must start with: '#NEXUS' string.");
	}
	
	

}
