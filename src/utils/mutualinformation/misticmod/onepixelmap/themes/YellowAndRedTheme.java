package utils.mutualinformation.misticmod.onepixelmap.themes;

import java.awt.Color;

public class YellowAndRedTheme implements MatrixColoringTheme{

	private double cutOff;
	private double maxMI;
	private double minMI;
	
	public YellowAndRedTheme(double cutOff) {
		super();
		this.cutOff = cutOff;
	}

	public Color getSeparatingLineColor(){
		
		return Color.black;
		
	}
	
	public int getRGBColor(Double mi ) {
		
		if (mi < (-900) ) {
			
			return new Color(0,50,0).getRGB();
			
		} else 
		
		if (mi<this.cutOff) {
			
			return Color.white.getRGB();
			
		} else {
			
			double v = 1 - (mi - this.cutOff)/(this.maxMI-this.cutOff);
			
			return new Color (255, (int) (255 * v),0).getRGB();
			
		}
		
	}

	/**
	 * @return the cutOff
	 */
	public double getCutOff() {
		return cutOff;
	}

	/**
	 * @param cutOff the cutOff to set
	 */
	public void setCutOff(double cutOff) {
		this.cutOff = cutOff;
	}

	/**
	 * @return the maxMI
	 */
	public double getMaxMI() {
		return maxMI;
	}

	/**
	 * @param maxMI the maxMI to set
	 */
	public void setMaxMI(double maxMI) {
		this.maxMI = maxMI;
	}

	/**
	 * @return the minMI
	 */
	public double getMinMI() {
		return minMI;
	}

	/**
	 * @param minMI the minMI to set
	 */
	public void setMinMI(double minMI) {
		this.minMI = minMI;
	}
}