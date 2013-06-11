package seqManipulation.fastamanipulator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import math.random.FischerYatesShuffle;
import mdsj.ClassicalScaling;

import seqManipulation.AlignmentSequenceEditor;
import seqManipulation.complementary.Complementary;
import seqManipulation.dottedalignment.ReconstructDottedAlignment;
import seqManipulation.fastamanipulator.commands.RemoveGappedRows;
import seqManipulation.filtersequences.FilterSequence;
import seqManipulation.filtersequences.FilterSequenceBooleanNOT;
import seqManipulation.filtersequences.FilterSequenceBooleanOR;
import seqManipulation.filtersequences.FilterSequenceContaining;
import seqManipulation.filtersequences.FilterSequenceContainingInTitle;
import seqManipulation.filtersequences.FilterSequenceGreaterThan;
import seqManipulation.filtersequences.FilterSequenceSmallerThan;
import seqManipulation.filtersequences.FilterSequenceStartingWith;
import seqManipulation.identity.IndentityMatrixCalculator;
import seqManipulation.orf.Translate;
import utils.mutualinformation.MICalculator;

import cmdGA.MultipleOption;
import cmdGA.NoOption;
import cmdGA.Option;
import cmdGA.Parser;
import cmdGA.SingleOption;
import cmdGA.exceptions.IncorrectParameterTypeException;
import cmdGA.parameterType.FloatParameter;
import cmdGA.parameterType.InFileParameter;
import cmdGA.parameterType.InputStreamParameter;
import cmdGA.parameterType.IntegerParameter;
import cmdGA.parameterType.PrintStreamParameter;
import cmdGA.parameterType.StringParameter;
import fileformats.fastaIO.FastaMultipleReader;
import fileformats.fastaIO.Pair;

public class FastaAlignmentManipulator {
	 
	private static final String VERSION = "0.0.2";

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
		
		NoOption degapOPt = new NoOption(parser, "-degap");
		uniques.add(degapOPt);
		
		NoOption translateOPt = new NoOption(parser, "-translate");
		uniques.add(translateOPt);
		
		SingleOption translateWithOPt = new SingleOption(parser, null,"-translateWith",InFileParameter.getParameter());
		uniques.add(translateWithOPt);

		NoOption geneticCodeHelpOPt = new NoOption(parser, "-genCodeHelp");
		uniques.add(geneticCodeHelpOPt);
		
		NoOption stripGappedColumnsOpt = new NoOption(parser, "-stripGappedColumns");
		uniques.add(stripGappedColumnsOpt);
		
		SingleOption stripGapColFrOpt = new SingleOption(parser,null, "-stripGappedColFr", FloatParameter.getParameter());
		uniques.add(stripGapColFrOpt);
		
		NoOption flushEndsOpt = new NoOption(parser, "-flush");
		uniques.add(flushEndsOpt);
		
		NoOption consensusOpt = new NoOption(parser, "-consensus");
		uniques.add(consensusOpt);
		
		NoOption randomBackTranslateOpt = new NoOption(parser, "-randomRT");
		uniques.add(randomBackTranslateOpt);
		
		NoOption calculateMIOpt = new NoOption(parser, "-MI");
		uniques.add(calculateMIOpt);

		SingleOption reconstructOpt = new SingleOption(parser, 0 ,"-recFromCon", IntegerParameter.getParameter());
		uniques.add(reconstructOpt);

		SingleOption filterSizeGreaterOpt = new SingleOption(parser, null, "-fGrTh", IntegerParameter.getParameter());
		uniques.add(filterSizeGreaterOpt);
		
		SingleOption filterSizeSmallerOpt = new SingleOption(parser, null, "-fSmTh", IntegerParameter.getParameter());
		uniques.add(filterSizeSmallerOpt);
		
		SingleOption startsWithOpt = new SingleOption(parser, null, "-fStartWith", StringParameter.getParameter());
		uniques.add(startsWithOpt);
		
		SingleOption containsOpt = new SingleOption(parser, null, "-contains", StringParameter.getParameter());
		uniques.add(containsOpt);
		
