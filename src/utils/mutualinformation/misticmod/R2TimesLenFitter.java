package utils.mutualinformation.misticmod;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Arrays;

import cmdGA.SingleOption;

import math.linearregression.LinearRegression;

public class R2TimesLenFitter extends HistogramFitter {

	double[][] fitness_r2;
	double[][] fitness_a;
	double[][] fitness_b;
	
	double fittest_a;
	double fittest_b;
	double fittest_r2;
	double first;
	double last;
	
	@Override
	public void fit(double[] data_counts_log, double[] data_MI_ranges_min_values) {

		this.calculateFitness(data_counts_log);
		
		this.searchFittesst(data_counts_log);
		
	}

	@Override
	public void reportFittest(PrintStream out) {
		
		out.println("#Slope : "+ this.fittest_a);

		out.println("#Origin: "+ this.fittest_b);

		out.println("#R2:     "+ this.fittest_r2);
		
		out.println("#First : "+ this.first);

		out.println("#Last  : "+ this.last);
		
	}
	

	private void searchFittesst(double[] data_counts_log) {
		
		double max_value = Double.NEGATIVE_INFINITY;
		
		int max_x = 0;
		
		int max_y = 0;
		
		for (int i=0; i< data_counts_log.length-1;i++) {
			
			for (int j=i+1; j< data_counts_log.length; j++) {

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
	
	private void calculateFitness(double[] data_counts_log) {
		
		this.fitness_a = new double[data_counts_log.length][data_counts_log.length];
		this.fitness_b = new double[data_counts_log.length][data_counts_log.length];
		this.fitness_r2 = new double[data_counts_log.length][data_counts_log.length];
		
		for (int i=0; i< data_counts_log.length-1;i++) {
			
			for (int j=i+1; j< data_counts_log.length; j++) {

				double [] x = new double[j-i];
				
				for (int k=0; k<x.length;k++) {
					
					x[k] = k;
					
				}
				
				double [] y = Arrays.copyOfRange( data_counts_log, i, j);
				
				LinearRegression lr = new LinearRegression(x, y);
				
				lr.calculate();
				
				this.fitness_a[i][j] = lr.getA();
				
				this.fitness_b[i][j] = lr.getB();
				
				this.fitness_r2[i][j] = lr.getR2() * lr.getR2()*Math.sqrt(j-i);
				
			}
			
		}
				
	}
	
	public void exportCVSfile(SingleOption cvsOpt, Histogrammer hi) {
		
		File file = (File) cvsOpt.getValue();
		
		PrintStream outputPrintStream = null;
		
		try {
			
			outputPrintStream = new PrintStream(file);
			
		} catch (FileNotFoundException e) {
			
			System.err.println("There was a problem writing cvs table:" + e.getMessage());
			
			System.exit(1);
			
		}

		for (int i=0; i<this.fitness_r2.length;i++) {
		
			StringBuilder sb = new StringBuilder();
			
			for (int j = 0; j < this.fitness_r2.length; j++) {

				if (j!=0) {
					
					sb.append(';');
					
				}
				
				sb.append(this.fitness_r2[i][j]);
				
			}
			
			outputPrintStream.println(sb.toString());
			
		}
		
	}

}
