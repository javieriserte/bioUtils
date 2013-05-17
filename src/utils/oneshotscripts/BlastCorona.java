package utils.oneshotscripts;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import blastSearch.BlastResult;

import fileformats.fastaIO.FastaMultipleReader;
import fileformats.fastaIO.FastaWriter;
import fileformats.fastaIO.Pair;

public class BlastCorona {

	/**
	 * Este script es para blastear contra coronavirus
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		String basepath = "/home/javier/Dropbox/Posdoc/ICTVtaxonomy/consultadb/";
		
		File fileqithqueries = new File(basepath+"CORONA_ref.fas");
		
		FastaMultipleReader fmr = new FastaMultipleReader();
		
		List<BlastResult> blastsresults = new ArrayList<BlastResult>();
		
		try {
			
			List<Pair<String,String>> fastas = fmr.readFile(fileqithqueries);
			
			FastaWriter fw = new FastaWriter();
			
			for (Pair<String, String> fasta : fastas) {
				
				fw.writeFile(basepath + "tmpfasta",fasta.getSecond(),fasta.getFirst());
				
				Process p = Runtime.getRuntime().exec("blastp -query tmpfasta -db coronadb -outfmt 6", null, new File(basepath));
				
				BufferedReader pinput= new BufferedReader(new InputStreamReader(p.getInputStream()));
				
				String line = null;
				
				while ((line = pinput.readLine())!=null) {
					
					BlastResult res = new BlastResult();
					
					res.parse(line);
					
					blastsresults.add(res);
					
				}
				
				Map<String,BlastResult> map = new LinkedHashMap<String, BlastResult>();
				
				for (BlastResult result: blastsresults ) {

					String gbId = result.getSubject().split("\\|")[1];
					
					if (!map.containsKey(gbId)) map.put(gbId, result);
					
				}
				
				String orfn = fasta.getFirst().split("\\|")[2];
				
				PrintWriter wr = new PrintWriter(new FileWriter(new File(basepath+"CORONA_ref_"+orfn+".fas")));
				
				for (BlastResult result : map.values()) {
					
					wr.println(result.getSubject());
					
				}
				
				wr.close();
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		

	}

}
