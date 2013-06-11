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

public class MassiveTblastnPicorna {
	
	private String tblastn_exec_path = "/usr/bin/tblastn";
	
	private String makeblastdb_exec_path = "/usr/bin/makeblastdb";

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {

		MassiveTblastnPicorna mbxp = new MassiveTblastnPicorna();
		
		List<String> subjectGI = mbxp.getSubjectGI(new File("/home/javier/Dropbox/Posdoc/PICORNAVIRIDAE/separateByProt/queriesGI"));
	
		Map<String, String> genomes = mbxp.loadGenomes(subjectGI, new File("/home/javier/Dropbox/Posdoc/PICORNAVIRIDAE/PICORNAVIRIDAE.fas"));
		
		File tmp_fasta = new File("/home/javier/Dropbox/Posdoc/PICORNAVIRIDAE/separateByProt/tmp.fas");

		File tmp_db = new File("/home/javier/Dropbox/Posdoc/PICORNAVIRIDAE/separateByProt/tmp");
		
		PrintStream out = new PrintStream("/home/javier/Dropbox/Posdoc/PICORNAVIRIDAE/separateByProt/tblastn.results");
		
		List<File> pssms = new ArrayList<File>();
		
		for (int i=1; i<12; i++) {
			
			String number = String.format("%02d", i);
			
			pssms.add(new File("/home/javier/Dropbox/Posdoc/PICORNAVIRIDAE/separateByProt/ORF"+number+".pssm"));
			
		}

		for (String string : subjectGI) {
			// Iterate over each genome
			
			String genome_sequence = genomes.get(string);
			
			mbxp.exportTmpFasta(genome_sequence, string, tmp_fasta);
			
			mbxp.makeblastdb(tmp_fasta, tmp_db);
			
			for (int i=0; i<11; i++) {
				// Iterate over each protein
				File orf_pssm = pssms.get(i);
				
				BlastResult result = mbxp.performTBlastn(orf_pssm, tmp_db);
				
				if (result != null) {

					String out_seq;
					
					int beginIndex = Math.min(result.getSubjectStart(),result.getSubjectEnd());
					
					int endIndex =  Math.max(result.getSubjectStart(),result.getSubjectEnd());
					
					out_seq = genome_sequence.substring(beginIndex-1,endIndex);
					
					if (result.getSubjectStart()>result.getSubjectEnd()) {
						
						out_seq = Complementary.reverseComplementary(out_seq);
					}
					
					out.println(">"+string+"|ORF"+String.format("%02d", i+1));
					
					out.println(out_seq);
				
				}
				
			}
			
			System.out.println("Genome "+ string + " Done.");
			
		}
		
		out.close();
		
	}
	
	
	private void makeblastdb(File in, File out) throws IOException {
		
		Runtime rt = Runtime.getRuntime();
		
		String commandline = makeblastdb_exec_path + " -in " + in.getAbsolutePath()+" -out " + out.getAbsolutePath() + " -dbtype nucl";
			
		rt.exec(commandline);
			
	}
	
	private Map<String, String> loadGenomes(List<String> GIs, File genomes) throws FileNotFoundException {
		
		Map<String,String> result =  new HashMap<String, String>();
		
		FastaMultipleReader fmr = new FastaMultipleReader();
			
		List<Pair<String, String>> list = fmr.readBuffer(new BufferedReader(new FileReader(genomes)));
			
		for (Pair<String, String> pair : list) {
				
				String first = pair.getFirst();
				
				first = first.split("\\|")[1];
				
				if (GIs.contains(first)) {
				
					result.put(first, pair.getSecond());
				
				}
				
		}
				
		return result;
		
	}
	
	private List<String> getSubjectGI(File queriesfile) throws IOException {
		
		BufferedReader inb = new BufferedReader(new FileReader(queriesfile));
		
		List<String> result = new ArrayList<String>();
		
		String line = null;
		
		while ((line = inb.readLine())!=null) {
			
			result.add(line);
			
		}
		
		inb.close();
		
		return result;
		
		
	}


	public BlastResult performTBlastn(File pssm, File db) {
		
		Runtime rt = Runtime.getRuntime();
		
		BlastResult result = null;
		
		try {
			
			String db_path = db.getAbsolutePath();
			
//			db_path = db_path.substring(0, db_path.lastIndexOf('.')-1);
			
			String commandline = tblastn_exec_path + " -in_pssm "+pssm.getAbsolutePath()+" -db "+ db_path + " -outfmt 6";
			
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

	
	private void exportTmpFasta(String sequence, String description, File path) {

		try {
			
			PrintStream out = new PrintStream(path);
			
			out.println(">"+description);
			out.println(sequence);
			
			out.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
