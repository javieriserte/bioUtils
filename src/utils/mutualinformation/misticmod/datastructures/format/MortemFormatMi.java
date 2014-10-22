package utils.mutualinformation.misticmod.datastructures.format;

import utils.mutualinformation.misticmod.datastructures.MI_Position;

public class MortemFormatMi extends FormatMi {
	
	public final static String FORMAT_LINE = "MI\\[ \\d+ [A-Z\\-] \\]\\[ \\d+ [A-Z\\-] \\] = -*\\d+\\.?\\d*[eE]*[-+]*\\d* -*\\d+\\.?\\d*[eE]*[-+]*\\d* -*\\d+\\.?\\d*[eE]*[-+]*\\d* -*\\d+\\.?\\d*[eE]*[-+]*\\d*$";
	
	public void fillPosition(MI_Position pos, String positionLine) {
		
		String[] data = positionLine.split(" ");
		
		pos.setPos1(Integer.valueOf(data[1]));
		pos.setPos2(Integer.valueOf(data[4]));
		pos.setAa1(data[2].charAt(0));
		pos.setAa2(data[5].charAt(0));
		pos.setMi(Double.valueOf(data[11]));
		pos.setRaw_mi(Double.valueOf(data[8]));
		pos.setMean_mi(Double.valueOf(data[9]));
		pos.setSd_mi(Double.valueOf(data[10]));
		
	}

	@Override
	public String getFormatLine() {
		return MortemFormatMi.FORMAT_LINE;
	}

	@Override
	public double getZscoreFrom(String line) {
		
		return Double.valueOf(line.split(" ")[11]);
		
	}

}