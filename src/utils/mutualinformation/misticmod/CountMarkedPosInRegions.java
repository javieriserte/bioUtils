package utils.mutualinformation.misticmod;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import cmdGA.MultipleOption;
import cmdGA.NoOption;
import cmdGA.Parser;
import cmdGA.SingleOption;
import cmdGA.exceptions.IncorrectParameterTypeException;
import cmdGA.parameterType.InputStreamParameter;
import cmdGA.parameterType.IntegerParameter;
import cmdGA.parameterType.PrintStreamParameter;

/**
 * When in a MSA of concatenated sequences, some positions are removed, keep tracking of what positions in the resulting alignment
 * correspond with the original alignment can be tricky. This class helps to maintain this tracking.
 * It needs, the positions removes or kept, and the lengths of each protein (or any interesting region) in the original alignment.
 * A list of the lengths of the each region in the resulting alignment is returned.
 * 
 * <pre>
 * Example:
 * Original alignment:
 * Region 1  | Region 2               | Region 3                |                 
 * ATGTACGACTACGACTACTGACGACTGACTACTGATCAGCGCGGAGCTTGACTTGACTGCGA
 * AAGTACGACTACTACTACTGACCACTGGCTACTGATCAGCACGCAGCTTGACTTCACTGGGG
 * ATGTCCGAGTACTACTGCTGACTACAGACCACTAATAAGTGCGGTGCGTGAGTAGACCGCCA
 * ATATCCCACTACCACGACTGATGAGTGACGACGGTTCTGTGCAGAGCATAACTTGCCTGAGA
 * ATGCAAGACTACCACAAATCACCACTAACTAATCATCAACCCAGCGATCGACTTACCTGCGA
 *     .    |    .    |    .    |    .    |    .    |    .    |  
 *     5   10   15   20   25   30   35   40   45   50   55   60
 * 
 * Lengths of each region: 11, 25, 26
 * Positions removed: 2,3, 14,18,45,46,47,48
 * 
 * Final Alignment:
 * 
 * Region 1| Region 2             | Region 3            |                 
 * ATACGACTACGCTATGACGACTGACTACTGATCAGCGCGGTGACTTGACTGCGA
 * ATACGACTACTCTATGACCACTGGCTACTGATCAGCACGCTGACTTCACTGGGG
 * ATCCGAGTACTCTGTGACTACAGACCACTAATAAGTGCGGTGAGTAGACCGCCA
 * ATCCCACTACCCGATGATGAGTGACGACGGTTCTGTGCAGTAACTTGCCTGAGA
 * ACAAGACTACCCAATCACCACTAACTAATCATCAACCCAGCGACTTACCTGCGA
 *     .    |    .    |    .    |    .    |    .    |          
 *     5   10   15   20   25   30   35   40   45   50      
 * 
 *Lengths of each region in final alignment: 9, 23, 22
 * 
 * </pre>
 * 
 * Parameter '-removed' is used to indicate that the positions given were removed from the original alignment.
 * If this option is not present, it is assumed that the positions given was kept in the alignment,
 * @author javier iserte
 */
public class CountMarkedPosInRegions {

	public static void main(String[] args) {

		Parser parser = new Parser();
		
		SingleOption inOpt = new SingleOption(parser, System.in, "-positions", InputStreamParameter.getParameter());
		
		MultipleOption prot_len_opt = new MultipleOption(parser, null, "-lengths", ',',IntegerParameter.getParameter());
		
		SingleOption outopt = new SingleOption(parser, System.out, "-outfile", PrintStreamParameter.getParameter());
		
		NoOption removedOpt = new NoOption(parser, "-removed");
		
		try {
			
			parser.parseEx(args);
			
		} catch (IncorrectParameterTypeException e) {
			
			e.printStackTrace();
			
		}
		
		if (!prot_len_opt.isPresent()) {
			
			System.err.println("-lengths option must be used.");
			
			System.exit(1);
			
		}
	
		PrintStream out = (PrintStream) outopt.getValue();

		BufferedReader in = new BufferedReader(new InputStreamReader((InputStream)inOpt.getValue()));
		
		int[] lengths = getRegionLengths(prot_len_opt); // Returns an array with the protein lengths as given in the input file indicated in commandline
		
		Integer[] strippedPos = getStrippedPos(in);  // Returns an array with the positions that were kept or removed in a MSA
		
		boolean removed = removedOpt.isPresent();
		
		int[] counts = countMarkedPosInRegions(lengths, strippedPos);
		
		export(out, lengths, counts, removed);
		
		
	}


	/***
	 * Exports the counts of marked positions
	 * 
	 * @param out
	 * @param lengths
	 * @param counts
	 * @param removed
	 */
	public static void export(PrintStream out, int[] lengths, int[] counts, boolean removed) {
		
		for (int i=0; i<lengths.length; i++) {
			
			int count = removed?(lengths[i] - counts[i]):counts[i]; // if positions were removed, then returns the difference with the total length of each region
			
			out.println(count);
			
		}
		
	}


	/**
	 * Given a list of positions and a list of region lengths, counts how many positions falls into each region.
	 * Regions are assumed to be in order as given in the input array.
	 * 
	 * @param lengths
	 * @param markedPositions
	 * @param removed
	 * @return
	 */
	public static int[] countMarkedPosInRegions(int[] lengths, Integer[] markedPositions) {
		
		int[] counts = new int[lengths.length];
		
		int nextStop = lengths[0]; // nextStop is a mark that indicates the last position of the current region
		
		int currentRegion = 0; // currentRegion tracks the index of the region being analyzed
		
		for(int pos : markedPositions) { // Iterates over each marked positions
			
			if (pos >= nextStop) { // Checks if the current positions falls outside the current region
				
				currentRegion++;   // If so, updates the current region
				
				nextStop = nextStop + lengths[currentRegion]; // And the nextStop
				
			} 
			
			counts[currentRegion] = counts[currentRegion] + 1; // Always, add one more count to the current region 
			
		}
		
		return counts;
		
	}
	
	/**
	 * Read positions from input file or buffer.
	 * 
	 * @param in
	 * @return
	 */
	private static Integer[] getStrippedPos(BufferedReader in) {

		
		try {

			String currentline = null;

			List<Integer> result = new ArrayList<Integer>(); 
			
			while((currentline=in.readLine())!=null) {
				
				if (!currentline.trim().equals("")) {
					
					Integer value = Integer.valueOf(currentline.trim());
					
					result.add(value);
					
				}
				
			}
			
			Integer[] resArray = new Integer[result.size()]; 
			
			return result.toArray(resArray);

			
		} catch (IOException e) {
			
			e.printStackTrace();
			
		} 

		return null;
			
	}

	/**
	 * Reads lengths from input commandlineoption
	 * @param prot_len_opt
	 * @return
	 */
	public static int[] getRegionLengths(MultipleOption prot_len_opt) {
		
		int[] lengths = new int[prot_len_opt.count()];

		int c = 0;
		
		for (Object o : prot_len_opt.getValues()) {
			
			lengths[c++] = (Integer) o;
			
		}
		return lengths;
	}

}
