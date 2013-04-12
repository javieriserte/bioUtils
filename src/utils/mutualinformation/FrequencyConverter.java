package utils.mutualinformation;

import java.util.Map;

public abstract class FrequencyConverter {

	protected int numberOfColumns;

	public abstract void convertToFrequencies(Map<Object, Double> freq, int numberOfElements);

	public int getNumberOfColumns() {
		
		return numberOfColumns;
		
	}

	public void setNumberOfColumns(int numberOfColumns) {
		this.numberOfColumns = numberOfColumns;
	}
	
}
