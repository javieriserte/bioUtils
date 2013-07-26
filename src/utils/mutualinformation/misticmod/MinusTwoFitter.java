package utils.mutualinformation.misticmod;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class MinusTwoFitter extends HistogramFitter {

	List<Double> cut_offs;
	int max_range;
	
	@Override
	public void fit(double[] data_counts_log, double[] data_MI_ranges_min_values) {
		
		this.cut_offs = new ArrayList<>();
		

		this.max_range = this.getMaxRange(data_counts_log);
		
		List<Integer> result = new ArrayList<>();
		
		for (int i=max_range+2;i<data_counts_log.length;i++) {
			
			if (data_counts_log[i]>data_counts_log[i-1] && data_counts_log[i]> data_counts_log[i-2]) {
				
				result.add(i);
				
				break;
				
			}
			
		}
		
		for (Integer integer : result) {
			
			this.cut_offs.add(data_MI_ranges_min_values[integer]);	
			
		}		
		
		 
	}

	@Override
	public void reportFittest(PrintStream out) {
		
		for (Double cut_off_value: this.cut_offs) {
			
			out.println("# Cut off value : "+cut_off_value);
			
		}
		

	}
	
	private int getMaxRange(double[] data_counts_log) {
		
		double max = Double.NEGATIVE_INFINITY;
		
		int max_range = 0;
		
		for (int i = 0; i < data_counts_log.length; i++) {
			
			if (data_counts_log[i]> max) {
				
				max = data_counts_log[i];
				
				max_range = i;
				
			}
			
		}
		
		return max_range;
		
	}



}
