package utils.mutualinformation.misticmod.top;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import utils.mutualinformation.misticmod.datastructures.MI_Position;

/**
 * Abstract class for filters for MI Data that require sorting the values.
 * 
 * @author Javier Iserte
 */

public abstract class SortedMiFilter extends MiFilter {

	//////////////////////////////////////////
	// Protected Methods
	/**
	 * Sort MI positions by its MI value.
	 * @return
	 */
	protected List<MI_Position> sortPositions(List<MI_Position> positions) {
		
		List<MI_Position> sorted = new ArrayList<MI_Position>();
		
		sorted.addAll(positions);
		
		Collections.sort(sorted, this.getComparator());
		
		return sorted;
		
	}
	
	/**
	 * Creates a comparator for MI data positions
	 * @return
	 */
	protected Comparator<MI_Position> getComparator() {
		
		Comparator<MI_Position> comp = new Comparator<MI_Position>() {

			@Override
			public int compare(MI_Position o1, MI_Position o2) {
				if (o1.getMi()<o2.getMi()) return 1;
				if (o1.getMi()>o2.getMi()) return -1;
				if (o1.getPos1()>o2.getPos1()) return 1; 
				if (o1.getPos1()<o2.getPos1()) return -1;
				if (o1.getPos2()>o2.getPos2()) return 1;
				if (o1.getPos2()<o2.getPos2()) return -1;
				return 0;
			}
		};
		
		return comp;
	}
	// End of protected methods
	///////////////////////////////////////////////////
}
