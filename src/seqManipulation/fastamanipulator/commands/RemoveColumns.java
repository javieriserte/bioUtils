package seqManipulation.fastamanipulator.commands;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seqManipulation.AlignmentSequenceEditor;

import pair.Pair;

public class RemoveColumns {

	//////////////////////
	// Class Constant
	private static final Character gapChar='-';

	//////////////////////////
	// Public Class Interface
	public static List<Pair<String,String>> removeIdenticalColumn(List<Pair<String,String>> align ) {
		
		AlignmentSequenceEditor ase = new AlignmentSequenceEditor(align);
		
		boolean[] keepers = new boolean[ase.getRowsSize()];
		
		for (int i=0; i< ase.getRowsSize(); i++) {
			
			Character[] currentColumn = ase.getColumnAt(i);
			
			keepers[i] = RemoveColumns.containsDifferentChars(currentColumn);
			
		}
		
		return filterPositions(align, keepers);
	}

	////////////////////////
	// Private Class Methods
	private static List<Pair<String, String>> filterPositions(List<Pair<String, String>> align, boolean[] keepers) {
		
		List<Pair<String,String>> result = new ArrayList<Pair<String,String>>();
		
		for (Pair<String, String> pair : align) {
			
			String seq = pair.getSecond();
			
			StringBuilder nseq = new StringBuilder();
			
			for (int i = 0; i<seq.length();i++  ) {
				
				if (keepers[i]) {
					
					nseq.append(seq.charAt(i));
					
				}
				
			}
			
			result.add(new Pair<String, String>(pair.getFirst(), nseq.toString()));
			
		}
		return result;
		
	}

	private static boolean containsDifferentChars(Character[] currentColumn) {

		Set<Character> chars = new HashSet<Character>();
		
		for (Character character : currentColumn) {
			
			if(character!=gapChar) chars.add(character);
			
		}
		
		return (chars.size()>1);
		
	}
	
}
