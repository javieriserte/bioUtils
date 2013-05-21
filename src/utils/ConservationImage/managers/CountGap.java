package utils.ConservationImage.managers;

import java.util.Set;

public class CountGap extends GapManager {

	@Override
	public Set<Character> attempToDeleteGaps(Set<Character> set) {

		set.remove('-');
		
		return set;
	}

	@Override
	public double attempToSumFreq(double freqSum, double d) {
		return freqSum + d;
	}


}
