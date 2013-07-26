package utils.mutualinformation.misticmod;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import cmdGA.Parser;
import cmdGA.SingleOption;
import cmdGA.exceptions.IncorrectParameterTypeException;
import cmdGA.parameterType.InputStreamParameter;
import cmdGA.parameterType.IntegerParameter;
import cmdGA.parameterType.OutFileParameter;
import cmdGA.parameterType.PrintStreamParameter;

public class Histogrammer {
	
	double[] data_MI;
	double[] data_counts;
	double[] data_counts_log;
	double[] data_MI_ranges_min_values;
	double   stepsize;

	double minMI;
	double maxMI;
	
	public static void main(String[] arg) {
		
		Parser parser =  new Parser();

		SingleOption inOpt = new SingleOption(parser, System.in, "-infile", InputStreamParameter.getParameter());
		
		SingleOption outOpt = new SingleOption(parser, System.out, "-outfile", PrintStreamParameter.getParameter());
		
		SingleOption cvsOpt = new SingleOption(parser, null, "-cvs", OutFileParameter.getParameter());
		
		SingleOption stepOpt = new SingleOption(parser, 100, "-steps", IntegerParameter.getParameter());
		
		SingleOption fitOpt = new SingleOption(parser, 1, "-fit", IntegerParameter.getParameter());
		
		try {
			parser.parseEx(arg);
		} catch (IncorrectParameterTypeException e1) {
			
			System.err.println("There was an error trying to parse the command line:" + e1.getMessage());
			System.exit(1);
		}
		
		// Get Values from command line
		BufferedReader in = new BufferedReader(new InputStreamReader((InputStream) inOpt.getValue()));
		
		PrintStream out = (PrintStream) outOpt.getValue();
		
		int step = (int) stepOpt.getValue();
		
		int fit = (int) fitOpt.getValue();
		
		HistogramFitter fitter = null;
		
		switch(fit) {
		case 1:
			fitter = new MinusTwoFitter();
			break;
		case 2: 
			fitter = new R2TimesLenFitter();
		} 
		

		// Create Histogrammer Object
		Histogrammer hi = new Histogrammer();
	
		// Initialize Histogrammer Object
		System.err.println("Reading Data...");
		hi.data_MI = readData(in);
		System.err.println("Reading Data Done");
		
		// Get Min & Max
		System.err.println("Getting Min and Max...");
		hi.minMI = hi.calculateMinimumMI();
		hi.maxMI = hi.calculateMaximumMI();
		System.err.println("Getting Min and Max done");
		
		// Counts
		System.err.println("Counting...");
		hi.count(step);
		hi.count_log();
		System.err.println("Counting Done");

		System.err.println("Fitting...");
		fitter.fit(hi.data_counts_log, hi.data_MI_ranges_min_values);
		System.err.println("Fitting Done");
		
		fitter.reportFittest(out);
		
		exportHistoGram(out, step, hi);
		
		if (cvsOpt.isPresent() && fit ==2) {
			
			((R2TimesLenFitter) fitter).exportCVSfile(cvsOpt, hi);
			
		}
		
	}



	public static void exportHistoGram(PrintStream out, int step, Histogrammer hi) {
		
		for (int i=0; i<step;i++) {
			
			out.println(String.format("%.8f\t%8d\t%.8f",hi.data_MI_ranges_min_values[i],Math.round(hi.data_counts[i]),hi.data_counts_log[i]));
			
		}
	}

	private void count_log() {
		
		this.data_counts_log = new double[data_counts.length];
		
		for(int i=0; i<this.data_counts.length; i++) {
			
			double value = this.data_counts[i];
			
			if (value!=0) {
			
				this.data_counts_log[i] = Math.log10(value);
				
			} else {
				
				this.data_counts_log[i] = 0;
				
			}
			
		}
		
	}


	/**
	 * Calculate the mininum MI input value.  
	 * 
	 * @return
	 */
	private double calculateMinimumMI() {

		double min = Double.POSITIVE_INFINITY;
		
		for (int i=0; i<this.data_MI.length; i++) {
			
			min = Math.min(min, this.data_MI[i]);
			
		}
		
		return min;
		
	}
	
	/**
	 * Calculate the maximum MI input value.
	 * 
	 * @return
	 */
	private double calculateMaximumMI() {

		double max = Double.NEGATIVE_INFINITY;
		
		for (int i=0; i<this.data_MI.length; i++) {
			
			max = Math.max(max, this.data_MI[i]);
			
		}
		
		return max;
		
	}



	private void count(int step) {
	
		double range = this.maxMI - this.minMI;
		
		this.stepsize = range / step;
		
		this.data_counts = new double[step];
		
		for (int i = 0 ;i < this.data_MI.length; i++) {
			
			int current = Math.min((int) ((this.data_MI[i] - this.minMI) / this.stepsize ),step-1);
			
			this.data_counts[current]++;
			
		}
		
		
		this.data_MI_ranges_min_values = new double[step];
		
		for (int i=0; i < this.data_MI_ranges_min_values.length;i++) {
			
			this.data_MI_ranges_min_values[i] = this.minMI + this.stepsize *i;
			
		}
		
		
	}

	private static double[] readData(BufferedReader in) {
		
		String currentLine = null;
		
		double[] result = null;
		
		List<Double> res = new ArrayList<>();
		
		try {
			
			while((currentLine=in.readLine())!=null) {
				
				res.add(Double.valueOf(currentLine));	
				
			}
			
			result = new double[res.size()];

			for (int i = 0; i < result.length; i++) {
				
				result[i] = res.get(i);
				
			}
			
		} catch (IOException e) {

			System.err.println("There was an error trying to read input data:"+e.getMessage());
			
		}
		
		return result;
		
	}

}
