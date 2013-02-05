package utils.ConservationImage;

import java.awt.image.BufferedImage;

public interface Renderer {

	public BufferedImage 		render								(ColoringStrategy color, double[] data, int windowLen);
	
}
