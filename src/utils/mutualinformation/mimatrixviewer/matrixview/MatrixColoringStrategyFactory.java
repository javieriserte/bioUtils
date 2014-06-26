package utils.mutualinformation.mimatrixviewer.matrixview;

import java.awt.Color;

import pair.Pair;
import utils.mutualinformation.mimatrixviewer.MI_Matrix;

/**
 * Creates Ready to use MatrixColoringStrategy objects.
 * @author Javier
 *
 */
public class MatrixColoringStrategyFactory {

	/***
	 * Creates a red and blue with min and max as range, and a cutoff value of 6.5
	 * @param matrix
	 * @return
	 */
	public static MatrixColoringStrategy createRedBlueGradient(MI_Matrix matrix) {

		Pair<Double, Double> minMax = MatrixColoringStrategyFactory.getMaxAndMinFromMiMatrix(matrix);
		
		return new RedBlueGradientMatrixColoringStrategy(minMax.getFirst(), minMax.getSecond(), 6.5);
		
	}
	
	/***
	 * Creates a red and blue with min and max as range, and a cutoff value of 10
	 * @param matrix
	 * @return
	 */
	public static MatrixColoringStrategy createRedBlueGradientGt10(MI_Matrix matrix) {

		Pair<Double, Double> minMax = MatrixColoringStrategyFactory.getMaxAndMinFromMiMatrix(matrix);
		
		return new RedBlueGradientMatrixColoringStrategy(minMax.getFirst(), minMax.getSecond(), 10);
		
	}
	
	/***
	 * Creates a red and blue with min and max as range and no cut off value.
	 * @param matrix
	 * @return
	 */
	public static MatrixColoringStrategy createRedBlueGradientNoCutOff(MI_Matrix matrix) {

		Pair<Double, Double> minMax = MatrixColoringStrategyFactory.getMaxAndMinFromMiMatrix(matrix);
		
		return new TwoColorsGradientMatrixColoringStrategy(Color.blue, Color.red, new Color(0,50,0),Color.white, minMax.getFirst(), minMax.getSecond());
		
	}
	
	////////////////////////////////
	// Private Methods
	private static  Pair<Double,Double> getMaxAndMinFromMiMatrix(MI_Matrix matrix) {
		double min = Double.POSITIVE_INFINITY;
		double max = Double.NEGATIVE_INFINITY;
		
		for (int i=0;i<matrix.getSize()-1;i++){
			
			for (int j=i+1;j<matrix.getSize();j++){

				Double mi = matrix.getValue(i+1,j+1 ).getMi();
				if (mi>-900) {
					min = Math.min(min, mi);
					max = Math.max(max, mi);
				}
				
			}
			
		}
		
		return new Pair<Double, Double>(min, max);
		
	}
	
	
	
}
