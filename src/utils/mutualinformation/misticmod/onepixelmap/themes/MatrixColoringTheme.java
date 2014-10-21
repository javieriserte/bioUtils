package utils.mutualinformation.misticmod.onepixelmap.themes;

import java.awt.Color;

public interface  MatrixColoringTheme {
	
	public int getRGBColor(Double mi);
	
	public Color getSeparatingLineColor();
	
	public double getCutOff() ;

	public void setCutOff(double cutOff) ;

	public double getMaxMI() ;

	public void setMaxMI(double maxMI) ;

	public double getMinMI();

	public void setMinMI(double minMI);
	
}