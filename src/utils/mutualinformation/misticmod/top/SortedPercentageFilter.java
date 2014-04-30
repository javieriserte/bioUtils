package utils.mutualinformation.misticmod.top;

import java.util.List;

import utils.mutualinformation.misticmod.MI_Position;
/**
 * Filter for MI Data.
 * Sort a list of MI Data Position by its MI value.
 * The nn positions in the highest nn-percentil pass the filter. 
 * 
 * @author Javier Iserte
 */
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
	// Public Interface
	@Override
	public List<MI_Position> filter(UnwantedManager unwanted, List<MI_Position> positions) {
		
		int topValues = (int) (this.getTopPercent() * positions.size() / 100);
		
		return new SortedValuesFilter(topValues).filter(unwanted, positions);
		
	}
	
	@Override
	public String getFormattedTagName() {
		return String.valueOf(this.getTopPercent() + "p");
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
	//////////////////////////////////////////

}
