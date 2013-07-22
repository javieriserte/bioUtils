package utils.mutualinformation.misticmod;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import math.linearregression.LinearRegression;

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
	double[][] fitness_r2;
	double[][] fitness_a;
	double[][] fitness_b;
	
	double fittest_a;
	double fittest_b;
	double fittest_r2;
	
	int first;
	int last;

	double minMI;
	double maxMI;
	
	public static void main(String[] arg) {
		
		Parser parser =  new Parser();

		SingleOption inOpt = new SingleOption(parser, System.in, "-infile", InputStreamParameter.getParameter());
		
		SingleOption outOpt = new SingleOption(parser, System.out, "-outfile", PrintStreamParameter.getParameter());
		
		SingleOption cvsOpt = new SingleOption(parser, null, "-cvs", OutFileParameter.getParameter());
		
		SingleOption stepOpt = new SingleOption(parser, 100, "-steps", IntegerParameter.getParameter());
		
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

		// Create Histogrammer Object
		Histogrammer hi = new Histogrammer();
	
		// Initialize Histogrammer Object
		
		System.err.println("Reading Data...");
		hi.data_MI = readData(in);
		System.err.println("Reading Data Done");
		
		// Get Min & Max
		System.err.println("Getting Min and Max...");
		hi.minMI = hi.getMinMI();
		hi.maxMI = hi.getMaxMI();
		System.err.println("Getting Min and Max done");
		
		// Counts
		System.err.println("Counting...");
		hi.count(step);
		hi.count_log();
		System.err.println("Counting Done");

		System.err.println("Fitting...");
		hi.calculateFitness();
		hi.searchFittesst();
		System.err.println("Fitting Done");
		
		
		out.println("#Slope : "+ hi.fittest_a);

		out.println("#Origin: "+ hi.fittest_b);

		out.println("#R2:     "+ hi.fittest_r2);
		
		out.println("#First : "+ hi.first);

		out.println("#Last  : "+ hi.last);
		
		for (int i=0; i<step;i++) {
			
			out.println(String.format("%.8f\t%.8f",hi.data_counts[i],hi.data_counts_log[i]));
			
		}
		
		
		if (cvsOpt.isPresent()) {
			
			File file = (File) cvsOpt.getValue();
			
			PrintStream of = null;
			try {
				of = new PrintStream(file);
			} catch (FileNotFoundException e) {
				
				System.err.println("There was a problem writing cvs table:" + e.getMessage());
				
				System.exit(1);
				
			}

			for (int i=0; i<hi.fitness_r2.length;i++) {
			
				StringBuilder sb = new StringBuilder();
				
				for (int j = 0; j < hi.fitness_r2.length; j++) {

					if (j!=0) {
						
						sb.append(';');
						
					}
					
					sb.append(hi.fitness_r2[i][j]);
					
				}
				
				of.println(sb.toString());
				
			}
			
		}
		
	}


	private void searchFittesst() {
		
		double max_value = Double.NEGATIVE_INFINITY;
		
		int max_x = 0;
		
		int max_y = 0;
		
		for (int i=0; i< this.data_counts_log.length-1;i++) {
			
			for (int j=i+1; j<this.data_counts_log.length; j++) {
				

				double current_value = this.fitness_r2[i][j];
				
				if (max_value<current_value) {
					
					max_x = i;
					
					max_y = j;
					
					max_value = current_value;
					
				}
				
			}
			
		}
		
		this.fittest_a = this.fitness_a[max_x][max_y];
		
		this.fittest_b = this.fitness_b[max_x][max_y];
		
		this.fittest_r2 = this.fitness_r2[max_x][max_y];
		
		this.first = max_x;
		
		this.last = max_y;
		
	}


	private void calculateFitness() {
		
		this.fitness_a = new double[this.data_counts_log.length][this.data_counts_log.length];
		this.fitness_b = new double[this.data_counts_log.length][this.data_counts_log.length];
		this.fitness_r2 = new double[this.data_counts_log.length][this.data_counts_log.length];
		
		for (int i=0; i< this.data_counts_log.length-1;i++) {
			
			for (int j=i+1; j<this.data_counts_log.length; j++) {

				double [] x = new double[j-i];
				
				for (int k=0; k<x.length;k++) {
					
					x[k] = k;
					
				}
				
				double [] y = Arrays.copyOfRange(this.data_counts_log, i, j);
				
				LinearRegression lr = new LinearRegression(x, y);
				
				lr.calculate();
				
				this.fitness_a[i][j] = lr.getA();
				
				this.fitness_b[i][j] = lr.getB();
				
				this.fitness_r2[i][j] = lr.getR2() * lr.getR2()*Math.sqrt(j-i);
				
			}
			
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


	private double getMinMI() {

		double min = Double.POSITIVE_INFINITY;
		
		for (int i=0; i<this.data_MI.length; i++) {
			
			min = Math.min(min, this.data_MI[i]);
			
		}
		
		return min;
		
	}
	
	private double getMaxMI() {

		double max = Double.NEGATIVE_INFINITY;
		
		for (int i=0; i<this.data_MI.length; i++) {
			
			max = Math.max(max, this.data_MI[i]);
			
		}
		
		return max;
		
	}



	private void count(int step) {
	
		double range = this.maxMI - this.minMI;
		
		this.data_counts = new double[step];
		
		for (int i = 0 ;i < this.data_MI.length; i++) {
			
			int current = Math.min((int) ((this.data_MI[i] - this.minMI) * step / range),step-1);
			
			this.data_counts[current]++;
			
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
