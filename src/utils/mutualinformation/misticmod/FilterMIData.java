package utils.mutualinformation.misticmod;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import cmdGA.SingleOption;

public abstract class FilterMIData {

	SingleOption inOpt;
	
	SingleOption outopt;
	
	static FilterMIData filter; 
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public void main() throws IOException {
	
		
		PrintStream out = (PrintStream) filter.outopt.getValue();
	
		BufferedReader in = new BufferedReader(new InputStreamReader((InputStream)filter.inOpt.getValue()));
	
		List<Double> values;
		
		List<MI_Position> MI_Data_Lines = null;
		
		try {
		
			MI_Data_Lines = filter.getMIData(in);
			
			values = filter.extractMIvalues(MI_Data_Lines);
		
			values = filter.filterMinusInfvalues(values);
		
			filter.filter(out, values, MI_Data_Lines);
		
		} catch (OutOfMemoryError e) {
			
			System.err.println("There was an out of memory error reading data:" + e.getMessage());
			System.err.println("Try to modifiy JVM memory parameters: Xms and Xmx,");
			System.err.println("i.e.: java -Xms1024m Xmx1024m -jar program.jar");
			System.err.println("      or ");
			System.err.println("      >export set JAVA_OPTS=\"-Xms1024m Xmx1024m\"");
			
		} finally {
			
			out.flush();
			
			out.close();
			
		}
		
	}
	
	public abstract void filter(PrintStream out, List<Double> values, List<MI_Position> MI_Data_Lines); 

	
	protected List<MI_Position> getMIData(BufferedReader in) throws IOException, OutOfMemoryError{
		
		List<MI_Position> MI_Data_Lines = new ArrayList<MI_Position>();
		
		String currentline = null;
		
		while ((currentline=in.readLine())!=null) {
		
//			String[] data = currentline.split("\t");
//		
//			MI_Data_Lines.add(new MI_Position(Integer.valueOf(data[0]), Integer.valueOf(data[2]), data[1], data[3], Double.valueOf(data[4])));
			
			MI_Data_Lines.add(MI_Position.valueOf(currentline));
		
		}
		
		return MI_Data_Lines;

	}

	/**
	 * eliminates from the list of MI values those that are not minus infinity (given by -999 number in the files generated by MISTIC)
	 * 
	 * 
	 * @param values
	 * @return
	 */
	protected List<Double> filterMinusInfvalues(List<Double> values) {
		List<Double> result = new ArrayList<Double>();
		
		for (Double value : values) {
			
			if (value>=-999) {
				
				result.add(value);
				
			}
			
		}
	
		return result;
	}

	/**
	 * reads a MI_data file generated by MISTIC and returns the values column 
	 * 
	 * @param mI_Data_Lines
	 * @return
	 */
	protected List<Double> extractMIvalues(List<MI_Position> mI_Data_Lines) {
		List<Double> result = new ArrayList<Double>();
		
		for (MI_Position pos : mI_Data_Lines) {
			
			result.add(pos.mi);
			
		}
		
		
		return result;
	}
	


}