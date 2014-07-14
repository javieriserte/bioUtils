package utils.mutualinformation.misticmod;

import io.onelinelister.OneLineListReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import utils.mutualinformation.misticmod.datastructures.MI_PositionWithProtein;
import cmdGA.NoOption;
import cmdGA.Parser;
import cmdGA.SingleOption;
import cmdGA.exceptions.IncorrectParameterTypeException;
import cmdGA.parameterType.DoubleParameter;
import cmdGA.parameterType.InFileParameter;
import cmdGA.parameterType.InputStreamParameter;
import cmdGA.parameterType.IntegerParameter;
import cmdGA.parameterType.PrintStreamParameter;

public class ResidueToProteinsCMI {

	/**
	 * Calculates the CMI (Cumulative Mutual Information).
	 * For a given alingment that represents many concatenated proteins and an
	 * selected protein wherein, calculates de CMI of each residue of selected protein
	 * and all other proteins individually.  
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		////////////////////////////////
		// Create Parser
		Parser parser = new Parser();
		
		////////////////////////////////
		// Define Command Line Arguments
		SingleOption inMIfileOpt = new SingleOption(parser, System.in, "-infile", InputStreamParameter.getParameter());
		
		SingleOption outfileOpt = new SingleOption(parser, System.out, "-outfile", PrintStreamParameter.getParameter());
		
		SingleOption lengthsOpt = new SingleOption(parser, null, "-lengths", InFileParameter.getParameter());
		
		// Protein numbers starts with Zero
		SingleOption proteinOpt = new SingleOption(parser, null, "-protein", IntegerParameter.getParameter());
		
		SingleOption cutOffOpt = new SingleOption(parser, 6.5, "-cutoff", DoubleParameter.getParameter());
		
		NoOption noCutOff = new NoOption(parser, "-nc");
		
		try {
			
			////////////////////////////////
			// Parse Command Line
			parser.parseEx(args);
			
			////////////////////////////////
			// Retrieve command line options
			if (!proteinOpt.isPresent() || !lengthsOpt.isPresent()) {
				
				System.err.println("-protein and -lengths are mandatory");

				System.exit(1);
				
			}
			
			BufferedReader in = new BufferedReader(new InputStreamReader( (InputStream) inMIfileOpt.getValue() ));
			
			PrintStream out = (PrintStream) outfileOpt.getValue();
			
			File lengthsFile = (File) lengthsOpt.getValue();
			
			int proteinNumber = (Integer) proteinOpt.getValue();
			
			double cutoff = calculateCutOff(cutOffOpt, noCutOff);
			
			/////////////////////////////////
			// Read MI Values
			List<Integer> lengths = (OneLineListReader.createOneLineListReaderForInteger()).read(lengthsFile);

			List<MI_PositionWithProtein> MI_values = readMIValues(in, lengths);
			
			
			/////////////////////////////////
			// Calculate the CMI
			HashMap<Integer, Double[]> residuesCMI = calculateCMIperResidue(proteinNumber, cutoff, MI_values, lengths.size());
			
			/////////////////////////////////
			// Generate Report
			exportResults(out,residuesCMI);
			
		} catch (IncorrectParameterTypeException e) {
			
			System.err.println("There was a problem trying to parse the command line:" + e.getMessage());

			System.exit(1);
			
		}

	}

	////////////////////////////
	// Private Methods
	/**
	 * Sends the output to a given output stream
	 * 
	 * The format of the output is:
	 * <pre>
	 * ResidueNumber   CMI vs prot1    CMI vs prot2   CMI vs prot3  .....  CMI vs protN
	 * </pre>
	 * @param out
	 * @param residuesCMI
	 */
	private static void exportResults(PrintStream out, HashMap<Integer, Double[]> residuesCMI) {

		List<Integer> residueNumbers = new ArrayList<>();
		
		residueNumbers.addAll(residuesCMI.keySet());
		
		Collections.sort(residueNumbers);
		
		for (Integer integer : residueNumbers) {

			StringBuilder sb = new StringBuilder();
			
			sb.append(String.valueOf(integer));
			
			for (double d : residuesCMI.get(integer)){
				
				sb.append("\t");

				sb.append(d);
				
			}
			
			out.println(sb.toString());
			
		}
		
	}

