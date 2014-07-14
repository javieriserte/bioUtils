package utils.mutualinformation.misticmod;

import io.bufferreaders.UncommenterBufferedReader;
import io.onelinelister.OneLineListReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utils.mutualinformation.misticmod.datastructures.MI_Position;
import utils.mutualinformation.misticmod.datastructures.MI_PositionLineParser;
import cmdGA2.CommandLine;
import cmdGA2.NoArgumentOption;
import cmdGA2.OptionsFactory;
import cmdGA2.SingleArgumentOption;

/**
 * Computes Cumulative MI for each protein residue using the MI output from 
 * MISTIC or MN soft MI calculation.
 * 
 * @author javier
 *
 */
public class CmiCalculator {
	/////////////////////////////////
	// Private class Constants
	private static final double MISTIC_Z_SCORE_CUTOFF = 6.5;
	private static final double NEGATIVE_INFINITY = -900;

	public static void main(String[] args) {

		////////////////////////////////////////////////
		// Create Command Line object
		CommandLine cmd = new CommandLine();
		
		////////////////////////////////////////////////
		// Add Options to Command Line
		SingleArgumentOption<InputStream> inOpt = OptionsFactory.createBasicInputStreamArgument(cmd);
		
		SingleArgumentOption<PrintStream> outOpt = OptionsFactory.createBasicPrintStreamArgument(cmd);
		
		NoArgumentOption noCutOff = new NoArgumentOption(cmd, "--no_cut_off");

		/////////////////////////////////////////////////
		// Parse Command Line
		cmd.readAndExitOnError(args);
		
		/////////////////////////////////////////////////
		// Get Values From Command line
		BufferedReader in = new UncommenterBufferedReader(new InputStreamReader(inOpt.getValue()));
		
		PrintStream out = outOpt.getValue();
		
		double cutoff = noCutOff.isPresent()           ?
				CmiCalculator.NEGATIVE_INFINITY         :
				CmiCalculator.MISTIC_Z_SCORE_CUTOFF;
		
		//////////////////////////////////////////////////
		// Program
		MI_PositionLineParser lineParser = new MI_PositionLineParser();

		OneLineListReader<MI_Position> reader = new OneLineListReader<MI_Position>(lineParser);
		
		try {
			
			List<MI_Position> positions = reader.read(in);
			
			Map<Integer, Double> cmi_values = getCmiValues(positions, cutoff);
			
			List<Integer> order = generateOutputOrderList(cmi_values);
			
			printOut(out, cmi_values, order);
			
		} catch (IOException e) {
			System.err.println("There was an error: "+ e.getMessage());
			
			System.exit(1);
		}
		
	}

	////////////////////////////////
	// Private static methods
	/**
	 * Accumulate MI values for each position
	 * @param positions
	 * @param cutoff
	 * @return
	 */
	private static Map<Integer, Double> getCmiValues(List<MI_Position> positions, double cutoff) {
		Map<Integer, Double> cmi_values = new HashMap<>();
		
		for (MI_Position position : positions) {
			
			add_cmi_value(cmi_values, position.getPos1(), position.getMi(), cutoff); 
			
			add_cmi_value(cmi_values, position.getPos2(), position.getMi(), cutoff);
			
		}
		return cmi_values;
	}

	private static List<Integer> generateOutputOrderList(
			Map<Integer, Double> cmi_values) {
		List<Integer> order = new ArrayList<>();
		
		order.addAll(cmi_values.keySet());
		
		Collections.sort(order);
		return order;
	}

	private static void printOut(PrintStream out,
			Map<Integer, Double> cmi_values, List<Integer> order) {
		for (Integer integer : order) {
			
			out.println(integer + "\t" + (cmi_values.get(integer)));
			
		}
	}

	private static void add_cmi_value(Map<Integer, Double> cmi_values,
			Integer pos2, Double mi, double cutoff) {
		
		if (mi>cutoff) {
			
			double old_value = 0;
			
			if (cmi_values.containsKey(pos2)) {
			
				old_value = cmi_values.get(pos2);
				
			}
			
			cmi_values.put (pos2, old_value + mi);
			
		}
		
	}

}
