package fileformats.readers.fasta;

import fileformats.readers.rules.AlignmentRule;

public class FastaSequenceLine extends AlignmentRule {

	public FastaSequenceLine() {
		super();
		this.setMessage("Sequence lines in fasta contains letters, '-', '*' and '.' symbols.");
	}

}
