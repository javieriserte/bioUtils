package utils.mutualinformation.mimatrixviewer.matrixview_new;

import java.awt.Color;

public interface ColoringStrategy {

	public Color getColor(double value);
	
	public boolean isValidValue(double value);
	
}