		NoOption invertFilterOpt = new NoOption(parser, "-inverseFilter");
		
		NoOption complementaryOpt = new NoOption(parser, "-comp");
		uniques.add(complementaryOpt);
		
		NoOption identityValuesOpt = new NoOption(parser, "-idValues");
		uniques.add(identityValuesOpt);
		
		NoOption identityMatrixOpt = new NoOption(parser, "-idMatrix");
		uniques.add(identityMatrixOpt);
		
		NoOption deinterLeaveOpt = new NoOption(parser, "-deInter");
		uniques.add(deinterLeaveOpt);
		
		SingleOption MDSopt = new SingleOption(parser,2, "-mds", IntegerParameter.getParameter());
		uniques.add(MDSopt);
		
		SingleOption pickRandomly = new SingleOption(parser, 1, "-pick", IntegerParameter.getParameter());
		uniques.add(pickRandomly);

		SingleOption titleConstainsOpt = new SingleOption(parser, 1, "-title", StringParameter.getParameter());
		uniques.add(titleConstainsOpt);
		
		MultipleOption titlesConstainOpt = new MultipleOption(parser, 1, "-titles", ',',StringParameter.getParameter());
		uniques.add(titlesConstainOpt);

		SingleOption keepPosOpt = new SingleOption(parser, 1, "-keeppos", InFileParameter.getParameter());
		uniques.add(keepPosOpt);
		
		SingleOption remPosOpt = new SingleOption(parser, 1, "-rempos", InFileParameter.getParameter());
		uniques.add(remPosOpt);
		
		SingleOption remIdPosOpt = new SingleOption(parser, 1, "-remIdPos", InFileParameter.getParameter());
		uniques.add(remIdPosOpt);
		
		NoOption removeAllGapRowsOpt = new NoOption(parser, "-remGapRows");
		uniques.add(removeAllGapRowsOpt);

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
		
