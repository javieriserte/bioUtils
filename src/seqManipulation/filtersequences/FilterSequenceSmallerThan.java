package seqManipulation.filtersequences;

import pair.Pair;

public class FilterSequenceSmallerThan extends FilterSequence {
	int maxSize;
	
	

	public FilterSequenceSmallerThan(int maxSize) {
		super();
		this.maxSize = maxSize;
	}



	@Override
	public boolean filter(Pair<String, String> sequence) {

		return sequence.getSecond().length() < maxSize;
		
	}

}
