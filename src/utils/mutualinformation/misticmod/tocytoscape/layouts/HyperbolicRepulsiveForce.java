package utils.mutualinformation.misticmod.tocytoscape.layouts;

public class HyperbolicRepulsiveForce extends Force {
	
	////////////////////////////////////////
	// Instance Variables
	private double repulsiveConsntant;
	////////////////////////////////////////
	
	////////////////////////////////////////
	// Constructor
	public HyperbolicRepulsiveForce(double repulsiveConstant) {
		super();
		this.repulsiveConsntant = repulsiveConstant;
	}
	/////////////////////////////////////////
	
	/////////////////////////////////////////
	// Public Interface
	public double force (double distance, Edge<? extends Object> edge) {
		
		return (-1) * this.getRepulsiveConsntant() / Math.pow(distance,2);
		
	}
	/////////////////////////////////////////
	
	/////////////////////////////////////////
	// Getters And Setters
	public double getRepulsiveConsntant() {
		return repulsiveConsntant;
	}

	public void setRepulsiveConsntant(double repulsiveConsntant) {
		this.repulsiveConsntant = repulsiveConsntant;
	}
	////////////////////////////////////////
	

}
