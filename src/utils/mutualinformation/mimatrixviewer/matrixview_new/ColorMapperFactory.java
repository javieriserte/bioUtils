package utils.mutualinformation.mimatrixviewer.matrixview_new;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import datatypes.range.Range;
import utils.mutualinformation.mimatrixviewer.MI_Matrix;

public class ColorMapperFactory {

	public static ColorMapper getBlueRedForMatrix(MI_Matrix matrix, double cutoff) {
		
		double min = matrix.getMinZscore();
		
		double max = matrix.getMaxZscore();
		
		List<ColoringStrategy> coloringMethods = new ArrayList<>();
		
		if (min<cutoff) {
			
			Range<Double> range = new Range<Double>(min, cutoff, true, false);
			coloringMethods.add(new GradientColoringStrategy(range, Color.blue, Color.black));
			
		}
		
		if (max >= cutoff) {
			
			Range<Double> range = new Range<Double>(cutoff, max, true, true);
			coloringMethods.add(new GradientColoringStrategy(range,  Color.black, Color.red));
			
		}
		
		coloringMethods.add(new SolidColoringStrategy(new Range<Double>(MI_Matrix.UNDEFINED, MI_Matrix.UNDEFINED, true, true), Color.green));
		
		ColorMapper mapper = new ColorMapper(coloringMethods);
		
		return mapper;
		
	}
	
	
	public static ColorMapper getBlackWhiteForZoom(MI_Matrix matrix, double cutoff) {
		
		double min = matrix.getMinZscore();
		
		double max = matrix.getMaxZscore();
		
		List<ColoringStrategy> coloringMethods = new ArrayList<>();
		
		if (min<cutoff) {
			
			Range<Double> range = new Range<Double>(min, cutoff, true, false);
			coloringMethods.add(new SolidColoringStrategy(range, Color.white));
			
		}
		
		if (max >= cutoff) {
			
			Range<Double> range = new Range<Double>(cutoff, max, true, true);
			coloringMethods.add(new SolidColoringStrategy(range, Color.black));
			
		}
		
		coloringMethods.add(new SolidColoringStrategy(new Range<Double>(MI_Matrix.UNDEFINED, MI_Matrix.UNDEFINED, true, true), Color.green));
		
		ColorMapper mapper = new ColorMapper(coloringMethods);
		
		return mapper;
		
	}
	
	
}
