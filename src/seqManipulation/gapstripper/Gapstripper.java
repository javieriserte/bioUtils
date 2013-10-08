package seqManipulation.gapstripper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import cmdGA2.CommandLine;
import cmdGA2.NoArgumentOption;
import cmdGA2.OptionsFactory;
import cmdGA2.SingleArgumentOption;
import cmdGA2.returnvalues.OutfileValue;
import fileformats.fastaIO.FastaMultipleReader;
import fileformats.fastaIO.FastaWriter;
import fileformats.fastaIO.Pair;


/**
 * This program is intended to perform three common pre-processing
 * features in alignments for MI calculation.
 * 
 * <ol>
 * <li> Maximum frequency filter</li>
 * <li> Strip with a reference sequence</li>
 * <li> Remove rows (sequences) with more than a given fraction of gaps.</li> 
 * </ol>
 * 
 * 
 * @author Javier Iserte
 *
 */
public class Gapstripper {

	////////////////////////////////
	// Class Constants
	private static final char gap = '-';
	/**
	 * Main Executable Method 
	 * @param args
	 */
	public static void main(String[] args) {

		///////////////////////////////
		// Create Command Line Object
		CommandLine cmdline = new CommandLine();
		
		///////////////////////////////
		// Add options to Command Line
		
		SingleArgumentOption<InputStream> inOpt = OptionsFactory.createBasicInputStreamArgument(cmdline); 
		// "--infile" option
		
		SingleArgumentOption<PrintStream> outOpt = OptionsFactory.createBasicPrintStreamArgument(cmdline); 
		// "--outfile" option
		
		SingleArgumentOption<Double> maxfreqOpt = OptionsFactory.createBasicDoubleArgument(cmdline, "--maxfreq", 1d); 
		// Keeps the columns whose maximum frequency 
		// is less than or equal to a given value 
		
		SingleArgumentOption<String> refIdOpt = OptionsFactory.createBasicStringArgument(cmdline, "--refId", null);
		// Sets reference sequence by its title or description
		
		SingleArgumentOption<Integer> refNumOpt = OptionsFactory.createBasicIntegerArgument(cmdline, "--refNum", null);
		// Sets reference sequence by the position in the alignment.
		// The counting starts with 1, not zero.
		
		SingleArgumentOption<Double> gapsInRowsOpt = OptionsFactory.createBasicDoubleArgument(cmdline, "--gapsInRows", 0.5d);
		// Removes rows that contains whose gap fraction is more than or equal to a given value. 
		
		SingleArgumentOption<File> refFileOpt = new SingleArgumentOption<File>(cmdline, "--ref_file", new OutfileValue(), null);
		// Path File to out reference map file
		
		NoArgumentOption helpOpt = new NoArgumentOption(cmdline, "--help");
		// Prints help and exit.
		
		////////////////////////////////
		// Parse Command Line
		cmdline.readAndExitOnError(args);
		
		///////////////////////////////
		// Checks for help flag
		if (helpOpt.isPresent()) {
			
			Gapstripper.printHelp();
			
			System.exit(1);
			
		}
		
		///////////////////////////////
		// Validate Command Line Options
		
		if (refIdOpt.isPresent() && refNumOpt.isPresent()) {
			
			System.err.println("--refId and --refNum can not be present at the same time.");
			
			Gapstripper.printHelp();
			
			System.exit(1);
			
		}
		
		//////////////////////////////
		// Get Values From Command Line
		BufferedReader in = new BufferedReader(new InputStreamReader(inOpt.getValue()));
		
		PrintStream out = outOpt.getValue();
		
		Double maxfreq = maxfreqOpt.getValue();
		
		Double gapsInRows = gapsInRowsOpt.getValue();
		
		File refFile = refFileOpt.getValue();
		
		/////////////////////////////
		// Create Gapstripper Object
		Gapstripper stripper = new Gapstripper();
		
		//////////////////////////////
		// Load Fasta File
		LinkedHashMap<String, String> sequences = stripper.loadFasta(in);

		//////////////////////////////
		// Get Maximum Frequencies Mask
		boolean[] mask = stripper.getMaxFreqMask(sequences, maxfreq);
		
		////////////////////////////////
		// Get Reference Sequence Description
		boolean[] gapstripMask;
		
		boolean useReferenceSequence = refIdOpt.isPresent() || refNumOpt.isPresent();
		
		if (useReferenceSequence) {
		
			String refSequence = stripper.getReferenceSequence(refIdOpt, refNumOpt, refFileOpt, sequences);
			
			gapstripMask = stripper.getReferenceSequenceMask(sequences, refSequence);
			
			mask = stripper.MaskAnd(mask,gapstripMask);
		
		}

		//////////////////////////////////////
		// Get Reference of removed positions
		LinkedHashMap<Integer,Integer> removedPositionsReferenceMap = new LinkedHashMap<>();
		// Create a data structure to store information
		// of the corresponding positions between 
		// initial and final alignments.

		/////////////////////////////////////
		// Removes columns of the alignment 
		// according the mask created before
		// and removes rows that contains too much gaps
		List<Pair<String, String>> finalSequences = stripper.removeColumnsAndRows(mask, sequences , gapsInRows, removedPositionsReferenceMap);

		
		////////////////////////////////////
		// Print Reference Map File
		if (refFileOpt.isPresent()) {
			
			printReferenceMapFile(refFile, removedPositionsReferenceMap);
			
		}
		
		//////////////////////////////////////
		// Print the new Alignment
		
		FastaWriter fw = new FastaWriter();
		
		fw.print(out, finalSequences);
		
	}
	////////////////////////////////////
	// Private Class methods
	private static void printHelp() {
		
		InputStream helpStream = Gapstripper.class.getResourceAsStream("Help");
		
		PrintStream ps = new PrintStream(System.err);
		
		byte current = -1;

		try {
			
			while ((current = (byte) helpStream.read())!=-1) {
			
				ps.write(current);
			
			}
			
		} catch (IOException e) {
			
			System.err.println("There was an error trying to print the help.");
			
		}
		
	}
	////////////////////////////////
	// Private methods

