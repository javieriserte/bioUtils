package fileformats.readers.pir;

import fileformats.readers.rules.AlignmentRule;

public class PirSequenceLineRule extends AlignmentRule {

	public PirSequenceLineRule() {
		super();
		this.setMessage("A sequence data line was expected. Data lines contains any non '*' symbol, except in the last line of a block that must end with a '*' symbol.");
	}

}
