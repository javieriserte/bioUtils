package utils.mutualinformation.misticmod;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

import cmdGA.Parser;
import cmdGA.SingleOption;
import cmdGA.exceptions.IncorrectParameterTypeException;
import cmdGA.parameterType.InputStreamParameter;
import cmdGA.parameterType.PrintStreamParameter;

public class KeepSixFive {

	public MI_Position filter(MI_Position pos) {


		if (pos.mi < 6.5) {
		
			pos.mi = -999.99d;

		}
		
		return pos;

	}

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		Parser parser = new Parser();
		
		SingleOption inOpt = new SingleOption(parser, System.in, "-infile", InputStreamParameter.getParameter());
		
		SingleOption outopt = new SingleOption(parser, System.out, "-outfile", PrintStreamParameter.getParameter());
		
		try {
			
			parser.parseEx(args);
			
		} catch (IncorrectParameterTypeException e) {
			
			System.err.println("There was an parsing command line:" + e.getMessage());
			
			System.exit(1);
			
		}

		BufferedReader in = new BufferedReader(new InputStreamReader((InputStream) inOpt.getValue()));
		
		PrintStream out = (PrintStream) outopt.getValue();

		KeepSixFive ksf = new KeepSixFive();
		
		String currentline =null;
		
		while ((currentline = in.readLine())!=null) {
			
			MI_Position pos = MI_Position.valueOf(currentline);
			
			pos = ksf.filter(pos);
			
			out.println(pos);
			
		}
		
	}

}
