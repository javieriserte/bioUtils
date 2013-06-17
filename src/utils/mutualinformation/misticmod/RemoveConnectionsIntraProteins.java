package utils.mutualinformation.misticmod;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.StringReader;

import removecomments.RemoveComments;
import cmdGA.MultipleOption;
import cmdGA.Parser;
import cmdGA.SingleOption;
import cmdGA.exceptions.IncorrectParameterTypeException;
import cmdGA.parameterType.InputStreamParameter;
import cmdGA.parameterType.IntegerParameter;
import cmdGA.parameterType.PrintStreamParameter;

public class RemoveConnectionsIntraProteins {

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

		String nocomments = (new RemoveComments()).uncomment((InputStream) inOpt.getValue());
		
		BufferedReader in = new BufferedReader(new StringReader(nocomments));
		
		String currentLine = null;
		
		int[] lengths = getProtLengths(prot_len_opt);
		
		try {
			while((currentLine = in.readLine())!= null) {
				
				String data[] = currentLine.split("\t");
				
				int p_1 = getProteinFromPosition(Integer.valueOf(data[0]),lengths);

				int p_2 = getProteinFromPosition(Integer.valueOf(data[2]),lengths);
				
				if (p_1!=p_2) {
					
					out.println(currentLine + "\t");
					
				} else {
					
					out.println(data[0]+"\t"+data[1]+"\t"+data[2]+"\t"+data[3]+"\t"+"-999");
					
				}
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		

	}


	public static int[] getProtLengths(MultipleOption prot_len_opt) {
		int[] lengths = new int[prot_len_opt.count()];

		int c = 0;
		
		for (Object o : prot_len_opt.getValues()) {
			
			lengths[c++] = (Integer) o;
			
		}
		return lengths;
	}
	
	
	public static int getProteinFromPosition(int position, int[] lengths ) {
		
		for (int i = 0; i<lengths.length; i++) {
			
			position = position - lengths[i];
			
			if (position<=0) return i+1;
			
		}
		
		return 0;
		
	}

}
