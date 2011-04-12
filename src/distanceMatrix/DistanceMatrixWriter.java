package distanceMatrix;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


public abstract class DistanceMatrixWriter {
	private int decimalPlaces=6;
	private DistanceMatrix distances=null;
	
	
	// CONSTRUCTORES
	
	public DistanceMatrixWriter() {
		super();
	}

	public DistanceMatrixWriter(int decimalPlaces) {
		super();
		this.setDecimalPlaces(decimalPlaces);
	}

	// GETTERS AND SETTERS
	
	protected int getDecimalPlaces() {
		return decimalPlaces;
	}

	protected void setDecimalPlaces(int decimalPlaces) {
		this.decimalPlaces = decimalPlaces;
	}

	protected void setDistances(DistanceMatrix distances) {
		this.distances = distances;
	}

	protected DistanceMatrix getDistances() {
		return distances;
	}
	
	// METODOS DE INSTANCIA
	public void printToFile(DistanceMatrix distanceMatrix, String filepath) {
		// template method
		this.setDistances(distanceMatrix);
		String output = this.prepareOutputFile();

	    try {
	        BufferedWriter out = new BufferedWriter(new FileWriter(filepath));
	        out.write(output);
	        out.close();
	    } catch (IOException e) {
	    }
		
	}

	protected abstract String prepareOutputFile(); 

}
