package utils.mutualinformation.mimatrixviewer;

import java.awt.Color;

import utils.mutualinformation.misticmod.MI_Position;

public class TwoColorsMatrixColoringStrategy implements MatrixColoringStrategy{
	////////////////////////////////
	// Instance Variables
	private Color lowValuesColor;
	private Color highValuesColor;
	private double lowerValue;
	private double higherValue;
	private Color undefinedValueColor;
	private double meanValue;
	private double midRange;
	private Color diagonalColor;

	/////////////////////////////////
	// Constructor
	public TwoColorsMatrixColoringStrategy(Color lowValues, Color highValues, Color undefinedValue, Color diagonalColor, double lowerValue, double higherValue) {
		
		this.setLowValuesColor(lowValues);
		this.setHighValuesColor(highValues);
		this.setLowerValue(lowerValue);
		this.setHigherValue(higherValue);
		this.calculateMeanValue();
		this.calculateMidRange();
		this.setUndefinedValueColor(undefinedValue);
		this.setDiagonalColor(diagonalColor);
		
	}

	/////////////////////////////////////
	// Getters And Setters
	public Color getLowValuesColor() {
		return lowValuesColor;
	}

	public void setLowValuesColor(Color lowValuesColor) {
		this.lowValuesColor = lowValuesColor;
	}

	public Color getHighValuesColor() {
		return highValuesColor;
	}

	public void setHighValuesColor(Color highValuesColor) {
		this.highValuesColor = highValuesColor;
	}
	private void setLowerValue(double lowerValue) {
		this.lowerValue = lowerValue;
		
	}
	private void setHigherValue(double higherValue) {
		this.higherValue = higherValue;
		
	}
	public double getLowerValue() {
		return lowerValue;
	}
	public double getHigherValue() {
		return higherValue;
	}
	public void setUndefinedValueColor(Color undefinedValue) {
		this.undefinedValueColor = undefinedValue;
		
	}
	public Color getUndefinedValueColor() {
		return this.undefinedValueColor;
	}
	private void setMeanValue(double meanValue) {
		this.meanValue = meanValue;
	}
	public double getMeanValue(){
		return this.meanValue; 
	}
	public double getMidRange() {
		return this.midRange;
	}

	private void setMidRange(double midRange) {
		this.midRange = midRange;
	}
	private void setDiagonalColor(Color diagonalColor) {
		this.diagonalColor = diagonalColor;
	}
	public Color getDiagonalColor() {
		return this.diagonalColor;
	}
	////////////////////////////////////
	// Public Interface
	@Override
	public Color getColor(MI_Position position) {
		Color usedColor = null;
		if (position==null) {
			return this.getDiagonalColor(); 
		}
		if (position.getMi()<-999) {
			return this.getUndefinedValueColor();
		} else 
		if (position.getMi()<this.getMeanValue()) {
			usedColor = this.getLowValuesColor();
		} else {
			usedColor = this.getHighValuesColor();
		}
		
		double c = Math.abs( (this.getMeanValue()- position.getMi()) / (this.getMidRange()));
		int r = (int) (usedColor.getRed()   * c);
		int g = (int) (usedColor.getGreen() * c);
		int b = (int) (usedColor.getBlue()  * c);
//		System.err.println(r +" " + g+ " " + b + " - " + c + " " + this.getMeanValue() + " " + position.getMi() + " " + this.getMidRange());
		return new Color (r,g,b );
		
	}
	
	//////////////////////////////////////
	// Private Methods
	private void calculateMeanValue() {
		this.setMeanValue((this.getHigherValue() + this.getLowerValue())/2);
		
	}
	private void calculateMidRange() {
		this.setMidRange((this.getHigherValue()-this.getLowerValue())/2);
	}
	
}
