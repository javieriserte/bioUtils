package utils.mutualinformation.mimatrixviewer;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class Model extends Observable {

	////////////////////////////////////////////////////////////////////////////
	// Instance Variables
	private List<DataContainer> data;
	private DataContainer currentData;
	////////////////////////////////////////////////////////////////////////////

	////////////////////////////////////////////////////////////////////////////
	// Contructor
	public Model() {
		
		this.setData(new ArrayList<DataContainer>());
		this.setCurrentData(null);
		
	}
	////////////////////////////////////////////////////////////////////////////
	
	
	////////////////////////////////////////////////////////////////////////////
	// Public interface
	public List<DataContainer> getData() {
		return data;
	}
	public void setData(List<DataContainer> data) {
		this.data = data;
		this.setChanged();
		this.notifyObservers();
		this.clearChanged();
	}
	public DataContainer getCurrentData() {
		return currentData;
	}
	public void setCurrentData(DataContainer currentData) {
		this.currentData = currentData;
		this.setChanged();
		this.notifyObservers();
		this.clearChanged();
	}
	public void addDataContainer(DataContainer data) {
		this.getData().add(data);
		this.setChanged();
		this.notifyObservers();
		this.clearChanged();
	}
	////////////////////////////////////////////////////////////////////////////
	
}
