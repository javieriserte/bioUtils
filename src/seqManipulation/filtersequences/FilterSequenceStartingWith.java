package seqManipulation.filtersequences;

import pair.Pair;

public class FilterSequenceStartingWith extends FilterSequence{

	String startingSequence = "";
	
	

	public FilterSequenceStartingWith(String startingSequence) {
		super();
		this.startingSequence = startingSequence;
	}



	@Override
	public boolean filter(Pair<String, String> sequence) {
		
		boolean result = sequence.getSecond().startsWith(startingSequence); 
		
		return result;
		
	}
	
	
	
}
