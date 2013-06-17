package utils.mutualinformation.misticmod;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import cmdGA.MultipleOption;
import cmdGA.Parser;
import cmdGA.SingleOption;
import cmdGA.exceptions.IncorrectParameterTypeException;
import cmdGA.parameterType.InputStreamParameter;
import cmdGA.parameterType.IntegerParameter;
import cmdGA.parameterType.PrintStreamParameter;

public class CountStrippedPosInProtein {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Parser parser = new Parser();
		
		SingleOption inOpt = new SingleOption(parser, System.in, "-infile", InputStreamParameter.getParameter());
		
		MultipleOption prot_len_opt = new MultipleOption(parser, null, "-lengths", ',',IntegerParameter.getParameter());
		
		SingleOption outopt = new SingleOption(parser, System.out, "-outfile", PrintStreamParameter.getParameter());
		
		
		try {
			
			parser.parseEx(args);
			
		} catch (IncorrectParameterTypeException e) {
			
			e.printStackTrace();
			
		}
	
		PrintStream out = (PrintStream) outopt.getValue();

		BufferedReader in = new BufferedReader(new InputStreamReader((InputStream)inOpt.getValue()));
		
		int[] lengths = getProtLengths(prot_len_opt);
		
		Integer[] strippedPos = getStrippedPos(in);
		
		int[] counts = countStrippedPosInProtein(lengths, strippedPos);
		
		export(out, lengths, counts);
		
		
	}


	public static void export(PrintStream out, int[] lengths, int[] counts) {
		for (int i=0; i<lengths.length; i++) {
			
			out.println(lengths[i] - counts[i]);
			
		}
	}


	public static int[] countStrippedPosInProtein(int[] lengths,
			Integer[] strippedPos) {
		int[] counts = new int[lengths.length];
		
		int nextStop = lengths[0];
		
		int currentMark = 0;
		
		for(int pos : strippedPos) {
			
			if (pos >= nextStop) {
				
				currentMark++;
				
				nextStop = nextStop + lengths[currentMark];
				
			} 
			
			counts[currentMark] = counts[currentMark] + 1; 
			
		}
		return counts;
	}
	
	
	private static Integer[] getStrippedPos(BufferedReader in) {

		
		try {

			String currentline = null;

			List<Integer> result = new ArrayList<Integer>(); 
			
			while((currentline=in.readLine())!=null) {
				
				if (!currentline.trim().equals("")) {
					
					Integer value = Integer.valueOf(currentline.trim());
					
					result.add(value);
					
				}
				
			}
			
			Integer[] resArray = new Integer[result.size()]; 
			
			return result.toArray(resArray);

			
		} catch (IOException e) {
			
			e.printStackTrace();
			
		} 

		return null;
			
	}


	public static int[] getProtLengths(MultipleOption prot_len_opt) {
		
		int[] lengths = new int[prot_len_opt.count()];

		int c = 0;
		
		for (Object o : prot_len_opt.getValues()) {
			
			lengths[c++] = (Integer) o;
			
		}
		return lengths;
	}

}
