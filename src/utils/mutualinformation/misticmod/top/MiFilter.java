package utils.mutualinformation.misticmod.top;

import java.util.List;

import utils.mutualinformation.misticmod.MI_Position;

/**
 * Abstract class to represent MI Data filters.
 * 
 * @author javier iserte
 *
 */
public abstract class MiFilter {
	/////////////////////////////////////////////////////
	// Public Interface
	/**
	 * Filter given MI data. 
	 * 
	 * @param unwanted Manager that 'knows' what to do with positions that do 
	 * no pass the filter.
	 * @param positions List MI data positions to be filtered.
	 * @return a new list of MI_data positions.
	 */
	public abstract List<MI_Position> filter(UnwantedManager unwanted, List<MI_Position> positions);
	
	/**
	 * Gets a short tag name for the current filter.
	 * @return
	 */
	public abstract String getFormattedTagName();
	//////////////////////////////////////////////////////
}
