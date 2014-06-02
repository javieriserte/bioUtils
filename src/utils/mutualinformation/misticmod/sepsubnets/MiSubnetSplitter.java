package utils.mutualinformation.misticmod.sepsubnets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import utils.mutualinformation.misticmod.MI_Position;

public class MiSubnetSplitter {

	/**
	 * Splits a net of MI data into subnets
	 * 
	 * @param positions
	 * @return
	 */
	public List<Set<MI_Position>> split(List<MI_Position> positions) {
		
		//////////////////////////////////////////////
		// Create data structures for edges
		// This will keep the results
		List<Set<MI_Position>> edgesBySubnet = new ArrayList<Set<MI_Position>>();
		/////////////////////////////////////////////
		
		///////////////////////////////////////////////
		// Fill edgesByNode map
		Map<Integer, List<MI_Position>> edgesByNode = new HashMap<>();
		createEdgesByNodeMap(edgesByNode, positions);
		/////////////////////////////////////////////////
		
		/////////////////////////////////////////////////
		// Creates a list of all nodes in data
		List<Integer> nodes = new ArrayList<>();
		nodes.addAll(edgesByNode.keySet());
		/////////////////////////////////////////////////

		/////////////////////////////////////////////////
		// Creates an auxiliary data structures to
		// keep record of nodes that are already 
		// visited through the expansion of the subnets
		Set<Integer> visitedNodes = new HashSet<>();
		/////////////////////////////////////////////////
		
		/////////////////////////////////////////////////
		// Separates the subnets
		for (Integer node : nodes) {
		// Iterate over each nodes in data
			if (!visitedNodes.contains(node)) {
		// If the node is not visited try to expand to the neighbors.
		// A no visited node correspond to a new subnet.
				List<Integer> newNodeList = new ArrayList<>();
		// Creates a data structure to store the nodes in the current subnet.
				Set<MI_Position> newEdgesSet = new HashSet<>();
		// Creates a data structure to store the edges in the current subnet.
				newNodeList.add(node);
		// Seed the current subnet list of nodes 
				expandSubnet(edgesByNode, visitedNodes, newNodeList, newEdgesSet);
		// Expand the subnet by walking through the neighbors of the seed node
		// Each new node is marked as visited
				edgesBySubnet.add(newEdgesSet);
		// Add all edges found to the current subnet egde set.
			}
		
			
		}
		return edgesBySubnet;
	}

	//////////////////////////////////////////////////////
	// Private methods
	private static void expandSubnet(
			Map<Integer, List<MI_Position>> edgesByNode,
			Set<Integer> visitedNodes, List<Integer> newNodeList,
			Set<MI_Position> newEdgesSet) {
		
		for (int i = 0; i< newNodeList.size(); i++) {
			
			Integer currentNode = newNodeList.get(i);
			
			visitedNodes.add(currentNode);

			List<MI_Position> currentEdges = edgesByNode.get(currentNode);
			
			newEdgesSet.addAll(currentEdges);
			
			addNodesToNodeList(newNodeList, currentEdges);
		
		}
	}

	private static void addNodesToNodeList(List<Integer> newNodeList,
			List<MI_Position> currentEdges) {
		for (MI_Position mi_Position : currentEdges) {
			
			if (!newNodeList.contains(mi_Position.getPos1())) {
				
				newNodeList.add(mi_Position.getPos1());
				
			}
			
		}

		for (MI_Position mi_Position : currentEdges) {
			
			if (!newNodeList.contains(mi_Position.getPos2())) {
				
				newNodeList.add(mi_Position.getPos2());
				
			}
			
		}
	}

	private static void createEdgesByNodeMap(
			Map<Integer, List<MI_Position>> edgesByNode,
			List<MI_Position> positions) {
		for (MI_Position mi_Position : positions) {
			
			if (!edgesByNode.containsKey(mi_Position.getPos1())) {
				
				edgesByNode.put(mi_Position.getPos1(), new ArrayList<MI_Position>());
				
			}
			
			if (!edgesByNode.containsKey(mi_Position.getPos2())) {
				
				edgesByNode.put(mi_Position.getPos2(), new ArrayList<MI_Position>());
				
			}
			
			edgesByNode.get(mi_Position.getPos1()).add(mi_Position);
			
			edgesByNode.get(mi_Position.getPos2()).add(mi_Position);
			
		}
	}

	//////////////////////////////////////////////////
	
}
