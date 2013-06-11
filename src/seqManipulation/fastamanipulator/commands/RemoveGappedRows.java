package seqManipulation.fastamanipulator.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fileformats.fastaIO.Pair;

public class RemoveGappedRows {

	//////////////////////
	// Class Constant
	private static final Character gapChar='-';

	//////////////////////////
	// Public Class Interface
	public List<Pair<String,String>> removeGappedRows(List<Pair<String,String>> align ) {

		List<Integer> toRemove = new ArrayList<Integer>();
		
		for (int i = 0 ; i<align.size(); i++) {

			String currentSequence = align.get(i).getSecond().replaceAll(String.valueOf(gapChar), "");
			
			if (currentSequence.equals("")) {
				
				toRemove.add(i);
				
			}
		}
		
		Collections.sort(toRemove);
		
		Collections.reverse(toRemove);
		
		for(int i : toRemove) {

			align.remove(i);

		}
		
		return align;
	}


}
