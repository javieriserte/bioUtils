package seqManipulation.filtersequences;

import pair.Pair;

public class FilterSequenceBooleanNOT extends FilterSequenceBoolean {

	private FilterSequence op;
	
	
	
	public FilterSequenceBooleanNOT(FilterSequence op) {
		super();
		this.op = op;
	}



	@Override
	public boolean filter(Pair<String, String> sequence) {
		
		boolean result = ! op.filter(sequence); 
		
		return result;
		
	}

}
