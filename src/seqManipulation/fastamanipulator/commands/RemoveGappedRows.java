package seqManipulation.fastamanipulator.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pair.Pair;

/**
 * Remove sequences that olny contains gaps from an alignment
 * 
 * @author javier
 *
 */
public class RemoveGappedRows {

	//////////////////////
	// Class Constant
	private static final Character gapChar='-';

	//////////////////////////
	// Public Class Interface
	public List<Pair<String,String>> removeAllGapRows(List<Pair<String,String>> align ) {

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
