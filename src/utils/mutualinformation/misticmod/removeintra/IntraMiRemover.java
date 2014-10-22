package utils.mutualinformation.misticmod.removeintra;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import collections.rangemap.RangeMap;
import utils.mutualinformation.misticmod.datastructures.MI_Position;
import datatypes.range.Range;

public class IntraMiRemover {

	////////////////////////////////////////////////////////////////////////////
	// Public interface
	/**
	 * Removes intra proteins connection in MI data.
	 * The values are not actually removed, instead they are set to -999, what
	 * means that is undefined.
	 * 
	 * @param in
	 * @param lengths
	 * @throws IOException
	 */
	public void removeConnections(PrintStream out, BufferedReader in, List<Integer> lengths) throws IOException {
		
		String currentLine = null;
		
		RangeMap<Integer, Integer> proteinAssignationMap = this.createAssignationMap(lengths);
		
		MI_Position.activateMortemPrinter();
		
		while((currentLine = in.readLine())!= null) {
			
			MI_Position pair = MI_Position.valueOf(currentLine);

			removeIntra(proteinAssignationMap, pair);
			
			out.println(pair);
			
		}
		
	}

	/**
	 * Removes intra proteins connection in MI data.
	 * The values are not actually removed, instead they are set to -999, what
	 * means that is undefined.
	 * 
	 * @param in
	 * @param lengths
	 * @throws IOException
	 */
	public void removeConnections(List<MI_Position> in, List<Integer> lengths) throws IOException {
		
		RangeMap<Integer, Integer> proteinAssignationMap = this.createAssignationMap(lengths);
		
		for (MI_Position pair : in) {
			
			removeIntra(proteinAssignationMap, pair);
			
		}
		
	}
	////////////////////////////////////////////////////////////////////////////
	
	////////////////////////////////////////////////////////////////////////////
	// private methods
	private void removeIntra(RangeMap<Integer, Integer> proteinAssignationMap,
			MI_Position pair) {
		Integer proteinOfRes1 = proteinAssignationMap.get(pair.getPos1()); 
		Integer proteinOfRes2 = proteinAssignationMap.get(pair.getPos2());
		
		if (proteinOfRes1==proteinOfRes2) {
			
			pair.setMi(-999.99);
			
		}
	}

	private RangeMap<Integer, Integer> createAssignationMap(
			List<Integer> lengths) {
		RangeMap<Integer, Integer> proteinAssignationMap = new RangeMap<>();
		
		int accumulator = 0;
		int proteinCounter = 1;
		for (Integer integer : lengths) {
			
			int lowerBound = accumulator + 1;
			int upperBound = accumulator + integer;
			accumulator = upperBound;
			Range<Integer> keyRange = new Range<Integer>(lowerBound, upperBound, true, true);
			proteinAssignationMap.put(keyRange, proteinCounter);
			proteinCounter++;
		}
		
		return proteinAssignationMap;
	}
	////////////////////////////////////////////////////////////////////////////

}