		if (geneticCodeHelpOPt.isPresent()) {
			
			genCodeHelpCommand(out);
			
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
		
		if (lensOpt.isPresent()) {

			lengthsCommand(out, seqs);
			
		}
		
		if (concatOpt.isPresent()) {
			
			concatenateCommand(concatOpt, out, fmr);
			
		}
		
		if (sliceOpt.isPresent()) {
			
			sliceCommand(sliceOpt, out, seqs);
		}
		
		if (degapOPt.isPresent()) {
			
			degapCommand(out, seqs);
		}
		
		if (translateOPt.isPresent()) {
			
			translateCommand(out, seqs);
			
		}
		
		if (translateWithOPt.isPresent()) {
			
			translateWithCommand(translateWithOPt, out, seqs);
			
		}
		
		if (stripGappedColumnsOpt.isPresent()) {
			
			stripGappedColumnsCommand(out, seqs);
			
		}
		
		if (flushEndsOpt.isPresent()) {
			
			flushEndsCommand(out, seqs);
			
		}
		
		if (consensusOpt.isPresent()) {
			
//			consensusCommand(out, seqs);
			// TODO Write consensusCommand Method!!
			
		}
		
		if (randomBackTranslateOpt.isPresent()) {
			
			randomBackTranslate(out,seqs);
			
		}
		
		if (calculateMIOpt.isPresent()) {
			
			calculateMICommand(out,seqs);
			
		}
		
		if (reconstructOpt.isPresent()) {
			
			reconstructConsensusCommand(out,seqs,(Integer)reconstructOpt.getValue());
			
		}
		
		if (filterSizeGreaterOpt.isPresent()) {
			
			Integer value = (Integer) filterSizeGreaterOpt.getValue();
			
			if (value!=null) {
				
				FilterSequence filter = new FilterSequenceGreaterThan(value);
				
				filterCommand(filter,out,seqs, invertFilterOpt.isPresent());

			}
			
		}
		
		if (filterSizeSmallerOpt.isPresent()) {
			
			Integer value = (Integer) filterSizeSmallerOpt.getValue();
			
			if (value!=null) {
				
				FilterSequence filter = new FilterSequenceSmallerThan(value);
				
				filterCommand(filter,out,seqs, invertFilterOpt.isPresent());

			}
			
		}
		
		if (complementaryOpt.isPresent()) {
			
			complementaryCommand(out,seqs);
			
		}
		
		if (startsWithOpt.isPresent())  {

			
			String value = (String) startsWithOpt.getValue();
			
			if (value!=null) {
				
				
				FilterSequence filter = new FilterSequenceStartingWith(value);
				
				filterCommand(filter,out,seqs, invertFilterOpt.isPresent());

			}
			
		}
		
		if (titleConstainsOpt.isPresent()) {
			
			String value = (String) titleConstainsOpt.getValue();
			
			if (value!=null) {
				
				
				FilterSequence filter = new FilterSequenceContainingInTitle(value);
				
				filterCommand(filter,out,seqs, invertFilterOpt.isPresent());

			}
			
		}
		
		if (titlesConstainOpt.isPresent()) {
			
			Object[] values = titlesConstainOpt.getValues();
			
			FilterSequence filter = new FilterSequenceContainingInTitle((String) values[0]);
			
			for(int i=1;i<values.length; i++) {
				
				filter = new FilterSequenceBooleanOR(filter, new FilterSequenceContainingInTitle((String) values[i]));
				
			}
			
			filterCommand(filter,out,seqs, invertFilterOpt.isPresent());
			
		}
		
		
		if (identityValuesOpt.isPresent()) {
			
			identityValuesCommand(out,seqs);
			
		}
		
		if (deinterLeaveOpt.isPresent()) {
			
			deinterleaveCommand(out,seqs);
			
		}
		
		if (containsOpt.isPresent()) {
			
			String value = (String) containsOpt.getValue();
			
			if (value!=null) {
				
				FilterSequence filter = new FilterSequenceContaining(value);
				
				filterCommand(filter,out,seqs, invertFilterOpt.isPresent());

			}
			
		}
		
		if (MDSopt.isPresent()) {
			
			MDScommand(out,seqs, (Integer) MDSopt.getValue()); 
			
		}
		
		if (pickRandomly.isPresent()) {
			
			pickCommand(out,seqs, (Integer) pickRandomly.getValue()); 
			
			
		}
		
		if (identityMatrixOpt.isPresent()) {
			
			identityMatrixCommand(out,seqs);
			
		}
		
		if (stripGapColFrOpt.isPresent()) {
			
			double fr = (Float) stripGapColFrOpt.getValue();
			
			stripGappedColumnsCommand(out, seqs, fr);
			
		}
		
		if(keepPosOpt.isPresent()) {
			
			keepPosCommand(out,seqs,keepPosOpt);
			
		}
		
		if (remPosOpt.isPresent()) {
			
			removePosCommand(out,seqs,remPosOpt);
			
		}
		
		if (remIdPosOpt.isPresent()) {
			
			removeIdPosCommand(out, seqs);
			
		}
		
		if (removeAllGapRowsOpt.isPresent()) {
			
			RemoveGappedRows rgr = new RemoveGappedRows();
			
			seqs = rgr.removeGappedRows(seqs);
			
			for (Pair<String, String> pair : seqs) {
				
				out.println(">"+pair.getFirst());
				
				out.println(pair.getSecond());
				
			}
			
			out.flush();
			
			out.close();
			
		}

	}

	///////////////////////////
	// Private Methods

	private static void removeIdPosCommand(PrintStream out, List<Pair<String, String>> seqs) {

		
		
	}

	private static void removePosCommand(PrintStream out, List<Pair<String, String>> seqs, SingleOption remPosOpt) {

	File in = (File) remPosOpt.getValue();
		
		List<Integer> removePos = new ArrayList<Integer>();
		
		if (in!=null && in.exists()) {

			String line = null;
			
			BufferedReader br;
			
			try {
				
				br = new BufferedReader(new FileReader(in));

				while ((line = br.readLine())!=null) {
					
					removePos.add(Integer.valueOf(line)); 
					
				}
				
				for (Pair<String, String> pair : seqs) {
				
					StringBuilder newseq = new StringBuilder();
					
					for(int i = 0; i < pair.getSecond().length(); i++) {
						
						if (!removePos.contains(i)) { 
						
							newseq.append(pair.getSecond().charAt(i));
						
						}
						
					}
					
					out.println(">" + pair.getFirst() );
					
					out.println(newseq.toString());
					
				} 
				
				
			} catch (IOException e) {
				
				e.printStackTrace();
				
			}
			
		}
		
		out.close();
		
		System.exit(0);
		
	}


