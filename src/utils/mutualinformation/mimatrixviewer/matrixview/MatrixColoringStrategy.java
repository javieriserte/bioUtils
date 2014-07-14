package utils.mutualinformation.mimatrixviewer.matrixview;

import java.awt.Color;

import utils.mutualinformation.misticmod.datastructures.MI_Position;

public interface MatrixColoringStrategy {

	public Color getColor(MI_Position p1);
	
}
