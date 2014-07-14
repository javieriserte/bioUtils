package utils.mutualinformation.misticmod;

import io.bufferreaders.UncommenterBufferedReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import utils.mutualinformation.misticmod.datastructures.MI_PositionWithProtein;
import cmdGA.MultipleOption;
import cmdGA.NoOption;
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
		
		/////////////////////////////
		// Create Command Line Parser
		Parser parser = new Parser();
		
		/////////////////////////////
		// Define Commanda Line Arguments
		SingleOption inOpt = new SingleOption(parser, System.in, "-infile", InputStreamParameter.getParameter());
		
		MultipleOption prot_len_opt = new MultipleOption(parser, null, "-lengths", ',',IntegerParameter.getParameter());
		
		SingleOption outopt = new SingleOption(parser, System.out, "-outfile", PrintStreamParameter.getParameter());
		
		NoOption helpOpt = new NoOption(parser, "-help", "-h");
		
		try {
			
			/////////////////////////
			// Parse Command Line
			parser.parseEx(args);
			
			///////////////////////////////
			// Read Command Line Arguments
			PrintStream out = (PrintStream) outopt.getValue();
			
			if (helpOpt.isPresent()) {
				
				printHelp(out);
				
				System.exit(1);
				
			}
			
			///////////////////////////////
			// Validate Command Line Arguments
			if (!prot_len_opt.isPresent()) {

				System.err.println("-lengths option is mandatory.");
				
				printHelp(out);
				
				System.exit(1);
				
			}
			
			BufferedReader in = new UncommenterBufferedReader(new InputStreamReader((InputStream) inOpt.getValue()));
			
			 List<Integer> lengths = getProtLengths(prot_len_opt);
			
			///////////////////////////////////////
			// Remove Connections
			removeConnections(out, in, lengths);
			
		} catch (IncorrectParameterTypeException | NumberFormatException | IOException e) {
			
			System.err.println("There was an error:" + e.getMessage());
			
		} 

	}


	public static void removeConnections(PrintStream out, BufferedReader in, 	List<Integer> lengths) throws IOException {
		
		String currentLine = null;
		
		while((currentLine = in.readLine())!= null) {
			
			MI_PositionWithProtein currentPosition = MI_PositionWithProtein.valueOf(currentLine);
			
			currentPosition.assignProteinNumber(lengths);
			
//			String data[] = currentLine.split("\t");
			
//			int p_1 = getProteinFromPosition(Integer.valueOf(data[0]),lengths);

//			int p_2 = getProteinFromPosition(Integer.valueOf(data[2]),lengths);
			
			int p_1 = currentPosition.getProtein_1();

			int p_2 = currentPosition.getProtein_2();
			
			if (p_1==p_2) {
				
				currentPosition.setMi(-999.99);
				
			}
			
			out.println(currentPosition);
			
		}
	}


	private static void printHelp(PrintStream out) {
		
		out.println("Remove Intraproteins Connections");
		
		out.println("  Usage: ProgramName.jar -infile file -outfile file -lenghts x,y,z -help");
		
	}


	public static List<Integer> getProtLengths(MultipleOption prot_len_opt) {
		
		List<Integer> lengths = new ArrayList<>();

		for (Object o : prot_len_opt.getValues()) {
			
			lengths.add((Integer) o);
			
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
