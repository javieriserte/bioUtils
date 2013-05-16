package utils.ConservationImage.color;

import java.awt.Color;

/**
 * A ColorStrategy that always returns a black color. 
 * 
 * @author Javier Iserte (jiserte@unq.edu.ar)
 *
 */
public class BlackColoringStrategy extends ColoringStrategy {

	@Override
	public Color getColor(double value) {
		return Color.black;
	}

}
