package utils.mutualinformation.mimatrixviewer.matrixview_new;

import java.awt.Color;
import java.util.List;

public class ColorMapper {

	private List<ColoringStrategy> colors;
	
	public ColorMapper(List<ColoringStrategy> colors) {
		super();
		this.setColors(colors);
	}

	public Color getColor(double value) {
		
		for (ColoringStrategy color : this.getColors()) {
			
			if (color.isValidValue(value)) {
				return color.getColor(value);
			}
			
		}
		
		return Color.gray;
	}
	
	public boolean isValid(double value) {
		
		for (ColoringStrategy color : this.getColors()) {

			if (color.isValidValue(value)) {
				return true;
			}

		}
		return false;

	}

	public List<ColoringStrategy> getColors() {
		return colors;
	}

	public void setColors(List<ColoringStrategy> colors) {
		this.colors = colors;
	}
	

	
}
