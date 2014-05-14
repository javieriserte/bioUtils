package seqManipulation.alignmentclusterweight;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pair.Pair;
import clustering.HobohmClusteringM1;
import cmdGA2.CommandLine;
import cmdGA2.NoArgumentOption;
import cmdGA2.OptionsFactory;
import cmdGA2.SingleArgumentOption;

/**
 * This class is used to calculate the weight of each cluster in
 * an alignment clustered with Hobohm-1 method.
 * 
 * @author javier
 *
 */
public class AlignmentClusterWeight {

	public static void main(String[] args) {

		///////////////////////////////
		// Create Command Line Object
		CommandLine cmdline = new CommandLine();
		//////////////////////////////
		
		///////////////////////////////
		// Add options to Command Line
		SingleArgumentOption<InputStream> inOpt = OptionsFactory.createBasicInputStreamArgument(cmdline); 
		// "--infile" option
		SingleArgumentOption<PrintStream> outOpt = OptionsFactory.createBasicPrintStreamArgument(cmdline); 
		// "--outfile" option
		SingleArgumentOption<Double> clusterIdOpt = OptionsFactory.createBasicDoubleArgument(cmdline, "--clusterId", 0.62); 
		// Identity fraction threshold for sequence clustering 
		NoArgumentOption helpOpt = new NoArgumentOption(cmdline, "--help");
		// Prints help and exit.
		// End of Adding options to the command line
		//////////////////////////////

		////////////////////////////////
		// Parse Command Line
		cmdline.readAndExitOnError(args);
		////////////////////////////////
		
		///////////////////////////////
		// Checks for help flag
		if (helpOpt.isPresent()) {
			
			System.err.println(AlignmentClusterWeight.getHelp());
			
			System.exit(1);
			
		}
		///////////////////////////////
		
		///////////////////////////////
		// Get values from the command line
		BufferedReader in = new BufferedReader (new InputStreamReader(inOpt.getValue()));
		PrintStream out = outOpt.getValue();
		double id = clusterIdOpt.getDefaultValue();
		///////////////////////////////
		
		////////////////////////////////
		// Get sequence map from the alignment
		LinkedHashMap<String, String> sequences = AlignmentToMapConverter.read(in);
		////////////////////////////////
		
		////////////////////////////////
		// Get the cluster weights
		Map<String, Double> weigths = AlignmentClusterWeight.calculateClustersWeights(sequences, id);
		////////////////////////////////
		
		////////////////////////////////
		// Print out the results
		for(String description : sequences.keySet() ) {
			out.println(description + "\t" + weigths.get(description));
		}
		////////////////////////////////
				
	}
	
	/**
	 * Calculates the weight of each sequence in an alignment given by the 
	 * size of the cluster it belongs. Sequence weight is calculated as
	 * 1 / (size of cluster). 
	 * @param sequences
	 * @param thresholdId
	 * @return
	 */
	public static Map<String, Double> calculateClustersWeights(Map<String, String> sequences, double thresholdId) {
		
		Map<String,Double> clusterWeigth = new HashMap<>();
		// Create map to store the weights of each cluster

		HobohmClusteringM1 clusterer = new HobohmClusteringM1();
		// Creates the object to be used to perform the clustering.
		
		List<Pair<String,String>> sequenceList = new ArrayList<>();
		for (String  description : sequences.keySet()) {
			sequenceList.add(new Pair<String, String>(description, sequences.get(description)));
		}
		// Creates a list to store the sequences of the alignment 
		// in a data structure understandable by the cluster.
		
		Set<List<Pair<String, String>>> clusters = clusterer.clusterize(sequenceList, thresholdId);
		// Perform clustering

		for (List<Pair<String, String>> cluster : clusters) {
			
			double currentClusterWeight = (double) ((double)1.0 / (double)cluster.size());
			
			for (Pair<String, String> sequence : cluster) {
				
				clusterWeigth.put(sequence.getFirst(), currentClusterWeight);
				
			}
			
		}
		// Calculates the weight for each sequence
		
		return clusterWeigth;
		
	}
	
	public static String getHelp() {
		InputStream inputHelpStream = AlignmentClusterWeight.class.getResourceAsStream("help");
		StringBuilder result = new StringBuilder();
		
		int currentChar = -1;
		
		try {
			while ((currentChar=inputHelpStream.read())>=0) {
				result.append((char)currentChar);
			}
		} catch (IOException e) {
			System.err.println("There was an error while reading help text: "+ e.getMessage());
			System.exit(1);
		}
		
		return result.toString();
	}

}
