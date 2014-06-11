package utils.mutualinformation.misticmod.tocytoscape.layouts;

public class Edge<K> {

	///////////////////////////////////
	// Instance Variables
	private Vertex<K> firstVertex;
	
	private Vertex<K> secondVertex;
	///////////////////////////////////

	///////////////////////////////////
	// Constructor
	private double distance ;
	public Edge(Vertex<K> firstVertex, Vertex<K> secondVertex, double distance) {
		super();
		this.firstVertex = firstVertex;
		this.secondVertex = secondVertex;
		this.distance = distance;
	}
	///////////////////////////////////
	
	///////////////////////////////////
	// Public Interface
	
	public void sumForcesToVertex(Force force) {
		
  		double x1 = this.getFirstVertex() .getX();
		double x2 = this.getSecondVertex().getX();
		double y1 = this.getFirstVertex() .getY();
		double y2 = this.getSecondVertex().getY();
		
		double distance = Math.sqrt(Math.pow(x2-x1, 2) + Math.pow(y2-y1,2 ));
		
		double dir_v1_x = ( x2-x1) / distance;
		double dir_v1_y = ( y2-y1) / distance;
		double dir_v2_x = -( x2-x1) / distance;
		double dir_v2_y = -( y2-y1) / distance;
		
		double forceMagnitude = force.force(distance, this);
		
		this.getFirstVertex().accumulateForce(forceMagnitude * dir_v1_x, forceMagnitude * dir_v1_y);
		
		this.getSecondVertex().accumulateForce(forceMagnitude * dir_v2_x, forceMagnitude * dir_v2_y);
	}
	
	public Vertex<K> getFirstVertex() {
		return firstVertex;
	}

	public void setFirstVertex(Vertex<K> firstVertex) {
		this.firstVertex = firstVertex;
	}

	public Vertex<K> getSecondVertex() {
		return secondVertex;
	}

	public void setSecondVertex(Vertex<K> secondVertex) {
		this.secondVertex = secondVertex;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((firstVertex == null) ? 0 : firstVertex.hashCode());
		result = prime * result
				+ ((secondVertex == null) ? 0 : secondVertex.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		@SuppressWarnings("unchecked")
		Edge<K> other = (Edge<K>) obj;
		// All null case
		boolean firstIsNull = firstVertex == null;
		boolean secondIsNull = secondVertex == null;
		boolean otherFirstIsNull = other.firstVertex == null;
		boolean otherSecondIsNull = other.secondVertex== null;
		
		int numNull = 0;
		numNull += firstIsNull?1:0;
		numNull += secondIsNull?1:0;
		numNull += otherFirstIsNull?1:0;
		numNull += otherSecondIsNull?1:0;
		
		switch (numNull) {
		case 0:
			
			return (firstVertex.equals(other.firstVertex) && secondVertex.equals(other.secondVertex) || 
					firstVertex.equals(other.secondVertex) && secondVertex.equals(other.firstVertex));
			
		case 4:
			
			return true;
			
		case 2:
			
			if (firstIsNull && otherFirstIsNull) {
				return secondVertex.equals(other.secondVertex);
			}
			if (firstIsNull && otherSecondIsNull) {
				return secondVertex.equals(other.firstVertex);
			}
			if (secondIsNull && otherFirstIsNull) {
				return firstVertex.equals(other.secondVertex);
			}
			if (secondIsNull && otherSecondIsNull) {
				return firstVertex.equals(other.firstVertex);
			}
			return false;
		default:
			return false;
		}
	
	}
	
	////////////////////////////////


}
