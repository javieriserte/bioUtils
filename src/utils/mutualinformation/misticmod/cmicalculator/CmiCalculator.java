package utils.mutualinformation.misticmod.cmicalculator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import utils.mutualinformation.misticmod.datastructures.MI_Position;
import utils.mutualinformation.misticmod.top.MiFilter;
import utils.mutualinformation.misticmod.top.RemoveUnwanted;

/**
 * Computes Cumulative MI for each protein residue using the MI output from 
 * MISTIC or MN soft MI calculation.
 * 
 * @author javier
 *
 */
public class CmiCalculator {
	////////////////////////////////////////////////////////////////////////////
	// Private class Constants
	public static final double MISTIC_Z_SCORE_CUTOFF = 6.5;
	public static final double NEGATIVE_INFINITY = -900;
	////////////////////////////////////////////////////////////////////////////

	////////////////////////////////////////////////////////////////////////////
	// Public interface
	/**
	 * Accumulate MI values for each position
	 * @param positions
	 * @param cutoff
	 * @return
	 */
	public Map<Integer, Double> getCmiValues(List<MI_Position> positions, double cutoff) {
		Map<Integer, Double> cmi_values = new HashMap<>();
		
		for (MI_Position position : positions) {
			
			if (position.getMi()>cutoff) {
				
				add_cmi_value(cmi_values, position.getPos1(), position.getMi()); 
			
				add_cmi_value(cmi_values, position.getPos2(), position.getMi());
			}
			
		}
		return cmi_values;
	}
	
	/**
	 * Accumulates MI values for each position from pairs that passes a given 
	 * filter
	 * @param positions
	 * @param filter
	 * @return
	 */
	public Map<Integer,Double> getCmiValues(List<MI_Position> positions, MiFilter filter) {
		
		Set<Integer> allResidueNumbers = this.getAllResidueNumbers(positions);
		
		Map<Integer, Double> cmiValues = this.CreateEmptyCmiValuesMap(allResidueNumbers);
		
		List<MI_Position> filteredPositions = filter.filter(new RemoveUnwanted(), positions);
		
		for (MI_Position position : filteredPositions) {
			
			add_cmi_value(cmiValues, position.getPos1(), position.getMi()); 
			
			add_cmi_value(cmiValues, position.getPos2(), position.getMi());
			
		}
		return cmiValues;
		
	}
	////////////////////////////////////////////////////////////////////////////	



	private Map<Integer, Double> CreateEmptyCmiValuesMap(Set<Integer> allResidueNumbers) {
		Map<Integer, Double> cmiValues = new HashMap<>();
		for (Integer integer : allResidueNumbers) {
			cmiValues.put(integer, 0d);
		}
		return cmiValues;
	}

	////////////////////////////////////////////////////////////////////////////
	// Private methods
	private void add_cmi_value(Map<Integer, Double> cmi_values,
			Integer positionNumber, Double mi) {

		double old_value = 0;

		if (cmi_values.containsKey(positionNumber)) {

			old_value = cmi_values.get(positionNumber);

		}

		cmi_values.put (positionNumber, old_value + mi);
		
	}
	
	private Set<Integer> getAllResidueNumbers(List<MI_Position> positions) {
		Set<Integer> result = new HashSet<Integer>();
		for (MI_Position mi_Position : positions) {
			result.add(mi_Position.getPos1());
			result.add(mi_Position.getPos2());
		}
		return result;
		
	}
	////////////////////////////////////////////////////////////////////////////

}
