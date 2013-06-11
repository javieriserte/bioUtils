package utils.oneshotscripts;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fileformats.fastaIO.FastaMultipleReader;
import fileformats.fastaIO.FastaWriter;
import fileformats.fastaIO.Pair;


public class ReorderPicornaAlignments {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		ReorderPicornaAlignments rpa = new ReorderPicornaAlignments();

		List<String> orderGI = rpa.loadOrder(new File("/home/javier/Dropbox/Posdoc/PICORNAVIRIDAE/separateByProt/allGIs"));
		
		List<Map<String,String>> sequences = rpa.loadSequences(new File("/home/javier/Dropbox/Posdoc/PICORNAVIRIDAE/separateByProt/"));
		
		List<Integer> sizes = getSubAlignmentsSize(sequences);
		
		List<Pair<String,String>> results = new ArrayList<Pair<String,String>>();
		
		for (String GI : orderGI) {
			
			StringBuilder currentPoly = new StringBuilder();
			
			for (int i=0; i<11; i++) {
				
				currentPoly.append(rpa.getSequenceFor(GI,i,sequences,sizes));
				
			}
			
			results.add(new Pair<String, String>(GI, currentPoly.toString()));
			
		}
		
		rpa.exportFasta(results, new File("/home/javier/Dropbox/Posdoc/PICORNAVIRIDAE/separateByProt/JT.Picorna.muscle.fas"));
		
	}
	
	private void exportFasta(List<Pair<String, String>> results, File out) {
		
		FastaWriter fw = new FastaWriter();

		
		for (Pair<String, String> pair : results) {
		
			fw.writeFile(out, pair.getSecond(), pair.getFirst(), true);
			
		}
		
	}

	private String getSequenceFor(String gI, int i, List<Map<String, String>> sequences, List<Integer> sizes) {
		
		String sequence = sequences.get(i).get(gI);
		
		if (sequence==null || sequence.equals("")) {
			
			return fullGapSequence(sizes.get(i)	);
			
		} else {
		
			return sequence;
		
		}
		
	}

	private static List<Integer> getSubAlignmentsSize( List<Map<String, String>> sequences) {
		
		List<Integer> results = new ArrayList<Integer>(); 
		
		for (Map<String,String> orthologs : sequences) {
			
			int size = 0;

			for (String protein : orthologs.values()) {
				
				size = protein.length();
				
				if (size!=0) {
					
					break;
				}
				
			}
			
			results.add(size);
			
		}
		
		return results;
		
		
	}

	private List<Map<String, String>> loadSequences(File file) {
		
		List<Map<String,String>> results = new ArrayList<Map<String,String>>();
		
		FastaMultipleReader fmr = new FastaMultipleReader();
		
		for(int i=1; i<12; i++) {

			try {
				
				File currentFile = new File(file,String.format("JTORF%02d.muscle.fas",i));
				
				List<Pair<String, String>> seqs = fmr.readFile(currentFile);
				
				Map<String,String> orthologs = new HashMap<String, String>();
				
				for (Pair<String, String> pair : seqs) {
					
					String extractedGI = pair.getFirst().split("\\|")[0];

					orthologs.put(extractedGI, pair.getSecond());
					
				}
				
				results.add(orthologs);
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
		}
		
		return results;
		
	}

	private List<String> loadOrder(File file) {
		
		List<String> results = new ArrayList<String>();
		
		try {
			
			BufferedReader in = new BufferedReader(new FileReader(file));
			
			String currentLine = null;
			
			while((currentLine = in.readLine())!= null) {
				
				results.add(currentLine);
				
			}
			in.close();
			return results;
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
		
	}

	public String fullGapSequence(int length) {
		
		StringBuilder sb = new StringBuilder(length);
		
		for (int i = 0; i<length; i++) {
			
			sb.append('-');
			
		}
		
		return sb.toString();
		
	}

}
