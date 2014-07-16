package seqManipulation.gapstripper.tests;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import seqManipulation.gapstripper.MaximumFrequencyProfiler;

public class MaximumFrequencyProfilerTest {

	
	
	@Test
	public void testCalculateProfileWithoutClustering() {
		
		MaximumFrequencyProfiler profiler  = new MaximumFrequencyProfiler();
		
		Map<String,String> sequences = new HashMap<String, String>();
		
		sequences.put("s1", "ACTGA");
		sequences.put("s2", "ACTGG");
		sequences.put("s3", "ACTAT");
		sequences.put("s4", "ACGRN");
		sequences.put("s5", "ARGTF");
		
		
		double[] profile = profiler.calculateProfileWithoutClustering(sequences);
		
		assertArrayEquals(new double[]{1.0,0.8,0.6,0.4,0.2}, profile, 0.001d);

		sequences.clear();
		
		sequences.put("s1", "ACTG-");
		sequences.put("s2", "ACT-G");
		sequences.put("s3", "AC-AT");
		sequences.put("s4", "A-GRN");
		sequences.put("s5", "-RGTF");
		
		
		profile = profiler.calculateProfileWithoutClustering(sequences);
		
		assertArrayEquals(new double[]{1.0,0.75,0.5,0.25,0.25}, profile, 0.001d);

		
	}

	@Test
	public void testCalculateProfileUsingClustering() {

		MaximumFrequencyProfiler profiler  = new MaximumFrequencyProfiler();
		
		Map<String,String> sequences = new HashMap<String, String>();
		
		//cluster 1 (weight = 1/2)
		sequences.put("s1", "ACTGAACTGAACTGA");
		sequences.put("s2", "ACTGAACTGAACTGG");

		//cluster 2	(weight = 1/3)	
		sequences.put("s3", "ACTGACTGAGACTAT");
		sequences.put("s4", "ACTGACTGAGACGRN");
		sequences.put("s5", "ACTGACTGAGARGTF");
		
		
		double[] profile = profiler.calculateProfileUsingClustering(sequences, 0.666 );
		
		assertArrayEquals(new double[]{ 1.0,1.0,1.0,1.0,1.0,
				                         0.5,0.5,0.5,0.5,0.5,
				                         1.0,0.83333,0.66666,0.5,0.25}, profile, 0.001d);

		// Max freq by column
		// 1 to 5 : (1/2 + 1/2 + 1/3 + 1/3 + 1/3) / (1/2 + 1/2 + 1/3 + 1/3 + 1/3) = 1 
		// 5 to 10: (1/2 + 1/2) / (1/2 + 1/2 + 1/3 + 1/3 + 1/3) = 1/2 
		//          (1/3 + 1/3 + 1/3) / (1/2 + 1/2 + 1/3 + 1/3 + 1/3) = 1/2
		// 11     : A : (1/2 + 1/2 + 1/3 + 1/3+ 1/3) / (1/2 + 1/2 + 1/3 + 1/3 + 1/3) = 1
		// 12     : C : (1/2 + 1/2 + 1/3 + 1/3) / (1/2 + 1/2 + 1/3 + 1/3 + 1/3) =  0.86666..
		// 13     : T : (1/2 + 1/2 + 1/3 ) / (1/2 + 1/2 + 1/3 + 1/3 + 1/3) =  0.66666..
		// 14     : G : (1/2 + 1/2 ) / (1/2 + 1/2 + 1/3 + 1/3 + 1/3) =  0.5
		// 15     : A o G  : (1/2) / (1/2 + 1/2 + 1/3 + 1/3 + 1/3) =  0.25		
		
		
		
		sequences.clear();
		
		//cluster 1 (weight = 1/2)
		sequences.put("s1", "ACTGAACTGAACTGA");
		sequences.put("s2", "ACTGAACTGAACTGG");

		//cluster 2	(weight = 1/3)	
		sequences.put("s3", "ACTGACTGAG-----");
		sequences.put("s4", "ACTGACTGAGACGRN");
		sequences.put("s5", "ACTGACTGAGARGTF");
		
		
		profile = profiler.calculateProfileUsingClustering(sequences, 0.666 );
		
		assertArrayEquals(new double[]{ 1.0,1.0,1.0,1.0,1.0,
				                         0.5,0.5,0.5,0.5,0.5,
				                         1.0,0.8,0.6,0.6,0.3}, profile, 0.001d);
		
		// Max freq by column
		// 1 to 5 : (1/2 + 1/2 + 1/3 + 1/3 + 1/3) / (1/2 + 1/2 + 1/3 + 1/3 + 1/3) = 1 
		// 5 to 10: (1/2 + 1/2) / (1/2 + 1/2 + 1/3 + 1/3 + 1/3) = 1/2 
		//          (1/3 + 1/3 + 1/3) / (1/2 + 1/2 + 1/3 + 1/3 + 1/3) = 1/2
		// 11     : A : (1/2 + 1/2 + 1/3 + 1/3) / (1/2 + 1/2 + 1/3 + 1/3) = 1
		// 12     : C : (1/2 + 1/2 + 1/3 ) / (1/2 + 1/2 + 1/3 + 1/3) =  0.8
		// 13     : T : (1/2 + 1/2 ) / (1/2 + 1/2 + 1/3 + 1/3) = 0.6  
		// 14     : G : (1/2 + 1/2 ) / (1/2 + 1/2 + 1/3 + 1/3)  =  0.6
		// 15     : A o G  : (1/2) / (1/2 + 1/2 + 1/3 + 1/3) =  0.3	
		
	}

}
