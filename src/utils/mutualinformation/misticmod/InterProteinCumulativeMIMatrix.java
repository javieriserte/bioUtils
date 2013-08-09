package utils.mutualinformation.misticmod;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cmdGA.MultipleOption;
import cmdGA.NoOption;
import cmdGA.Parser;
import cmdGA.SingleOption;
import cmdGA.exceptions.IncorrectParameterTypeException;
import cmdGA.parameterType.InputStreamParameter;
import cmdGA.parameterType.IntegerParameter;
import cmdGA.parameterType.PrintStreamParameter;

public class InterProteinCumulativeMIMatrix {

	List<MI_PositionWithProtein> data;
	
	public static void main(String[] args) throws IncorrectParameterTypeException {

		Parser parser = new Parser();
		
		SingleOption inOpt = new SingleOption(parser, System.in, "-infile", InputStreamParameter.getParameter());
		
		SingleOption outOpt = new SingleOption(parser, System.out,"-outfile", PrintStreamParameter.getParameter());
		
		MultipleOption lengthsOpt = new MultipleOption(parser, null, "-lengths", ',', IntegerParameter.getParameter());
		
		NoOption countAllPairsOpt = new NoOption(parser, "-countall");
		
		NoOption helpOpt = new NoOption(parser, "-help");
		
		parser.parseEx(args);
		
		PrintStream out = (PrintStream)outOpt.getValue();
		
		boolean help = helpOpt.isPresent();
		
		if (help || !lengthsOpt.isPresent()) {
			
			printHelp(out);
			
			System.exit(1);
			
		}
		
		InterProteinCumulativeMIMatrix ipcm = new InterProteinCumulativeMIMatrix();

		Integer[] lengths = getLengths(lengthsOpt);
		
		ipcm.readMiData((InputStream) inOpt.getValue());
		
		ipcm.assignProteinNumber(lengths);

		Normalizer normalizer = countAllPairsOpt.isPresent()?(ipcm.new NormalizeWithAll(lengths)):(ipcm.new NormalizeWithPositives(lengths.length, ipcm.data, lengths));
		
		Double[][] cmi_inter = ipcm.calculateCMIInter(lengths.length, lengths,normalizer, ipcm.data);
		
		export(out,cmi_inter);

		out.flush();
		
		out.close();
		
	}
	
	private static void printHelp(PrintStream out) {
		
		out.println("Options:");
		out.println("  -infile         : Input Data of MI values of MISTIC");
		out.println("  -outfile        : Output file");
		out.println("  -countall       : Count all pairs in MI-IP normalization");
		out.println("  -lengths        : Size of proteins");
		
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



	public Double[][] calculateCMIInter(int length, Integer[] lengths, Normalizer normalizer, List<MI_PositionWithProtein> data) {
		
		Double[][] results   = new Double[length][length];
		
		for (int i=0; i<results.length;i++) {
		
			Arrays.fill(results[i], 0d); // Initializes Array
			
		}

		results = sumMI(results,data); // Sum MI
		
		//////////////////////////////////
		// Normalize sum
		for(int i=0 ; i<length ; i++) {
			
			for(int j=0; j<length ; j++) {

				double denominator = normalizer.denominator(i,j);
				
				results[i][j] = (denominator==0)?0:results[i][j] / denominator; 
				
			}
			
		}
		
		return results;
		
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
			
			double cv = pos.mi>0?pos.mi:0;

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
			
			pos.setProtein_1(this.getProteinNumberFromPos(pos.pos1,lengths));
			
			pos.setProtein_2(this.getProteinNumberFromPos(pos.pos2,lengths));
			
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
		
		BufferedReader inb = new BufferedReader(new InputStreamReader(in));
		
		this.data = new ArrayList<MI_PositionWithProtein>();
		
		try {
			while((currentLine = inb.readLine())!=null) {
			
				String[] data = currentLine.split("\t");
				
				this.data.add(new MI_PositionWithProtein(Integer.valueOf(data[0]), Integer.valueOf(data[2]), data[1].charAt(0), data[3].charAt(0), Double.valueOf(data[4])));
				
			}
		} catch (IOException e) {

			e.printStackTrace();
			
		}
		
	}
	
	///////////////////////
	// Helper Classes
	abstract class Normalizer { 
		
		public abstract double  denominator(int row, int column); 		
		
	}
	/**
	 * Normalize the sum of MI values among two proteins with all pairs.
	 * This normalization includes pairs with MI = -999.99.
	 * 
	 * @author javier iserte
	 *
	 */
	class NormalizeWithAll extends Normalizer {

		Integer[] lengths;
		
		public NormalizeWithAll(Integer[] lengths) {
			super();
			this.lengths = lengths;
		}

		@Override
		public double denominator(int row, int column) {
			
			return (lengths[row] * lengths[column]);
			
		}
		
	}
	
	/**
	 * Normalize the sum of MI values among two proteins with only positive pairs.
	 * Discarding all pairs with MI = -999.99 marked by others programs.
	 * 
	 * @author javier iserte
	 *
	 */
	class NormalizeWithPositives extends Normalizer {
	
		/////////////////////////////////
		// Private Instance Variables
		private Double[][] normalize; // Normalize counts the number of links between every pair of proteins 
		
		//////////////////////////
		// Constructor
		public NormalizeWithPositives(int length, List<MI_PositionWithProtein> data, Integer[] lengths) {
			
			//////////////////////////////
			// Initialize internal state
			normalize = new Double[length][length];
			
			for (int i =0 ; i < length ;i ++) {
				
				for (int j=0; j < length ;j++) {
					
					normalize[i][j] = 0d;
					
				}
				
			}
			
			////////////////////////////////////
			// Counts links with MI values > 0 
			/// (usually MI>0 implies MI>6.5 due to previous filters)
			for (MI_PositionWithProtein pos : data) {
				
				double nv = pos.mi>0?1:0;
				
				normalize[pos.getProtein_1()][pos.getProtein_2()] = normalize[pos.getProtein_1()][pos.getProtein_2()] + nv ;
				
				normalize[pos.getProtein_2()][pos.getProtein_1()]= normalize[pos.getProtein_2()][pos.getProtein_1()] + nv;
				
			}
			
		}

		@Override
		public double denominator(int row, int column) {
			
			return normalize[row][column];
			
		}
		
	}
	

}
