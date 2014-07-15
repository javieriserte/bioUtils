package seqManipulation.gapstripper;

import java.util.Map;

public abstract class AbstractProfiler {

	public abstract double[] calculateProfileWithoutClustering(Map<String, String> sequences);
	
	public abstract double[] calculateProfileUsingClustering(Map<String, String> sequences, double thresholdId);
	
}
