package seqManipulation;

import java.util.ArrayList;
import java.util.List;

import fileformats.fastaIO.Pair;

public class GapToolbox {

	/**
	 * Creates a new alignment removing the gapped positions from another one
	 * 
	 * @param alignment
	 * @return
	 */
	public List<Pair<String,String>> removeGapedColumns(List<Pair<String,String>> alignment) {
		
		// removes the columns of the alignment that contain a gap
		
		boolean[] keepers = getKeepers(alignment);
		
		return this.removeGappedColumns(alignment, keepers);
		
	}
	
	
	/**
	 * Creates a new alignment removing the positions indicated by a keepers list
	 * 
	 * 
	 * @param alignment
	 * @return
	 */
	
	public List<Pair<String,String>> removeGappedColumns(List<Pair<String,String>> alignment, boolean[] keepers) {
		
		// removes the columns of the alignment that contain a gap
		
		List<Pair<String,String>> newAlignment = new ArrayList<Pair<String,String>>();
		
		for (Pair<String, String> seq : alignment) {
			
			StringBuilder nseq = new StringBuilder();
			
			String oldSeq = seq.getSecond();
			
			for (int j = 0; j< oldSeq.length();j++) {
				
				if (keepers[j]) nseq.append(oldSeq.charAt(j)); 
				
			}
			
			newAlignment.add(new Pair<String, String>(seq.getFirst(), nseq.toString()));
			
		}
		
		return alignment;
		
	}
	
	/** 
	 * Look for the columns that have at least one gap.
	 * Retrieves an array of integer values with the positions of the gapped columns.
	 * 
	 * @param alignment
	 * @return
	 */
	public boolean[] getKeepers(List<Pair<String,String>> alignment) {

		// Creates a temporal array to indicate what positions constains a gap.
		boolean[] keeper = new boolean[alignment.get(0).getSecond().length()];
		
		// Sets as none column with gaps initially
		for (int i=0; i<keeper.length; i++) {
			
			keeper[i] = true;
			
		}

		// Iterate over each sequence in one alignment
		for (Pair<String, String> seq : alignment) {
			
			// Scan every position of each sequence
			// looking for gaps...
			for (int j = 0; j< seq.getSecond().length();j++) {
				
				// ...when one is found is marked in 'keeper' array.
				if (seq.getSecond().charAt(j) == '-') {keeper[j]=false;}
				
			}
			
		}
		
		// returns an array istead of a list
		return keeper;
		
	}
	
	/**
	 * Converts the keepers array to a list with the positions 
	 * 
	 * @param keepers
	 * @return
	 */
	public Integer[] getPositionsWithGaps(boolean[] keepers) {
		
		List<Integer> positions = new ArrayList<Integer>();
		
		for(int i=0; i<keepers.length;i++) {
			
			if (!keepers[i]) positions.add(i);
			
		}
		
		return positions.toArray(new Integer[0]);
		
	}
	
}
