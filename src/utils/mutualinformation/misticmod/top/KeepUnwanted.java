package utils.mutualinformation.misticmod.top;

import utils.mutualinformation.misticmod.datastructures.MI_Position;

public class KeepUnwanted extends UnwantedManager {

	@Override
	public MI_Position tryToKeep(MI_Position currentPosition) {
		MI_Position newPosition = new MI_Position(currentPosition.getPos1(),
				currentPosition.getPos2(), 
				currentPosition.getAa1(), 
				currentPosition.getAa2(),
				-999d);
		newPosition.setMean_mi(-999d);
		newPosition.setMi(-999d);
		newPosition.setRaw_mi(-999d);
		newPosition.setSd_mi(-999d);
		return newPosition;
	}

}
