package utils.mutualinformation.mimatrixviewer;

import java.awt.Color;

public class RedAndBlueMatrixColoringStrategy extends TwoColorsWithCutOffMatrixColoringStrategy {

	///////////////////////////////
	// Constructors
	private RedAndBlueMatrixColoringStrategy(Color lowValues, Color highValues, Color undefinedValue, Color diagonalColor, double lowerValue, double higherValue, double cutOffValue) {
		super(lowValues, highValues, undefinedValue, diagonalColor, lowerValue, higherValue,cutOffValue);
	}
	public RedAndBlueMatrixColoringStrategy(double lowerValue, double higherValue, double cutOffValue) {
		super(new Color(0,0,150), Color.red, new Color(0,50,0), Color.white , lowerValue, higherValue, cutOffValue);
	}
	public RedAndBlueMatrixColoringStrategy(double lowerValue, double higherValue) {
		super(new Color(0,0,150), Color.red, new Color(0,50,0), Color.white , lowerValue, higherValue, 6.5);
	}
}
