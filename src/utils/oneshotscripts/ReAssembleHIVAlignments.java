package utils.oneshotscripts;

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
import java.util.Map;

import cmdGA.MultipleOption;
import cmdGA.Parser;
import cmdGA.SingleOption;
import cmdGA.exceptions.IncorrectParameterTypeException;
import cmdGA.parameterType.InFileParameter;
import cmdGA.parameterType.InputStreamParameter;
import cmdGA.parameterType.PrintStreamParameter;
import fileformats.fastaIO.FastaMultipleReader;
import pair.Pair;

public class ReAssembleHIVAlignments {

	/**
	 * @param args
	 * @throws IncorrectParameterTypeException 
	 */
	public static void main(String[] args) throws IncorrectParameterTypeException {
		
		Parser parser = new Parser();
		
		SingleOption infileOpt = new SingleOption(parser, System.in, "-accessions", InputStreamParameter.getParameter());
		
		MultipleOption inAlignments = new MultipleOption(parser, null, "-alignments", ',', InFileParameter.getParameter());
		
		SingleOption outfileOpt = new SingleOption(parser, System.out, "-outfile", PrintStreamParameter.getParameter());

		parser.parseEx(args);
		
		
		if (!infileOpt.isPresent() || !inAlignments.isPresent()) {
			
			System.err.println("-accessions and -alignments options are required");
			
			System.exit(0);
			
		}
		
		////////////////////////////////////////
		// Create main object
		
		ReAssembleHIVAlignments raha = new ReAssembleHIVAlignments();
		
		////////////////////////////////////////
		// Read parameters
		
		BufferedReader inAcc = new BufferedReader(new InputStreamReader( (InputStream) infileOpt.getValue() ));
		
		Object[] alignmentFileAsObject = inAlignments.getValues();
		
		PrintStream outfile = (PrintStream) outfileOpt.getValue();
		
		
		///////////////////////////////////////
		// Preparing data
		
		List<String> genomes = raha.loadGenomeAccessions(inAcc);
		
		System.err.println("Sequences Loaded: " + genomes.size());
		
		List<Map<String,Pair<String,String>>> alignments = raha.loadAlignments(alignmentFileAsObject);
		
		System.err.println("Proteins: " + alignments.size());
		
		for (Map<String, Pair<String, String>> map : alignments) {
			
			System.err.println("   keyset size:" + map.keySet().size());
			
		}
		
		///////////////////////////////////////
		// Fetching lengths of alignments
		
		List<Integer> lengths = raha.fetchAlignmentLengths(alignments);
		
		for (Integer integer : lengths) {
			
			System.err.println("   Protein Alignment length: " +integer);
			
		}
		
		///////////////////////////////////////
		// Re - Assembling the alignments
		
		for (String accession : genomes) {
			
			StringBuilder currentAlignmentRow = new StringBuilder();
			
			for (int i = 0 ; i< alignments.size(); i++) {
				
				Map<String,Pair<String,String>> alignment = alignments.get(i);
				
				String protSeq = "";
				
				if (alignment.containsKey(accession)) {
				
					protSeq = alignment.get(accession).getSecond();
					
				} else {
					
					int l = lengths.get(i);
					
					StringBuilder sb = new StringBuilder();
					
					while (l>0) {
						
						sb.append('-');
						
						l--;
						
					}
					
					protSeq = sb.toString();
					
				}
				
				currentAlignmentRow.append(protSeq);
			}
			
			outfile.println(">" + accession);
			
			outfile.println(currentAlignmentRow.toString());

		}
		
		outfile.flush();
		
		outfile.close();

	}

	private List<Integer> fetchAlignmentLengths( List<Map<String, Pair<String, String>>> alignments) {
		
		List<Integer> results = new ArrayList<>();
		
		for (Map<String, Pair<String, String>> map : alignments) {
			
			results.add((map.values().iterator().next().getSecond().length()));
			
		}
		
		return results;
		
	}

	private List<Map<String, Pair<String, String>>> loadAlignments(Object[] alignemtFiles) {
		
		List<Map<String, Pair<String, String>>> results = new ArrayList<>();
		
		FastaMultipleReader fmr = new FastaMultipleReader();

		for (Object file : alignemtFiles) {
			
			File align = (File) file;
			
			try {
				
				BufferedReader br = new BufferedReader(new FileReader(align));
				
				List<Pair<String, String>> seqs = fmr.readBuffer(br);
				
				Map<String, Pair<String,String>> seqMap = new HashMap<>();
				
				for (Pair<String, String> pair : seqs) {
					
					seqMap.put(pair.getFirst(), pair);
					
				}
				
				results.add(seqMap);
				
			} catch (FileNotFoundException e) {
				
				e.printStackTrace();
				
			}
			
			
		}
		
		return results;
		
	}

	private List<String> loadGenomeAccessions(BufferedReader in) {
		
		List<String> results = new ArrayList<>();
		
		String currentline = null;
		
		try {
			
			
			while ((currentline=in.readLine())!= null) {
				
				results.add(currentline);
				
			}
		} catch (IOException e) {
			
			e.printStackTrace();
			
		}
		
		return results;
	}

}
