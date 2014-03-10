package fileformats.readers.pir;

import fileformats.readers.rules.AlignmentRule;

public class PirFirstLineRule extends AlignmentRule {

	public PirFirstLineRule() {
		super();
		this.setMessage("A header line with format '>XX;sequenceId' was expected.");
	}
	
}
