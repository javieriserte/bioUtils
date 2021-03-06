package seqManipulation.filtersequences;

import pair.Pair;

public abstract class FilterSequence {
	/**
	 * 
	 * return true if the test sequence is successful.
	 * 
	 * @param sequence
	 * @return
	 */
	public abstract boolean filter(Pair<String,String> sequence);
	
}
