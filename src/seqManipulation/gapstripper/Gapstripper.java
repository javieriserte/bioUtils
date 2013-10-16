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
 * features in alignments before MI calculation.
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
		
		NoArgumentOption clusterOpt = new NoArgumentOption(cmdline, "--cluster");
		// Perform clustering before maximum frequency filter
		
		SingleArgumentOption<Double> clusterIdOpt = OptionsFactory.createBasicDoubleArgument(cmdline, "--clusterId", 0.62); 
		// Identity fraction threshold for sequence clustering 
		
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
		boolean[] mask = stripper.getMaxFreqMask(sequences, maxfreq, clusterOpt.isPresent(), clusterIdOpt.getValue());
		
		////////////////////////////////
		// Get Reference Sequence Description
		boolean[] gapstripMask;
		// Create data structure to store logical mask for
		// gap stripping by reference sequence
		
		boolean useReferenceSequence = refIdOpt.isPresent() || refNumOpt.isPresent();
		// Check if a reference sequence is given
		
		if (useReferenceSequence) {
		// If a reference sequence is given then ...

		
			String refSequence = stripper.getReferenceSequence(refIdOpt, refNumOpt, refFileOpt, sequences);
			// ... retrieves the description as an id
			
			gapstripMask = stripper.getReferenceSequenceMask(sequences, refSequence);
			// ... perform the gap stripping and gets the logical mask
			
			mask = stripper.MaskAnd(mask,gapstripMask);
			// ... and combines the gaps stripping mask with 
			// the maximum frequency mask
		
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
	/**
	 * Prints the help into the standard error buffer.
	 * Help is read from a resource file.
	 */
	private static void printHelp() {
		
		InputStream helpStream = Gapstripper.class.getResourceAsStream("Help");
		
		PrintStream ps = new PrintStream(System.err);
		
		byte current = -1;

		try {
			
			while ((current = (byte) helpStream.read())!=-1) {
			
				ps.write(current);
			
			}
		
		ps.flush();
		
		ps.close();
			
		} catch (IOException e) {
			
			System.err.println("There was an error trying to print the help.");
			
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
	public List<Pair<String, String>> removeColumnsAndRows(boolean[] mask,
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
					
					if (removedPositionsReferenceMap != null) {
					// Check if a reference map that links
					// positions before and after removing columns
					// if asked.
						
					
						removedPositionsReferenceMap.put(maskedPositionCounter+1, i+1);
						// if so...
						// updates the reference Map
						// The '+1' is just because is more common count the sequences
						// starting from 1 than 0.
						// maskedPositionCounter counts the positions in the new alignment
						// i counts the positions in the old alignment
					
						maskedPositionCounter++;
						
					}
					
					
				}
				
			}
			
			String newSequence = newSequenceBuilder.toString();
			// After all positions are traversed
			// a new sequence is created with the passed symbols
			
			double gapFreq = this.calculateGapFrequency(newSequence);
			// Calculates the fraction of gaps in a single row of the alignment
			
			if (gapFreq<gapsInRows || (gapsInRows==0 && gapFreq==0) ) {
				// If the fraction of gap is above the threshold
				// or is equal to zero, that means no gap allowed
				// then that sequence is skipped from the final results.
				// The original implementation compares only by 'lesser than', 
				// do not take into account the case of 'gapsInRows==0'. But 
				// i think this is a bug.
				// Also, it could be expressed as: 'gapFreq<=gapsInRows', 
				// but this is the exact behavior of the
				// original implementation.
				// In addition, original implementation has another bug,
				// it doesn't count well the number of gaps in a sequence, it is
				// always one less than the real number. It's not a big 
				// difference in big alignments.
				
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
	public double calculateGapFrequency(String newSequence) {
		
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
	public boolean[] MaskAnd(boolean[] mask1, boolean[] mask2) {
		
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
	public boolean[] getReferenceSequenceMask(
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

			return getReferenceSequenceById(refIdOpt.getValue(), sequences);
			
		} else {
			
			int index = refNumOpt.getValue();
			
			return getReferenceSequenceByIndex(index, sequences);
			
		}
		
	}
	
	/**
	 * Gets the reference sequence by its position (or index)
	 * in the alignment.
	 * 
	 * @param index is the position of the desired reference sequence
	 * @param sequences is a LinkedHashMap of all the sequence, that can be 
	 *         iterated in the same order that the original alignment. 
	 * @return the description of the reference sequence.
	 */
	public String getReferenceSequenceByIndex(
			int index,
			LinkedHashMap<String, String> sequences) {
		
		Iterator<String> iterator = sequences.keySet().iterator();

		if (index>sequences.size()) {
			
			System.err.println("Asked for sequence at position " + index);
			
			System.err.println("And alignment has "+sequences.size()+" sequences.");

			System.exit(1);
			
		}
		
		while (index>1) {
		// Iterates the key set until the ith value is
		// reached

			index--;
		
			iterator.next();
			
		}
		
		return iterator.next();
	}
	
	
	/**
	 * Dummy method to retrieve the reference sequence by its 
	 * description. If found gets the same description as result.
	 *  
	 * @param refIdOpt
	 * @param sequences
	 * @return
	 */
	public String getReferenceSequenceById(
			String currentDescription ,
			LinkedHashMap<String, String> sequences) {
		
		if (sequences.containsKey(currentDescription)) {
		
			return currentDescription; 
		
		} else {

			System.err.println("Description: "+ currentDescription + " was not found.");
			
			System.err.println("Search is case sensitive");
			
			System.exit(1);
			
			return null;
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
	public boolean[] getMaxFreqMask(LinkedHashMap<String, String> sequences, double maxfreq, boolean performClustering, double thresholdId) {
		
		MaximumFrequencyProfiler maxFreqProfiler = new  MaximumFrequencyProfiler();
		// Creates the object tha can calculate the profile
		
		double[] profile = null;
		
		if (performClustering) {
			
			profile = maxFreqProfiler.calculateProfileUsingClustering(sequences,thresholdId);
			
		} else {
			
			profile = maxFreqProfiler.calculateProfileWithoutClustering(sequences);
		}
		
		 
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
	public LinkedHashMap<String, String> loadFasta(BufferedReader in) {
		
		FastaMultipleReader fmr = new FastaMultipleReader();
		
		List<Pair<String, String>> seq = fmr.readBuffer(in);
		
		LinkedHashMap<String, String> sequences = new LinkedHashMap<>();
		
		for (Pair<String, String> pair : seq) {
			
			sequences.put(pair.getFirst(), pair.getSecond());
			
		}
		
		return sequences;
		
	}
	/////////////////////////////////////////
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


}
