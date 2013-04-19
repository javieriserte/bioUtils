package seqManipulation.filtersequences;

import fileformats.fastaIO.Pair;

public class FilterSequenceBooleanOR extends FilterSequenceBoolean {

	private FilterSequence op1;
	
	private FilterSequence op2;

	
	public FilterSequenceBooleanOR(FilterSequence op1, FilterSequence op2) {
		super();
		this.op1 = op1;
		this.op2 = op2;
	};
	
	
	@Override
	public boolean filter(Pair<String, String> sequence) {

		return op1.filter(sequence) || op2.filter(sequence);


	}

}
