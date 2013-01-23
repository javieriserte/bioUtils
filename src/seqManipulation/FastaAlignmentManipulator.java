package seqManipulation;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import cmdGA.MultipleOption;
import cmdGA.NoOption;
import cmdGA.Option;
import cmdGA.Parser;
import cmdGA.SingleOption;
import cmdGA.exceptions.IncorrectParameterTypeException;
import cmdGA.parameterType.InputStreamParameter;
import cmdGA.parameterType.IntegerParameter;
import cmdGA.parameterType.PrintStreamParameter;
import fileformats.fastaIO.FastaMultipleReader;
import fileformats.fastaIO.Pair;

public class FastaAlignmentManipulator {
	 
	private static final String VERSION = "0.0.1";

	/**
	 * A simple program to manipulate Alignments given in fasta format.
	 * 
	 * @param args
	 */
	public static void main(String[] args)  {
		
		List<Option> uniques = new ArrayList<Option>();

		// Step One : Create the parser
		
		Parser parser = new Parser();
		
		// Step Two : Add Command Line Options
		
		SingleOption inputStreamOpt = new SingleOption(parser, System.in, "-infile", InputStreamParameter.getParameter());
		SingleOption printStreamOpt = new SingleOption(parser, System.out, "-outfile", PrintStreamParameter.getParameter());
		
		MultipleOption extractOpt = new MultipleOption(parser, 0, "-extract", ' ', IntegerParameter.getParameter());
		uniques.add(extractOpt);
		
		NoOption countOpt = new NoOption(parser, "-count"); 
		uniques.add(countOpt);
		
		NoOption defOpt = new NoOption(parser, "-def"); 
		uniques.add(defOpt);
		
		NoOption helpOpt = new NoOption(parser, "-help");
		uniques.add(helpOpt);
		
		NoOption versionOpt = new NoOption(parser, "-ver");
		uniques.add(versionOpt);
		
		// Step Three : Try to parse the command line
		
		try {
			parser.parseEx(args);
		} catch (IncorrectParameterTypeException e) {
			System.err.println("There was a error parsing the command line");
			System.exit(1);
		}
		
		
		
		// Program Itself
		
		BufferedReader in = new BufferedReader(new InputStreamReader((InputStream)inputStreamOpt.getValue()));
		PrintStream out = (PrintStream) printStreamOpt.getValue();
		
		int parametersUsed = 0;
		
		for (Option option : uniques) {		
			if (option.isPresent()) parametersUsed++; 
		}
		
		if (parametersUsed==0) {
			out.print(FastaAlignmentManipulator.Help());
			System.exit(0);
		} 
		if (parametersUsed>1) {
			System.err.println("Only one option is expected\n");
			System.exit(0);			
		}
		
		if (helpOpt.isPresent()) {
			out.print(FastaAlignmentManipulator.Help());
			System.exit(0);
		}
		if (versionOpt.isPresent()) {
			out.print(FastaAlignmentManipulator.VERSION);
			System.exit(0);
		}
		
		
		FastaMultipleReader fmr = new FastaMultipleReader();
		
		List<Pair<String,String>> seqs = fmr.readBuffer(in);
		
		if (countOpt.isPresent()) {
			// Counts the number of sequences in the alignment
			out.println(seqs.size() + System.getProperty("line.separator"));
			System.exit(0);
		}
		
		if (defOpt.isPresent()) {
			int i=0;
			for (Pair<String, String> pair : seqs) {
				i++;
				out.println(i + ": " + pair.getFirst());
			}
			System.exit(0);
		}

		if (extractOpt.isPresent()) {
			// Extracts some of the sequences of the alignment			
			Object[] pos = extractOpt.getValues();
			
			for (Object inte : pos) {
				int integer = (Integer) inte;
				if (integer>0 && integer<=seqs.size()) {
//					String ls = System.getProperty("line.separator");
					out.println(">" + seqs.get(integer-1).getFirst());
					out.println(seqs.get(integer-1).getSecond());
				}
			}
			System.exit(0);
		}
	}

	/**
	 * Retrieves the help of the program.
	 * @return
	 */
	private static String Help() {
		return "Fasta Alignment Manipulator - Version " + FastaAlignmentManipulator.VERSION + 
		       "\nOptions: -infile   : is the input fasta file (or stdin if no present)" +
		       "\n         -outfile  : is the path to the output file (or stdout if no present)" +
		       "\n         -extract  : extracts some of the sequences of the alignment."+
		       "\n                     a list of the order numbers of the sequence to be retrieved is needed."+
		       "\n                     the number 1 is the first sequence."+
			   "\n         -count    : counts the number of sequences in a fasta file."+
			   "\n         -def      : shows and numerates the definitions."+			   
			   "\n         -ver      : prints the number of the version in stdout."+
			   "\n         -help     : shows this help.";
			   
		
	}

}
