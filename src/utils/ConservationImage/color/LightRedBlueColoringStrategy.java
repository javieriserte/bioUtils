package utils.ConservationImage.color;

import java.awt.Color;

public class LightRedBlueColoringStrategy extends ColoringStrategy {

	@Override
	public Color getColor(double value) {
		
		// 1 : 255, 200, 200
		// 0 : 0  ,   0, 255
		if (value>1) value=1;
		if (value<0) value=0;
		int r = (int) (value * 255);
		int g = (int) (value * 200);
		int b = (int) (255 - 55 * value);
		return new Color(r,g,b);
		
	}

}
