package utils.mutualinformation.misticmod;

import io.bufferreaders.UncommenterBufferedReader;
import io.onelinelister.OneLineListReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.List;

import pair.Pair;
import cmdGA2.CommandLine;
import cmdGA2.NoArgumentOption;
import cmdGA2.OptionsFactory;
import cmdGA2.SingleArgumentOption;
import cmdGA2.returnvalues.ReturnValueParser;

/**
 * This class reads an output Mutual Information 
 * data file and removes some residues pair data.
 * The pair is not actually removed from file, instead is 
 * set to -999.99 in all numeric fields.
 * Two ranges must be given. All pairs formed choosing one residue from one 
 * range and one residue from the other range are removed.
 * 
 * @author Javier Iserte.
 *
 */
public class RemoveMIRangeData {

	public static void main(String[] args) {
		////////////////////////////////////////
		// Creates command line object
		CommandLine cmd = new CommandLine();
		////////////////////////////////////////
		
		////////////////////////////////////////
		// Add options to the command line
		SingleArgumentOption<InputStream> inOpt = OptionsFactory.createBasicInputStreamArgument(cmd);
		SingleArgumentOption<PrintStream> outOpt = OptionsFactory.createBasicPrintStreamArgument(cmd);
		IntegerPairValue returnValueParser = new IntegerPairValue();
		SingleArgumentOption<Pair<Integer,Integer>> range1Opt = new SingleArgumentOption<Pair<Integer,Integer>>(cmd, "--r1", returnValueParser, null);
		SingleArgumentOption<Pair<Integer,Integer>> range2Opt = new SingleArgumentOption<Pair<Integer,Integer>>(cmd, "--r2", returnValueParser, null);
		NoArgumentOption helpOpt = new NoArgumentOption(cmd, "--help");
		////////////////////////////////////////

		///////////////////////////////////////
		// Parse Command line 
		cmd.readAndExitOnError(args);
		///////////////////////////////////////

		///////////////////////////////////////
		// Get values from command line
		BufferedReader in = new BufferedReader(new InputStreamReader(inOpt.getValue()));
		PrintStream out = outOpt.getValue();
		Pair<Integer, Integer> range1 = range1Opt.getValue();
		Pair<Integer, Integer> range2 = range2Opt.getValue();
		boolean help = helpOpt.isPresent();
		///////////////////////////////////////
		
		///////////////////////////////////////
		// Check for help
		if (help) {
			
			out.println("RemoveMIRangeData - Removes data from mi data file\n");
			out.println("This program reads an output Mutual Information");
			out.println("data file and removes some residues pair data.");
			out.println("The pair is not actually removed from file, instead is");
			out.println("set to -999.99 in all numeric fields.");
			out.println("Two ranges must be given. All pairs formed choosing one residue from one");
			out.println("range and one residue from the other range are removed.\n");
			
			out.println("Usage:");
			out.println("  RemoveMIRangeData.jar <Options>\n");
 			
			out.println("Options:");
			out.println("  --infile    Input MI data file. Std input if ignored.");
			out.println("  --outfile   Output MI data file.");
			out.println("  --r1        First range.");
			out.println("  --r2        Second range.");
			out.println("              Range format is: 'number1-number2' with number2 greater of equal to number1");
			out.println("  --help      Prints this help.");
			
		}
		///////////////////////////////////////
		
		///////////////////////////////////////
		// Validate
		if (range1 == null || range2 == null) {
			System.err.println("One or more ranges are incorrect.");
			System.err.println("Use --help option to print help");
			System.exit(1);
		}
		
		if ((range1.getFirst() == null || range1.getSecond()==null || range2.getFirst()==null || range2.getSecond()==null) || 
		   (range1.getFirst() > range1.getSecond() || range2.getFirst() > range2.getSecond())) {
			System.err.println("One or more ranges are incorrect.");
			System.err.println("Use --help option to print help");
			System.exit(1);
		}
		////////////////////////////////////////
		// Read all Mi data file
		List<MI_Position> miData = null; 
		try {
			miData = new OneLineListReader<MI_Position>(new MI_PositionLineParser()).read(new UncommenterBufferedReader(in));
		} catch (IOException e) {
			System.err.println("There was an error reading MI data file");
			System.exit(1);
		}
		/////////////////////////////////////////
		
		/////////////////////////////////////////
		// Iterate over each mi position element,
		// check if it is in the ranges and
		// prints result.
		MI_Position.activateMortemPrinter();
		for (MI_Position mi_Position : miData) {
			
			int p1 = mi_Position.getPos1();
			int p2 = mi_Position.getPos2();
			
			boolean p1IsInRange1 = (p1>=range1.getFirst() && p1 <= range1.getSecond()); 
			boolean p1IsInRange2 = (p1>=range2.getFirst() && p1 <= range2.getSecond());
			boolean p2IsInRange1 = (p2>=range1.getFirst() && p2 <= range1.getSecond());
			boolean p2IsInRange2 = (p2>=range2.getFirst() && p2 <= range2.getSecond());
			
			if  (p1IsInRange1 && p2IsInRange2 || p1IsInRange2 && p2IsInRange1) {
				
				mi_Position.setMi(-999.99);
				mi_Position.setRaw_mi(-999.99);
				
			}
				
			out.println(mi_Position);
		}
		/////////////////////////////////////////
		
		out.close();

	}
	
	public static class IntegerPairValue extends ReturnValueParser<Pair<Integer,Integer>> {

		@Override
		public Pair<Integer, Integer> parse(String token) {
			String[] components = token.split("-");
			
			if (components.length == 2 ) {
			
				Pair<Integer, Integer> pair = new Pair<Integer, Integer>(Integer.valueOf(components[0]), Integer.valueOf(components[1]));
				
				return pair;
			}
			
			return null;
		}
		
	}

}
