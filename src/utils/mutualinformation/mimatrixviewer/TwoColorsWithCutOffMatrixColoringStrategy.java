package utils.mutualinformation.mimatrixviewer;

import java.awt.Color;

import utils.mutualinformation.misticmod.MI_Position;

public class TwoColorsWithCutOffMatrixColoringStrategy extends TwoColorsMatrixColoringStrategy {

	//////////////////////////////
	// Instance Variables
	double upperRange;
	double lowerRange;

	//////////////////////////////
	// Constructors
	private TwoColorsWithCutOffMatrixColoringStrategy(Color lowValues, 
			Color highValues, Color undefinedValue, Color diagonalColor,
			double lowerValue, double higherValue) {
		super(lowValues, highValues, undefinedValue, diagonalColor, lowerValue,
				higherValue);
	}
	
	public TwoColorsWithCutOffMatrixColoringStrategy(Color lowValues, 
			Color highValues, Color undefinedValue, Color diagonalColor,
			double lowerValue, double higherValue, double cutOffValue) {
		super(lowValues, highValues, undefinedValue, diagonalColor, lowerValue, higherValue);
		this.setMeanValue(cutOffValue);
		this.setLowerRange(cutOffValue - lowerValue);
		this.setUpperRange(higherValue - cutOffValue);
		
	}
	////////////////////////////////////
	// Public Interface
	@Override
	public Color getColor(MI_Position position){

		Color usedColor = null;
		double range = 0;
		if (position==null) {
			return this.getDiagonalColor(); 
		}
		if (position.getMi()<-999) {
			return this.getUndefinedValueColor();
		} else 
		if (position.getMi()<this.getMeanValue()) {
			usedColor = this.getLowValuesColor();
			range = this.getLowerRange();
		} else {
			usedColor = this.getHighValuesColor();
			range = this.getUpperRange();
		}
		
		double c = Math.abs( (this.getMeanValue()- position.getMi()) / (range));
		
		int r = (int) (Math.min(usedColor.getRed()   * c,255));
		int g = (int) (Math.min(usedColor.getGreen() * c,255));
		int b = (int) (Math.min(usedColor.getBlue()  * c,255));
		return new Color (r,g,b );
		
	}
	@Override
	@Deprecated
	protected void calculateMeanValue() {}

	@Override
	@Deprecated
	protected void calculateMidRange() {}
	
	//////////////////////////////////
	// Getters And Setters
	public double getUpperRange() {
		return upperRange;
	}

	public void setUpperRange(double upperRange) {
		this.upperRange = upperRange;
	}
	
	public double getLowerRange() {
		return lowerRange;
	}

	public void setLowerRange(double lowerRange) {
		this.lowerRange = lowerRange;
	}
}
