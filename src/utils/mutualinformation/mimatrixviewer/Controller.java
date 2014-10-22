package utils.mutualinformation.mimatrixviewer;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Observer;

public class Controller {
	
	private Model model;

	
	
	
	public Controller(Model model) {
		super();
		this.model = model;
	}

	/**
	 * add data files to the model
	 * @param selectedFiles
	 */
	public void addDataFiles(File[] selectedFiles) {
		
		for (File file : selectedFiles) {
			
			MIMatrixReader reader = new MIMatrixReader();
			
			MI_Matrix matrix;
			try {
				matrix = reader.read(file);
				this.model.addDataContainer(new DataContainer(matrix, file.getName(), null, null));
								
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	
	public void registerModelObserver(Observer o) {
		
		this.model.addObserver(o);
		
	}

	public void setActiveData(DataContainer value) {
		
		this.model.setCurrentData(value);
		
	}

	public int getCurrentMatrixSize() {
		return this.model.getCurrentData().getData().getSize();
	}

	public void setCurrentMatrixNames(List<String> names) {
		String[] namesArray = new String[names.size()];
		
		int indexCounter = 0;
		for (String name : names) {
			namesArray[indexCounter] = name;
			indexCounter++;
		}
		
		this.model.getCurrentData().setProteinNames(namesArray);		
		
	}

	public void setCurrentMatrixLengths(List<Integer> lengths) {
		int[] lengthsArray = new int[lengths.size()];
		
		int indexCounter = 0;
		
		for (Integer length : lengths) {
			
			lengthsArray[indexCounter] = length;
			
			indexCounter++;
			
		}
		
		this.model.getCurrentData().setLengths(lengthsArray);
		
	}

	
	
}
