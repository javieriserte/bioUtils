package utils.mutualinformation.misticmod;

import io.bufferreaders.UncommenterBufferedReader;
import io.onelinelister.OneLineListReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import math.ranks.SpearmanCorrelation;

import cmdGA2.CommandLine;
import cmdGA2.OptionsFactory;
import cmdGA2.SingleArgumentOption;

public class StatisticsForMI {
    private static final double Z_SCORE_CUT_OFF = 6.5;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		////////////////////////////////////////////////
		// Create Command Line object
		CommandLine cmd = new CommandLine();
		
		////////////////////////////////////////////////
		// Add Options to Command Line
		SingleArgumentOption<InputStream> inOpt = OptionsFactory.createBasicInputStreamArgument(cmd);
		
		SingleArgumentOption<PrintStream> outOpt = OptionsFactory.createBasicPrintStreamArgument(cmd);

		/////////////////////////////////////////////////
		// Parse Command Line
		cmd.readAndExitOnError(args);
		
		/////////////////////////////////////////////////
		// Get Values From Command line
		BufferedReader in = new UncommenterBufferedReader(new InputStreamReader(inOpt.getValue()));
		
		PrintStream out = outOpt.getValue();
		
		MI_PositionLineParser lineParser = new MI_PositionLineParser();

		OneLineListReader<MI_Position> reader = new OneLineListReader<MI_Position>(lineParser);
		
		int numberOfResidues = 0;
		
		int numberOfPairs = 0;
		
		int numberOfPairsAboveCutOff = 0;
		
		int numberOfPairsCalculated = 0;
		
		int numberOfPairsNonCalculated = 0;
		
		try {
			
			List<MI_Position> positions = reader.read(in);
			
			numberOfPairs = positions.size();
			
			numberOfResidues = (int) (Math.sqrt(numberOfPairs * 8 + 1) +1) / 2;   
			
			List<MI_Position> positionsAboveCutOff = getZscoreAbove(positions, Z_SCORE_CUT_OFF);
			
			numberOfPairsAboveCutOff = positionsAboveCutOff.size();
			
			positions = removeNonCalculatedValues(positions);
			
			numberOfPairsCalculated = positions.size();
			
			numberOfPairsNonCalculated = numberOfPairs - numberOfPairsCalculated;

			Map<MI_Position, Integer> orderMap = getOrderMap (positions); 
			
			Map<Integer, Integer> mi_rank = getRawMIRank (positions, orderMap);
			
			Map<Integer, Integer> z_rank = getZRank (positions, orderMap);
			
			double spearman_coef = (new SpearmanCorrelation()).compare(mi_rank, z_rank);
			
			double min_z = positions.get(numberOfPairsCalculated-1).getMi();
			
			double max_z = positions.get(0).getMi();
			
			double[] requiredPercentils = new double[]{0.001, 0.005, 0.01, 0.05, 0.1};
			
			double[] percentils = getPercentilValues(positions, requiredPercentils);
			
			out.println("Total residues\t"+ numberOfResidues);
			
			out.println("Total pairs\t"+ numberOfPairs);
			
			out.println("Calculated pairs\t"+ numberOfPairsCalculated);
			
			out.println("No calculated pairs\t"+ numberOfPairsNonCalculated);
			
			out.println("Pairs Above Cutoff\t"+ numberOfPairsAboveCutOff);
			
			out.println("Pairs Above Cutoff(%)\t"+ ((double) numberOfPairsAboveCutOff * 100 / numberOfPairsCalculated));
			
			out.println("Min value %\t"+ min_z);
			
			out.println("Max value %\t"+ max_z);
			
			out.println("MI raw vs Z-Score Spearman%\t"+ spearman_coef);
			
			for (int i = 0; i < percentils.length; i++) {
				
				double d = percentils[i];
				
				out.println("Percentil %"+(requiredPercentils[i]*100) + "\t"+ d);
				
			}
			
			out.flush();
			
			out.close();
			
			System.exit(0);
			
			
		} catch (IOException e) {
			
			System.err.println("There was an error: "+ e.getMessage());
			
			System.exit(1);
		}
		
	}
	
	private static double[] getPercentilValues(List<MI_Position> positions,
			double[] ds) {
		
		double[] result  = new double[ds.length]; 
		
		for (int i = 0; i < result.length; i++) {
		
			int pos = (int) (ds[i] * positions.size());
			
			result[i] = positions.get(pos).getMi();
			
		}
		
		return result;
	}

	private static  Map<MI_Position, Integer> getOrderMap (List<MI_Position> positions) {
		
		Map<MI_Position, Integer> orderMap = new HashMap<> ();

		Integer counter = 1;
		for (MI_Position mi_Position : positions) {
			
			orderMap.put(mi_Position, counter);
			
			counter++;
		}
		
		return orderMap;
		
	}
	
	private static Map<Integer, Integer> getRawMIRank(List<MI_Position> positions, Map<MI_Position, Integer> orderMap) {
		
		Comparator<MI_Position> compareByRawMI = new Comparator<MI_Position>() {
			
			@Override
			public int compare(MI_Position o1, MI_Position o2) {
				
				double diff = (o1.getRaw_mi() - o2.getRaw_mi());
				
				if (diff>0) {
					return -1;
				} else if (diff==0) {
					return 0;
				}
				return  1 ;
				
			}
		};
		
		return getRawMIRank (positions, orderMap, compareByRawMI) ;
		
	}
	
	private static Map<Integer, Integer> getZRank(List<MI_Position> positions, Map<MI_Position, Integer> orderMap) {
		
		Comparator<MI_Position> compareByZ = new Comparator<MI_Position>() {
			
			@Override
			public int compare(MI_Position o1, MI_Position o2) {
				
				double diff = (o1.getMi() - o2.getMi());
				
				if (diff>0) {
					return -1;
				} else if (diff==0) {
					return 0;
				}
				return  1 ;
				
			}
		};
		
		return getRawMIRank (positions, orderMap, compareByZ) ;
		
	}
	
	private static Map<Integer, Integer> getRawMIRank (List<MI_Position> positions, Map<MI_Position, Integer> orderMap, Comparator<MI_Position> comp) {
		
		Collections.sort(positions, comp);
		
		Map<Integer, Integer> result = new HashMap<>(); 
				
		Integer counter = 1;
		
		for (MI_Position mi_Position : positions) {
			
			result.put(orderMap.get(mi_Position), counter);
			
			counter++;
			
		}
		
		return result;
		
	}

	private static List<MI_Position> removeNonCalculatedValues(List<MI_Position> position) {

		return getZscoreAbove (position, -900) ;
		
	}

	public static List<MI_Position> getZscoreAbove (List<MI_Position> positions, double cutoff) {
		
		List<MI_Position> result = new ArrayList<>();
		
		int counter = 0;
		
		for (MI_Position position : positions) {
			
			counter++;
			
			if (position.getMi() == null) {
				
				System.out.println(counter);
				
				System.out.println(position);
				
			}
			
			if (position.getMi() >= cutoff ) {
				
				result.add(position);
				
			}
			
		}
		
		return result;
		
	}

}