	private static void keepPosCommand(PrintStream out, List<Pair<String, String>> seqs, SingleOption keepPosOpt) {

		File in = (File) keepPosOpt.getValue();
		
		List<Integer> keepPos = new ArrayList<Integer>();
		
		if (in!=null && in.exists()) {

			String line = null;
			
			BufferedReader br;
			
			try {
				
				br = new BufferedReader(new FileReader(in));

				while ((line = br.readLine())!=null) {
					
					keepPos.add(Integer.valueOf(line)); 
					
				}
				
				for (Pair<String, String> pair : seqs) {
				
					StringBuilder newseq = new StringBuilder();
					
					for (Integer integer : keepPos) {
						
						newseq.append(pair.getSecond().charAt(integer));
						
					}
					
					out.println(">" + pair.getFirst() );
					
					out.println(newseq.toString());
					
				} 
				
				
			} catch (IOException e) {
				
				e.printStackTrace();
				
			}
			
		}
		
		out.close();
		
		System.exit(0);
	}


	private static void stripGappedColumnsCommand(PrintStream out, List<Pair<String, String>> seqs, double fr) {
		// removes the columns of the alignment that contain a gap
		double[] gapFreq = new double[seqs.get(0).getSecond().length()];
		
		int N = seqs.size();
		
		for (Pair<String, String> seq : seqs) {
			
			for (int j = 0; j< seq.getSecond().length();j++) {
				
				gapFreq[j] = gapFreq[j] + ((seq.getSecond().charAt(j) == '-')?1:0);
				
			}
			
		}
		
		for (Pair<String, String> seq : seqs) {
			
			StringBuilder nseq = new StringBuilder();
			
			String oldSeq = seq.getSecond();
			
			for (int j = 0; j< oldSeq.length();j++) {
				
				if (gapFreq[j]/N<=fr) nseq.append(oldSeq.charAt(j)); 
				
			}
			
			out.println(">" + seq.getFirst());
			
			out.println(nseq.toString());
			
		}
		
		out.close();
		
		System.exit(0);
		
	}


	private static void identityMatrixCommand(PrintStream out, List<Pair<String, String>> seqs) {
		
		Map<Pair<Integer, Integer>, Double> matrix = IndentityMatrixCalculator.calculateIdentityMatrix(seqs);
		
		int len = seqs.size();
		
		for (int i=0;i<len;i++) {
			
			StringBuilder sb = new StringBuilder();
			
			for (int j=0;j<len;j++) {
				
				double value = 1;
				
				int pi = Math.min(i, j);
				
				int pj = Math.max(i, j);
				
				
				Pair<Integer,Integer> pair = new Pair<Integer, Integer>(pi, pj);
				
				if (matrix.containsKey(pair)) {
					
					value = matrix.get(pair) ;
					
				}
				
				sb.append(value+ ";");
				
			}
			
			sb.deleteCharAt(sb.length()-1);

			out.println(sb.toString());
			
		}
		
		out.close();
		
		System.exit(0);
		
	}


	private static void pickCommand(PrintStream out, List<Pair<String, String>> seqs, Integer value) {

		List<Object> s = new ArrayList<Object>();
		
		for (Pair<String, String> pair : seqs) {
			
			s.add(pair);
			
		}
		
		if (seqs.size()>0) {
				
			FischerYatesShuffle.shuffle(value, s);
				
		}
			
		
		for (int i = 0; i<value; i++) {
			
			@SuppressWarnings("unchecked")
			Pair<String, String> pair = (Pair<String,String>)s.get(i);
			
			out.println(">"+ pair.getFirst());
			out.println(pair.getSecond());
			
		}
		
		
		out.close();
		
		System.exit(0);

		
	}


