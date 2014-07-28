package utils.mutualinformation.misticmod.cmicircos;

import java.util.List;

import utils.mutualinformation.misticmod.datastructures.MI_PositionWithProtein;

/**
 * Normalize the sum of MI values among two proteins with only positive pairs.
 * Discarding all pairs with MI = -999.99 marked by others programs.
 * 
 * @author javier iserte
 *
 */
public class NormalizeWithPositives extends Normalizer {

	////////////////////////////////////////////////////////////////////////////
	// Private Instance Variables
	private Double[][] normalize; // Normalize counts the number of links between every pair of proteins 
	////////////////////////////////////////////////////////////////////////////
	
	////////////////////////////////////////////////////////////////////////////
	// Constructor
	public NormalizeWithPositives(int length, List<MI_PositionWithProtein> data, Integer[] lengths) {
		
		////////////////////////////////////////////////////////////////////////
		// Initialize internal state
		normalize = new Double[length][length];
		
		for (int i =0 ; i < length ;i ++) {
			
			for (int j=0; j < length ;j++) {
				
				normalize[i][j] = 0d;
				
			}
			
		}
		////////////////////////////////////////////////////////////////////////
		
		////////////////////////////////////////////////////////////////////////
		// Counts links with MI values > 0 
		/// (usually MI>0 implies MI>6.5 due to previous filters)
		for (MI_PositionWithProtein pos : data) {
			
			double nv = pos.getMi()>0?1:0;
			
			normalize[pos.getProtein_1()][pos.getProtein_2()] = normalize[pos.getProtein_1()][pos.getProtein_2()] + nv ;
			
			normalize[pos.getProtein_2()][pos.getProtein_1()]= normalize[pos.getProtein_2()][pos.getProtein_1()] + nv;
			
		}
		////////////////////////////////////////////////////////////////////////
		
	}
	// End of Constructor
	////////////////////////////////////////////////////////////////////////////

	////////////////////////////////////////////////////////////////////////////
	// Public Interface	
	@Override
	public double denominator(int row, int column) {
		
		return normalize[row][column];
		
	}
	////////////////////////////////////////////////////////////////////////////
	
}