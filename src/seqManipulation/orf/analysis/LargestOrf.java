package seqManipulation.orf.analysis;

import java.util.List;

import seqManipulation.orf.replication.Replicate;

public class LargestOrf extends ExtractSequences {

	public LargestOrf(Replicate replicator, int minSize, Integer[] frames) {
		
		super(replicator, minSize, frames);
		
	}

	@Override
	protected void attempToKeepLargest(List<String> orfs) {

		String largestString = "";
		
		int largetsSize =0;
		
		for (String string : orfs) {
			
			if (string.length() > largetsSize) {
				
				largestString = string;
				
				largetsSize = string.length(); 
				
			}
			
		}
		
		orfs.clear();
		
		orfs.add(largestString);
		
	}

}
