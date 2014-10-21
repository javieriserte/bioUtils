package utils.mutualinformation.misticmod.onepixelmap.themes;

import java.awt.Color;

public class BlueAndRedTheme implements MatrixColoringTheme{

	private double cutOff;
	private double maxMI;
	private double minMI;
	
	public BlueAndRedTheme(double cutOff) {
		super();
		this.cutOff = cutOff;
	}

	public Color getSeparatingLineColor(){
		return Color.white;
	}
	
	public int getRGBColor(Double mi) {
		
		if (mi < (-900) ) {
			
			return new Color(0,50,0).getRGB();
			
		} else 
		
		if (mi<this.cutOff) {
			
			double v = (mi / this.minMI);
			
			return new Color (0 , 0 ,(int) (255 * Math.abs(v))).getRGB();
			
		} else {
			
			return new Color ((int) (255 * (mi / this.maxMI)),0,0).getRGB();
			
		}
		
	}

	@Override
	public double getCutOff() {
		return this.cutOff;
	}

	@Override
	public void setCutOff(double cutOff) {
		
		this.cutOff = cutOff;
		
	}

	@Override
	public double getMaxMI() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setMaxMI(double maxMI) {
		
		this.maxMI = maxMI;
		
	}

	@Override
	public double getMinMI() {
		
		return this.maxMI;
		
	}

	@Override
	public void setMinMI(double minMI) {
		
		this.minMI = minMI;
		
	}
	
}