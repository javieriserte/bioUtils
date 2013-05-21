package utils.ConservationImage.managers;

import java.util.Set;

public class NoCountGap extends GapManager {

	@Override
	public Set<Character> attempToDeleteGaps(Set<Character> set) {
		
		return set;
		
	}

	@Override
	public double attempToSumFreq(double freqSum, double d) {
		return 1;
	}

}
