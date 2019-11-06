package pdb;

import io.onelinelister.OneLineListReader;
import io.onelinelister.LineParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cmdGA2.CommandLine;
import cmdGA2.OptionsFactory;
import cmdGA2.SingleArgumentOption;
import cmdGA2.returnvalues.InfileValue;
import pair.Pair;
import org.jiserte.biopdb.structures.SpacePoint;
import org.jiserte.biopdb.structures.SpacePointLineParser;

public class ResidueDistanceCalculator {
	
	/**
	 * Calculates the minimum distance of any two atoms between two residues.
	 * 
	 * @param firstResidue is the list of atoms of the first residue
	 * @param secondResidue is the list of atoms of the second residue
	 * @return a real number
	 */
	public double minDistance(List<SpacePoint> firstResidue, List<SpacePoint> secondResidue) {
		
		double min = Double.POSITIVE_INFINITY;
		
		for (SpacePoint spacePoint_1 : firstResidue) {
			
			for (SpacePoint spacePoint_2 : secondResidue) {
				
				min = Math.min(spacePoint_1.distanceTo(spacePoint_2),min);
				
			}
			
		}
		
		return min;
	}
	
	/**
	 * Calculates the minimum distance of any two atoms between two residues and returns 
	 * those atoms.
	 * 
	 * @param firstResidue is the list of atoms of the first residue
	 * @param secondResidue is the list of atoms of the second residue
	 * @return a real number
	 */
	public Pair<Double,Pair<Integer,Integer>> minDistanceAtoms(List<SpacePoint> firstResidue, List<SpacePoint> secondResidue) {
		
		double min = Double.POSITIVE_INFINITY;
		int min_atom1 = 0;
		int min_atom2 = 0;
		
		for (SpacePoint spacePoint_1 : firstResidue) {
			
			for (SpacePoint spacePoint_2 : secondResidue) {
				
				double distanceTo = spacePoint_1.distanceTo(spacePoint_2);
				
				if (min>distanceTo) {
				
					min = distanceTo;
					
					min_atom1 = spacePoint_1.getAtomSerialNumber();
					
					min_atom2 = spacePoint_2.getAtomSerialNumber();
				
				}
				
			}
			
		}
		
		return new Pair<Double, Pair<Integer,Integer>>(min, new Pair<>(min_atom1, min_atom2));
		
	}
	
	/**
	 * Calculates the minimum distance of any two atoms between two residues and returns 
	 * those atoms. Also checks that at least one of the atom belongs to a given chain.
	 * 
	 * @param firstResidue is the list of atoms of the first residue
	 * @param secondResidue is the list of atoms of the second residue
	 * @return a real number
	 */
	public Pair<Double,Pair<Integer,Integer>> minDistanceAtomsWithReferenceChain(List<SpacePoint> firstResidue, List<SpacePoint> secondResidue, char referenceChain) {
		
		double min = Double.POSITIVE_INFINITY;
		int min_atom1 = 0;
		int min_atom2 = 0;
		
		for (SpacePoint spacePoint_1 : firstResidue) {
			
			for (SpacePoint spacePoint_2 : secondResidue) {
				
				double distanceTo = spacePoint_1.distanceTo(spacePoint_2);
				
				if (min>distanceTo && (spacePoint_1.getChainIdentifier()==referenceChain || spacePoint_2.getChainIdentifier()==referenceChain )) {
				
					min = distanceTo;
					
					min_atom1 = spacePoint_1.getAtomSerialNumber();
					
					min_atom2 = spacePoint_2.getAtomSerialNumber();
				
				}
				
			}
			
		}
		
		return new Pair<Double, Pair<Integer,Integer>>(min, new Pair<>(min_atom1, min_atom2));
		
	}
	
