package utils.mutualinformation.misticmod.top;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import utils.mutualinformation.misticmod.MI_Position;

public abstract class SortedMiFilter extends MiFilter {

	protected List<MI_Position> sortPositions(List<MI_Position> positions) {
		
		List<MI_Position> sorted = new ArrayList<MI_Position>();
		
		sorted.addAll(positions);
		
		Collections.sort(sorted, this.getComparator());
		
		return sorted;
	}

	
	protected Comparator<MI_Position> getComparator() {
		
		Comparator<MI_Position> comp = new Comparator<MI_Position>() {

			@Override
			public int compare(MI_Position o1, MI_Position o2) {
				if (o1.getMi()<o2.getMi()) return -1;
				if (o1.getMi()>o2.getMi()) return 1;
				return 0;
			}
		};
		
		return comp;
	}
	
}
