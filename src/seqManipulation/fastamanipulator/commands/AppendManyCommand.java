package seqManipulation.fastamanipulator.commands;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import cmdGA.MultipleOption;
import fileformats.fastaIO.FastaMultipleReader;
import fileformats.fastaIO.Pair;

public class AppendManyCommand extends FastaCommand<MultipleOption> {

	/**
	 * Gets Many alignments with the same number of sequences an returns other with the same number of sequences,
	 * where each row is the concatanation of the sequences in all alignments for tha same row number. 
	 * 
	 * @param inputstream
	 * @param output
	 * @param option
	 */
	public AppendManyCommand(InputStream inputstream, PrintStream output, 	MultipleOption option) {
		super(inputstream, output, option);
	}

	@Override
	protected List<String> performAction() {
		
		List<String> results = new ArrayList<>();
		
		List<StringBuilder> temp_results = new ArrayList<>();
		
		FastaMultipleReader fmr = new FastaMultipleReader();
		
		List<String> desc = new ArrayList<>();
		
		// Iterate over each input file
		for (int i =0 ; i<this.getOption().count(); i++) {
			
			try {
				
				BufferedReader in = new BufferedReader( new FileReader((File) this.getOption().getValue(i)));
				
				List<Pair<String, String>> pairs = fmr.readBuffer(in);
				
				int counter= 0;
				
				// Iterate over each sequence in a input file
				for (Pair<String, String> pair : pairs) {
					
					// If reading the first input file, prepare the temporary data structure
					if (i==0) {
						
						// Description used is from first inpor file
						desc.add(pair.getFirst());
						
						// creates a temporary StringBuilder to hold the growing sequences.
						temp_results.add(new StringBuilder(100));
						
					}
					
					// Appends the new sequence to the temporaty StringBuilder
					temp_results.get(counter).append(pair.getSecond().trim());
					
					counter++;
					
				}
				
			} catch (IOException e) {
				
				System.err.println("There was an error trying to open a file: "+e.getMessage());
				
			}
			
		}
		
		int j =0;
		
		for (StringBuilder sb: temp_results) {
			
			results.add(">"+desc.get(j));
			
			results.add(sb.toString());
			
			j++;
			
		}
		
		return results;
		
	}

}
