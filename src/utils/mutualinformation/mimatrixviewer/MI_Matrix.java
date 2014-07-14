package utils.mutualinformation.mimatrixviewer;

import io.onelinelister.OneLineListReader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import utils.mutualinformation.misticmod.datastructures.MI_Position;
import utils.mutualinformation.misticmod.datastructures.MI_PositionLineParser;

/**
 * Class to represent a square MI matrix.
 * First position of the matrix is one.
 * @author javier iserte
 *
 */
public class MI_Matrix {
	public static double UNDEFINED = Double.NEGATIVE_INFINITY; 
	
	private double     [] mi;
	private double     [] apc;
	private double     [] zscore;
	
	private double        minMi;
	private double        minApc;
	private double        minZscore;

	private double        maxMi;
	private double        maxApc;
	private double        maxZscore;
	
	private char[]        refSequence;
	
	private MI_Position[] miValues; // From old representation 
	private int size;
	
	/////////////////////////////////
	// Constructors
	public MI_Matrix(int size) {
		this.setSize(size);
		MI_Position[] values = new MI_Position[sum(size-1)];
		this.setMIValues(values);
	}
	
	public MI_Matrix(int size, boolean useMI, boolean useAPC, boolean useZscore) {
		
		this.setSize(size);
		
		int totalPositions = this.sum (this.getSize() -1);
		
		this.setMi( useMI?new double[totalPositions]:null);
		this.setApc( useAPC?new double[totalPositions]:null);
		this.setZscore( useZscore?new double[totalPositions]:null);
		
	}
	
	/////////////////////////////////
	// Public Interface
	
	public double getMIValue(int position1, int position2) {
		
		return this.getGenericValue(position1, position2, this.getMi());
	}
	
	


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
	
	public int count() {
		return this.sum(this.getSize());
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
	
	protected double[] getMi() {
		return mi;
	}

	protected void setMi(double[] mi) {
		this.mi = mi;
	}

	protected double[] getApc() {
		return apc;
	}

	protected void setApc(double[] apc) {
		this.apc = apc;
	}

	protected double[] getZscore() {
		return zscore;
	}

	protected void setZscore(double[] zscore) {
		this.zscore = zscore;
	}

	protected double getMinMi() {
		return minMi;
	}

	protected void setMinMi(double minMi) {
		this.minMi = minMi;
	}

	protected double getMinApc() {
		return minApc;
	}

	protected void setMinApc(double minApc) {
		this.minApc = minApc;
	}

	protected double getMinZscore() {
		return minZscore;
	}

	protected void setMinZscore(double minZscore) {
		this.minZscore = minZscore;
	}

	protected double getMaxMi() {
		return maxMi;
	}

	protected void setMaxMi(double maxMi) {
		this.maxMi = maxMi;
	}

	protected double getMaxApc() {
		return maxApc;
	}

	protected void setMaxApc(double maxApc) {
		this.maxApc = maxApc;
	}

	protected double getMaxZscore() {
		return maxZscore;
	}

	protected void setMaxZscore(double maxZscore) {
		this.maxZscore = maxZscore;
	}

	protected char[] getRefSequence() {
		return refSequence;
	}

	protected void setRefSequence(char[] refSequence) {
		this.refSequence = refSequence;
	}

	protected MI_Position[] getMiValues() {
		return miValues;
	}

	protected void setMiValues(MI_Position[] miValues) {
		this.miValues = miValues;
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
	
	private double getGenericValue(int position1, int position2, double[] dataMatrix) {
		
		int transformedPosition = this.translateCoordinates(position1,position2);
		
		if (transformedPosition<0 || transformedPosition>this.count()) {
			
			return MI_Matrix.UNDEFINED;
			
		} else {
			
			return dataMatrix[transformedPosition];
			
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
	
	public static MI_Matrix loadFromZippedFile(File infile){
		
		ZipFile zf;
		try {
			
			zf = new ZipFile(infile);
			Enumeration<? extends ZipEntry> entries = zf.entries();
			
			InputStream fis = zf.getInputStream(entries.nextElement());
			List<MI_Position> values = (new OneLineListReader<MI_Position>(new MI_PositionLineParser())).readZipped(fis);
	
			int matrixSize = (int)(Math.sqrt(values.size()*8+1)+1)/2;
			MI_Matrix matrix = new MI_Matrix(matrixSize);
			for (MI_Position mi_Position : values) {
				matrix.setValue(mi_Position.getPos1(), mi_Position.getPos2(), mi_Position);
			}
			zf.close();
			return matrix;
			
		} catch (IOException e1) {
			e1.printStackTrace();
		} 
		return null;
	}
}
