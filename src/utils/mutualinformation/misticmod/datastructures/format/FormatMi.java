package utils.mutualinformation.misticmod.datastructures.format;


import utils.mutualinformation.misticmod.datastructures.MI_Position;

public abstract class FormatMi {
	
	public abstract String getFormatLine();
	
	public abstract void fillPosition(MI_Position pos, String positionLine);
	
	public abstract double getZscoreFrom(String line);
	
}
