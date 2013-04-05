package blastSearch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cmdGA.NoOption;
import cmdGA.Option;
import cmdGA.Parser;
import cmdGA.SingleOption;
import cmdGA.exceptions.IncorrectParameterTypeException;
import cmdGA.parameterType.InputStreamParameter;
import cmdGA.parameterType.IntegerParameter;
import cmdGA.parameterType.PrintStreamParameter;


public class BlastResultManipulator {

	private static final String VERSION = "0.0.1";

	public static void main(String[] args) {
		
		List<Option> uniques = new ArrayList<Option>();
		
		// Step One : Create the parser
		
		Parser parser = new Parser();
		
		// Step Two : Add Command Line Options
		
		SingleOption inputStreamOpt = new SingleOption(parser, System.in, "-infile", InputStreamParameter.getParameter());
		
		SingleOption printStreamOpt = new SingleOption(parser, System.out, "-outfile", PrintStreamParameter.getParameter());
		
		SingleOption bestOpt = new SingleOption(parser, 1, "-best", IntegerParameter.getParameter());
		uniques.add(bestOpt);
		
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
			out.print(BlastResultManipulator.Help());
			System.exit(0);
		} 
		if (parametersUsed>1) {
			System.err.println("Only one option is expected");
			System.exit(0);			
		}
		
		if (helpOpt.isPresent()) {
			out.print(BlastResultManipulator.Help());
			System.exit(0);
		}
		if (versionOpt.isPresent()) {
			out.print(BlastResultManipulator.VERSION);
			System.exit(0);
		}
		
		
		List<BlastResult> results = null;
		
		results = BlastResultManipulator.readBuffer(in);
		
		if (bestOpt.isPresent()) {
			
			bestCommand(results,out,(Integer)bestOpt.getValue());
			
		}
		
		
	}
	
	/**
	 * Retrieves the n best result from a blast search.
	 * The criteria for "best" if lower e-value.
	 * 
	 * @param results
	 * @param out
	 */
	private static void bestCommand(List<BlastResult> results, PrintStream out, int nBest) {
		
		Map<String,List<BlastResult>> map = new HashMap<String,List<BlastResult>>();
		
		List<String> queryOrder = new ArrayList<String>();
		
		for (BlastResult current : results) {
			
			String query = current.getQuery();
			
			if (map.containsKey(query)) {
				
				List<BlastResult> list = map.get(query);
				
				int counter =0;
				
				while(counter<list.size() && 
						current.getEvalue() > list.get(counter).getEvalue()
						) counter++;
				
				list.add(counter, current);

				if (list.size()>nBest) {
					
					list.remove(list.size()-1);
					
				}
				
			} else {
				
				queryOrder.add(current.getQuery());
				
				List<BlastResult> newlist = new ArrayList<BlastResult>();
				
				newlist.add(current);
				
				map.put(query, newlist);
			
			}
			
		}
		
		for (String query : queryOrder) {
			
			for (BlastResult br : map.get(query)) {
				out.println(br.toString());					
			}

		}
		
	}
	
	private static List<BlastResult> readBuffer(BufferedReader in) {
		
		String line;
		
		List<BlastResult> result = new ArrayList<BlastResult>();
		
		try {
			
			while(( line = in.readLine())!=null) {
				
				BlastResult br = new BlastResult();
				br.parse(line);
				result.add(br);
				
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return result;
		
	}

	public static String Help() {
		return "Blast Result Manipulator - Version " + BlastResultManipulator.VERSION + 
	       "\nOptions: -infile        : is the input fasta file (or stdin if no present)" +
	       "\n         -outfile       : is the path to the output file (or stdout if no present)" +
	       "\n         -best          : extracts extracts the n best results for each query."+
	       "\n                           Example:  -best 1 extracts the results with the minimum evalue."+	       
		   "\n         -ver           : prints the number of the version in stdout."+
		   "\n         -help          : shows this help.";
		
	}
	
}
