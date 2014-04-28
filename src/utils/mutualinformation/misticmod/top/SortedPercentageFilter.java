package utils.mutualinformation.misticmod.top;

import java.util.List;

import utils.mutualinformation.misticmod.MI_Position;

public class SortedPercentageFilter extends SortedMiFilter {

	//////////////////////////////////////////
	// Instance Variables
	private double topPercent;
	//////////////////////////////////////////

	//////////////////////////////////////////
	// Constructor
	public SortedPercentageFilter (double topPercent) {
		super();
		this.setTopPercent(topPercent);
	}
	//////////////////////////////////////////
	
	///////////////////////////////////////////
	// Protected Methods
	@Override
	protected List<MI_Position> getFilteredPositions(UnwantedManager unwanted, List<MI_Position> positions) {
		
		int topValues = (int) (this.getTopPercent() * positions.size() / 100);
		
		return new SortedValuesFilter(topValues).getFilteredPositions(unwanted, positions);
		
	}
	//////////////////////////////////////////
	
	//////////////////////////////////////////
	// getters and Setters
	protected double getTopPercent() {
		return topPercent;
	}

	protected void setTopPercent(double topPercent) {
		this.topPercent = topPercent;
	}
	///////////////////////////////////////////
}
