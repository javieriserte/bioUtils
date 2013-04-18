package seqManipulation;

import java.util.ArrayList;
import java.util.List;

import fileformats.fastaIO.Pair;

public class ReconstructDottedAlignment {

	public static List<Pair<String,String>> reconstruct(List<Pair<String,String>> alignment, int referenceConsensus) {
	
		List<Pair<String,String>> result = new ArrayList<Pair<String,String>>();

		
		String consensus = alignment.get(referenceConsensus).getSecond();
		
		for(int i = 0; i<alignment.size(); i++) {
			
			Pair<String,String> newPair = new Pair<String, String>(null,null);

			newPair.setFirst(alignment.get(i).getFirst());
			
			if (referenceConsensus!=i) {
				
				newPair.setSecond(ReconstructDottedAlignment.reconstructSequence(alignment.get(i).getSecond(), consensus));
				
			}
			
		}
		
		return result;
		
	}

	private static String reconstructSequence(String second, String consensus) {

		StringBuilder newseq = new StringBuilder();
		
		for(int i =0; i<second.length(); i++) {
			
			if (second.charAt(i) == '.') {
				
				newseq.append(consensus.charAt(i));
				
			} else {
				
				newseq.append(second.charAt(i));
				
			}
			
		}
		
		return newseq.toString();
		
		
	}
	
}
