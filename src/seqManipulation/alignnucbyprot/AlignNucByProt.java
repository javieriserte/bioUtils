package seqManipulation.alignnucbyprot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cmdGA2.CommandLine;
import cmdGA2.NoArgumentOption;
import cmdGA2.OptionsFactory;
import cmdGA2.SingleArgumentOption;
import cmdGA2.returnvalues.InfileValue;
import fileformats.fastaIO.FastaMultipleReader;
import fileformats.fastaIO.Pair;
/**
 * Given a MSA of protein sequences and a set of nucleic acids sequences that
 * correspond to the untranslated sequences of the proteins in the MSA, a new alignment
 * of nucleic acids is returned and the protein alignment is used as template for its 
 * construction. Descriptions of MSA and input nucleic acids sequences must be the same for
 * each (protein / nucleic acid) pair.
 * <pre>
 * Example protein MSA:
 * 
 * >seq_1
 * MRLA---ERL
 * >seq_2
 * -RLALLTDLL

 * Example nucleic acids:
 * ATGCGTCTAGCAGAAAGACTT
 * CGACTAGCACTACTGACTGACTTACTT
 * 
 * Example output alignment of nucleic acids:
 * ATGCGTCTAGCA---------GAAAGACTT
 * ---CGACTAGCACTACTGACTGACTTACTT
 * </pre>
 */
public class AlignNucByProt {
	/////////////////////////////////
	// Private Class Constants
	private static final int    CODON_LENGTH = 3;
	private static final String GAPPED_CODON = "---";
	private static final char   GAP          = '-';
	
	/**
	 * Main executable method.
	 * 
	 * @param args commnad line arguments.
	 */
	public static void main(String[] args) {
		/////////////////////////////////
		// Creates Command Line 
		CommandLine cmdline = new CommandLine();
		
		/////////////////////////////////
		// Add options to the command line
		SingleArgumentOption<File> protMsaOpt    = new SingleArgumentOption<>(cmdline, "-prot_msa", new InfileValue(), null);
		
		SingleArgumentOption<File> nucSeqOpt     = new SingleArgumentOption<>(cmdline, "-nuc_seq", new InfileValue(), null);
		
		SingleArgumentOption<PrintStream> outOpt = OptionsFactory.createBasicPrintStreamArgument(cmdline);
		
		NoArgumentOption helpopt                 = new NoArgumentOption(cmdline, "-help");
		
		//////////////////////////////////
		// Parse command line
		cmdline.readAndExitOnError(args);
		
		//////////////////////////////////
		// Validate Command line
		if (helpopt.isPresent()) {
			System.err.println(" Usage: program.jar -prot_msa <path> -nuc_seq <path>");
			System.exit(1);
		}
		if (! (protMsaOpt.isPresent() && nucSeqOpt.isPresent()) ) {
			System.err.println("-prot_msa and -nuc_seq are mandatory.");
			System.exit(1);
		}
		
		try {

			/////////////////////////////////
			// Get Values From Command line
			BufferedReader inMSA = new BufferedReader(new FileReader(protMsaOpt.getValue()));
			
			BufferedReader inNuc = new BufferedReader(new FileReader(nucSeqOpt.getValue()));
			
			PrintStream out = outOpt.getValue();
			
			////////////////////////////////////
			// Creates AlignNucByProt Object
			AlignNucByProt alignNuc = new AlignNucByProt();
			
			////////////////////////////////////
			// Creates the new alignment
			List<Pair<String, String>> result = alignNuc.generateAlignment(inMSA, inNuc);
			
			/////////////////////////////////
			// Sends the new alignment to
			// the output
			alignNuc.exportNewNucAlignment(out, result);
			
		} catch (FileNotFoundException e) {
			
			System.err.println("There was an error: "+e.getMessage());
			
			System.exit(1);
			
		}
		
	}
	/**
	 * Creates the new alignment
	 * @param inMSA a buffer with the input template MSA
	 * @param inNuc a buffer with the input nucleic acid sequences
	 * @return
	 */
	public List<Pair<String, String>> generateAlignment(BufferedReader inMSA, BufferedReader inNuc) {
		
		List<Pair<String,String>> result = new ArrayList<>();
			// Creates the temporary data structure to store the resulting new alignment
		
		List<Pair<String, String>> msa = readSeqs(inMSA);
			// Reads input protein alignment into a (description,sequence) pairs list
		
		Map<String, String> nuc_hash = readNucMap(inNuc);
			// Reads input nucleic acids sequence into a (description -> sequence) map 
		
		this.checkLengths(msa, nuc_hash);
			// Checks thaat the lengths of protein sequences and nucleic acid sequence
			// corresponds one to another
		
		for (Pair<String, String> pair : msa) {
			// Iterates over each protein in the input MSA
			
			result.add(this.generateOneAlignmentRow(nuc_hash, pair));
				// Generates a new aligned nucleic acid sequence and
			 	// adds it into the new growing alignment
		}
		
		return result;
			// Returns the new alignment
	}
	
