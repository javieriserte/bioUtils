package utils.oneshotscripts;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
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
		
		String basepath = "/home/javier/Dropbox/Posdoc/ICTVtaxonomy/consultadb/";
		
		String allProteinsPath = "/home/javier/Dropbox/Posdoc/ICTVtaxonomy/consultadb/CORONA.Prot.pred.fas";
		
		
		File fileqithqueries = new File(basepath+"CORONA_ref.fas");
		
		FastaMultipleReader fmr = new FastaMultipleReader();
		
		List<BlastResult> blastsresults; 
		
		Map<String,String> allproteins = readAllProteins(allProteinsPath);
		
		try {
			
			List<Pair<String,String>> fastas = fmr.readFile(fileqithqueries);
	
			FastaWriter fw = new FastaWriter();
			
			for (Pair<String, String> fasta : fastas) {
				
				fw.writeFile(basepath + "tmpfasta",fasta.getSecond(),fasta.getFirst(),false);
				
				Process p = Runtime.getRuntime().exec("blastp -query tmpfasta -db corona_pred -outfmt 6", null, new File(basepath));
				
				BufferedReader pinput= new BufferedReader(new InputStreamReader(p.getInputStream()));
				
				String line = null;
				
				blastsresults = new ArrayList<BlastResult>();
				
				while ((line = pinput.readLine())!=null) {
					
					BlastResult res = new BlastResult();
					
					res.parse(line);
					
					if (res.getEvalue()<=0.0001) 
						blastsresults.add(res);
					
				}
				
				Map<String,BlastResult> map = new LinkedHashMap<String, BlastResult>();
				
				for (BlastResult result: blastsresults ) {

					String gbId = result.getSubject().split("\\|")[1];
					
					if (!map.containsKey(gbId)) map.put(gbId, result);
					
				}
				
				String orfn = fasta.getFirst().split("\\|")[2];
				
				String outfilepath = basepath+"CORONA_ref_"+orfn+".fas";
				
				FastaWriter wr = new FastaWriter();
				
				
				for (BlastResult result : map.values()) {
					
					String subject = result.getSubject();

					wr.writeFile(outfilepath, allproteins.get(subject), subject, true);
					
				}
				
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		

	}

	private static Map<String, String> readAllProteins(String filepath) {
		
		FastaMultipleReader fmr = new FastaMultipleReader();
		
		try {
			List<Pair<String, String>> proteins = fmr.readFile(filepath);
			Map<String,String> map = new HashMap<String, String>();
			
			for (Pair<String, String> pair : proteins) {
				
				map.put(pair.getFirst(), pair.getSecond());
				
			}
			
			return map;
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

}
