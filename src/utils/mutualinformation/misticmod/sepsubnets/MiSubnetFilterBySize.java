package utils.mutualinformation.misticmod.sepsubnets;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import utils.mutualinformation.misticmod.MI_Position;

public class MiSubnetFilterBySize {

	/////////////////////////////////
	// instance Variables
	private int sizeCutOff;
	/////////////////////////////////
	
	/////////////////////////////////
	// Constructor
	/**
	 * Creates a new MiSubnetFilterBySize.
	 * 
	 * @param sizeCutOff is the cut off value. Subnets with more than
	 *        sizeCutOff edge will be kept.
	 */
	public MiSubnetFilterBySize(int sizeCutOff) {
		super();
		this.sizeCutOff = sizeCutOff;
	};
	/////////////////////////////////
	
	/////////////////////////////////
	// Public Interface
	/**
	 * Filter a collection of subnets by the number of edges.
	 * 
	 * @param edgesBySubnet
	 * @return
	 */
	public List<Set<MI_Position>> filter(List<Set<MI_Position>> edgesBySubnet) {
		
		List<Set<MI_Position>> resultList = new ArrayList<>();
		
		List<Set<MI_Position>> markedForRemove = new ArrayList<>();
		
		for (Set<MI_Position> set : edgesBySubnet) {
			
			if (set.size()<this.sizeCutOff) {
				markedForRemove.add(set);
			}
			
		}
		
		resultList.addAll(edgesBySubnet);
		
		resultList.removeAll(markedForRemove);
		
		return resultList;
		
	}

	/////////////////////////////////
	
}
