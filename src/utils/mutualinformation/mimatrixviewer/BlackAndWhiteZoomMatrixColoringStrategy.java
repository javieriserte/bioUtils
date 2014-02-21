package utils.mutualinformation.mimatrixviewer;

import java.awt.Color;

public class BlackAndWhiteZoomMatrixColoringStrategy implements
		ZoomMatrixColoringStrategy {

	@Override
	public Color getColor(double miValue) {
		
		if(miValue<-900) {
			return Color.green;			
		} else {
			return (miValue>=10)?Color.black:Color.white;
		}
		
	}

}
