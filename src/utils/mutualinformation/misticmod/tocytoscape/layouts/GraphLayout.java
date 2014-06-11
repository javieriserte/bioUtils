package utils.mutualinformation.misticmod.tocytoscape.layouts;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GraphLayout<K> {

	///////////////////////////////
	// Instance Variables
	private Force atractiveForce;
	
	private Force repulsiveForce;
	
	private double displacement;
	/////////////////////////////////

	/////////////////////////////////
	// Constructor
    public GraphLayout(Force atractiveForce, Force repulsiveForce, double displacement) {
		super();
		this.atractiveForce = atractiveForce;
		this.setRepulsiveForce(repulsiveForce);
		this.displacement = displacement;
	}
    /////////////////////////////////
    
    /////////////////////////////////
    // Public Interface
    
    public void applyLayout(List<Edge<K>> edges, int iterations) {
    	
    	///////////////////////////////////////
    	// Get A list of all vertex and all  
    	// unconnected vertex pairs
    	List<Vertex<K>> allVertex = this.getAllVertex(edges);
    	List<Edge<K>> unconnectedVertexPairs = this.getUnconnectedVertex(allVertex,edges);
    	///////////////////////////////////////
    	
    	
    	//////////////////////////////////////////
    	// Initialize Vertex in Random positions
    	// TODO: Make an object to initialize vertex positions
    	for (Vertex<K> vertex: allVertex) {
			vertex.setX(Math.random()*20-10);
			vertex.setY(Math.random()*20-10);
		}
    	//////////////////////////////////////////
    	
    	while (iterations-- > 0) {
    		
    		//////////////////////////////////////
    		// Reset all forces in vertex
        	for (Vertex<K> vertex: allVertex) {
    			vertex.resetForce();
    		}
    		//////////////////////////////////////
    		
	    	for (Edge<K> edge : unconnectedVertexPairs) {

	    		// Sum repulsive force
	    		edge.sumForcesToVertex(this.getRepulsiveForce());

			} 
	    	
	    	for (Edge<K> edge : edges) {
	    		
	    		// sum attractive force
	    		edge.sumForcesToVertex(this.getAtractiveForce());
	    		
	    	}
	    	
	    	for (Vertex<K> vertex : allVertex) {
	    		
	    		vertex.displaceInForceDirection(this.getDisplacement());
	    		
	    	}
	    	
    	
    	}
    ///////////////////////////////////////
    	
    }
    
	/////////////////////////////////
    
	/////////////////////////////////
	// Private and protected methods
    private List<Edge<K>> getUnconnectedVertex(List<Vertex<K>> allVertex, List<Edge<K>> edges) {
    	
    	Set<Edge<K>> unconnnectedEdges = new HashSet<Edge<K>>();
    	
    	for (int i = 0 ; i< allVertex.size() - 1 ;i++) {
    		
    		for (int j = i+1; j < allVertex.size(); j++) {

    			unconnnectedEdges.add( new Edge<K>(allVertex.get(i), allVertex.get(j), 0));
    			
			}
    		
    	}
    	
    	Set<Edge<K>> markedForRemove = new HashSet<Edge<K>>();
    	
    	for (Edge<K> unconentedEdge : unconnnectedEdges) {
    		
    		if (edges.contains(unconentedEdge)) {
    			markedForRemove.add(unconentedEdge);
    		}
    		
    	}
    	
    	unconnnectedEdges.removeAll(markedForRemove);
    	
    	
    	
    	
    	List<Edge<K>> result = new ArrayList<Edge<K>>();
    	
    	result.addAll(unconnnectedEdges);
    	
		return result;
	}

	private List<Vertex<K>> getAllVertex(List<Edge<K>> edges) {
		
		Set<Vertex<K>> vertexSet = new HashSet<Vertex<K>>();
		
		List<Vertex<K>> result = new ArrayList<Vertex<K>>();
		
		for (Edge<K> edge : edges) {

			vertexSet.add(edge.getFirstVertex());
			
			vertexSet.add(edge.getSecondVertex());
			
		}
		
		result.addAll(vertexSet);
		
		return result;
		
	}

	protected double getDisplacement() {
		return displacement;
	}

	protected void setDisplacement(double displacement) {
		this.displacement = displacement;
	}

	public Force getRepulsiveForce() {
		return repulsiveForce;
	}

	public void setRepulsiveForce(Force repulsiveForce) {
		this.repulsiveForce = repulsiveForce;
	}

	public Force getAtractiveForce() {
		return atractiveForce;
	}

	public void setAtractiveForce(Force atractiveForce) {
		this.atractiveForce = atractiveForce;
	}
	
	
	/////////////////////////////////////
}
