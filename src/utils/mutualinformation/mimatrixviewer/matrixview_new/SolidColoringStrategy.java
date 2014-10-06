package utils.mutualinformation.mimatrixviewer.matrixview_new;

import java.awt.Color;

import datatypes.range.Range;

public class SolidColoringStrategy implements ColoringStrategy {

	private Range<Double> range;
	private Color color;
	
	public SolidColoringStrategy(Range<Double> range, Color color) {
		super();
		this.setRange(range);
		this.setColor(color);
	}

	@Override
	public Color getColor(double value) {

		if (this.getRange().inRange(value)) {
			return this.getColor();
		} else {
			return null;
		}
		
	}

	@Override
	public boolean isValidValue(double value) {

		return this.getRange().inRange(value);
		
	}
	
	private Range<Double> getRange() {
		return range;
	}

	private void setRange(Range<Double> range) {
		this.range = range;
	}
	
	private Color getColor() {
		return color;
	}

	private void setColor(Color color) {
		this.color = color;
	}


}
