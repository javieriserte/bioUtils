package utils.mutualinformation.misticmod.tocytoscape.layouts;

public class Vertex<K> {

	/////////////////////////////////////////////
	// Instance Variables
	private double x;
	
	private double y;
	
	private K label;
	
	private double xForce;
	
	private double yForce;
	/////////////////////////////////////////////
	
	/////////////////////////////////////////////
	// Constructor
	public Vertex(double x, double y, K label) {
		super();
		this.x = x;
		this.y = y;
		this.label = label;
		this.xForce = 0;
		this.yForce = 0;
	}
	/////////////////////////////////////////////
	
	/////////////////////////////////////////////
	// Public interface
	
	public void resetForce() {
		
		this.setxForce(0);
		
		this.setyForce(0);
		
	}
	
	public void accumulateForce(double xForce, double yForce) {
		
		this.setxForce(this.getxForce() + xForce);
		
		this.setyForce(this.getyForce() + yForce);
		
	}
	
	public void displaceInForceDirection(double distance) {
		
		this.setX( this.getX() + this.getxForce() * distance);
		
		this.setY( this.getY() + this.getyForce() * distance);
		
	}
	
	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
	public K getLabel() {
		return label;
	}

	public void setLabel(K label) {
		this.label = label;
	}

	public double getxForce() {
		return xForce;
	}

	public void setxForce(double xForce) {
		this.xForce = xForce;
	}

	public double getyForce() {
		return yForce;
	}

	public void setyForce(double yForce) {
		this.yForce = yForce;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((label == null) ? 0 : label.hashCode());
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
		Vertex<K> other = (Vertex<K>) obj;
		if (label == null) {
			if (other.label != null)
				return false;
		} else if (!label.equals(other.label))
			return false;
		return true;
	}
	
	//////////////////////////////////////////////


	
}
