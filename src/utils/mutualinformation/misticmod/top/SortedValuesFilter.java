package utils.mutualinformation.misticmod.top;

import java.util.List;

import utils.mutualinformation.misticmod.MI_Position;

public class SortedValuesFilter extends SortedMiFilter {

	private int topValues;

	//////////////////////////////////////////
	// Constructor
	public SortedValuesFilter (int topValues) {
		super();
		this.setTopValues(topValues);
	}
	//////////////////////////////////////////

	
	@Override
	protected List<MI_Position> getFilteredPositions(UnwantedManager unwanted, List<MI_Position> positions) {
		List<MI_Position> sortedPositions = this.sortPositions(positions);
		for (int i = sortedPositions.size()-1; i>=0;i--) {
			sortedPositions.remove(i);
		}
		
		
		return null;
	}

	protected int getTopValues() {
		return topValues;
	}


	protected void setTopValues(int topValues) {
		this.topValues = topValues;
	}
	
}
