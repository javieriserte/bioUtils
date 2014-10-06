package utils.mutualinformation.mimatrixviewer.matrixview_new;

import java.awt.Color;

import datatypes.range.Range;

public class GradientColoringStrategy implements ColoringStrategy {

	private Range<Double> range;

	private Color initial;
	private Color end;
	
	public GradientColoringStrategy(Range<Double> range, Color initial,
			Color end) {
		super();
		this.setRange(range);
		this.setInitial(initial);
		this.setEnd(end);
	}

	@Override
	public Color getColor(double value) {
		
		double f = (value - getRange().getLowerBound()) / (getRange().getUpperBound()-getRange().getLowerBound());
		
		int r = (int) (this.getInitial().getRed() * (1-f) + f * (this.getEnd().getRed()));
		int g = (int) (this.getInitial().getGreen() * (1-f) + f * (this.getEnd().getGreen()));
		int b = (int) (this.getInitial().getBlue() * (1-f) + f * (this.getEnd().getBlue()));
		System.out.println(r + "," +g + "," + b);
		return new Color(r, g, b);
		
	}

	@Override
	public boolean isValidValue(double value) {
		
		return this.getRange().inRange(value);
		
	}

	private Color getInitial() {
		return initial;
	}

	private void setInitial(Color initial) {
		this.initial = initial;
	}

	private Color getEnd() {
		return end;
	}

	private void setEnd(Color end) {
		this.end = end;
	}

	private Range<Double> getRange() {
		return range;
	}

	
	private void setRange(Range<Double> range) {
		this.range = range;
	}

}
