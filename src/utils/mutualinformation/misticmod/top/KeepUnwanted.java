package utils.mutualinformation.misticmod.top;

import utils.mutualinformation.misticmod.MI_Position;

public class KeepUnwanted extends UnwantedManager {

	@Override
	public MI_Position tryToKeep(MI_Position currentPosition) {
		currentPosition.setMean_mi(-999d);
		currentPosition.setMi(-999d);
		currentPosition.setRaw_mi(-999d);
		currentPosition.setSd_mi(-999d);
		return currentPosition;
	}

}
