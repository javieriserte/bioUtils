package distanceMatrix;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class PrintedNumber {

	
	public static String formatedFloat(Number number,int digitosMantisa, int digitosDecimales) throws ImposibleNumberRepresentationException {
		// PRECONDICION : digitosMantisa >= mantisaSize
		int mantisaSize=0;
		String minus="";
		Double num = number.doubleValue(); 
		StringBuilder format = new StringBuilder();
		DecimalFormatSymbols simbolos = new DecimalFormatSymbols();

		if (num<0) {
			minus="-";
			num = num*(-1);
		} else {
			minus="";
		}
		mantisaSize = ((Long)number.longValue()).toString().trim().length();

			
		
		if (digitosMantisa-mantisaSize>=0) { 
			format = format.append(repeatedString(digitosMantisa-mantisaSize,' '));
			format = format.append(minus);
			format = format.append(repeatedString(mantisaSize,'0'));
			format = format.append('.');
		} else {
			
			if (mantisaSize < digitosMantisa + digitosDecimales) {
				digitosDecimales = digitosDecimales + digitosMantisa-mantisaSize;
				format = format.append(minus);
				format = format.append('.');
			} else if (mantisaSize == digitosMantisa + digitosDecimales) {
				format = format.append(' ');
				format = format.append(minus);
				digitosDecimales = 0;
			} else if (mantisaSize > digitosMantisa + digitosDecimales) {
				throw new ImposibleNumberRepresentationException();			
			}
		}
		


		format = format.append(repeatedString(digitosDecimales,'0'));
		
		simbolos.setDecimalSeparator('.');
		
		DecimalFormat df = new DecimalFormat(format.toString(),simbolos);
		
		
		return df.format(num);
		
	
	}
	
	protected static String repeatedString(int length, char c) {
		
		StringBuilder sb = new StringBuilder(length);
		for(int x=0;x<length;x++) sb.insert(x, c);
		return sb.toString();
		
	}
}
