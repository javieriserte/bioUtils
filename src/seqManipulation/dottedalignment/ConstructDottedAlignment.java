package seqManipulation.dottedalignment;

import java.util.ArrayList;
import java.util.List;

import pair.Pair;

/**
 * Creates a dotted alignment from a MSA.<br>
 * 
 * A dotted alignment converts all characters that are identical to a reference sequence to a dot char.<br>
 * 
 * Example
 * <pre>
 * Original alignment
 * Seq_1 ATGTTGTGATGTGATCCTCGACTG
 * Seq_2 ATGTTGTGAAGTGATCTTCGACTG
 * Seq_3 ATGTTGTGAAGTGATCTTCGACTG
 *
 * Dotted alignment with reference 1
 * Seq_1 ATGTTGTGATGTGATCCTCGACTG
 * Seq_2 .........A......T.......
 * Seq_3 .........A......T.......
 * <pre>
 * @author javier
 *
 */
public class ConstructDottedAlignment {
	
	final public static char dotChar = '.';
	
	final public static char gapChar = '-'; 

	/**
	 * Creates a new dotted alignment from a multiple non-dotted alignment 
	 * 
	 * @param alignment is the alignment to be converted into a dotted alignment
	 * @param refSeqIndex the index of the sequence in the alignment to be used as reference 
	 * @return a new dotted alignment
	 * @throws ReferenceSequenceOutOfIndex if refSeqIndex is equal or greater than the size of the alignment of smaller then zero.
	 */
	public static List<Pair<String,String>> getDottedAlignment(List<Pair<String,String>> alignment, int refSeqIndex) throws ReferenceSequenceOutOfIndex {
		
		int nseq = alignment.size();
		
		List<Pair<String,String>> result = new ArrayList<Pair<String,String>>();
		
		if (!(refSeqIndex <nseq) && refSeqIndex<0) {
			
			throw new ReferenceSequenceOutOfIndex();
		}
		
		String refseq = alignment.get(refSeqIndex).getSecond();
		
		for (int index = 0 ; index <nseq; index++) {
			
			if (index==refSeqIndex) {
				
				result.add(new Pair<String,String>(alignment.get(refSeqIndex).getFirst(),refseq));
				
			} else {
				
				String currentseq= alignment.get(index).getSecond();
				
				String newseq = ConstructDottedAlignment.getDottedSequence(currentseq, refseq);

				result.add(new Pair<String,String>(alignment.get(index).getFirst(),newseq));
				
			}
			
		}
		
		return result;
	
	}

	/**
	 * Create a dotted sequence from a given sequence and a reference sequence
	 * 
	 * @param currentseq
	 * @param refseq
	 * @return a new dotted sequence
	 */
	public static String getDottedSequence( String currentseq, String refseq) {
		
		StringBuilder newseq = new StringBuilder();

		for (int i=0; i<refseq.length(); i++) {
			
			char currentchar = currentseq.charAt(i);
			
			if (currentchar !=gapChar && currentchar == refseq.charAt(i)) {
				
				newseq.append(dotChar);
				
			} else {
				
				newseq.append(currentchar);
				
			}
			
		}
		return newseq.toString();
	}

	/**
	 * Auxiliary Exception class to report when the reference sequence is out of range
	 * 
	 * @author javier
	 *
	 */
	public static class ReferenceSequenceOutOfIndex extends Exception {

		private static final long serialVersionUID = 1L;
		
	}
	
}
