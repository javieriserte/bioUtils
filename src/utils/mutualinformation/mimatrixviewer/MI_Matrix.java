package utils.mutualinformation.mimatrixviewer;

import io.onelinelister.OneLineListReader;

import java.io.File;
import java.util.List;

import utils.mutualinformation.misticmod.MI_Position;
import utils.mutualinformation.misticmod.MI_PositionLineParser;

/**
 * Class to represent a square MI matrix.
 * First position of the matrix is one.
 * @author javier iserte
 *
 */
public class MI_Matrix {
	private MI_Position[] miValues;
	private int size;
	
	/////////////////////////////////
	// Constructor
	public MI_Matrix(int size) {
		this.setSize(size);
		MI_Position[] values = new MI_Position[sum(size-1)];
		this.setMIValues(values);
	}
	
	/////////////////////////////////
	// Public Interface
	public MI_Position getValue(int p1, int p2) {
		int pos = translateCoordinates(p1,p2);
		if (pos<0) {
			return null;
		} else {
			return this.getMIValues()[pos];
		}
	}
	public void setValue(int p1, int p2, MI_Position mi_Position) {
		int pos = translateCoordinates(p1,p2);
		if (pos>=0) {
			this.getMIValues()[pos] = mi_Position;
		}
	}
	
	/////////////////////////////////
	// Getters and Setters
	private void setMIValues(MI_Position[] MIValues) {
		this.miValues = MIValues;
	}
	private MI_Position[] getMIValues() {
		return this.miValues;
	}

	public int getSize() {
		return size;
	}

	private void setSize(int size) {
		this.size = size;
	}
	
	////////////////////////////////
	// Private Methods
	private int sum(int i) {
		return i*(i+1)/2;
	}
	private int translateCoordinates(int p1, int p2) {
		int diff = p2-p1;
		if (diff==0) {
			return -1;
		} else 
		if (diff>0) {
			return sum(p2-2)+p1-1;
		} else {
			return sum(p1-2)+p2-1;
		}
	}
	
	///////////////////////////////////
	// Class Methods
	public static MI_Matrix loadFromFile(File infile){
		List<MI_Position> values = (new OneLineListReader<MI_Position>(new MI_PositionLineParser())).read(infile);
		int matrixSize = (int)(Math.sqrt(values.size()*8+1)+1)/2;
		MI_Matrix matrix = new MI_Matrix(matrixSize);
		for (MI_Position mi_Position : values) {
			matrix.setValue(mi_Position.getPos1(), mi_Position.getPos2(), mi_Position);
		}
		return matrix;
	}
}
