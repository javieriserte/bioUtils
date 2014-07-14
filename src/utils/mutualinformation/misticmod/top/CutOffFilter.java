package utils.mutualinformation.misticmod.top;

import java.util.ArrayList;
import java.util.List;

import utils.mutualinformation.misticmod.datastructures.MI_Position;

/**
 * Filter for MI Data.
 * The MI Data positions whose MI is greater than a given cutoff value pass
 * the filter.
 * 
 * @author Javier Iserte
 */
public class CutOffFilter extends MiFilter {

	///////////////////////////////////////////////
	// Instance Variables
	private double cutoff;
	///////////////////////////////////////////////
	
	///////////////////////////////////////////////
	// Constructor
	public CutOffFilter(double cutoff) {
		super();
		this.setCutoff(cutoff);
	}
	///////////////////////////////////////////////
	
	///////////////////////////////////////////////
	// Public Interface 
	@Override
	public List<MI_Position> filter(UnwantedManager unwanted, List<MI_Position> positions) {
		
		List<MI_Position> resultList = new ArrayList<MI_Position>();

		for (MI_Position mi_Position : positions) {
			if (mi_Position.getMi()>=this.getCutoff()) {
				resultList.add(mi_Position);
			} else {
				MI_Position tryToKeep = unwanted.tryToKeep(mi_Position);
				if (tryToKeep != null) {
					resultList.add(tryToKeep);
				}
			}
		}
		
		return resultList;
	}
	
	@Override
	public String getFormattedTagName() {
		return String.valueOf(this.getCutoff())+ "c";
	}
	// 
	//////////////////////////////////////////////////
	
	//////////////////////////////////////////////////
	// Getters And Setters
	protected double getCutoff() {
		return cutoff;
	}
	protected void setCutoff(double cutoff) {
		this.cutoff = cutoff;
	}
	// End Of Getters And Setters
	//////////////////////////////////////////////////


}
