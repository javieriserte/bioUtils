package utils.mutualinformation.mimatrixviewer;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ColoringSelectionPane extends JPanel {
	
	////////////////////////////////
	// Class Variables and Constants
	private static final long serialVersionUID = 345483844902809702L;
	////////////////////////////////

	////////////////////////////////
	// Components
	JComboBox<MatrixColoringStrategy> matrixColoringComboBox;
	JComboBox<ZoomMatrixColoringStrategy> zoomMatrixColoringComboBox;
	////////////////////////////////
	
	///////////////////////////////
	// Instance Variables
	private List<MatrixColoringStrategy> matrixColoringModel;
	private List<ZoomMatrixColoringStrategy> zoomMatrixColoringModel;
	///////////////////////////////
	
	///////////////////////////////
	// Constructor
	public ColoringSelectionPane() {
		super();
		this.createGUI();
	}
	
	///////////////////////////////

	////////////////////////////////
	// Getters and Setters
	public List<MatrixColoringStrategy> getMatrixColoringModel() {
		return matrixColoringModel;
	}

	public void setMatrixColoringModel(List<MatrixColoringStrategy> matrixColoringModel) {
		this.matrixColoringModel = matrixColoringModel;
	}

	public List<ZoomMatrixColoringStrategy> getZoomMatrixColoringModel() {
		return zoomMatrixColoringModel;
	}

	public void setZoomMatrixColoringModel(List<ZoomMatrixColoringStrategy> zoomMatrixColoringModel) {
		this.zoomMatrixColoringModel = zoomMatrixColoringModel;
	}
	public JComboBox<MatrixColoringStrategy> getMatrixColoringComboBox() {
		return matrixColoringComboBox;
	}

	public void setMatrixColoringComboBox(
			JComboBox<MatrixColoringStrategy> matrixColoringComboBox) {
		this.matrixColoringComboBox = matrixColoringComboBox;
	}

	public JComboBox<ZoomMatrixColoringStrategy> getZoomMatrixColoringComboBox() {
		return zoomMatrixColoringComboBox;
	}

	public void setZoomMatrixColoringComboBox(
			JComboBox<ZoomMatrixColoringStrategy> zoomMatrixColoringComboBox) {
		this.zoomMatrixColoringComboBox = zoomMatrixColoringComboBox;
	}

	///////////////////////////////

	///////////////////////////////
	// Private and protected methods
	private void createGUI() {
		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints constraints = new GridBagConstraints();
		layout.columnWidths = new int[]{50,100,20,50,100};
		layout.columnWeights = new double[]{0,1,0,0,1};
		layout.rowHeights = new  int[]{25};
		
		//////////////////////////
		// Configure Components
		constraints.fill = constraints.BOTH;
		constraints.gridheight = 1;
		constraints.gridwidth = 1;
		constraints.gridy = 0;
		this.setLayout(layout);
		constraints.gridx = 0;

		JLabel labelMatrixColoring = new JLabel("Normal Matrix colors:");
		this.add(labelMatrixColoring);

		this.setMatrixColoringModel(matrixColoringModel);
		
//		this.getMatrixColoringModel()
		
		this.getMatrixColoringModel();
		
		ComboBoxModel<MatrixColoringStrategy> matrixColoringModel = new DefaultComboBoxModel<>();
		
		this.setMatrixColoringComboBox(new JComboBox<MatrixColoringStrategy>());
		
		constraints.gridx = 3;
		JLabel labelZoomMatrixColoring = new JLabel("Zoom Matrix colors:");
		this.add(labelZoomMatrixColoring);
		
		
	}
	////////////////////////////////
	
}
