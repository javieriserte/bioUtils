package seqManipulation.fastamanipulator.commands.modify;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cmdGA2.MultipleArgumentOption;
import pair.Pair;
import seqManipulation.fastamanipulator.commands.FastaCommand;

/**
 * Remove one or many rows from an alignment.
 * The first row is number one, not zero.
 * 
 * @author javier iserte
 *
 */
public class RemoveCommand extends FastaCommand<MultipleArgumentOption<Integer>> {

	public RemoveCommand(InputStream inputstream, PrintStream output, MultipleArgumentOption<Integer> option) {
		
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
		
		int numberOfRowsToRemove = this.getOption().getValues().size();
		int[] valuesToRemove = new int[numberOfRowsToRemove+1];
		
		int counter =0;
		
		for (int i=0; i<numberOfRowsToRemove;i++) {
			
			Integer current = this.getOption().getValues().get(i);
			
			if (current>0) {
			
				valuesToRemove[counter] = current - 1 ;
				
				counter ++;
				
			} 
			
		}
		
		valuesToRemove[counter] = last; // Adding a guard
		
		return Arrays.copyOfRange(valuesToRemove,0,counter+1);
		
	}

	
	
}
