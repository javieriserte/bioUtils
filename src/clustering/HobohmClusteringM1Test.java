package clustering;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import pair.Pair;

public class HobohmClusteringM1Test extends HobohmClusteringM1 {

	@Test
	public void testClusterize() {
		
		HobohmClusteringM1 clusterer = new HobohmClusteringM1();
		
		List<Pair<String, String>> sequences = new ArrayList<>();
		
		sequences.add(new Pair<String, String>("s1", "ACTGACTGACHHHHHHHHHH"));
		sequences.add(new Pair<String, String>("s2", "ACTGACTGACHHHHHAAAAA"));
		sequences.add(new Pair<String, String>("s3", "ACTGACTGACBBBBBBBBBB"));
		sequences.add(new Pair<String, String>("s4", "ACTGACTGACBBBBBCCCCC"));
		
		double identityThreshold = 0.75;
		
		Set<List<Pair<String, String>>> result = clusterer.clusterize(sequences, identityThreshold);
		
		assertEquals(2, result.size());
		
		for (List<Pair<String, String>> list : result) {
		
			assertEquals(2 , list.size());
			// There are two clusters of size 2
						
		}
		
		identityThreshold = 0.80;
		
		result = clusterer.clusterize(sequences, identityThreshold);
		
		assertEquals(4, result.size());
		
		for (List<Pair<String, String>> list : result) {
		
			assertEquals(1 , list.size());
			// There are four clusters of size 1
						
		}
		

		identityThreshold = 0.50;
		
		result = clusterer.clusterize(sequences, identityThreshold);
		
		assertEquals(1, result.size());
		
		for (List<Pair<String, String>> list : result) {
		
			assertEquals(4 , list.size());
			// There is one clusters of size 4
						
		}
		
		sequences.clear();

		sequences.add(new Pair<String, String>("s1", "ACTGACTGAC----------"));
		sequences.add(new Pair<String, String>("s2", "ACTGACTGAC----------"));
		sequences.add(new Pair<String, String>("s3", "ACTGACTGACBBBBBB----"));
		sequences.add(new Pair<String, String>("s4", "ACTGACTGACBBBBBB----"));

		identityThreshold = 1.0;
		
		result = clusterer.clusterize(sequences, identityThreshold);
		
		assertEquals(2, result.size());
		
		for (List<Pair<String, String>> list : result) {
		
			assertEquals(2 , list.size());
			// There is two clusters of size 2
						
		}
		
		identityThreshold = 0.625;
		
		result = clusterer.clusterize(sequences, identityThreshold);
		
		assertEquals(1, result.size());
		
		for (List<Pair<String, String>> list : result) {
		
			assertEquals(4 , list.size());
			// There is 1 clusters of size 4
						
		}
		
	}

}
