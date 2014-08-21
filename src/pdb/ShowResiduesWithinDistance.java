package pdb;

import io.onelinelister.OneLineListReader;
import io.onelinelister.OneLineListReader.LineParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pair.Pair;
import pdb.structures.SpacePoint;
import pdb.structures.SpacePointLineParser;
import cmdGA.NoOption;
import cmdGA.Parser;
import cmdGA.SingleOption;
import cmdGA.exceptions.IncorrectParameterTypeException;
import cmdGA.parameterType.DoubleParameter;
import cmdGA.parameterType.InFileParameter;
import cmdGA.parameterType.InputStreamParameter;
import cmdGA.parameterType.PrintStreamParameter;
import cmdGA.parameterType.StringParameter;

public class ShowResiduesWithinDistance {

	public static void main(String[] args) {
		
		/////////////////////////////
		// Create Command Line Parser
		Parser parser = new Parser();
		
		/////////////////////////////
		// Define Command Line Options
		SingleOption inPDBOpt = new SingleOption(parser, System.in, "-pdb", InputStreamParameter.getParameter());
		
		SingleOption outOpt = new SingleOption(parser, System.out, "-out", PrintStreamParameter.getParameter());
		
		SingleOption distOpt = new SingleOption(parser, 5.0d, "-dist", DoubleParameter.getParameter());
		
		SingleOption pairsOpt = new SingleOption(parser, null, "-pairs", InFileParameter.getParameter());
		
		NoOption exportAtomsOpt = new NoOption(parser, "-exportAtoms");
		
		SingleOption chainOpt  = new SingleOption(parser, "A", "-chain", StringParameter.getParameter());
		
		try {
			//////////////////////////
			// Parse Command Line
			parser.parseEx(args);
			
			//////////////////////////
			// Get Values from command line
			
			BufferedReader in = new BufferedReader(new InputStreamReader((InputStream) inPDBOpt.getValue()));
			
			PrintStream out = (PrintStream) outOpt.getValue();
			
			double dist = (Double) distOpt.getValue();
			
			File pairsFile = (File) pairsOpt.getValue();

			///////////////////////////
			// Checks That Is Not Null
			if (pairsFile==null) {
				
				System.err.println("-pairs option must be present and a valid file");
				
				System.exit(1);
				
			}
			
			//////////////////////////
			// Read Input PDB file
			List<SpacePoint> pdb = new OneLineListReader<SpacePoint>(new SpacePointLineParser()).read(in);
			
			Map<Integer,List<SpacePoint>> residues = separateByResidueNumber(pdb);
			
			////////////////////////////
			// Read Pairs to be compared
			List<Integer[]> pairs = readPairsFile(pairsFile);
			
			////////////////////////////
			// Calculate Distances
			ResidueDistanceCalculator resCalculator = new ResidueDistanceCalculator();
			
			for (Integer[] integers : pairs) {

				if (integers!=null) {
					
					Pair<Double, Pair<Integer, Integer>> r = null;
					
					if (chainOpt.isPresent()) {
					
						r = resCalculator.minDistanceAtomsWithReferenceChain(residues.get(integers[0]), residues.get(integers[1]), ((String)chainOpt.getValue()).charAt(0));
						
					} else {
						
						r = resCalculator.minDistanceAtoms(residues.get(integers[0]), residues.get(integers[1]));
						
					}
					
					double d = r.getFirst(); 
							
					if (d<= dist) {
						
						if (exportAtomsOpt.isPresent()) {
							
							out.println(r.getSecond().getFirst()+  "\t" + r.getSecond().getSecond());
							
						} else {
						
							out.println(integers[0] + "\t" + integers[1] + "\t" + d);
						
						}
						
					}
					
				}
				
			}
			
		} catch (IncorrectParameterTypeException | IOException e) {

			System.err.println("There was an error: "+ e.getMessage());
			
			System.exit(1);
			
		}
		
		
	}

	private static List<Integer[]> readPairsFile(File pairsFile) {
		
		LineParser<Integer[]> pairLineParser = new LineParser<Integer[]>() {

			@Override
			public Integer[] parse(String line) {
				
				String[] data = line.split("\t");
				
				if (!data[0].trim().equals("-") && !data[1].trim().equals("-")) {
				
					Integer res_1 = Integer.valueOf(data[0]);
				
					Integer res_2 = Integer.valueOf(data[1]);
				
					return new Integer[]{res_1,res_2};
				
				} else {
					
					return null;
					
				}
				
			}
			
		};
		
		return new OneLineListReader<Integer[]>(pairLineParser).read(pairsFile);
		
	}

	private static Map<Integer,List<SpacePoint>> separateByResidueNumber(List<SpacePoint> pdb) {

		Map<Integer, List<SpacePoint>> results = new HashMap<>();
		
		for (SpacePoint point : pdb) {
			
			Integer resNumber = point.getResidueSequenceNumber();
			
			if (!results.containsKey(resNumber)) {
				
				results.put(resNumber, new ArrayList<SpacePoint>());
	
			}
			
			results.get(resNumber).add(point);
			
		}
		
		return results;
		
	}
	
}
