package fileformats.readers.clustal;

import fileformats.readers.rules.AlignmentRule;

public class ClustalHeaderRule extends AlignmentRule {

	public ClustalHeaderRule() {
		super();
		this.setMessage("First no blank line do not starts with 'CLUSTAL'");
	} 
}
