package utils.mutualinformation.misticmod.top;

import java.util.ArrayList;
import java.util.List;

import utils.mutualinformation.misticmod.MI_Position;

public abstract class MiFilter {

	public List<MI_Position> filter(UnwantedManager unwanted, List<MI_Position> positions) {

		List<MI_Position> filtered = this.getFilteredPositions(unwanted, positions);
		
		List<MI_Position> kept = new ArrayList<MI_Position>();
		
		for (MI_Position mi_Position : filtered) {
			
			kept.add(unwanted.tryToKeep(mi_Position));
			
		}
		
		return kept;
		
	}

	protected abstract List<MI_Position> getFilteredPositions(UnwantedManager unwanted, List<MI_Position> positions);
	
}
