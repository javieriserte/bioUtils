package seqManipulation.fastamanipulator.commands;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cmdGA.MultipleOption;
import pair.Pair;

/**
 * Remove one or many rows from an alignment.
 * The first row is number one, not zero.
 * 
 * @author javier iserte
 *
 */
public class RemoveCommand extends FastaCommand<MultipleOption> {

	public RemoveCommand(InputStream inputstream, PrintStream output, MultipleOption option) {
		
		super(inputstream, output, option);
		
	}

	@Override
	protected List<String> performAction() {
		
		ArrayList<String> result = new ArrayList<>();
		
		List<Pair<String, String>> sequences = this.getSequences();
		
		int[] valuesToRemove = getValuesToRemove(sequences.size());
		
		Arrays.sort(valuesToRemove);

		int counter =0;
		
		int current = valuesToRemove[counter];
		
		for(int i=0; i<sequences.size();i++) {
			
			if (i<current) {
				
				Pair<String, String> currentPair = sequences.get(i);
				
				result.add(">" + currentPair.getFirst());
				
				result.add(currentPair.getSecond());
								
			} else {
				
				counter++;
				
				current = valuesToRemove[counter];
				
			}
			
		}
		
		
		return result;
		
	}

	public int[] getValuesToRemove(int last) {
		
		int[] valuesToRemove = new int[this.getOption().count()+1];
		
		int counter =0;
		
		for (int i=0; i<this.getOption().count();i++) {
			
			if ((Integer) this.getOption().getValue(i)>0) {
			
				valuesToRemove[counter] = (Integer) this.getOption().getValue(i) - 1 ;
				
				counter ++;
				
			} 
			
		}
		
		valuesToRemove[counter] = last; // Adding a guard
		
		return Arrays.copyOfRange(valuesToRemove,0,counter+1);
		
	}

	
	
}
