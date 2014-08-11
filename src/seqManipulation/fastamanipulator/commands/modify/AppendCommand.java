package seqManipulation.fastamanipulator.commands.modify;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import cmdGA2.SingleArgumentOption;
import fileformats.fastaIO.FastaMultipleReader;
import pair.Pair;
import seqManipulation.fastamanipulator.commands.FastaCommand;

/**
 * Get two alignments of the same numbers of sequences and combines them into another one.
 * The resulting alignment has the same number of sequences and each sequence is the result of
 * appending one sequence from the first alignment with the corresponding sequence of the 
 * second alignment.
 * 
 * 
 * @author javier
 *
 */
public class AppendCommand extends FastaCommand<SingleArgumentOption<File>> {

	public AppendCommand(InputStream inputstream, PrintStream output, SingleArgumentOption<File> option) {

		super(inputstream, output, option);

	}

	@Override
	protected List<String> performAction() {
		
		List<String> results = new ArrayList<String>();
		
		List<Pair<String,String>> leftSequences = this.getSequences();
		
		File rightSequencesFile = this.getOption().getValue();
		
		try {
			
			if (rightSequencesFile!=null && rightSequencesFile.exists()) {
				
				FastaMultipleReader fmr = new FastaMultipleReader();
				
				List<Pair<String,String>> rigthSequences = fmr.readFile(rightSequencesFile);
				
				if (rigthSequences.size()==leftSequences.size()) {
					
					for (int index = 0; index<leftSequences.size() ; index++) {
					
						results.add(">" + leftSequences.get(index).getFirst());
					
						results.add(leftSequences.get(index).getSecond() + rigthSequences.get(index).getSecond());

					}
					
				}
			
			} else {
				
				throw new Exception("Alignments must have the same number of sequences.");
				
			}
				
		} catch (FileNotFoundException e) {
				
			System.err.println("The file to be appended can't be read.");
				
		} catch (Exception e) {
			
			System.err.println(e.getMessage());
			
		}
		
		return results;
		
	}

}
