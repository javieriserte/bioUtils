package seqManipulation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
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
import cmdGA.parameterType.InFileParameter;
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
		
		SingleOption appendOpt = new SingleOption(parser, null, "-append", InFileParameter.getParameter());
		uniques.add(appendOpt);

		NoOption lenOpt = new NoOption(parser, "-length");
		uniques.add(lenOpt);

		NoOption lensOpt = new NoOption(parser, "-lengths");
		uniques.add(lensOpt);

		MultipleOption concatOpt = new MultipleOption(parser, null, "-concatenate", ',', InFileParameter.getParameter());
		uniques.add(concatOpt);
		
		MultipleOption sliceOpt = new MultipleOption(parser, null, "-slice", ',', IntegerParameter.getParameter());
		uniques.add(sliceOpt);

		
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
			System.err.println("Only one option is expected");
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
		
		List<Pair<String,String>> seqs = null;
		
		if (!concatOpt.isPresent()) {
			seqs = fmr.readBuffer(in);
		}
		
		
		if (countOpt.isPresent()) {
			countCommand(out, seqs);
		}
		
		if (defOpt.isPresent()) {
			definitionsCommand(out, seqs);
		}

		if (extractOpt.isPresent()) {
			extractCommand(extractOpt, out, seqs);
		}
		
		if (appendOpt.isPresent()) {
			
			appendCommand(appendOpt, out, fmr, seqs);
			
		}
		
		if (lenOpt.isPresent()) {
			
			lengthCommand(out, seqs);
			
		}
		
		if (lenOpt.isPresent()) {

			lengthsCommand(out, seqs);
			
		}
		
		if (concatOpt.isPresent()) {
			
			concatenateCommand(concatOpt, out, fmr);
			
		}
		
		if (sliceOpt.isPresent()) {
			
			sliceCommand(sliceOpt, out, seqs);
		}
		


	}

	private static void sliceCommand(MultipleOption sliceOpt, PrintStream out,
			List<Pair<String, String>> seqs) {
		if (sliceOpt.count()==2) {
			
			// This option requires exactly two parameters

			int from = (Integer) sliceOpt.getValue(0)-1;

			int to = (Integer) sliceOpt.getValue(1);
			
			int len = seqs.get(0).getSecond().length();
			
			if (to <= len && from < to && from >=0) {
				
				for (Pair<String, String> pair : seqs) {
					
					out.println(pair.getFirst());
					
					out.println(pair.getSecond().substring(from, to));
					
				}
				
			}
			
			
		}
		
		out.close();

		System.exit(0);
	}

	private static void concatenateCommand(MultipleOption concatOpt,
			PrintStream out, FastaMultipleReader fmr) {
		File[] files = (File[]) concatOpt.getValues();
		
		if (files != null) {
			
			for (File file : files) {

				try {
					
					List<Pair<String,String>> pairs = fmr.readFile(file);
					
					for (Pair<String, String> pair : pairs) {
						
						out.println(pair.getFirst());
						
						out.println(pair.getSecond());
						
					}
					
				} catch (FileNotFoundException e) {

					System.err.println("There was an error reading: "+ file.getAbsolutePath());
					
				}
				
			}
			
		}

		out.close();
		
		System.exit(0);
	}

	private static void lengthsCommand(PrintStream out,
			List<Pair<String, String>> seqs) {
		for (Pair<String,String> pair : seqs) {

			out.println(pair.getSecond().length());
 
		}
		out.close();
		
		System.exit(0);
		
	}

	private static void lengthCommand(PrintStream out,
			List<Pair<String, String>> seqs) {
		int[] lens = new int[seqs.size()];
		
		int count=0;
		
		for (Pair<String,String> pair : seqs) {
			
			lens[count++] = pair.getSecond().length();
			
		}

		boolean allEqual = true;

		if (lens.length>1) {
			
			for (int i = 1; allEqual==true && i<lens.length; i++) {
				
				allEqual = allEqual & (lens[i] == lens[i-1]);
				
			}

		}
		
		if (allEqual) out.println(lens[0]);
		
			else out.println(0);
	
		out.close();
		
		System.exit(0);

	}

	private static void appendCommand(SingleOption appendOpt, PrintStream out,
			FastaMultipleReader fmr, List<Pair<String, String>> seqs) {
		File otherfile = (File) appendOpt.getValue();
		if (otherfile!=null) {
			try {
				List<Pair<String,String>> other = fmr.readFile(otherfile);
				
				if (other.size()==seqs.size()) {
					
					for (int i =0; i<seqs.size();i++) {
					
						out.println(">" + seqs.get(i).getFirst());
					
						out.println(seqs.get(i).getSecond() + other.get(i).getSecond());

					}
					
				}
				
			} catch (FileNotFoundException e) {
				
				System.err.println("The file to be appended can't ve read.");
				
			}
				
		}
		
		out.close();
		
		System.exit(0);

	}

	private static void extractCommand(MultipleOption extractOpt,
			PrintStream out, List<Pair<String, String>> seqs) {
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
		out.close();
		
		System.exit(0);

	}

	private static void definitionsCommand(PrintStream out,
			List<Pair<String, String>> seqs) {
		int i=0;
		for (Pair<String, String> pair : seqs) {
			i++;
			out.println(i + ": " + pair.getFirst());
		}
		out.close();
		
		System.exit(0);

	}

	private static void countCommand(PrintStream out,
			List<Pair<String, String>> seqs) {
		// Counts the number of sequences in the alignment
		out.println(seqs.size());
		
		out.close();
		
		System.exit(0);

	}

	/**
	 * Retrieves the help of the program.
	 * @return
	 */
	private static String Help() {
		return "Fasta Alignment Manipulator - Version " + FastaAlignmentManipulator.VERSION + 
		       "\nOptions: -infile       : is the input fasta file (or stdin if no present)" +
		       "\n         -outfile      : is the path to the output file (or stdout if no present)" +
		       "\n         -extract      : extracts some of the sequences of the alignment."+
		       "\n                          a list of the order numbers of the sequence to be retrieved is needed."+
		       "\n                          the number 1 is the first sequence."+
			   "\n         -count        : counts the number of sequences in a fasta file."+
			   "\n         -length       : counts the number of columns in the alignment. If all of them haven't the same size return 0."+
			   "\n         -lengths      : counts the number of columns in each row of the alignment."+
			   "\n         -concatenate  : joins many alignments into one."+
			   "\n         -def          : shows and numerates the definitions."+
			   "\n         -append       : creates one alignment from two. The sequences of the new alignment are the combination from the other two."+
			   "\n         -slice        : cuts a segment of the alignment and keeps it. The rest is removed. " +
			   "\n                          you need to give the starting position and the last position. " +
			   "\n                          Example:  -slice=1,20  | keeps the 20 first characters of the alignment." +
			   "\n         -ver          : prints the number of the version in stdout."+
			   "\n         -help         : shows this help.";
			   
		
	}

}
