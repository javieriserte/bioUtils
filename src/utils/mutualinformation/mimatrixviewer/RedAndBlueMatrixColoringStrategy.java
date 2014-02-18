package utils.mutualinformation.mimatrixviewer;

import java.awt.Color;

public class RedAndBlueMatrixColoringStrategy extends TwoColorsMatrixColoringStrategy {

	///////////////////////////////
	// Constructors
	private RedAndBlueMatrixColoringStrategy(Color lowValues, Color highValues, Color undefinedValue, Color diagonalColor, double lowerValue, double higherValue) {
		super(lowValues, highValues, undefinedValue, diagonalColor, lowerValue, higherValue);
	}
	public RedAndBlueMatrixColoringStrategy(double lowerValue, double higherValue) {
		super(Color.blue, Color.red, new Color(0,50,0), Color.white , lowerValue, higherValue);
	}
}
