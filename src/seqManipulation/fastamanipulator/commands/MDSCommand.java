package seqManipulation.fastamanipulator.commands;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import mdsj.ClassicalScaling;
import seqManipulation.identity.IndentityMatrixCalculator;

import cmdGA.SingleOption;
import fileformats.fastaIO.Pair;

/**
 * Performs and Multidimensional Scaling (MDS) for the identities values from a MSA.
 * Uses package 'msdj' from "Algorithmics Group. MDSJ: Java Library for Multidimensional Scaling (Version 0.2). 
 * Available at http://www.inf.uni-konstanz.de/algo/software/mdsj/. University of Konstanz, 2009."
 * 
 * @author javier
 *
 */
public class MDSCommand extends FastaCommand<SingleOption> {
	
	int  dimensions;

	public MDSCommand(InputStream inputstream, PrintStream output, SingleOption option) {
		
		super(inputstream, output, option);
		
		this.dimensions = (Integer) this.getOption().getValue();
		
	}

	@Override
	protected List<String> performAction() {

		List<Pair<String, String>> seqs = this.getSequences();
		
		List<String> results = new ArrayList<String>();
		
		Map<Pair<Integer, Integer>, Double> a = IndentityMatrixCalculator.calculateIdentityMatrix(seqs);

		double[][] input = new  double[seqs.size()][seqs.size()]; 
		
		for (int i=0; i<seqs.size();i++) {    		// Initialize Input Matrix 
			
			for (int j=0; j<seqs.size();j++) {
				
				input[i][j] = 0;
				
			}
			
		}
		
		for (Pair<Integer, Integer> pair: a.keySet()) { // Convert Identities Values to distances (distance = 1 - identity) 
			
			Integer first = pair.getFirst();
			
			Integer second = pair.getSecond();
			
			input[first][second] = 1d - a.get(pair);
			
			input[second][first] = 1d - a.get(pair);
			
		}

		int n=input[0].length;    // number of data objects
		
		
		double[][] evecs = new double[this.dimensions][n];      // Array to store eigenvectors
		
		double[] evals = new double[this.dimensions];           // Array to store eigenvalues
		
		ClassicalScaling.eigen(input, evecs, evals);  // Perform MDS

	    StringBuilder headersb = new StringBuilder(); // Prepare output
	    
		for(int i = 0 ; i< this.dimensions; i++) {

			headersb.append("d"+i+",");
			
		}
		
		String header = headersb.toString();
		
		header = header.substring(0, header.length()-1);
		
		results.add(header);
	    
		for (int i = 0; i < n; i++) {
			
			StringBuilder line = new StringBuilder();
			
			for (int j=0; j<this.dimensions; j++) {
				
				line.append(evecs[j][i]);
				line.append(",");				
				
			}
			
			String printline = line.toString();
			
			results.add(printline.substring(0,printline.length()-1));
			
		}
		
		for(int i = 0 ; i< this.dimensions; i++) {

			System.err.println("eigenvalue["+i+"]: " + evals[i]);
			
		}
		return results;
		
	}

}
