package utils.mutualinformation.misticmod.top;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import utils.mutualinformation.misticmod.datastructures.MI_Position;

/**
 * Filter for MI Data.
 * Sort a list of MI Data Position by its MI value.
 * The nn positions with the highest MI pass the filter. 
 * 
 * @author Javier Iserte
 */
public class SortedValuesFilter extends SortedMiFilter {

	private int topValues;

	//////////////////////////////////////////
	// Constructor
	public SortedValuesFilter (int topValues) {
		super();
		this.setTopValues(topValues);
	}
	//////////////////////////////////////////
	
	/////////////////////////////////////////
	// Public Interface
	@Override
	public List<MI_Position> filter(UnwantedManager unwanted, List<MI_Position> positions) {
		
		////////////////////////////////////////////////////////////////////////
		// New implementation.
		// Instead of sorting every element, creates a sorted set for the
		// top nn elements.
		int numberOfTopValues = this.getTopValues();
		
		SortedSet<MI_Position> tops = new TreeSet<MI_Position>(this.getComparator());
		
		for (MI_Position mi_Position : positions) {
			
			if (tops.size() == numberOfTopValues) {

				MI_Position lower = tops.last();
				
				if (this.getComparator().compare(mi_Position, lower)<0 ) {
					// If mi_Position is greater than lower
					// Remove the lower value and then add the new one.
					tops.remove(lower);
					
					tops.add(mi_Position);
					
				}
				
			} else {
				
				tops.add(mi_Position);
				
			}
			
		}

		List<MI_Position> resultList = new ArrayList<>();
		
		for (MI_Position miPosition : positions) {
			if (tops.contains(miPosition)) {
				resultList.add(miPosition);
			} else{
				MI_Position unwantedPosition = unwanted.tryToKeep(miPosition);
				if (unwantedPosition!=null) {
					resultList.add(unwantedPosition);
				}
			}
		}
		return resultList;
		// End of new implementation
		////////////////////////////////////////////////////////////////////////
		
		////////////////////////////////////////////////////////////////////////
		// Old implementation
		// Sorts all values and the pick the top nn elements.
//      List<MI_Position> sortedPositions = this.sortPositions(positions);
//
//		for (int i = sortedPositions.size()-1; i >= this.getTopValues();i--) {
//			sortedPositions.remove(i);
//		}
//		
//		List<MI_Position> resultList = new ArrayList<>();
//		
//		for (MI_Position miPosition : positions) {
//			if (sortedPositions.contains(miPosition)) {
//				resultList.add(miPosition);
//			} else{
//				MI_Position unwantedPosition = unwanted.tryToKeep(miPosition);
//				if (unwantedPosition!=null) {
//					resultList.add(unwantedPosition);
//				}
//			}
//		}
//		return resultList;
		// End of old implementation
		////////////////////////////////////////////////////////////////////////
	}
	
	@Override
	public String getFormattedTagName() {
		return String.valueOf(this.getTopValues()) + "v";
	}
	/////////////////////////////////////////

	//////////////////////////////////////////////////
	// Getters And Setters
	protected int getTopValues() {
		return topValues;
	}

	protected void setTopValues(int topValues) {
		this.topValues = topValues;
	}
	// End Of Getters And Setters
	//////////////////////////////////////////////////


}
