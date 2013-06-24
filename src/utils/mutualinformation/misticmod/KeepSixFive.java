package utils.mutualinformation.misticmod;

import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import cmdGA.Parser;
import cmdGA.SingleOption;
import cmdGA.exceptions.IncorrectParameterTypeException;
import cmdGA.parameterType.InputStreamParameter;
import cmdGA.parameterType.PrintStreamParameter;

public class KeepSixFive extends FilterMIData {

	@Override
	public void filter(PrintStream out, List<Double> values, List<MI_Position> MI_Data_Lines) {

		for (MI_Position pos : MI_Data_Lines) {
			
			if (pos.mi < 6.5) {
	
				pos.mi = -999.99d;
				
			}
			
			out.println(pos);
		}

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
			
			e.printStackTrace();
			
		}

		
		KeepSixFive ksf = new KeepSixFive();
		
		FilterMIData.filter = ksf;
		
		filter.inOpt = inOpt;
		
		filter.outopt = outopt;
		
		ksf.main();
		

	}

}
