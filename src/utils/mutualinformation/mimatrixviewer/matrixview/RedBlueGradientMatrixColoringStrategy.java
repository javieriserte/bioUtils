package utils.mutualinformation.mimatrixviewer.matrixview;

import java.awt.Color;

public class RedBlueGradientMatrixColoringStrategy extends TwoColorsGradientWithCutOffMatrixColoringStrategy {

	///////////////////////////////
	// Constructors
	private RedBlueGradientMatrixColoringStrategy(Color lowValues, Color highValues, Color undefinedValue, Color diagonalColor, double lowerValue, double higherValue, double cutOffValue) {
		super(lowValues, highValues, undefinedValue, diagonalColor, lowerValue, higherValue,cutOffValue);
	}
	public RedBlueGradientMatrixColoringStrategy(double lowerValue, double higherValue, double cutOffValue) {
		super(new Color(0,0,150), Color.red, new Color(0,50,0), Color.white , lowerValue, higherValue, cutOffValue);
	}
	public RedBlueGradientMatrixColoringStrategy(double lowerValue, double higherValue) {
		super(new Color(0,0,150), Color.red, new Color(0,50,0), Color.white , lowerValue, higherValue, 6.5);
	}
	
	//////////////////////////////
	// Public Interface
	@Override
	public String toString() {
		return "Red And Blue Gradient. With CutOff:"+this.getMeanValue();
	}

	
	
}
