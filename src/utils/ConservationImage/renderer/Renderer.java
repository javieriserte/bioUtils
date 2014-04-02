package utils.ConservationImage.renderer;

import java.awt.image.BufferedImage;

import utils.ConservationImage.color.ColoringStrategy;

public interface Renderer {

	public BufferedImage 		render								(ColoringStrategy color, double[] data, int windowLen);

	public DrawingLayout      getDefaultLayout                    ();

	public void setLayout(DrawingLayout layout);
	
	public boolean isDefault();
	
	public void setDefault(boolean isDefault);
	
}
