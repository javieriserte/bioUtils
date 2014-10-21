package utils.mutualinformation.misticmod.onepixelmap;

import java.util.List;

import utils.mutualinformation.misticmod.datastructures.MI_PositionWithProtein;

/**
 * This class permits retrieve max residue number, min MI value and Max MI value
 * from MI data.
 * @author javier
 *
 */
public class MiDataAnalitics {

	/**
	 * Gets the higher number of any residue in MI data.
	 * @param positions
	 * @return
	 */
	public int getMaxResidueNumber(List<MI_PositionWithProtein> positions) {
		
		int max = Integer.MIN_VALUE;
		
		for (MI_PositionWithProtein mi_PositionWithProtein : positions) {
			
			max= Math.max(mi_PositionWithProtein.getPos1(), max);
			
			max= Math.max(mi_PositionWithProtein.getPos2(), max);
			
		}
		
		return max;
		
	}
	
	/**
	 * Gets the minimum MI value of all residue pairs.
	 * @param positions
	 * @return
	 */
	public double getMinValue(List<MI_PositionWithProtein> positions) {
		
		double min = Double.POSITIVE_INFINITY;

		for (MI_PositionWithProtein mi_PositionWithProtein : positions) {
			
			Double current_MI = mi_PositionWithProtein.getMi();
			
			if (current_MI>-900) {

				min = Math.min(min, current_MI);
				
			}
			
		}
		
		return min;
		
	}
	
	/**
	 * Gets the maximum MI value of all residue pairs.
	 * @param positions
	 * @return
	 */
	public double getMaxValue(List<MI_PositionWithProtein> positions) {
		
		double max = Double.NEGATIVE_INFINITY;

		for (MI_PositionWithProtein mi_PositionWithProtein : positions) {
			
			max = Math.max(max, mi_PositionWithProtein.getMi());
			
		}
		
		return max;
	}
	
}
