package utils.mutualinformation.mimatrixviewer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class to represent a square MI matrix.
 * First position of the matrix is one.
 * @author javier iserte
 *
 */
public class MI_Matrix {
	public static final double UNDEFINED = Double.NEGATIVE_INFINITY; 
	
	private double     [] mi;
	private double     [] apc;
	private double     [] zscore;
	
	private double        minMi;
	private double        minApc;
	private double        minZscore;

	private double        maxMi;
	private double        maxApc;
	private double        maxZscore;
	
	private char[]        referenceSequence;
	
	private Map<Integer,Integer> residueNumberLabelToIndexMap;
	// Residue numbers in MI data files are treated like labels.
	// this map is a reference fot these label numbers to
	// actual positions in the matrix.

	private int           size;
	// MiMatrix is a (n x n) square matrix,
	// size is n.
	
	private int           definedValues;
	// Is the number of values being actually calculated

	private boolean[]     needRecalculate;
	// Keep tracks of modifications made on the matrix in order to know if
	// Min, Max, NumberOfDefined and NumberOfUndefined values need to 
	// be recalculated.
	// needRecalculate matrix should store four values:
	// index 0 : for Mi recalculation
	// index 1 : for Apc recalculation
	// index 2 : for Z-score recalculation
	// index 3 : for NumberOfDefinedValue recalculation
	




	/////////////////////////////////
	// Constructors
	public MI_Matrix(int size, boolean useMI, boolean useAPC, boolean useZscore, List<Integer> residueLabels) {
		
		this.setSize(size);
		
		int totalPositions = this.sum (this.getSize() -1);
		
		this.setMi( useMI?new double[totalPositions]:null);
		this.setApc( useAPC?new double[totalPositions]:null);
		this.setZscore( useZscore?new double[totalPositions]:null);
		
		
		this.setResidueNumberLabelToIndexMap(this.createResidueNumberLabelMap(residueLabels));
		this.setNeedRecalculate(new boolean[]{true,true,true,true});

	}
	


	////////////////////////////////////////////////////////////////////////////
	// Public Interface
	public int count() {
		return this.sum(this.getSize()-1);
	}
	
	public int countDefinedValues() {
		
		if (this.needRecalculateDefinedValues()) {
			
			int ndef=0;
			for (double value : this.getZscore()) {
				
				ndef +=(value==MI_Matrix.UNDEFINED)?0:1;
				
			}
			
			this.setDefinedValues(ndef);
			
		}
		
		return this.definedValues;
		
	}
	
	public char[] getReferenceSequence() {
		return this.referenceSequence;
	}
	
	public char getReferenceSequenceCharAt(int position) {
		
		int indexForLabel = this.getResidueNumberLabelToIndexMap().get(position);
		
		return this.getReferenceSequence()[indexForLabel-1];
		
	}
	
	
	public String getReferenceSequenceAsString() {
		return String.valueOf(this.getReferenceSequence());
	}
	
	
	public void setReferenceSequence(String refSequence) {
		this.referenceSequence = refSequence.toCharArray();
	}

	public void setReferenceSequence(char[] refSequence) {
		this.referenceSequence = refSequence;
	}
	
	public double getMIValue(int position1, int position2) {
		
		return this.getGenericValue(position1, position2, this.getMi());
	
	}
		
	public void setMIValue(double newMiValu, int position1, int position2) {
		
		this.setGenericValue(newMiValu, position1, position2, this.getMi());
	
	}
	
	public double getZscoreValue(int position1, int position2) {
		
		return this.getGenericValue(position1, position2, this.getZscore());
	
	}
		
	public void setZscoreValue(double newMiValu, int position1, int position2) {
		
		this.setGenericValue(newMiValu, position1, position2, this.getZscore());
	
	}
	
	public double getApcValue(int position1, int position2) {
		
		return this.getGenericValue(position1, position2, this.getApc());
	
	}
		
	public void setApcValue(double newMiValu, int position1, int position2) {
		
		this.setGenericValue(newMiValu, position1, position2, this.getApc());
	
	}
	
	public int getSize() {
		return size;
	}
	
	public double[] getMi() {
		return mi;
	}

	public double[] getApc() {
		return apc;
	}

	public double[] getZscore() {
		return zscore;
	}
	
	public double getMaxZscore() {
		if (this.needRecalculateZscore()) {
		
			this.calculateMinMaxZscore();
			
			this.getNeedRecalculate()[3]=false;
			
		}
		return this.maxZscore;
		
	}
	
	public double getMinZscore() {
		if (this.needRecalculateZscore()) {
		
			this.calculateMinMaxZscore();
			
			this.getNeedRecalculate()[2]=false;
			
		}
		return this.minZscore;
		
	}
	
	public double getMaxMI() {
		if (this.needRecalculateMi()) {
		
			this.calculateMinMaxMI();
			
			this.getNeedRecalculate()[0]=false;
			
		}
		return this.maxMi;
		
	}
	public double getMinMI() {
		if (this.needRecalculateMi()) {
		
			this.calculateMinMaxMI();
			
			this.getNeedRecalculate()[0]=false;
			
		}
		return this.minMi;
		
	}
	public double getMaxAPC() {
		if (this.needRecalculateMi()) {
		
			this.calculateMinMaxMI();
			
			this.getNeedRecalculate()[1]=false;
			
		}
		return this.maxApc;
		
	}
	public double getMinAPC() {
		if (this.needRecalculateApc()) {
		
			this.calculateMinMaxAPC();
			
			this.getNeedRecalculate()[1]=false;
			
		}
		return this.minApc;
		
	}
	// End of public interface
	////////////////////////////////////////////////////////////////////////////
	