	/**
	 * Prints out the reference map file
	 * The first column is the position in the output alignment,
	 * the second column is the corresponding position in the original
	 * alignment.
	 * 
	 * @param refFile
	 * @param removedPositionsReferenceMap
	 */
	private static void printReferenceMapFile(File refFile,
			LinkedHashMap<Integer, Integer> removedPositionsReferenceMap) {
		
		// Prints out the reference map file
		// The first column is the position in the output alignment,
		// the second column is the corresponding position in the original
		// alignment.
		
		try {
			
			PrintStream outRef = new PrintStream(refFile);
			
			for (Integer newValue : removedPositionsReferenceMap.keySet()) {
				
				outRef.println(newValue + "\t" + removedPositionsReferenceMap.get(newValue));
				
			}
			
			outRef.flush();
			
			outRef.close();
			
		} catch (FileNotFoundException e) {
			
			System.err.println("There was a problem while writing reference file: " + e.getMessage());
			
			System.exit(1);
			
		}
	}

	/**
	 * Process an alignment and removes some columns by a given logical mask
	 * and some rows by the fraction of gaps present.
	 * In addition, modifies a given Map to make a reference of the columns in 
	 * the original and the resulting alignment. 
	 * 
	 * @param mask
	 * @param sequences
	 * @param gapsInRows
	 * @param removedPositionsReferenceMap
	 * @return
	 */
	private List<Pair<String, String>> removeColumnsAndRows(boolean[] mask,
			LinkedHashMap<String, String> sequences, Double gapsInRows,
			LinkedHashMap<Integer, Integer> removedPositionsReferenceMap) {

		List<Pair<String, String>> result = new ArrayList<>();
		// Creates a temporary data structure 
		// to store the new sequences
		
		Set<String> descriptionSet = sequences.keySet();
		// Gets the descriptions of the input sequences
		
		for (String description : descriptionSet) {
		// Iterates over each sequence
			
			String currentSequence = sequences.get(description);
			// Get one of the sequences
			
			StringBuilder newSequenceBuilder = new StringBuilder();
			// Creates a data structure for creating a new sequence
			
			int maskedPositionCounter = 0;
			// Counter to take reference of 
			// the positions that are kept in the new alignment
			
			for (int i=0; i<currentSequence.length();i++) {
			// Iterates over each column position of the 
			// current sequence
				
				if (mask[i]) {
				// If the positions is masked...
					
					newSequenceBuilder.append(currentSequence.charAt(i));
					// then this positions is added to the new growing sequence 
					
					removedPositionsReferenceMap.put(maskedPositionCounter+1, i+1);
					// Also...
					// updates the reference Map
					// The '+1' is just because is more common count the sequences
					// starting from 1 than 0.
					// maskedPositionCounter counts the positions in the new alignment
					// i counts the positions in the old alignment
					
					maskedPositionCounter++;
					
				}
				
			}
			
			String newSequence = newSequenceBuilder.toString();
			// After all positions are traversed
			// a new sequence is created with the passed symbols
			
			double gapFreq = this.calculateGapFrequency(newSequence);
			// Calculates the fraction of gaps in a single row of the alignment
			
			if (gapFreq<gapsInRows) {
				// If the fraction of gap is above the threshold
				// then that sequence is skipped from the final results.
				
				result.add(new Pair<String, String>(description, newSequence));
				// else it is added into the final result alignment.
				
			}
			
		}
		
		return result;
	}

