package utils.mutualinformation.misticmod.tocytoscape;

public class BoundedHyperbolicTransformation extends MiToDistanceTransformation {

	/////////////////////////////////////
	// Instance Variables
	private double minimumDistance;
	private double maximumDistance;
	private double affinityConstant;
	private double minimumMIValue;
	/////////////////////////////////////
	
	/////////////////////////////////////
	// Constructor
	public BoundedHyperbolicTransformation(double minimumDistance, double maximumDistance, double meanMI, double minimumMI) {
		super();
		this.setMinimumDistance(minimumDistance);
		this.setMaximumDistance(maximumDistance);
		this.setAffinityConstant(meanMI-minimumMI);
		this.setMinimumMIValue(minimumMI);
	}
	/////////////////////////////////////
	
	/////////////////////////////////////
	// Public Interface
	@Override
	public double distance(double MI) {
		
		double transformedMI = this.transformMI(MI);
		
		return this.getMaximumDistance() - (this.getMaximumDistance()- this.getMinimumDistance()) * transformedMI  / (this.getAffinityConstant() + transformedMI) ;
		
	}
	public double getMinimumDistance() {
		return minimumDistance;
	}

	public void setMinimumDistance(double minimumDistance) {
		this.minimumDistance = minimumDistance;
	}

	public double getMaximumDistance() {
		return maximumDistance;
	}

	public void setMaximumDistance(double maximumDistance) {
		this.maximumDistance = maximumDistance;
	}

	public double getAffinityConstant() {
		return affinityConstant;
	}

	private void setAffinityConstant(double affinityConstant) {
		this.affinityConstant = affinityConstant;
	}
	//////////////////////////////////////

	//////////////////////////////////////
	// Private and protected methods
	protected double getMinimumMIValue() {
		return minimumMIValue;
	}

	protected void setMinimumMIValue(double minimumMIValue) {
		this.minimumMIValue = minimumMIValue;
	}

	protected double transformMI(double MI) {
		return MI - this.getMinimumMIValue();
	}
	/////////////////////////////////////

}
