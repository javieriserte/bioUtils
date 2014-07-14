package utils.mutualinformation.misticmod.datastructures.format;

import utils.mutualinformation.misticmod.datastructures.MI_Position;
	
public class MisticFormatMi extends FormatMi {
	
	public static final String FORMAT_LINE="\\d+\\t[A-Z\\-]\\t\\d+\\t[A-Z\\-]\\t-*\\d+\\.?\\d*[eE]*[-+]*\\d*$";
	
	public void fillPosition(MI_Position pos, String positionLine) {
		
		String[] data = positionLine.split("\t");

		pos.setPos1(Integer.valueOf(data[0]));
		pos.setPos2(Integer.valueOf(data[2]));
		pos.setAa1(data[1].charAt(0));
		pos.setAa2(data[3].charAt(0));
		pos.setMi(Double.valueOf(data[4]));
		pos.setRaw_mi(0d);
		pos.setMean_mi(0d);
		pos.setSd_mi(0d);
		
	}

	@Override
	public String getFormatLine() {
		return MisticFormatMi.FORMAT_LINE;
	}

}