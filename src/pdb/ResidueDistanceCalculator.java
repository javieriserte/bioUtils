package pdb;

import java.util.List;

import pair.Pair;

public class ResidueDistanceCalculator {
	
	public double minDistance(List<SpacePoint> firstResidue, List<SpacePoint> secondResidue) {
		
		double min = Double.POSITIVE_INFINITY;
		
		for (SpacePoint spacePoint_1 : firstResidue) {
			
			for (SpacePoint spacePoint_2 : secondResidue) {
				
				min = Math.min(spacePoint_1.distanceTo(spacePoint_2),min);
				
			}
			
		}
		
		return min;
	}
	
	
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
	
}