	////////////////////////////////////////////////////////////////////////////
	// Private Methods
	private void calculateMinMaxZscore() {

		this.setMaxZscore(Double.MIN_VALUE);
		this.setMinZscore(Double.MAX_VALUE);
		
		double localMax = this.maxZscore;
		double localMin = this.minZscore;
		
		for (double value : this.getZscore()) {
			
			if (value>localMax && value!=MI_Matrix.UNDEFINED) {
				localMax = value;
			}
			

			if (value<localMin && value!=MI_Matrix.UNDEFINED) {
				localMin = value;
			}
			
		}
		
		this.setMaxZscore(localMax);
		this.setMinZscore(localMin);

	}
	private void calculateMinMaxMI() {

		this.setMaxMi(Double.MIN_VALUE);
		this.setMinMi(Double.MAX_VALUE);
		
		double localMax = this.maxMi;
		double localMin = this.minMi;
		
		for (double value : this.getMi()) {
			
			if (value>localMax && value!=MI_Matrix.UNDEFINED) {
				localMax = value;
			}
			

			if (value<localMin && value!=MI_Matrix.UNDEFINED) {
				localMin = value;
			}
			
		}
		
		this.setMaxMi(localMax);
		this.setMinMi(localMin);

	}
	
	private void calculateMinMaxAPC() {

		this.setMaxZscore(Double.MIN_VALUE);
		this.setMinZscore(Double.MAX_VALUE);
		
		double localMax = this.maxApc;
		double localMin = this.minApc;
		
		for (double value : this.getApc()) {
			
			if (value>localMax && value!=MI_Matrix.UNDEFINED) {
				localMax = value;
			}
			

			if (value<localMin && value!=MI_Matrix.UNDEFINED) {
				localMin = value;
			}
			
		}
		
		this.setMaxApc(localMax);
		this.setMinApc(localMin);

	}

	private void setMi(double[] mi) {
		this.mi = mi;
	}

	private void setApc(double[] apc) {
		this.apc = apc;
	}
	private void setZscore(double[] zscore) {
		this.zscore = zscore;
	}

	private void setMinZscore(double minZscore) {
		this.minZscore = minZscore;
	}
	private void setMaxApc(double maxApc) {
		this.maxApc = maxApc;
	}
	private void setMaxZscore(double maxZscore) {
		this.maxZscore = maxZscore;
	}

	private void setMinApc(double minApc) {
		this.minApc = minApc;
	}

	private void setMaxMi(double maxMi) {
		this.maxMi = maxMi;
	}

	private void setSize(int size) {
		this.size = size;
	}
	private void setMinMi(double minMi) {
		this.minMi = minMi;
	}

	private void setDefinedValues(int nDefined) {
		this.definedValues = nDefined;
	}

	private boolean[] getNeedRecalculate() {
		return needRecalculate;
	}
	
	private boolean needRecalculateMi() {
		return this.getNeedRecalculate()[0];
	}
	
	private boolean needRecalculateApc() {
		return this.getNeedRecalculate()[1];
	}
	
	private boolean needRecalculateZscore() {
		return this.getNeedRecalculate()[2];
	}
	
	private boolean needRecalculateDefinedValues() {
		return this.getNeedRecalculate()[3];
	}

	private int sum(int i) {
		return i*(i+1)/2;
	}
	
	private void setNeedRecalculate(boolean[] needRecalculate) {
		this.needRecalculate = needRecalculate;
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
		
		int translatedPosition = this.translateCoordinates(
				this.getResidueNumberLabelToIndexMap().get(position1),
				this.getResidueNumberLabelToIndexMap().get(position2));
		
		if (translatedPosition<0 || translatedPosition > this.count() || dataMatrix == null) {
			
			return MI_Matrix.UNDEFINED;
			
		} else {
			
			return dataMatrix[translatedPosition];
			
		}

	}
	
	private void setGenericValue(double newValue, int position1, int position2, double[] dataMatrix) {
		
		int translatedPosition = this.translateCoordinates(
				this.getResidueNumberLabelToIndexMap().get(position1),
				this.getResidueNumberLabelToIndexMap().get(position2));
		
		if (translatedPosition>=0 && translatedPosition<=this.count() && dataMatrix != null) {
			
			dataMatrix[translatedPosition] = newValue;
			
			this.getNeedRecalculate()[0]=true;
			this.getNeedRecalculate()[1]=true;
			this.getNeedRecalculate()[2]=true;
			this.getNeedRecalculate()[3]=true;
			
		}

	}
	
	private Map<Integer, Integer> getResidueNumberLabelToIndexMap() {
		return residueNumberLabelToIndexMap;
	}

	private void setResidueNumberLabelToIndexMap(
			Map<Integer, Integer> residueNumberLabelToIndexMap) {
		this.residueNumberLabelToIndexMap = residueNumberLabelToIndexMap;
	}


	private Map<Integer, Integer> createResidueNumberLabelMap(
			List<Integer> residueLabels) {
		
		Map<Integer, Integer> result = new HashMap<Integer, Integer>();
		int counter=1;
		for (Integer integer : residueLabels) {
			
			result.put(integer, counter);
			counter++;
			
		}
		return result;
	}

}
