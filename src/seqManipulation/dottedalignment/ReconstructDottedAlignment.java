package seqManipulation.dottedalignment;

import java.util.ArrayList;
import java.util.List;

import fileformats.fastaIO.Pair;

/**
 * Reconstruct a normal MSA from a dotted alignment
 * 
 * Example
 * <pre>
 * Original alignment
 * Seq_1 ATGTTGTGATGTGATCCTCGACTG
 * Seq_2 .........A......T.......
 * Seq_3 .........A......T.......
 *
 * Reconstructed alignment with reference 1
 * Seq_1 ATGTTGTGATGTGATCCTCGACTG
 * Seq_2 ATGTTGTGAAGTGATCTTCGACTG
 * Seq_3 ATGTTGTGAAGTGATCTTCGACTG
 * <pre>
 * @author javier
 */
public class ReconstructDottedAlignment {
	
	final public static char dotChar = '.';
	
	/**
	 * Reconstruct an alignment from a dotted alignment
	 * 
	 * @param alignment a dotted MSA to be reconstructed to a normal MSA
	 * @param referenceConsensusIndex is the index from the dotted MSA to be used as reference
	 * @return a new reconstructed normal MSA.
	 */
	public static List<Pair<String,String>> reconstruct(List<Pair<String,String>> alignment, int referenceConsensusIndex) {
	
		List<Pair<String,String>> result = new ArrayList<Pair<String,String>>();
		
		String consensus = alignment.get(referenceConsensusIndex).getSecond();
		
		for(int i = 0; i<alignment.size(); i++) {
			
			Pair<String,String> newPair = new Pair<String, String>(null,null);

			newPair.setFirst(alignment.get(i).getFirst());
			
			if (referenceConsensusIndex!=i) {
				
				newPair.setSecond(ReconstructDottedAlignment.reconstructSequence(alignment.get(i).getSecond(), consensus));
				
			} else {
				
				newPair.setSecond(consensus);
				
			}
			
			result.add(newPair);
			
		}
		
		return result;
		
	}

	/**
	 * Guesses the reference dotted sequence to be used. and returns the alignment
	 * 
	 * @param alignment a dotted MSA to be reconstructed to a normal MSA
	 * @return a new reconstructed normal MSA or null if the reference sequence couldn't be guessed
	 */
	public static List<Pair<String,String>> reconstruct(List<Pair<String,String>> alignment) {
		
		int guessedindex = ReconstructDottedAlignment.guessIndex(alignment);

		if (guessedindex!=-1) {
			
			return ReconstructDottedAlignment.reconstruct(alignment, guessedindex);
			
		} else {
			
			return null;
			
		}
				
	}
	
	/**
	 * Reconstruct a single sequence from a dotted sequence and a reference sequence
	 * @param dotseq is the dotted sequence to be concerted
	 * @param refseq is the reference sequence 
	 * @return a new reconstructed normal sequence
	 */
	public static String reconstructSequence(String dotseq, String refseq) {

		StringBuilder newseq = new StringBuilder();
		
		for(int i =0; i<dotseq.length(); i++) {
			
			if (dotseq.charAt(i) == ReconstructDottedAlignment.dotChar) {
				
				newseq.append(refseq.charAt(i));
				
			} else {
				
				newseq.append(dotseq.charAt(i));
				
			}
			
		}
		
		return newseq.toString();
		
	}
	
	/**
	 * Guess the index of the reference sequence of a dotted alignment.
	 * 
	 * @param alignment a dotted alignment
	 * 
	 * @return the index of the reference sequence or -1 if couldn't find it
	 */
	private static int guessIndex(List<Pair<String, String>> alignment) {

		int lastFound = 0;
		
		int counter =0;
		
		String dotString = String.valueOf(dotChar);

		for (int i=0; i<alignment.size();i++) {

			if (!alignment.get(i).getSecond().contains(dotString)) {

				counter ++;
				
				lastFound = i;
				
				System.out.println(counter + " - " + lastFound);
				
			}
			
		}
		
		return (counter==1)?lastFound:(-1); // if only one sequence without dots is found return its index
		
	}
	
}
