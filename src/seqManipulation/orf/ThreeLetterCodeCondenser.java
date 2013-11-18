package seqManipulation.orf;

import io.onelinelister.OneLineListReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cmdGA2.CommandLine;
import cmdGA2.OptionsFactory;
import cmdGA2.SingleArgumentOption;

/**
 * Converts list a amino acid represented as a list of three letter codes into a 
 * protein sequence.
 * <pre>
 * Example.
 * Input data:
 * MET
 * TYR
 * ALA
 * -
 * CYS
 * 
 * Output Data
 * MYA-C
 * </pre>
 * 
 * @author Javier Iserte
 *
 */
public class ThreeLetterCodeCondenser {

	private static Map<String, Character> ThreeLetterNameToCharMap = createThreeLetterNameToCharMap(); 
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		//////////////////////////////////
		// Create Command Line
		CommandLine cmd = new CommandLine();
		//////////////////////////////////
		
		///////////////////////////////////
		// Add Options to Command Line
		SingleArgumentOption<InputStream> inOpt = OptionsFactory.createBasicInputStreamArgument(cmd);
		SingleArgumentOption<PrintStream> outOpt = OptionsFactory.createBasicPrintStreamArgument(cmd);
		///////////////////////////////////
		
		///////////////////////////////////
		// Parse Command Line
		cmd.readAndExitOnError(args);
		///////////////////////////////////
		
		///////////////////////////////////
		// Get Command Line Values
		BufferedReader in = new BufferedReader(new InputStreamReader(inOpt.getValue()));
		PrintStream out = outOpt.getValue();
		///////////////////////////////////
		
		try {
			///////////////////////////////////
			// Read Data
			List<String> threeLetterList = (OneLineListReader.createOneLineListReaderForString()).read(in);
			///////////////////////////////////
			
			///////////////////////////////////
			// Create result string
			StringBuilder protein = new StringBuilder(threeLetterList.size());
			
			for (String string : threeLetterList) {
				
				string = string.trim();

				if (ThreeLetterCodeCondenser.ThreeLetterNameToCharMap.containsKey(string)) {
				
					char currentChar = ThreeLetterCodeCondenser.ThreeLetterNameToCharMap.get(string.toUpperCase());
					
					protein.append(currentChar);
				
				} else if (string.equals("-") || string.equals("---") || string.equals("")) {
				
					protein.append('-');
				
				} else {
					
					protein.append('X');
					
				}
				
			}
			///////////////////////////////////
			
			///////////////////////////////////
			// Print out the result
			out.println(protein);
			///////////////////////////////////
			
		} catch (IOException e) {
			
			System.err.println("There was an error: "+ e.getMessage());
			
			System.exit(1);
			
		}

	}

	//////////////////////////////////////
	// Private Methods
	private static Map<String, Character> createThreeLetterNameToCharMap() {
		Map<String,Character> resultMap = new HashMap<>();
		
		resultMap.put("GLN", 'Q');
		resultMap.put("TRP", 'W');
		resultMap.put("GLU", 'E');
		resultMap.put("ARG", 'R');
		resultMap.put("THR", 'T');
		resultMap.put("TYR", 'Y');
		resultMap.put("ILE", 'I');
		resultMap.put("PRO", 'P');
		resultMap.put("ALA", 'A');
		resultMap.put("SER", 'S');
		resultMap.put("ASP", 'D');
		resultMap.put("PHE", 'F');
		resultMap.put("GLY", 'G');
		resultMap.put("HIS", 'H');
		resultMap.put("LYS", 'K');
		resultMap.put("LEU", 'L');
		resultMap.put("CYS", 'C');
		resultMap.put("VAL", 'V');
		resultMap.put("ASN", 'N');
		resultMap.put("MET", 'M');

		return resultMap;
	
	}	

}
