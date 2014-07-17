package seqManipulation.gapstripper.tests;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import seqManipulation.gapstripper.ClusteringWeighter;
import seqManipulation.gapstripper.EntropyProfiler;
import seqManipulation.gapstripper.UniformWeighter;

public class EntropyProfilerTest {

	@Test
	public void testCalculateProfile() {
		EntropyProfiler profiler  = new EntropyProfiler();
		
		Map<String,String> sequences = new HashMap<String, String>();
		
		sequences.put("s1", "ACTGA");
		sequences.put("s2", "ACTGG");
		sequences.put("s3", "ACTAT");
		sequences.put("s4", "ACGRN");
		sequences.put("s5", "ARGTF");
		
		
		double[] profile = profiler.calculateProfile(sequences,new UniformWeighter());
		
		assertArrayEquals(new double[]{0, 0.167038, 0.224653, 0.444699, 0.537231}, profile, 0.001d);

		sequences.clear();
		
		sequences.put("s1", "ACTG-");
		sequences.put("s2", "ACT-G");
		sequences.put("s3", "AC-AT");
		sequences.put("s4", "A-GRN");
		sequences.put("s5", "-RGTF");
		
		
		profile = profiler.calculateProfile(sequences,new UniformWeighter());
		
		assertArrayEquals(new double[]{0, 0.1877, 0.231362, 0.462758, 0.462758}, profile, 0.001d);

			
		//cluster 1 (weight = 1/2)
		sequences.put("s1", "ACTGAACTGAACTGA");
		sequences.put("s2", "ACTGAACTGAACTGG");

		//cluster 2	(weight = 1/3)	
		sequences.put("s3", "ACTGACTGAGACTAT");
		sequences.put("s4", "ACTGACTGAGACGRN");
		sequences.put("s5", "ACTGACTGAGARGTF");
		
		
		profile = profiler.calculateProfile(sequences, new ClusteringWeighter(sequences, 0.62 ));
		
		assertArrayEquals(new double[]{ 0, 0, 0, 0, 0, 
				0.231362, 0.231362, 0.231362, 0.231362, 0.231362, 
				0, 0.150414, 0.212469, 0.414757, 0.530421}, profile, 0.001d);
		
		sequences.clear();
		
		//cluster 1 (weight = 1/2)
		sequences.put("s1", "ACTGAACTGAACTGA");
		sequences.put("s2", "ACTGAACTGAACTGG");

		//cluster 2	(weight = 1/3)	
		sequences.put("s3", "ACTGACTGAG-----");
		sequences.put("s4", "ACTGACTGAGACGRN");
		sequences.put("s5", "ACTGACTGAGARGTF");
		
		
		profile = profiler.calculateProfile(sequences, new ClusteringWeighter(sequences, 0.666 ));
		
		assertArrayEquals(new double[]{ 0, 0, 0, 0, 0, 
				0.231362, 0.231362, 0.231362, 0.231362, 0.231362, 
				0, 0.167038, 0.224653, 0.317218, 0.456049}, profile, 0.001d);
	}

}
