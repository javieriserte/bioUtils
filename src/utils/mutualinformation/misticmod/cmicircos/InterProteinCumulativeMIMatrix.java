package utils.mutualinformation.misticmod.cmicircos;

import io.bufferreaders.UncommenterBufferedReader;
import io.resources.ResourceContentAsString;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import utils.mutualinformation.misticmod.datastructures.MI_PositionWithProtein;
import cmdGA.MultipleOption;
import cmdGA.NoOption;
import cmdGA.Parser;
import cmdGA.SingleOption;
import cmdGA.exceptions.IncorrectParameterTypeException;
import cmdGA.parameterType.InputStreamParameter;
import cmdGA.parameterType.IntegerParameter;
import cmdGA.parameterType.PrintStreamParameter;
import cmdGA.parameterType.StringParameter;

public class InterProteinCumulativeMIMatrix {

	////////////////////////////////////////////////////////////////////////////
	// Instance variables
	List<MI_PositionWithProtein> data;
	////////////////////////////////////////////////////////////////////////////

	////////////////////////////////////////////////////////////////////////////
	// Executable main
	public static void main(String[] args) throws IncorrectParameterTypeException {

		////////////////////////////////////////////////////////////////////////
		// Create Command Line Parser
		Parser parser = new Parser();
		////////////////////////////////////////////////////////////////////////
		
		////////////////////////////////////////////////////////////////////////
		// Add command line options
		SingleOption inOpt = new SingleOption(parser, System.in, "-infile", InputStreamParameter.getParameter());
		
		SingleOption outOpt = new SingleOption(parser, System.out,"-outfile", PrintStreamParameter.getParameter());
		
		MultipleOption lengthsOpt = new MultipleOption(parser, null, "-lengths", ',', IntegerParameter.getParameter());
		
		MultipleOption namesOpt = new MultipleOption(parser, null, "-names", ',', StringParameter.getParameter());
		
		NoOption tableOutOpt = new NoOption(parser, "-table");
		
		NoOption countAllPairsOpt = new NoOption(parser, "-countall");
		
		NoOption helpOpt = new NoOption(parser, "-help");
		////////////////////////////////////////////////////////////////////////

		////////////////////////////////////////////////////////////////////////
		// Parse Command line
		parser.parseEx(args);
		////////////////////////////////////////////////////////////////////////
		
		////////////////////////////////////////////////////////////////////////
		// Check for help
		if (helpOpt.isPresent() ) {
			
			String helpText = (new ResourceContentAsString()).readContents(
					"cmi_ip_calc_help", InterProteinCumulativeMIMatrix.class);
			
			System.err.println(helpText);
			
			System.exit(0);
			
		}
		////////////////////////////////////////////////////////////////////////

		////////////////////////////////////////////////////////////////////////
		// Get values from command line
		PrintStream out = (PrintStream)outOpt.getValue();
		InputStream in = (InputStream) inOpt.getValue();
		Integer[] lengths = null;
		if (lengthsOpt.isPresent()) {
			lengths = getLengths(lengthsOpt);
		} else {
			System.err.println("-lengths option is mandatory.");
			System.err.println("Use -help for help.");
			System.exit(0);
		}
		////////////////////////////////////////////////////////////////////////
		
		////////////////////////////////////////////////////////////////////////
		// Creates Cmi-Ip Calculator object
		InterProteinCumulativeMIMatrix ipcm = new InterProteinCumulativeMIMatrix();
		////////////////////////////////////////////////////////////////////////

		////////////////////////////////////////////////////////////////////////
		// Read Input data
		ipcm.readMiData(in);
		ipcm.assignProteinNumber(lengths);
		////////////////////////////////////////////////////////////////////////

		////////////////////////////////////////////////////////////////////////
		// Check that the number of positions in MI data matches with the 
		// protein lengths passed
		int totalLength = 0;
		for (Integer currentLengths : lengths) {
			totalLength += currentLengths;
		}
		
		if (ipcm.data.size() != totalLength * (totalLength - 1) / 2 ) {

			System.err.println("Lengths passed do not match with the number of positions in MI Data.");
			
		}
		////////////////////////////////////////////////////////////////////////

		////////////////////////////////////////////////////////////////////////
		// Creates normalizer object
		Normalizer normalizer = countAllPairsOpt.isPresent()?
				new NormalizeWithAll(lengths):
				new NormalizeWithPositives(lengths.length, ipcm.data, lengths);
		////////////////////////////////////////////////////////////////////////

		////////////////////////////////////////////////////////////////////////
		// Calculates Cmi Ip	
		Double[][] cmi_inter = ipcm.calculateCMIInter(
				lengths.length, lengths, normalizer, ipcm.data);
		////////////////////////////////////////////////////////////////////////
		
		////////////////////////////////////////////////////////////////////////
		// Print the data
		if (tableOutOpt.isPresent()) {
			
			List<String> names = getNames(namesOpt,lengths);
			
			InterProteinCumulativeMIMatrix.exportTable(out,cmi_inter, names);
			
		
		} else {
		
			export(out,cmi_inter);
			
		}
		////////////////////////////////////////////////////////////////////////

		out.flush();
		
		out.close();
		
	}
	// End of Executable main
	////////////////////////////////////////////////////////////////////////////
	
	
	/**
	 * Print the results of the calculation as a <Tab> separated table.
	 * @param out <code>PrintStream</code> to print the results.
	 * @param cmi_inter a <code>Double[][]</code> matrix with the results of the calculation
	 * @param names a <code>List</code> of the protein's names.
	 */
	private static void exportTable(PrintStream out, Double[][] cmi_inter,
			List<String> names) {
		
		for (int i=0; i<=names.size() ; i++) {
			
			for (int j=0; j<=names.size(); j++) {

				if (i==0 && j>0 || i>0 && j==0) {
					
					out.print( ((i==0)?"\t":"") + names.get(Math.max(i-1,j-1)));
					
				} else if (j>0 && i>0) {
					
					String value = (j>i)?String.format(Locale.US,"%6.5f",cmi_inter[i-1][j-1]):"-"; 
					
					out.print("\t" + value);
					
				}

			}
			
			out.print(System.getProperty("line.separator"));			
			
		}
		
	}

