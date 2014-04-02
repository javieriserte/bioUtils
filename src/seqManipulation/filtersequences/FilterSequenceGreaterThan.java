package seqManipulation.filtersequences;

import pair.Pair;

public class FilterSequenceGreaterThan extends FilterSequence {

	int minSize = 0;
	
	
	
	public FilterSequenceGreaterThan(int minSize) {
		super();
		this.minSize = minSize;
	}

	@Override
	public boolean filter(Pair<String, String> sequence) {
		
		return sequence.getSecond().length()>minSize;
		
	}

}