	////////////////////////////////////////
	// Main Executable
	/**
	 * Read a PDB file and a list of residue pairs and returns the minimum distance between
	 * any two atoms of each pair.
	 * The residue list must have this format:
	 * <pre>
	 * chain_1 residue_number_1 chain_2 residue_number_2
	 * the spacers are expected to be tabs.
	 * example:
	 * A	1	B	1
	 * A	2	B	189
	 * </pre>
	 * @param args
	 */
	public static void main(String[]  args) {
		
		///////////////////////////////
		// Create command cine object
		CommandLine commandLine = new CommandLine();
		///////////////////////////////
		
		///////////////////////////////
		// Add Options to the command line
		SingleArgumentOption<InputStream> inOpt  = OptionsFactory.createBasicInputStreamArgument(commandLine);
		SingleArgumentOption<PrintStream> outOpt = OptionsFactory.createBasicPrintStreamArgument(commandLine);
		SingleArgumentOption<File>        pdbOpt = new SingleArgumentOption<File>(commandLine, "-pdb", new InfileValue(), null);
		///////////////////////////////
		
		///////////////////////////////
		// Parse command line arguments
		commandLine.readAndExitOnError(args);
		///////////////////////////////
		
		///////////////////////////////
		// Get values from command line
		if (!pdbOpt.isPresent()) {
			System.err.println("-pdb option is mandatory.");
			System.exit(1);
		}
		File pdbFile = pdbOpt.getValue();
		BufferedReader in = new BufferedReader(new InputStreamReader(inOpt.getValue()));
		PrintStream out = outOpt.getValue();
		///////////////////////////////
		

		try {
			///////////////////////////////
			// Read input pdb file
			List<SpacePoint> pdb = new OneLineListReader<SpacePoint>(new SpacePointLineParser()).read(pdbFile);
			List<Pair<Pair<Character,Integer>,Pair<Character,Integer>>> pairs;
			pairs = new OneLineListReader<Pair<Pair<Character,Integer>,Pair<Character,Integer>>>(new ResiduePairSpecificationParser()).read(in);
			
			
			ResidueDistanceCalculator distanceCalculator= new ResidueDistanceCalculator(); 

			Map<Character, List<SpacePoint>> atomByChainMap = SpacePoint.separateByChainIdentifier(pdb);
			
			Map<Character,Map<Integer,List<SpacePoint>>> residuesByChainMap = new HashMap<Character, Map<Integer,List<SpacePoint>>>();
			
			for (Character chain : atomByChainMap.keySet()) {
				
				Map<Integer, List<SpacePoint>> residues = SpacePoint.separateByResidueNumber(atomByChainMap.get(chain));
				
				residuesByChainMap.put(chain, residues);
				
			}
			
			for (Pair<Pair<Character, Integer>, Pair<Character, Integer>> pair : pairs) {
				
				List<SpacePoint> res_1 = residuesByChainMap.get(pair.getFirst().getFirst()).get(pair.getFirst().getSecond());
				List<SpacePoint> res_2 = residuesByChainMap.get(pair.getSecond().getFirst()).get(pair.getSecond().getSecond());
				
				double distance = distanceCalculator.minDistance(res_1, res_2);
				
				out.println(pair.getFirst().getFirst()  + ":"+pair.getFirst().getSecond()  + " - " + 
				            pair.getSecond().getFirst() + ":"+pair.getSecond().getSecond() + " = " + distance);
				
			}
			
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static class ResiduePairSpecificationParser implements LineParser<Pair<Pair<Character,Integer>,Pair<Character,Integer>>> {

		@Override
		public Pair<Pair<Character,Integer>,Pair<Character,Integer>> parse(String line) {
			
			String[] fields = line.split("\t");
			
			if (fields.length==4) {
				Pair<Character, Integer> res_1 = new Pair<Character, Integer>(fields[0].charAt(0), Integer.valueOf(fields[1]));
				Pair<Character, Integer> res_2 = new Pair<Character, Integer>(fields[2].charAt(0), Integer.valueOf(fields[3]));
				
				return new Pair<Pair<Character,Integer>, Pair<Character,Integer>>(res_1, res_2);
				
			}
			return null;
			
		}
		
	}
	////////////////////////////////////////
	
}
