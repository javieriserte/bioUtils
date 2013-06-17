package utils.mutualinformation.misticmod;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

import cmdGA.MultipleOption;
import cmdGA.Parser;
import cmdGA.SingleOption;
import cmdGA.exceptions.IncorrectParameterTypeException;
import cmdGA.parameterType.InputStreamParameter;
import cmdGA.parameterType.IntegerParameter;
import cmdGA.parameterType.PrintStreamParameter;

public class KeepTopMIData {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Parser parser = new Parser();
		
		SingleOption inOpt = new SingleOption(parser, System.in, "-infile", InputStreamParameter.getParameter());
		
		SingleOption outopt = new SingleOption(parser, System.out, "-outfile", PrintStreamParameter.getParameter());
		
		SingleOption topPercentOpt = new SingleOption(parser, null, "-toppercent", IntegerParameter.getParameter());
		
		try {
			
			parser.parseEx(args);
			
		} catch (IncorrectParameterTypeException e) {
			
			e.printStackTrace();
			
		}
	
		PrintStream out = (PrintStream) outopt.getValue();

		BufferedReader in = new BufferedReader(new InputStreamReader((InputStream)inOpt.getValue()));

		
		
	}
	

}
