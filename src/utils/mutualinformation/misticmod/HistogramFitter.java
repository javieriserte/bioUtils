package utils.mutualinformation.misticmod;

import java.io.PrintStream;

public abstract class HistogramFitter {
	
	public abstract void fit(double[] data_counts_log, double[] data_MI_ranges_min_values) ;
	
	public abstract void reportFittest(PrintStream out);

}