	/**
	 * Given a sequence, calculates the fraction of the symbol in that sequence
	 * that are gaps.
	 * 
	 * @param newSequence
	 * @return the fraction of gaps.
	 */
	private double calculateGapFrequency(String newSequence) {
		
		int gapCount =0;
		
		for (int i =0; i<newSequence.length(); i++) {

			gapCount += (newSequence.charAt(i)==Gapstripper.gap)?1:0;
			
		}
		
		return (double)gapCount / (double) newSequence.length();
		
	}

	/**
	 * Perform a logical <code>And</code> operation over 
	 * two arrays of the same length. 
	 * 
	 * @param mask1
	 * @param mask2
	 * @return
	 */
	private boolean[] MaskAnd(boolean[] mask1, boolean[] mask2) {
		
		for (int i = 0; i < mask2.length; i++) {
			
			mask1[i] = mask1[i] && mask2[i];
			
		}
		
		return mask1;
	}

	/**
	 * Creates a logical Map of positions that contains gaps in a given sequence
	 * 
	 * @param sequences
	 * @param refSequence
	 * @return
	 */
	private boolean[] getReferenceSequenceMask(
			LinkedHashMap<String, String> sequences, String refSequence) {
		
		String currentSequence = sequences.get(refSequence);
		
		boolean[] mask = new boolean[currentSequence.length()];
		
		for (int i =0; i<currentSequence.length(); i++) {
			
			mask[i] = currentSequence.charAt(i)!=Gapstripper.gap;
			
		}
		 		
		
		return mask;
		
	}

	/**
	 * Search for the reference sequence of an alignment
	 * given the command line options.
	 * 
	 * @param refIdOpt
	 * @param refNumOpt
	 * @param refFileOpt
	 * @param sequences
	 * @return
	 */
	private String getReferenceSequence(
			SingleArgumentOption<String> refIdOpt,
			SingleArgumentOption<Integer> refNumOpt,
			SingleArgumentOption<File> refFileOpt,
			LinkedHashMap<String, String> sequences) {
		
		if ( refIdOpt.isPresent() ) {

			String currentDescription = refIdOpt.getValue();
			
			if (sequences.containsKey(currentDescription)) {
			
				return currentDescription; 
			
			} else {

				System.err.println("Description: "+ currentDescription + " was not found.");
				
				System.err.println("Search is case sensitive");
				
				System.exit(1);
				
				return null;
			}
			
		} else {
			
			Iterator<String> iterator = sequences.keySet().iterator();
			
			Integer counter = refNumOpt.getValue();

			if (counter>sequences.size()) {
				
				System.err.println("Asked for sequence at position " + counter);
				
				System.err.println("And alignment has "+sequences.size()+" sequences.");

				System.exit(1);
				
			}
			
			while (counter>1) {
			// Iterates the key set until the ith value is
			// reached

				counter--;
			
				iterator.next();
				
			}
			
			return iterator.next();
			
		}
		
	}


	/**
	 * Checks which columns of and alignment has lesser or equal that
	 * a given value of maximum frequency,  
	 * @param sequences
	 * @param maxfreq
	 * @return an array that represents a logical mask of which column passes 
	 * the maximum frequency filter. 
	 */
	private boolean[] getMaxFreqMask(LinkedHashMap<String, String> sequences, double maxfreq) {
		
		MaximumFrequencyProfiler maxFreqProfiler = new  MaximumFrequencyProfiler();
		// Creates the object tha can calculate the profile
		
		double[] profile = maxFreqProfiler.calculateProfile(sequences);
		// Creates the profile
		
		boolean[] mask = new boolean[profile.length];
		// Creates a data structure to store and return  
		// the result masking data.
		// A True value means that this position should be kept.
		
		for (int i = 0; i<profile.length; i++) {
		// Iterates over each position of the profile 
			
			mask[i] = profile[i]<=maxfreq;
			// Checks if the current value of the profile 
			// passes the maximum frequency filter.
			
		}
		
		return mask;
		
	}
	
	
	/**
	 * Reads a fasta file and returns a map whose keys are sequences descriptions
	 * and values are sequences.
	 * This Map preserves the order the sequences in the input.
	 * 
	 * @param in a BufferedReader to be read.
	 * @return a LinkedHashMap with the sequences.
	 */
	private LinkedHashMap<String, String> loadFasta(BufferedReader in) {
		
		FastaMultipleReader fmr = new FastaMultipleReader();
		
		List<Pair<String, String>> seq = fmr.readBuffer(in);
		
		LinkedHashMap<String, String> sequences = new LinkedHashMap<>();
		
		for (Pair<String, String> pair : seq) {
			
			sequences.put(pair.getFirst(), pair.getSecond());
			
		}
		
		return sequences;
		
	}

}
