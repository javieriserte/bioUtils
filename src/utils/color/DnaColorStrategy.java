package utils.color;

import java.awt.Color;

public class DnaColorStrategy extends ColorStrategy{

	@Override public Color getColor(char base) {
		
		Color result = null;
		
		switch (Character.toUpperCase(base)) {
		case 'A': result = new Color(0	,128,0	); break;
		case 'C': result = new Color(0	,0	,255); break;
		case 'G': result = new Color(0	,0	,0	); break;
		case 'T': result = new Color(255,0	,0	); break;
		default : result = new Color(0	,0	,0	); break;
		}
		
		return result;
		
	}
	
}
