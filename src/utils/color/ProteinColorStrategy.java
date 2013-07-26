package utils.color;

import java.awt.Color;

public class ProteinColorStrategy extends ColorStrategy{

	@Override public Color getColor(char c) {
		
		Color result = null;
		
		switch (Character.toUpperCase(c)) {
		case 'D': result = new Color(230,10,10); break; 
		case 'E': result = new Color(230,10,10); break;
		case 'C': result = new Color(230,230,0); break; 
		case 'M': result = new Color(230,230,0); break;
		case 'K': result = new Color(20,90,255); break; 
		case 'R': result = new Color(20,90,255); break;
		case 'S': result = new Color(250,150,0); break; 
		case 'T': result = new Color(250,150,0); break;
		case 'F': result = new Color(50,50,170); break; 
		case 'Y': result = new Color(50,50,170); break;
		case 'N': result = new Color(0,220,220); break; 
		case 'Q': result = new Color(0,220,220); break;
		case 'G': result = new Color(235,235,235); break;
		case 'L': result = new Color(15,130,15); break; 
		case 'V': result = new Color(15,130,15); break; 
		case 'I': result = new Color(15,130,15); break;
		case 'A': result = new Color(200,200,200); break;
		case 'W': result = new Color(180,90,180); break;
		case 'H': result = new Color(130,130,210); break;
		case 'P': result = new Color(220,150,130); break;
		case 'X': result = new Color(0,0,0); break;
		default : result = new Color(0	,0	,0	); break;
		}
		return result;
	}

}
