package utils.mutualinformation.mimatrixviewer;

import java.awt.Color;

public class BlackAndWhiteZoomMatrixColoringStrategy implements ZoomMatrixColoringStrategy {

	////////////////////////////////
	// Instance Variables
	private double cutOffValue;
	
	////////////////////////////////
	// Constructors
	public BlackAndWhiteZoomMatrixColoringStrategy(double cutOffValue) {
	
		this.setCutOffValue(cutOffValue);
	}
	

	///////////////////////////////////
	// Public Interface
	@Override
	public Color getColor(double miValue) {
		
		if(miValue<-900) {
			return Color.green;			
		} else {
			return (miValue>=this.getCutOffValue())?Color.black:Color.white;
		}
		
	}

	
	@Override
	public String toString() {
		
		return "Black and White with cut off value: "+ this.getCutOffValue();
		
	}


	/////////////////////////////////
	// Getters and Setters
	public double getCutOffValue() {
		return cutOffValue;
	}


	public void setCutOffValue(double cutOffValue) {
		this.cutOffValue = cutOffValue;
	}

}
