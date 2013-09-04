package utils.ConservationImage.color;

import java.awt.Color;

public class RedBlueColoringStrategy extends ColoringStrategy{

	@Override public Color getColor(double value) {
		
		Color newColor=null;

		if (value>1) value=1;
		if (value<0) value=0;
		newColor = new Color((float) value, 0, (float) (1- value));	
		return newColor;

	}

}