	private static void MDScommand(PrintStream out, List<Pair<String, String>> seqs, int dim) {

		Map<Pair<Integer, Integer>, Double> a = IndentityMatrixCalculator.calculateIdentityMatrix(seqs);

		double[][] input = new  double[seqs.size()][seqs.size()]; 
		
		for (int i=0; i<seqs.size();i++) {    		// Initialize Input Matrix 
			
			for (int j=0; j<seqs.size();j++) {
				
				input[i][j] = 0;
				
			}
			
		}
		
		for (Pair<Integer, Integer> pair: a.keySet()) { // Convert Identities Values to distances (distance = 1 - identity) 
			
			Integer first = pair.getFirst();
			
			Integer second = pair.getSecond();
			
			input[first][second] = 1d - a.get(pair);
			
			input[second][first] = 1d - a.get(pair);
			
		}

		int n=input[0].length;    // number of data objects
		
		
		double[][] evecs = new double[dim][n];      // Array to store eigenvectors
		
		double[] evals = new double[dim];           // Array to store eigenvalues
		
		ClassicalScaling.eigen(input, evecs, evals);  // Perform MDS

	    StringBuilder headersb = new StringBuilder(); // Prepare output
	    
		for(int i = 0 ; i< dim; i++) {

			headersb.append("d"+i+",");
			
		}
		
		String header = headersb.toString();
		
		header = header.substring(0, header.length()-1);
		
		out.println(header);
	    
		for (int i = 0; i < n; i++) {
			StringBuilder line = new StringBuilder();
			
			for (int j=0; j<dim; j++) {
				
				line.append(evecs[j][i]);
				line.append(",");				
				
			}
			
			String printline = line.toString();
			
			out.println(printline.substring(0,printline.length()-1));
			
		}
		
		for(int i = 0 ; i< dim; i++) {

			System.err.println("eigenvalue["+i+"]: " + evals[i]);
			
		}
		
		out.flush();
		
		out.close();
		
		System.exit(0);

		
	}


	private static void deinterleaveCommand(PrintStream out, 	List<Pair<String, String>> seqs) {
		
		for (Pair<String, String> pair : seqs) {
			
			out.println(">" + pair.getFirst());
			
			out.println(pair.getSecond());
			
		}
		
		out.close();
		
		System.exit(0);
		
	}


	private static void identityValuesCommand(PrintStream out, List<Pair<String, String>> seqs) {
		
		List<Double> values = IndentityMatrixCalculator.listOfIdentityValues(seqs);
		
		for (Double value : values) {

			out.println(value);
			
		} 
		
		
		out.close();
		
		System.exit(0);

		
	}


	private static void complementaryCommand(PrintStream out, List<Pair<String, String>> seqs) {

		for (Pair<String, String> pair : seqs) {
			
			out.println(">" + pair.getFirst());
			
			out.println(Complementary.reverseComplementary(pair.getSecond()));
			
		}
		
		
		out.close();
		
		System.exit(0);

		
	}


	private static void filterCommand(FilterSequence filter, PrintStream out, List<Pair<String, String>> seqs, boolean invertFilter) {

		if (invertFilter) filter = new FilterSequenceBooleanNOT(filter);
		
		for (Pair<String, String> pair : seqs) {
			
			if (filter.filter(pair) ) {
				
				out.println(">" + pair.getFirst());
				
				out.println(pair.getSecond());
				
			}
			
		}
		
		
		out.close();
		
		System.exit(0);

		
	}


	private static void reconstructConsensusCommand(PrintStream out, 	List<Pair<String, String>> seqs, Integer reference) {
	
		
		List<Pair<String,String>> rec = ReconstructDottedAlignment.reconstruct(seqs, reference); 
		
		for (Pair<String, String> pair : rec) {
			
			out.println(">" + pair.getFirst());
			
			out.println(pair.getSecond());
			
		}
		
		out.flush();
		
		out.close();
		
		System.exit(0);
		
	}
	


