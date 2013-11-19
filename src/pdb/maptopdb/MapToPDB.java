package pdb.maptopdb;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cmdGA2.CommandLine;
import cmdGA2.NoArgumentOption;
import cmdGA2.OptionsFactory;
import cmdGA2.SingleArgumentOption;
import fileformats.fastaIO.FastaMultipleReader;
import fileformats.fastaIO.Pair;

public class MapToPDB {

	/**
	 * MapToPDB reads an alignment of two sequences and creates a map from positions 
	 * of the first sequence to the second sequence. The second sequence is assumed
	 * to correspond with a sequence into a PDB file. The sequences may contain 
	 * any character including gaps. The only special character is '-' that do not count 
	 * for position advance. (Gaps symbols are from individual sequences, Dot symbol 
	 * are added while aligning the two sequences). 
	 * 
	 * <pre>
	 * Example:
	 * Input Alignment:
	 * >s1
	 * KRQ-FR.NMC
	 * >pdb
	 * -RQMFRAN.C
	 * 
	 * Output Map:
	 * 1	1
	 * 2	2
	 * 3	3
	 * 4	4
	 * 5	5
	 * 6	6
	 * 7	8 // by the '.' in the first sequence.
	 * 8	8 // by the '.' in the second sequence.
	 * 9	9
	 * </pre>
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		//////////////////////////////////////
		// Create Command Line
		CommandLine cmd = new CommandLine();
		//////////////////////////////////////
		
		//////////////////////////////////////
		// Add options to command line
		SingleArgumentOption<InputStream> inOpt = OptionsFactory.createBasicInputStreamArgument(cmd);
		SingleArgumentOption<PrintStream> outopt = OptionsFactory.createBasicPrintStreamArgument(cmd);
		NoArgumentOption helpOpt = new NoArgumentOption(cmd, "--help");
		//////////////////////////////////////
		
		//////////////////////////////////////
		// Parse Command line
		cmd.readAndExitOnError(args);
		//////////////////////////////////////
		
		//////////////////////////////////////
		// Get values from command line
		BufferedReader in = new BufferedReader( new InputStreamReader(inOpt.getValue()));
		PrintStream out = outopt.getValue();
		//////////////////////////////////////
		
		//////////////////////////////////////
		// Check is help flag is present
		if (helpOpt.isPresent()) {
			
			MapToPDB.printHelp(out);
			
			System.exit(1);
			
		}
		//////////////////////////////////////
		
		//////////////////////////////////////
		// Read Alignment
		List<Pair<String, String>> alignment = (new FastaMultipleReader()).readBuffer(in);
		//////////////////////////////////////
		
		//////////////////////////////////////
		// Create Map from alignment
		Map<Integer, Integer> positionsMap = null;
		try {
			positionsMap = MapToPDB.createMap(alignment);
		} catch (NotTwoSequencesInAlignmentException e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}
		//////////////////////////////////////
		
		//////////////////////////////////////
		// Export Map
		MapToPDB.exportMap(positionsMap, out);
		//////////////////////////////////////

	}


	private static void printHelp(PrintStream out) {
		
		out.println("Map To PDB");
		out.println("  Usage: maptopdb.jar --infile alignment --outfile out.map");
		
	}


	/**
	 * Prints out the results.
	 * 
	 * @param positionsMap
	 * @param out
	 */
	private static void exportMap(Map<Integer, Integer> positionsMap,
			PrintStream out) {
		
		for (Integer domainValue : positionsMap.keySet()) {
			
			out.println(domainValue + "\t" + positionsMap.get(domainValue));
			
		}
		
	}


	/**
	 * Reads an alignmente and returns Map pairing positions from the first sequence
	 * of the alignment into the second.
	 * @param alignment
	 * @return
	 * @throws NotTwoSequencesInAlignmentException when alignment provided does not contain exactly two sequences.
	 */
	public static Map<Integer, Integer> createMap(List<Pair<String, String>> alignment) throws NotTwoSequencesInAlignmentException {
		
		if (alignment.size()==2) {
			
			Map<Integer, Integer> result =  new LinkedHashMap<>(); 
			// Data structure for store result map
			// Is a LinkedHashMap to conserve the order
			// of the positions as read.

			String[] seqs = new String[]{alignment.get(0).getSecond(),
					                     alignment.get(1).getSecond()};
			// Get the first and second sequence of the alignment.
			
			int[] counters = new int[]{0,0};
			// Counters for the advance of the sequences
			
			for (int i = 0; i< seqs[0].length(); i++) {
			// Iterate over each position of both alignments

				for (int j=0; j<2; j++) {
				// Update counters for each sequence

					if (seqs[j].charAt(i)!='.') {
						
						counters[j]++;
						// Counter increase if current char is not a dot.
						
					}
					
				}
				
				result.put(counters[0], counters[1]);
				// Add current counter into the map
				
			}
			
			return result;
			
		} else {

			throw new NotTwoSequencesInAlignmentException();
			
		}
		
	}
	
	

}