	/**
	 * Get the proteins names from the corresponding command line option.
	 * If the command line is incorrect or absent, generic protein names are 
	 * returned.
	 * 
	 * @param namesOpt a <code>MultipleOption</code> command line option 
	 *        corresponding to the protein's names. 
	 * @param lengths a <code>Integer</code> array with the length of each protein.
	 * @return a <code>List{@literal <}String{@literal >}</code> with the protein's names.
	 */
	private static List<String> getNames(MultipleOption namesOpt, Integer[] lengths) {
		
		List<String> names = new ArrayList<String>();
		
		if (namesOpt.isPresent() &&  namesOpt.count() == lengths.length) {
			
			for (int i=0; i< namesOpt.count(); i++) {

				names.add( (String) namesOpt.getValue(i));

			}
			
		} else {
			
			for (int i = 0; i< lengths.length ; i++) {
				
				names.add("Prot. " + String.valueOf(i));
				
			}
			
		}
		
		return names;
		
	}

	/***
	 * export CMI inter-protein data.
	 * Prints one line for protein-protein CMI.
	 * And shows the protein number for each protein and CMI, separated by a tab.
	 * 
	 * @param out
	 * @param cmi_inter
	 */
	private static void export(PrintStream out, Double[][] cmi_inter) {
		
		for (int i = 0 ; i<cmi_inter.length-1;i++) {
			
			for (int j=i+1; j<cmi_inter.length; j++) {
				
				out.println((i+1) +"\t" + (j+1) + "\t" + cmi_inter[i][j]);
				
			}
			
		}
		
	}

