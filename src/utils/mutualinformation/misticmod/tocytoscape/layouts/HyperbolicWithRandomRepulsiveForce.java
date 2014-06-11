package utils.mutualinformation.misticmod.tocytoscape.layouts;

public class HyperbolicWithRandomRepulsiveForce extends
		HyperbolicRepulsiveForce {

	private double ignoreFraction;

	public HyperbolicWithRandomRepulsiveForce(double repulsiveConstant, double ignoreFraction) {
		super(repulsiveConstant);
		this.setIgnoreFraction(ignoreFraction);
	}

	@Override
	public double force(double distance, Edge<? extends Object> edge) {
	
		double r = Math.random();
		
		return (r<this.getIgnoreFraction())?super.force(distance, edge):0;
		
	}

	
	private void setIgnoreFraction(double ignoreFraction) {
		this.ignoreFraction = ignoreFraction;
		
	}


	private double getIgnoreFraction() {
		return this.ignoreFraction;
	}

	
	
}
