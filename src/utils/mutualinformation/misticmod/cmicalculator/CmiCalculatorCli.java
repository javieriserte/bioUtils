package utils.mutualinformation.misticmod.cmicalculator;

import io.bufferreaders.UncommenterBufferedReader;
import io.onelinelister.OneLineListReader;
import io.resources.ResourceContentAsString;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import utils.mutualinformation.misticmod.datastructures.MI_Position;
import utils.mutualinformation.misticmod.datastructures.MI_PositionLineParser;
import utils.mutualinformation.misticmod.top.CutOffFilter;
import utils.mutualinformation.misticmod.top.MIFilterReturnValue;
import utils.mutualinformation.misticmod.top.MiFilter;
import cmdGA2.CommandLine;
import cmdGA2.NoArgumentOption;
import cmdGA2.OptionsFactory;
import cmdGA2.SingleArgumentOption;

public class CmiCalculatorCli {

	public static void main(String[] args) {

		////////////////////////////////////////////////////////////////////////
		// Create Command Line object
		CommandLine cmd = new CommandLine();
		////////////////////////////////////////////////////////////////////////
		
		////////////////////////////////////////////////////////////////////////
		// Add Options to Command Line
		SingleArgumentOption<InputStream> inOpt = OptionsFactory.createBasicInputStreamArgument(cmd);
		
		SingleArgumentOption<PrintStream> outOpt = OptionsFactory.createBasicPrintStreamArgument(cmd);
		
		SingleArgumentOption<MiFilter> filterOpt = new SingleArgumentOption<MiFilter>(cmd, "--filter", new MIFilterReturnValue(), new CutOffFilter(CmiCalculator.MISTIC_Z_SCORE_CUTOFF));
		
		NoArgumentOption helpOpt = new NoArgumentOption(cmd, "--help");  
		////////////////////////////////////////////////////////////////////////

		////////////////////////////////////////////////////////////////////////
		// Parse Command Line
		cmd.readAndExitOnError(args);
		////////////////////////////////////////////////////////////////////////

		////////////////////////////////////////////////////////////////////////
		// Check For Help
		if (helpOpt.isPresent()) {
			String helpText = new ResourceContentAsString().readContents("help", CmiCalculatorCli.class);
			System.err.println(helpText);
			System.exit(0);
		}
		////////////////////////////////////////////////////////////////////////

		
		////////////////////////////////////////////////////////////////////////
		// Get Values From Command line
		BufferedReader in = new UncommenterBufferedReader(new InputStreamReader(inOpt.getValue()));
		
		PrintStream out = outOpt.getValue();
		
		MiFilter filter = filterOpt.getValue();
		////////////////////////////////////////////////////////////////////////
		
		////////////////////////////////////////////////////////////////////////
		// Program
		MI_PositionLineParser lineParser = new MI_PositionLineParser();

		OneLineListReader<MI_Position> reader = new OneLineListReader<MI_Position>(lineParser);
		
		CmiCalculator cmiCalc = new CmiCalculator();
		
		try {
			
			List<MI_Position> positions = reader.read(in);
			
			Map<Integer, Double> cmi_values = cmiCalc.getCmiValues(positions, filter);
			
			List<Integer> order = CmiCalculatorCli.generateOutputOrderList(cmi_values);
			
			CmiCalculatorCli.printOut(out, cmi_values, order);
			
		} catch (IOException e) {
			
			System.err.println("There was an error: "+ e.getMessage());
			
			System.exit(1);
		}
		
	}
	
	private static void printOut(PrintStream out,
			Map<Integer, Double> cmi_values, List<Integer> order) {
		for (Integer integer : order) {
			
			out.println(integer + "\t" + (cmi_values.get(integer)));
			
		}
	}
	
	private static List<Integer> generateOutputOrderList(
			Map<Integer, Double> cmi_values) {
		List<Integer> order = new ArrayList<>();
		
		order.addAll(cmi_values.keySet());
		
		Collections.sort(order);
		return order;
	}


}
