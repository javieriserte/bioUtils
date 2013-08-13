package utils.mutualinformation.misticmod;

import io.bufferreaders.UncommenterBufferedReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

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
			
			BufferedReader in = new UncommenterBufferedReader(new InputStreamReader((InputStream) inOpt.getValue()));
			
			int[] lengths = getProtLengths(prot_len_opt);
			
			///////////////////////////////////////
			// Remove Connections
			removeConnections(out, in, lengths);
			
		} catch (IncorrectParameterTypeException | NumberFormatException | IOException e) {
			
			System.err.println("There was an error:" + e.getMessage());
			
		} 

	}


	public static void removeConnections(PrintStream out, BufferedReader in, 	int[] lengths) throws IOException {
		
		String currentLine = null;
		
		while((currentLine = in.readLine())!= null) {
			
			String data[] = currentLine.split("\t");
			
			int p_1 = getProteinFromPosition(Integer.valueOf(data[0]),lengths);

			int p_2 = getProteinFromPosition(Integer.valueOf(data[2]),lengths);
			
			if (p_1!=p_2) {
				
				out.println(currentLine);
				
			} else {
				
				out.println(data[0]+"\t"+data[1]+"\t"+data[2]+"\t"+data[3]+"\t"+"-999.99");
				
			}
			
		}
	}


	private static void printHelp(PrintStream out) {
		
		out.println("Remove Intraproteins Connections");
		
		out.println("  Usage: ProgramName.jar -infile file -outfile file -lenghts x,y,z -help");
		
	}


	public static int[] getProtLengths(MultipleOption prot_len_opt) {
		int[] lengths = new int[prot_len_opt.count()];

		int c = 0;
		
		for (Object o : prot_len_opt.getValues()) {
			
			lengths[c++] = (Integer) o;
			
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