	private static void calculateMICommand(PrintStream out, List<Pair<String, String>> seqs) {
		
		AlignmentSequenceEditor ase = new AlignmentSequenceEditor(seqs);

		int length = ase.getRowsSize();
		
		Map<Pair<Integer, Integer>, Double> MI = MICalculator.calculateProteinMIMatrix(seqs);
			
		for (int i = 0; i <length;i++ ) {
			
			StringBuilder currentLine = new StringBuilder();

			for (int j = 0; j <length;j++ ) {
				
				
				int pa = Math.min(i, j);
				int pb = Math.max(i, j);
				
				if (pa==pb) {
					currentLine.append(0);
				} else {
					
					double value = MI.get(new Pair<Integer,Integer>(pa,pb));
					
					Locale defLocale = Locale.getDefault();
					
					currentLine.append(String.format(defLocale, "%.4f",value));
				}
				
				if (j<length-1)currentLine.append("; ");
				
			}

			out.println(currentLine);
			
		}
		
	}


	private static void randomBackTranslate(PrintStream out, List<Pair<String, String>> seqs) {

		Map<String,String> geneticCode = Translate.getInstance().getStandardGeneticCode();
		
		Map<String,List<String>> geneticCodeRev = new HashMap<String, List<String>>();
		
		for (String codon : geneticCode.keySet()) {
			
			String aa = geneticCode.get(codon);
			
			if (geneticCodeRev.containsKey(aa)) {
				
				geneticCodeRev.get(aa).add(codon);
				
			} else {
				
				List<String> codons = new ArrayList<String>();
				
				codons.add(codon);
				
				geneticCodeRev.put(aa, codons);
				
			}
			
		}
		
		for (Pair<String, String> pair : seqs) {
			
			out.println(">" + pair.getFirst());
			
			StringBuilder newSeq = new StringBuilder();
			
			for (int i=0;i<pair.getSecond().length();i++) {
				
				String current = pair.getSecond().substring(i, i+1);
				
				if (geneticCodeRev.containsKey(current)) {
				
					List<String> list = geneticCodeRev.get(current); 
				
					int randomIndex = ((Double)(Math.random()*list.size())).intValue();
					
					newSeq.append(list.get(randomIndex));
				
				}
				
			}
			
			out.println(newSeq.toString());
			
		}
		
		out.close();
		
		System.exit(0);
		
	}


	protected static void genCodeHelpCommand(PrintStream out) {
		
		out.println("The File Format for the genetic code is like:");
		out.println("");
		out.println("A, GCT, GCC, GCA, GCG");
		out.println("C, TGT, TGC");
		out.println("D, GAT, GAC");
		out.println("E, GAA, GAG");
		out.println("F, TTT, TTC");
		out.println("G, GGT, GGC, GGA, GGG");
		out.println("H, CAT, CAC");
		out.println("I, ATT, ATC, ATA");
		out.println("K, AAA, AAG");
		out.println("L, TTA, TTG, CTT, CTC, CTA, CTG");
		out.println("M, ATG");
		out.println("N, AAT, AAC");
		out.println("P, CCT, CCC, CCA, CCG");
		out.println("Q, CAA, CAG");
		out.println("R, CGT, CGC, CGA, CGG, AGA, AGG");
		out.println("S, TCT, TCC, TCA, TCG, AGT, AGC");
		out.println("T, ACT, ACC, ACA, ACG");
		out.println("V, GTT, GTC, GTA, GTG");
		out.println("W, TGG");
		out.println("Y, TAT, TAC");
		out.println("*, TAA, TGA, TAG");
		
		out.close();
		
		System.exit(0);
		
	}
	
	// private and protected methods

