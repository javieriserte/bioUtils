package utils.ConservationImage.renderer;

import java.awt.image.BufferedImage;

import utils.ConservationImage.color.ColoringStrategy;

public interface Renderer {

	public BufferedImage 		render								(ColoringStrategy color, double[] data, int windowLen);

	abstract public DrawingLayout      getDefaultLayout                    ();

	public abstract void setLayout(DrawingLayout layout);
	
}