	/**
	 * Performs the Cmi-Ip Calculation and returns a matrix with the results.
	 * 
	 * @param length is the number of proteins.
	 * @param lengths is a <code>Integer</code> array with the length of each protein.
	 * @param normalizer is the method to normalize the sum of MI values for each
	 *        pair of proteins. 
	 * @param data is MI data for every pair of positions.
	 * @return a <code>Double</code> matrix with the result of the calculation. 
	 */
	public Double[][] calculateCMIInter(int length, Integer[] lengths, Normalizer normalizer, List<MI_PositionWithProtein> data) {
		
		Double[][] results   = new Double[length][length];
		
		////////////////////////////////////////////////////////////////////////
		// Initializes Array of results
		for (int i=0; i<results.length;i++) {
		
			Arrays.fill(results[i], 0d); 
			
		}
		////////////////////////////////////////////////////////////////////////

		////////////////////////////////////////////////////////////////////////
		// Sum all MI
		results = this.sumMI(results,data); 
		////////////////////////////////////////////////////////////////////////
		
		////////////////////////////////////////////////////////////////////////
		// Normalize sum
		results = normalize(length, normalizer, results);
		////////////////////////////////////////////////////////////////////////
		
		return results;
		////////////////////////////////////////////////////////////////////////
		
	}

	/**
	 * Normalize the results of the sum of MI for all proteins pairs.
	 * 
	 * @param length is the number of proteins
	 * @param normalizer is the method for normalization
	 * @param data is a <code>Double</code> matrix with the sum data.
	 *        This matrix is modified as side-effect. 
	 * @return the modified input data.
	 */
	private Double[][] normalize(int length, Normalizer normalizer, Double[][] data) {
		
		for(int i=0 ; i<length ; i++) {
			
			for(int j=0; j<length ; j++) {

				double denominator = normalizer.denominator(i,j);
				
				data[i][j] = (denominator==0)?0:data[i][j] / denominator; 
				
			}
			
		}
		
		return data;
		
	}



	/***
	 * 
	 * Fill an array with MI values
	 * 
	 * @param results
	 * @return
	 */
	public Double[][] sumMI(Double[][] results, List<MI_PositionWithProtein> data) {
		
		for (MI_PositionWithProtein pos : data) {
			
			double cv = pos.getMi()>0 ? pos.getMi(): 0;

			results[pos.getProtein_1()][pos.getProtein_2()] = results[pos.getProtein_1()][pos.getProtein_2()] + cv ;
			
			results[pos.getProtein_2()][pos.getProtein_1()]= results[pos.getProtein_2()][pos.getProtein_1()] + cv;

		}
		
		return results;
	}


	/**
	 * Sets the protein data that correspond for each protein
	 * 
	 * @param lengths
	 */
	public void assignProteinNumber(Integer[] lengths) {
		
		for (MI_PositionWithProtein pos : this.data) {
			
			pos.setProtein_1(this.getProteinNumberFromPos(pos.getPos1(),lengths));
			
			pos.setProtein_2(this.getProteinNumberFromPos(pos.getPos2(),lengths));
			
		}
		
	}

	/**
	 * Get the protein value from the position
	 * 
	 * @param pos2
	 * @param lengths
	 * @return
	 */
	private int getProteinNumberFromPos(int pos2, Integer[] lengths) {
		
		for (int i = 0; i<lengths.length;i++) {
			
			pos2 = pos2 - lengths[i];
			
			if (pos2<=0) return i;
			
		}
		
		return 0;
		
	}



	/**
	 * transforms lengths option from command line to a Integer array 
	 * 
	 * @param lengthsOpt
	 * @return
	 */
	public static Integer[] getLengths(MultipleOption lengthsOpt) {

		Integer[] result = new Integer[lengthsOpt.count()];
		
		for (int i = 0; i<lengthsOpt.count(); i++) {
			
			result[i] = (Integer) lengthsOpt.getValue(i);
			
		}
		
		return result;
	}



	/**
	 * Reads strings for MI data generated by MISTIC from the InputStream
	 * and returns a list of objects representing the data  
	 * 
	 * @param in
	 */
	public void readMiData(InputStream in) {
		
		String currentLine = null;
		
		BufferedReader inb = new UncommenterBufferedReader(new InputStreamReader(in));
		
		this.data = new ArrayList<MI_PositionWithProtein>();
		
		try {
			while((currentLine = inb.readLine())!=null) {

				MI_PositionWithProtein mi_pos = MI_PositionWithProtein.valueOf(currentLine);
				
				this.data.add(mi_pos);

			}
		} catch (IOException e) {

			e.printStackTrace();
			
		}
		
	}
	
}
