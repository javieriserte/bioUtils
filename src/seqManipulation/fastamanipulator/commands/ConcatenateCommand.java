package seqManipulation.fastamanipulator.commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import cmdGA.MultipleOption;
import fileformats.fastaIO.FastaMultipleReader;
import fileformats.fastaIO.Pair;

/**
 * Joins two or more alignments into another. 
 * 
 * @author Javier Iserte
 *
 */
public class ConcatenateCommand extends FastaCommand<MultipleOption> {

	public ConcatenateCommand(InputStream inputstream, PrintStream output, MultipleOption option) {

		super(inputstream, output, option);

	}

	@Override
	protected List<String> performAction() {
		
		List<String> results = new ArrayList<String>();
		
		Object[] filesAsObjectArray = this.getOption().getValues();
		
		File[] files = new File[filesAsObjectArray.length];
		
		FastaMultipleReader fmr = new FastaMultipleReader();
		
		for (int i =0; i<filesAsObjectArray.length; i++) {
			
			files[i] = (File) filesAsObjectArray[i];
			
		}
		
		if (files != null) {
			
			for (File file : files) {

				try {
					
					List<Pair<String,String>> pairs = fmr.readFile(file);
					
					for (Pair<String, String> pair : pairs) {
						
						results.add(">" + pair.getFirst());
						
						results.add(pair.getSecond());
						
					}
					
				} catch (FileNotFoundException e) {

					System.err.println("There was an error reading: "+ file.getAbsolutePath());
					
				}
				
			}
			
		}
		
		return results;

	}

}
