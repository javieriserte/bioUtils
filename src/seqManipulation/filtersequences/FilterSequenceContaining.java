package seqManipulation.filtersequences;

import pair.Pair;

public class FilterSequenceContaining extends FilterSequence {

	private String query = "";
	
	
	
	public FilterSequenceContaining(String query) {
		super();
		this.query = query;
	}



	@Override
	public boolean filter(Pair<String, String> sequence) {
		
		return sequence.getSecond().contains(query);
		
	}

}
