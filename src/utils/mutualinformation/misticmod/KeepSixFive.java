package utils.mutualinformation.misticmod;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

import cmdGA.Parser;
import cmdGA.SingleOption;
import cmdGA.exceptions.IncorrectParameterTypeException;
import cmdGA.parameterType.DoubleParameter;
import cmdGA.parameterType.InputStreamParameter;
import cmdGA.parameterType.PrintStreamParameter;

public class KeepSixFive {
	
	public static final double DEFAULT_CUT_OFF = 6.5; 
	
	private double cut_off;


	public KeepSixFive(double cut_off) {
		super();
		this.cut_off = cut_off;
	}

	public MI_Position filter(MI_Position pos) {


		if (pos.mi < this.cut_off) {
		
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
		
		SingleOption ctOpt = new SingleOption(parser, DEFAULT_CUT_OFF, "-c", DoubleParameter.getParameter());
		
		try {
			
			parser.parseEx(args);
			
		} catch (IncorrectParameterTypeException e) {
			
			System.err.println("There was an parsing command line:" + e.getMessage());
			
			System.exit(1);
			
		}

		BufferedReader in = new BufferedReader(new InputStreamReader((InputStream) inOpt.getValue()));
		
		PrintStream out = (PrintStream) outopt.getValue();
		
		double ct = (Double) ctOpt.getValue();
		
		KeepSixFive ksf = new KeepSixFive(ct);
		
		String currentline =null;
		
		while ((currentline = in.readLine())!=null) {
			
			MI_Position pos = MI_Position.valueOf(currentline);
			
			pos = ksf.filter(pos);
			
			out.println(pos);
			
		}
		
	}

}