	private static HashMap<Integer, Double[]> calculateCMIperResidue( int proteinNumber, double cutoff, List<MI_PositionWithProtein> MI_values, int numberOfProteins) {
		
		// This Map stores the calculated CMI for every residue.
		// The Integer element is the position number of the chosen protein
		// The Double[] element contains the CMI vs each other protein,
		//  one position of the array for each protein
		HashMap<Integer, Double[]> residuesCMI = new HashMap<>();
		
		// Iterates over each MI position
		for (MI_PositionWithProtein mi_PositionWithProtein : MI_values) {
			
			// MI positions have two residues, this checks if the current MI position contains the selected protein as its first protein
			boolean proteinNumberIsFirstProtein = proteinNumber == mi_PositionWithProtein.getProtein_1();

			// MI positions have two residues, this checks if the current MI position contains the selected protein as its second protein
			boolean proteinNumberIsSecondProtein = proteinNumber==mi_PositionWithProtein.getProtein_2();
			
			// If the current MI position contains the selected protein.
			if(proteinNumberIsFirstProtein || proteinNumberIsSecondProtein) {
				
				// Gets the resiude from the current MI position that belongs to the selected protein
				int currentResidueNumber = proteinNumberIsFirstProtein?mi_PositionWithProtein.getPos1():mi_PositionWithProtein.getPos2();
				
				// Gets the number of the other protein
				int otherProtein = proteinNumberIsFirstProtein?mi_PositionWithProtein.getProtein_2():mi_PositionWithProtein.getProtein_1();

				// Sums the MI
				if (residuesCMI.containsKey(currentResidueNumber)) {
					
					Double mi = mi_PositionWithProtein.getMi();
					
					if (mi > cutoff) {
					
						residuesCMI.get(currentResidueNumber)[otherProtein] = residuesCMI.get(currentResidueNumber)[otherProtein] + mi;
					
					}
					
				} else {
					
					Double[] emptyArray = new Double[numberOfProteins];
					
					Arrays.fill(emptyArray,0d);
					
					residuesCMI.put(currentResidueNumber, emptyArray);					
					
				}
				
			}
			
		}
		
		return residuesCMI;
	}

	private static double calculateCutOff(SingleOption cutOffOpt, NoOption noCutOff) {
		
		double cutoff;
		
		if (noCutOff.isPresent() && cutOffOpt.isPresent()) {
			
			System.err.println("-nc and -cutoff options must not be present at the same time");
			
			System.exit(1);
			
		} 
		
		if (noCutOff.isPresent()) {
			
			cutoff = -900; 
			
		} else {
			
			cutoff = (Double) cutOffOpt.getValue();
			
		}
		
		return cutoff;
	}

	private static List<MI_PositionWithProtein> readMIValues(BufferedReader in, List<Integer> lengths) {
		
		try {

			List<String> position_strings = (OneLineListReader.createOneLineListReaderForString()).read(in);
			
			List<MI_PositionWithProtein> positions = new ArrayList<>();
			
			while(!position_strings.isEmpty()) {
				
				MI_PositionWithProtein currentPos = MI_PositionWithProtein.valueOf(position_strings.remove(position_strings.size()-1));
				
				currentPos.assignProteinNumber(lengths);
				
				positions.add(currentPos);
				
			}
			
			Collections.reverse(positions);
			
			return positions;
			
		} catch (IOException e) {
			
			System.err.println("There was an error while reading MI values: "+e.getMessage());
			
			System.exit(1);
			
		}
		
		return null;
		
	}

}
