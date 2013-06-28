package blastSearch;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;


import cmdGA.Parser;
import cmdGA.SingleOption;
import cmdGA.exceptions.IncorrectParameterTypeException;
import cmdGA.parameterType.InFileParameter;
import cmdGA.parameterType.OutFileParameter;
import cmdGA.parameterType.PrintStreamParameter;
import cmdGA.parameterType.StringParameter;
import fileformats.fastaIO.FastaMultipleReader;
import fileformats.fastaIO.FastaWriter;
import fileformats.fastaIO.Pair;

public class MapGenomeToProteinsDB{
	
	public static void main(String[] args) throws IOException {
		
		Parser parser = new Parser();
		
		SingleOption outOpt = new SingleOption(parser, System.out, "-outfile", PrintStreamParameter.getParameter());
		
		SingleOption genomeQuery = new SingleOption(parser, null, "-genome", InFileParameter.getParameter());
		
		SingleOption dbProtName = new SingleOption(parser, null, "-db", StringParameter.getParameter());
		
		SingleOption alignedProt = new SingleOption(parser, null, "-aligned", InFileParameter.getParameter());
		
		SingleOption idFileopt = new SingleOption(parser, null, "-idfile", OutFileParameter.getParameter());
	
		try {
			parser.parseEx(args);
		} catch (IncorrectParameterTypeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		///////////////////////////////////////
		// Read Options
		
		File aligned = (File) alignedProt.getValue();
		
		PrintStream out = (PrintStream) outOpt.getValue();

		PrintStream out_id = new PrintStream( (File) idFileopt.getValue());

		File value = (File) genomeQuery.getValue();
		
		String db = (String) dbProtName.getValue();

		MapGenomeToProteinsDB m = new MapGenomeToProteinsDB();

		////////////////////////////////////////
		// Create Fasta Reader and Writer Objects 
		
		FastaMultipleReader fmr = new FastaMultipleReader();
		
		FastaWriter fw = new FastaWriter();
		
		///////////////////////////////////////
		// Load Genomes sequences 
		// Load Aligned Proteins
		
		List<Pair<String, String>> seqs = fmr.readFile(value);
		
		Map<String, Pair<String,String>> alignedProteins = m.readAlignedProteins(aligned);
		
		int length = alignedProteins.values().iterator().next().getSecond().length();

		
		
		////////////////////////////////////////
		// Read Working directory
		File wd = new File(System.getProperty("user.dir"));
		
		////////////////////////////////////////
		// Itearate over each genome 
		// and perform blastx search
		for (Pair<String, String> pair : seqs) {
			
			fw.writeFile("tmpfasta",pair.getSecond(),pair.getFirst(),false);
			
			List<BlastResult> r = m.performBlastCommand("blastx", "tmpfasta", db, "", wd);
			
			/////////////////////////////////////
			// Print Id and sequences to 
			// output files
			
			if (r.size()>0) {
				
				BlastResult best = r.get(0); // Best
				
				String key = best.getSubject();
				
				String seq = alignedProteins.get(key).getSecond();
				
				export(out, out_id, pair, best.getIdPercent(), pair.getFirst() ,seq);
					
			} else {
					
				String s = getGapRegion(length);

				export(out, out_id, pair, 0f, pair.getFirst() ,s);
					
			}
			
		}
		
		closePrintStreams(out, out_id);
		
	}

	public static void closePrintStreams(PrintStream out, PrintStream out_id) {
		out_id.flush();
		
		out_id.close();
		
		out.flush();
		
		out.close();
	}

	public static String getGapRegion(int length) {
		
		StringBuilder s = new StringBuilder();
		
		int l = length;
			
		while(l-->0) {
				
			s.append("-");
				
		}
		return s.toString();
	}

	public static void export(PrintStream out, PrintStream out_id, Pair<String, String> pair, double idPercent, String desc, String seq) {
		
		out_id.println(desc + "\t" + idPercent);
		
		out.println(">"+desc);
			
		out.println(seq);
	}
	
	private Map<String, Pair<String, String>> readAlignedProteins(File aligned) {
		
		Map<String, Pair<String, String>> result = new HashMap<>();
		
		FastaMultipleReader fmr = new FastaMultipleReader();
		
		try {
			
			List<Pair<String, String>> seq = fmr.readFile(aligned);
			
			for (Pair<String, String> pair : seq) {
				
				result.put(pair.getFirst(), pair);
				
			}
			
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
			
		}
		
		return result;
		
	}

	public List<BlastResult> performBlastCommand(String blastProgram, String querypath, String dbname, String extraParameters, File basePath) {
		
		Runtime run = Runtime.getRuntime();
		
		Map<String, String> env = System.getenv();
		
		String[] en = new String[1];
		
		en[0] = "PATH=" + env.get("PATH");
				
		Process proccess = null;
		
		List<BlastResult> results = new ArrayList<>();
		
		try {
			
			String commandline = blastProgram + " -query "+ querypath + " -db " + dbname + " " + extraParameters+" -outfmt 6";
			
			
			proccess = run.exec(commandline, en,  basePath);
			
			BufferedReader p_out = new BufferedReader(new InputStreamReader(proccess.getInputStream()));
			
			String currentline = null;
			
			while((currentline = p_out.readLine())!=null) {

				BlastResult r = new BlastResult();
				
				r.parse(currentline);
						
				results.add(r);
				
			}
			
			
		} catch (IOException e) {
			
			System.err.println("there was an error executing blast:");
			
			System.err.println(e.getMessage());
			
		}
		
		return results;
		
	}

}
