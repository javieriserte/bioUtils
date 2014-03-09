package fileformats.readers.clustal;

import fileformats.readers.rules.AlignmentRule;

public class SequenceOrConservationClustalRule extends AlignmentRule {

	public SequenceOrConservationClustalRule() {
		
		super();
		
		this.setMessage("Line content do not match a sequence line or a conservation line.");
		
	}

	
	
}
