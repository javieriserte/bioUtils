package utils.oneshotscripts;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import seqManipulation.complementary.Complementary;

import fileformats.fastaIO.FastaMultipleReader;
import fileformats.fastaIO.Pair;

import blastSearch.BlastResult;

public class MassiveBlastXPicorna {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {

		MassiveBlastXPicorna mbxp = new MassiveBlastXPicorna();
		
		List<String> queriesGI = mbxp.getQueriesGI();
		
		PrintStream out = new PrintStream(new File("/home/javier/Dropbox/Posdoc/PICORNAVIRIDAE/separateByProt/XORFs.fas"));
		
		Map<String,String> allsequences = mbxp.loadGenomes(queriesGI); // QuerySequences
		
		Map<String,String> allProt = mbxp.loadProt(); // Subject Sequences
		
		int blastcounter=0;
		
		for (String string : queriesGI) {
			
			for (int i=0; i<11; i++) { // for each orf
				
				String number = String.format("%02d", i+1);
			
				// Perform Blastx

				String tmpfile = mbxp.exportTmpFasta(allsequences.get(string), string);
			
				BlastResult r = mbxp.performBlastX(tmpfile, "orf"+number);
				blastcounter++;
			
				
				// get extended region
				
				System.err.println(string + " , " + number);
				
				if (r != null) {
					String querygi = r.getQuery();
					
					String querygenome = allsequences.get(querygi);
					
					String subjectprot = allProt.get(r.getSubject());
					
					int beginIndex;
					
					int endIndex;
					
					if (r.getQueryStart()>r.getQueryEnd()) {
						

						endIndex = r.getQueryStart() - 3 * (r.getSubjectStart()-1);
						
						beginIndex = r.getQueryEnd() + 3* (subjectprot.length() - r.getSubjectEnd());
						
						querygenome = Complementary.reverseComplementary(querygenome.substring(beginIndex,endIndex));
						
						
					} else {
					
						beginIndex = r.getQueryStart() - 3 * (r.getSubjectStart()-1);
					
						endIndex = r.getQueryEnd() + 3* (subjectprot.length() - r.getSubjectEnd());
						
						querygenome = querygenome.substring(beginIndex,endIndex);
					
					}
					
					// printout orfs
					
					out.println(">" + querygi+"|ORF"+number);
					
					out.println(querygenome);
				
				}
				
			}
			
			out.close();
			
			System.err.println(blastcounter);
			
		}

	}
	
	private String exportTmpFasta(String sequence, String gi) {

		String path = "/home/javier/Dropbox/Posdoc/PICORNAVIRIDAE/separateByProt/tmp";
		
		try {
			
			PrintStream out = new PrintStream(new File(path	));
			
			out.println(">"+gi);
			out.println(sequence);
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return path;
		
	}

	private Map<String, String> loadProt() throws FileNotFoundException {
		
		Map<String,String> result =  new HashMap<String, String>();
		
		String basepath = "/home/javier/Dropbox/Posdoc/PICORNAVIRIDAE/separateByProt/";
		
		for (int i=0; i<11; i++) { // for each orf
			
			String number = String.format("%02d", i+1);
			
			FastaMultipleReader fmr = new FastaMultipleReader();
			
			List<Pair<String, String>> list = fmr.readBuffer(new BufferedReader(new FileReader(new File(basepath + "ORF"+ number+ ".fas"))));
			
			for (Pair<String, String> pair : list) {
				
				result.put(pair.getFirst(), pair.getSecond());
				
			}
			
		}
		
		return result;
		
	}

	private List<String> getQueriesGI() throws IOException {
		
		File in = new File("/home/javier/Dropbox/Posdoc/PICORNAVIRIDAE/separateByProt/queriesGI");
		
		BufferedReader inb = new BufferedReader(new FileReader(in));
		
		List<String> result = new ArrayList<String>();
		
		String line = null;
		
		while ((line = inb.readLine())!=null) {
			
			result.add(line);
			
		}
		
		inb.close();
		
		return result;
		
		
	}

	private Map<String, String> loadGenomes(List<String> GIs) throws FileNotFoundException {
		
		Map<String,String> result =  new HashMap<String, String>();
		
		String basepath = "/home/javier/Dropbox/Posdoc/PICORNAVIRIDAE/PICORNAVIRIDAE.fas";
		
		FastaMultipleReader fmr = new FastaMultipleReader();
			
		List<Pair<String, String>> list = fmr.readBuffer(new BufferedReader(new FileReader(new File(basepath))));
			
		for (Pair<String, String> pair : list) {
				
				String first = pair.getFirst();
				
				first = first.split("\\|")[1];
				
				if (GIs.contains(first)) {
				
					result.put(first, pair.getSecond());
				
				}
				
		}
				
		return result;
		
	}


	public BlastResult performBlastX(String filepathquery, String db) {
		
		Runtime rt = Runtime.getRuntime();
		
		BlastResult result = null;
		
		try {
			String commandline = "/usr/bin/blastx -query "+filepathquery+" -db /home/javier/Dropbox/Posdoc/PICORNAVIRIDAE/separateByProt/" +db + " -outfmt 6";
			
			Process pr = rt.exec(commandline);
			
			BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));
			
			String line = null;
			
			double minEvalue = 0.01;
			
			while ((line=in.readLine())!=null) {
				
				BlastResult r = new BlastResult();
				
				r.parse(line);
				
				if (r.getEvalue()<minEvalue) {
					
					result = r;
					
					minEvalue= r.getEvalue();
					
				}
				
			}
			
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return result;
	}

}
