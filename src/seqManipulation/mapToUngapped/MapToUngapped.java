package seqManipulation.mapToUngapped;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cmdGA.Parser;
import cmdGA.SingleOption;
import cmdGA.exceptions.IncorrectParameterTypeException;
import cmdGA.parameterType.InputStreamParameter;
import cmdGA.parameterType.PrintStreamParameter;
import fileformats.fastaIO.FastaMultipleReader;
import fileformats.fastaIO.Pair;

public class MapToUngapped {

	public static void main(String[] args) {
		
		Parser parser = new Parser();
		
		SingleOption inOpt = new SingleOption(parser, System.in, "-infile", InputStreamParameter.getParameter());
		
		SingleOption outopt = new SingleOption(parser, System.out, "-outfile", PrintStreamParameter.getParameter());
		
		parseCommandLine(args, parser);
		
		BufferedReader in = new BufferedReader(new InputStreamReader((InputStream) inOpt.getValue()));
		
		PrintStream out = (PrintStream) outopt.getValue();
		
		String gapped = readData(in);
		
		Map<Integer, Integer> map = compare(gapped);
		
		printout(map,out);
		
		
	}

	private static void printout(Map<Integer, Integer> map, PrintStream out) {

		Set<Integer> set = map.keySet();
		
		for (Integer integer : set) {

			out.println((integer +1) + "\t" + (map.get(integer)+1));
			
		}
		
	}

	private static Map<Integer, Integer> compare(String gapped) {

		Map<Integer,Integer> map  = new LinkedHashMap<>();

		int ungappedCounter = 0;
		
		for (int gapped_counter = 0; gapped_counter<gapped.length(); gapped_counter++) {
			
			char current = gapped.charAt(gapped_counter);
			
			if (current!='-') {
				
				map.put(gapped_counter, ungappedCounter);
				
				ungappedCounter++;
				
			}
			
		}
		
		return map;
		
	}

	public static void parseCommandLine(String[] args, Parser parser) {
		try {
			
			parser.parseEx(args);
			
		} catch (IncorrectParameterTypeException e) {
			
			System.err.println("There was an error trying to parse the command line" + e.getMessage());
			
		}
	}

	private static String readData(BufferedReader in) {

		FastaMultipleReader fmr = new FastaMultipleReader();
		
		List<Pair<String, String>> pairs = fmr.readBuffer(in);
		
		if (pairs.size()>0) {
			
			return pairs.get(0).getSecond();
			
		}
		
		return null;
		
	}
	
}