	protected static void translateWithCommand(SingleOption translateWithOPt, PrintStream out, List<Pair<String, String>> seqs) {
		Map<String, String> geneticCode=null;
		
		InputStreamReader gcISR = (InputStreamReader) translateWithOPt.getValue();
		
		BufferedReader infile = new BufferedReader(gcISR);
		
		geneticCode = Translate.readGeneticCode(infile);

		Translate translator = Translate.getInstance();
		
		for (Pair<String, String> pair : seqs) {
			
			out.println(">"+ pair.getFirst());
			
			out.println(translator.translateSeq(pair.getFirst(),geneticCode));
			
		}
		
		out.close();
		
		System.exit(0);
	}


	
	protected static void translateCommand(PrintStream out,	List<Pair<String, String>> seqs) {

		Translate translator = Translate.getInstance();
		
		for (Pair<String, String> pair : seqs) {
			
			out.println(">"+ pair.getFirst());
			
			out.println(translator.translateSeq(pair.getSecond()));
			
		}
		
		out.close();
		
		System.exit(0);
	}

	private static void degapCommand(PrintStream out, List<Pair<String, String>> seqs) {

		for (Pair<String, String> pair : seqs) {

			String newSeq = pair.getSecond().replaceAll("-", "");
			
			out.println(">" + pair.getFirst());

			out.println(newSeq);
			
		}
		
		out.close();

		System.exit(0);
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
					
					out.println(">" + pair.getFirst());
					
					out.println(pair.getSecond().substring(from, to));
					
				}
				
			}
			
			
		}
		
		out.close();

		System.exit(0);
	}

	private static void concatenateCommand(MultipleOption concatOpt, PrintStream out, FastaMultipleReader fmr) {
		
		Object[] filesAsObjectArray = concatOpt.getValues();
		File[] files = new File[filesAsObjectArray.length];
		
		for (int i =0; i<filesAsObjectArray.length; i++) {
			
			files[i] = (File) filesAsObjectArray[i];
			
		}
		
		if (files != null) {
			
			for (File file : files) {

				try {
					
					List<Pair<String,String>> pairs = fmr.readFile(file);
					
					for (Pair<String, String> pair : pairs) {
						
						out.println(">" + pair.getFirst());
						
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
				
				System.err.println("The file to be appended can't be read.");
				
			}
				
		}
		
		out.close();
		
		System.exit(0);

	}

	private static void extractCommand(MultipleOption extractOpt, PrintStream out, List<Pair<String, String>> seqs) {
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
	
	private static void stripGappedColumnsCommand(PrintStream out,
			List<Pair<String, String>> seqs) {
		// removes the columns of the alignment that contain a gap
		boolean[] keeper = new boolean[seqs.get(0).getSecond().length()];
		
		for (int i=0; i<keeper.length; i++) {
			keeper[i] = true;
		}
		
		for (Pair<String, String> seq : seqs) {
			
			for (int j = 0; j< seq.getSecond().length();j++) {
				
				keeper[j] = keeper[j] && !(seq.getSecond().charAt(j) == '-');
				
			}
			
		}
		
		for (Pair<String, String> seq : seqs) {
			
			StringBuilder nseq = new StringBuilder();
			
			String oldSeq = seq.getSecond();
			
			for (int j = 0; j< oldSeq.length();j++) {
				
				if (keeper[j]) nseq.append(oldSeq.charAt(j)); 
				
			}
			
			out.println(">" + seq.getFirst());
			
			out.println(nseq.toString());
			
		}
		
		out.close();
		
		System.exit(0);

	}
	
	private static void flushEndsCommand(PrintStream out,
			List<Pair<String, String>> seqs) {
		// Removes columns from the ends while the beginning of at least one sequence hadn't started 

		int min=0;
		
		int max = seqs.get(0).getSecond().length();
		
		for (Pair<String, String> pair : seqs) {
			
			String currentSeq = pair.getSecond();
			
			int currentMin = 0;
			
			int currentMax = 0;
			
			for (int i = 0; i < currentSeq.length();i++) {
				
				if (currentSeq.charAt(i)!='-') {
					
					currentMin = i;
					
					break;
					
				}
				
			}
			
			for (int i = currentSeq.length()-1; 1>= 0;i--) {
				
				if (currentSeq.charAt(i)!='-') {
					
					currentMax = i;
					
					break;
					
				}
				
			}
			
			min = Math.max(min, currentMin);
			
			max = Math.min(max, currentMax);
			
		}
		
		for (Pair<String, String> pair : seqs) {
			
			out.println(">" + pair.getFirst());
			
			out.println(pair.getSecond().substring(min, max));
			
		}

	}
	



	/**
	 * Retrieves the help of the program.
	 * @return
	 */
	private static String Help() {
		return "Fasta Alignment Manipulator - Version " + FastaAlignmentManipulator.VERSION + 
		       "\nOptions: -infile            : is the input fasta file (or stdin if no present)" +
		       "\n         -outfile           : is the path to the output file (or stdout if no present)" +
		       "\n         -revcomp           : gets the reverse complementary sequence"+
		       "\n         -extract           : extracts some of the sequences of the alignment."+
		       "\n                               a list of the order numbers of the sequence to be re-stripGappedColFrtrieved is needed."+
		       "\n                               the number 1 is the first sequence."+
			   "\n         -count             : counts the number of sequences in a fasta file."+
			   "\n         -length            : counts the number of columns in the alignment. If all of them haven't the same size return 0."+
			   "\n         -lengths           : counts the number of columns in each row of the alignment."+
			   "\n         -concatenate       : joins many alignments into one."+
			   "\n                               the names of files to be concatenated must be separated by a comma (',') chaarcter."+
			   "\n         -def               : shows and numerates the definitions."+
			   "\n         -append            : creates one alignment from two. The sequences of the new alignment are the combination from the other two."+
			   "\n         -slice             : cuts a segment of the alignment and keeps it. The rest is removed. " +
			   "\n                               you need to give the starting position and the last position. " +
			   "\n                               Example:  -slice=1,20  | keeps the 20 first characters of the alignment." +
			   "\n         -degap             : removes \"-\" symbols from each sequence." +
			   "\n         -translate         : translate DNA sequences into aminoacid." +
			   "\n         -translateWith     : translate DNA sequences into aminoacid using the given file containing" +
			   "\n                                a genetic code" +
			   "\n         -genCodeHelp       : shows help about the genetic code format" +
			   "\n         -randomRT          : back-translate a protein sequence into a DNA sequence, choosing one the posible codons randomly" +
			   "\n         -recFromCon        : reconstruct an alignment from a dotted alignment with reference consensus sequence" +
			   "\n                               Example: -recFromCon=1  | uses the first sequence as reference" +
			   "\n         -fGrTh             : looks through the alignment and removes all the sequences, except the ones that are greater than a given size" +
			   "\n                               Example: -fGrTh=7000    | removes sequences with lenghts lower or equal to 7000" +
			   "\n         -fSmTh             : looks through the alignment and removes all the sequences, except the ones that are smaller than a given size" +
			   "\n                               Example: -fSmTh=7000    | removes sequences with lenghts greater or equal to 7000" +
			   "\n         -title             : filter sequences that containg a given string in the title" +
			   "\n                               Example: -title=gi00001 | keeps the sequences that contains the string gi00001 in the title" +
			   "\n         -idValues          : gets list of identity percent values between all the sequences in the alignment" +
			   "\n         -idMatrix          : exports an csv file with a symmetric matrix with identities values" +
			   "\n         -mds               : performs a Multidimensional Scaling analysis (using MDSJ package developed by Christian Pich (University of Konstanz))" +
			   "\n                               a number that indicantes the number of output dimensions"+
			   "\n         -stripGappedColFr  : Removes columns of the alignment that contains more than a given proportion of gaps"+
			   "\n         -pick              : pick a random number set of the sequences" +
			   "\n                               Example: -pick 5        | pick 5 random sequences"+
			   "\n         -keeppos           : reads a file that contains one number per line and keeps these numbers position of the alignment and eliminate the others" +
			   "\n         -rempos            : reads a file that contains one number per line and removes these numbers position of the alignment and keeps the others" +
			   "\n         -remIdPos          : removes columsn of an alignment that contains only one char (ignoring gaps)" +
			   "\n         -ver               : prints the number of the version in stdout."+
			   "\n         -help              : shows this help." +
			   "\n";
	}
	
}
