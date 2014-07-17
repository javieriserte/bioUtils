package seqManipulation.gapstripper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pair.Pair;
import clustering.HobohmClusteringM1;
/**
 * Calculates the weight of a sequence in a MSA as the (1/number of sequences in
 * cluster). Clustering is performed using Hobohm-1 method. 
 * 
 * @author javier
 *
 */
public class ClusteringWeighter extends SequenceWeighter {

	////////////////////////////////////////////////////////////////////////////
	// Instance Variables
	private Map<String,Double> clusterWeigth = new HashMap<>();
	////////////////////////////////////////////////////////////////////////////
	
	////////////////////////////////////////////////////////////////////////////
	// Constructor
	public ClusteringWeighter(Map<String, String> sequences, double thresholdId) {
		
		this.setClusterWeigth(new HashMap<String, Double>());
		
		HobohmClusteringM1 clusterer = new HobohmClusteringM1();
		
		List<Pair<String,String>> sequenceList = new ArrayList<>();
		
		for (String  description : sequences.keySet()) {
			
			sequenceList.add(new Pair<String, String>(description, sequences.get(description)));
			
		}
		
		Set<List<Pair<String, String>>> clusters = clusterer.clusterize(sequenceList, thresholdId);
		
		for (List<Pair<String, String>> list : clusters) {
			
			double currentClusterWeight = list.size();
			
			for (Pair<String, String> pair : list) {
				
				this.getClusterWeigth().put(pair.getFirst(), 1/currentClusterWeight);
				
			}
			
		}
		
	}
	////////////////////////////////////////////////////////////////////////////
	
	////////////////////////////////////////////////////////////////////////////
	// Public interface
	/**
	 * Get the weight of the current sequence.
	 */
	@Override
	public double getWeight(String sequenceIdentifier) {
		
		if (this.getClusterWeigth().containsKey(sequenceIdentifier)) {
		
			return this.getClusterWeigth().get(sequenceIdentifier);
			
		} else {
			
			return 0;
			
		}
		
	}
	////////////////////////////////////////////////////////////////////////////

	////////////////////////////////////////////////////////////////////////////
	// Getters And Setters
	/**
	 * @return the clusterWeigth
	 */
	protected Map<String, Double> getClusterWeigth() {
		return clusterWeigth;
	}

	/**
	 * @param clusterWeigth the clusterWeigth to set
	 */
	protected void setClusterWeigth(Map<String, Double> clusterWeigth) {
		this.clusterWeigth = clusterWeigth;
	}
	////////////////////////////////////////////////////////////////////////////

}