	/**
	 * Creates the new alignment
	 * @param inMSA a file with the input template MSA
	 * @param inNuc a file with the input nucleic acid sequences
	 * @return
	 */
	public List<Pair<String, String>> generateAlignment(File inMSA, File inNuc) {

		try {
						
			return this.generateAlignment(new BufferedReader(new FileReader(inMSA)), new BufferedReader(new FileReader(inNuc)));
				// Transforms input files into buffers
				// then use them to generate the new alignment
			
		} catch (FileNotFoundException e) {
			
			System.err.println("There was an error: "+e.getMessage());
			
		}
		
		return new ArrayList<Pair<String, String>>();
			// Returns an empty alignment if something happens
		
	}
	/**
	 * Verifies that the length of each protein sequence (without gaps) is exactacly one third of
	 * the nucleic acid sequence
	 * @param msa
	 * @param nuc_hash
	 */
	private void checkLengths(List<Pair<String, String>> msa, Map<String, String> nuc_hash) {

		boolean mismatch = false;
		// Temporary variable to track any mismatch in lengths
		
		for (Pair<String, String> pair : msa) {
			// Iterates over each protein in the input MSA
			
			int prot_len = pair.getSecond().replaceAll("-", "").length();
				// Calculates the length of the protein in amino acid residues
			
			int nuc_len = nuc_hash.get(pair.getFirst()).length();
				// Calculates the length of the nucleic acid sequence in bases 
			
			if (prot_len*CODON_LENGTH != nuc_len) {
				// If the lengths of the protein sequence do not correspond the 
				// the lengths of the nucleic acid sequence...
				
				System.err.println("Length mismatch for "+pair.getFirst()+": prot: "+ prot_len + "("+(3*prot_len)+")" + ", nuc: "+ nuc_len+".");
					// ...logs the mismatch
				
				mismatch = true;
					// and mark the mismatch for tracking
				
			}
			
		}
		
		if (mismatch) {
			// Is a mismatch occurs...
			
			System.exit(1);
				// the exit
		}
		
	}
	/**
	 * Generates a new row of the nucleic acid alignment using a protein sequence from the
	 * input alignment and a map with all the nucleic acid sequences.
	 * 
	 * @param nuc_hash a map with all the nucleic acid sequences
	 * @param pair a protein sequence from the input protein MSA
	 * @return a row for the alignment
	 */
	private Pair<String, String> generateOneAlignmentRow( Map<String, String> nuc_hash, Pair<String, String> pair) {
		
		String currentAlignmentSeq = pair.getSecond();
			// Gets the amino acid sequence of the alignment
			// discards description data
		
		String nucleicSeq = nuc_hash.get(pair.getFirst());
			// Looks in the map the corresponding
			// nucleic acid sequence
		
		int nucleicSeqCounter = 0;
			// A counter to track the advance into the nucleic acid sequence
			// while it is read.
		
		StringBuilder newNucAlignSeq = new StringBuilder();
			// A temporary structure to store the growing new row of the 
			// nucleic acids alignment

		for (int i = 0; i<currentAlignmentSeq.length(); i++) {
			// Iterates over each position of the template protein sequence
			
			char curChar = currentAlignmentSeq.charAt(i);
				// Gets the char at the current position
			
			if (curChar == GAP) {
				// If the current position is a gap... 
				
				newNucAlignSeq.append(GAPPED_CODON);
				// ...then append three gaps into the new nucleic acid sequence
				// and do not count any advance into the read nucleic acid sequence
				
			} else {
				// If the current position is not a gap...
				
				newNucAlignSeq.append(nucleicSeq.substring(nucleicSeqCounter, nucleicSeqCounter+CODON_LENGTH));
					// ...then copy a codon from the read nucleic acid sequence into the new
				    // nucleic acid sequence 
				
				nucleicSeqCounter=nucleicSeqCounter+CODON_LENGTH;
					// and count the advance into the read sequence
			}
			
		}
		
		return new Pair<String, String>(">"+pair.getFirst(), newNucAlignSeq.toString());
			// Returns the new sequence
		
	}

	/**
	 * Prints a list of (description, sequence) pairs into a given 
	 * PrintStream object with fasta format.
	 * @param out
	 * @param result
	 */
	private void exportNewNucAlignment(PrintStream out, List<Pair<String, String>> result) {
		
		for (Pair<String, String> pair : result) {
			// Iterates over each sequence
			
			out.println(pair.getFirst());
				// Print description
			
			out.println(pair.getSecond());
				// Print sequence
		}
		
	}
	/**
	 * Reads a buffer with ungapped nucleic acid sequences in fasta format 
	 * and returns a map containing sequence descriptions as keys and 
	 * sequences as values. 
	 * 
	 * @param inNuc input buffer
	 * @return a map of descriptions to sequences
	 */
	private static Map<String, String> readNucMap(BufferedReader inNuc) {
		
		List<Pair<String, String>> nuc = readSeqs(inNuc);
			// Reads fasta sequences into a list
		
		Map<String,String> nuc_hash= new HashMap<>();
			// Creates the Map to be returned
		
		for (Pair<String, String> pair : nuc) {
			// Iterates over each sequence in the list
			
			nuc_hash.put(pair.getFirst(), pair.getSecond());
				// Add the current sequence into the map 
		}
		
		return nuc_hash;
			// Returns the map
	}

	/**
	 * Reads a buffer with ungapped nucleic acid sequences in fasta format
	 * and returns a list of (description, sequence) pairs.
	 * 
	 * @param inMSA input buffer
	 * @return a list of sequences
	 */
	private static List<Pair<String, String>> readSeqs(BufferedReader inMSA) {
		
		FastaMultipleReader fmr = new FastaMultipleReader();
			// Creates the object thar parses fasta format
		
		List<Pair<String, String>> msa = fmr.readBuffer(inMSA);
			// Parses the input data
		
		return msa;
			// return the list of sequences
	}

}
