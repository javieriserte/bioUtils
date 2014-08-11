package seqManipulation.fastamanipulator.commands;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import seqManipulation.fastamanipulator.FastaAlignmentManipulator;

import cmdGA.NoOption;

/**
 * Generates the Help for FastaAlignmentManipulator.
 * 
 * 
 * @author Javier Iserte
 *
 */
public class HelpCommand extends FastaCommand<NoOption> {

	public HelpCommand(InputStream inputstream, PrintStream output, NoOption option) {
		
		super(inputstream, output, option);

	}

	@Override
	protected List<String> performAction() {
		
		List<String> results = new ArrayList<String>();
		
		results.add("Fasta Alignment Manipulator - Version " + FastaAlignmentManipulator.VERSION); 
		results.add("Options: -infile            : is the input fasta file (or stdin if no present);");
		results.add("         -outfile           : is the path to the output file (or stdout if no present);");
		results.add("         -revcomp           : gets the reverse complementary sequence");
		results.add("         -extract           : extracts some of the sequences of the alignment.");
		results.add("                               a list of the order numbers of the sequence to be re-stripGappedColFrtrieved is needed.");
		results.add("                               the number 1 is the first sequence.");
		results.add("         -count             : counts the number of sequences in a fasta file.");
		results.add("         -length            : counts the number of columns in the alignment. If all of them haven't the same size return 0.");
		results.add("         -lengths           : counts the number of columns in each row of the alignment.");
		results.add("         -concatenate       : joins many alignments into one.");
		results.add("                               the names of files to be concatenated must be separated by a comma (','); chaarcter.");
		results.add("         -def               : shows and numerates the definitions.");
		results.add("         -append            : creates one alignment from two. The sequences of the new alignment are the combination from the other two.");
		results.add("         -slice             : cuts a segment of the alignment and keeps it. The rest is removed. ");
		results.add("                               you need to give the starting position and the last position. ");
		results.add("                               Example:  -slice=1,20  | keeps the 20 first characters of the alignment.");
		results.add("         -degap             : removes \"-\" symbols from each sequence.");
		results.add("         -translate         : translate DNA sequences into aminoacid.");
		results.add("         -translateWith     : translate DNA sequences into aminoacid using the given file containing");
		results.add("                                a genetic code");
		results.add("         -genCodeHelp       : shows help about the genetic code format");
		results.add("         -randomRT          : back-translate a protein sequence into a DNA sequence, choosing one the posible codons randomly");
		results.add("         -recFromCon        : reconstruct an alignment from a dotted alignment with reference consensus sequence");
		results.add("                               Example: -recFromCon=1  | uses the first sequence as reference");
		results.add("         -fGrTh             : looks through the alignment and removes all the sequences, except the ones that are greater than a given size");
		results.add("                               Example: -fGrTh=7000    | removes sequences with lenghts lower or equal to 7000");
		results.add("         -fSmTh             : looks through the alignment and removes all the sequences, except the ones that are smaller than a given size");
		results.add("                               Example: -fSmTh=7000    | removes sequences with lenghts greater or equal to 7000");
		results.add("         -title             : filter sequences that containg a given string in the title");
		results.add("                               Example: -title=gi00001 | keeps the sequences that contains the string gi00001 in the title");
		results.add("         -idValues          : gets list of identity percent values between all the sequences in the alignment");
		results.add("         -idMatrix          : exports an csv file with a symmetric matrix with identities values");
		results.add("         -mds               : performs a Multidimensional Scaling analysis (using MDSJ package developed by Christian Pich (University of Konstanz););");
		results.add("                               a number that indicantes the number of output dimensions");
		results.add("         -stripGappedColFr  : Removes columns of the alignment that contains more than a given proportion of gaps");
		results.add("         -pick              : pick a random number set of the sequences");
		results.add("                               Example: -pick 5        | pick 5 random sequences");
		results.add("         -keeppos           : reads a file that contains one number per line and keeps these numbers position of the alignment and eliminate the others");
		results.add("         -rempos            : reads a file that contains one number per line and removes these numbers position of the alignment and keeps the others");
		results.add("         -remIdPos          : removes columsn of an alignment that contains only one char (ignoring gaps);");
		results.add("         -pad               : fills with gaps the sequences of an alignment that are smaller than the alignment itdelf");
		results.add("         -repUncommon       : replace uncommon chars that might be found in some sequences by a given char");
		results.add("                               two arguments must be passed. the first in the type of sequences to look for and the second is the replacement char");
		results.add("                               the values of first argument may be:");
		results.add("                               b for non degenerated bases");
		results.add("                               d for degenerated bases");
		results.add("                               a for amino acids");
		results.add("         -extract_titles    : Extract selected sequences from an alignment by their title.");
		results.add("         -ver               : prints the number of the version in stdout.");
		results.add("         -help              : shows this help.");

		return results;
		
	}

}
