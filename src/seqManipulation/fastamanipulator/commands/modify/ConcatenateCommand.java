package seqManipulation.fastamanipulator.commands.modify;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import cmdGA2.MultipleArgumentOption;
import fileformats.fastaIO.FastaMultipleReader;
import pair.Pair;
import seqManipulation.fastamanipulator.commands.FastaCommand;

/**
 * Joins two or more alignments into another. 
 * 
 * @author Javier Iserte
 *
 */
public class ConcatenateCommand extends FastaCommand<MultipleArgumentOption<File>> {

	public ConcatenateCommand(InputStream inputstream, PrintStream output, MultipleArgumentOption<File> option) {

		super(inputstream, output, option);

	}

	@Override
	protected List<String> performAction() {
		
		List<String> results = new ArrayList<String>();
		
		List<File> inFiles = this.getOption().getValues();
		
		FastaMultipleReader fmr = new FastaMultipleReader();
		
		if (inFiles != null) {
			
			for (File file : inFiles) {

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
