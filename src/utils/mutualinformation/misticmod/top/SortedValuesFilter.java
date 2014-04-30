package utils.mutualinformation.misticmod.top;

import java.util.ArrayList;
import java.util.List;

import utils.mutualinformation.misticmod.MI_Position;

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
		List<MI_Position> sortedPositions = this.sortPositions(positions);
		for (int i = sortedPositions.size()-1; i >= this.getTopValues();i--) {
			sortedPositions.remove(i);
		}
		
		List<MI_Position> resultList = new ArrayList<>();
		
		for (MI_Position miPosition : positions) {
			if (sortedPositions.contains(miPosition)) {
				resultList.add(miPosition);
			} else{
				MI_Position unwantedPosition = unwanted.tryToKeep(miPosition);
				if (unwantedPosition!=null) {
					resultList.add(unwantedPosition);
				}
			}
		}
		return resultList;
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
