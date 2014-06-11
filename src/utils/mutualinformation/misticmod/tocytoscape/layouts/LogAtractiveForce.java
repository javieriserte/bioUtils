package utils.mutualinformation.misticmod.tocytoscape.layouts;

public class LogAtractiveForce extends Force {

	/////////////////////////////////////////
	// Instance Variables
	private double atractiveConstant;
	/////////////////////////////////////////
	
	
	/////////////////////////////////////////
	// Constructor
	public LogAtractiveForce(double atractiveConstant) {
		this.setAtractiveConstant(atractiveConstant);
	}
	/////////////////////////////////////////
	
	/////////////////////////////////////////
	// Public Interface
	@Override
	public double force(double distance, Edge<? extends Object> edge) {
		
		return this.getAtractiveConstant() * Math.log(distance/ edge.getDistance());
		
	}
	/////////////////////////////////////////

	/////////////////////////////////////////
	// Getters ans Setters
	public double getAtractiveConstant() {
		return atractiveConstant;
	}
	public void setAtractiveConstant(double atractiveConstant) {
		this.atractiveConstant = atractiveConstant;
	}
	////////////////////////////////////////
}
