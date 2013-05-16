package utils.ConservationImage.color;

import java.awt.Color;

public class RedBlueColorringStrategy extends ColoringStrategy{

	@Override public Color getColor(double value) {
		
		Color newColor=null;
		try {
			if (value>1) value=1;
			if (value<0) value=0;
			newColor = new Color((float) value, 0, (float) (1- value));	
		} catch (Exception e) {
		
			System.out.println("el valor es: " + value);

		}
		return newColor;

	}

}
