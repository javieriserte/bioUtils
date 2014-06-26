package utils.mutualinformation.mimatrixviewer.matrixview;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import utils.mutualinformation.mimatrixviewer.MIMatrixViewer;

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
	private DefaultComboBoxModel<MatrixColoringStrategy> matrixColoringModel;
	private DefaultComboBoxModel<ZoomMatrixColoringStrategy> zoomMatrixColoringModel;
	private MIMatrixViewer viewer;
	///////////////////////////////
	
	///////////////////////////////
	// Constructor
	public ColoringSelectionPane(MIMatrixViewer viewer) {
		super();
		this.setViewer(viewer);
		this.createGUI();
	}
	
	///////////////////////////////

	///////////////////////////////
	// Public Interface
	public void addMatrixColoringStrategy(MatrixColoringStrategy coloring){

		this.getMatrixColoringModel().addElement(coloring);
		
	}
	
	public void addZoomMatrixColoringStrategy(ZoomMatrixColoringStrategy coloring){

		this.getZoomMatrixColoringModel().addElement(coloring);
		
	}
	
	///////////////////////////////
	
	////////////////////////////////
	// Getters and Setters
	public DefaultComboBoxModel<MatrixColoringStrategy> getMatrixColoringModel() {
		return matrixColoringModel;
	}

	public void setMatrixColoringModel(DefaultComboBoxModel<MatrixColoringStrategy> matrixColoringModel) {
		this.matrixColoringModel = matrixColoringModel;
	}

	public DefaultComboBoxModel<ZoomMatrixColoringStrategy> getZoomMatrixColoringModel() {
		return zoomMatrixColoringModel;
	}

	public void setZoomMatrixColoringModel(DefaultComboBoxModel<ZoomMatrixColoringStrategy> zoomMatrixColoringModel) {
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
	

	public MIMatrixViewer getViewer() {
		return viewer;
	}

	public void setViewer(MIMatrixViewer viewer) {
		this.viewer = viewer;
	}

	///////////////////////////////
	// Private and protected methods
	private void createGUI() {

		//////////////////////////
		// General Laout Settings
		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints constraints = new GridBagConstraints();
		layout.columnWidths = new int[]{50,100,20,50,100};
		layout.columnWeights = new double[]{0,1,0,0,1};
		layout.rowHeights = new  int[]{25};
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridheight = 1;
		constraints.gridwidth = 1;
		constraints.gridy = 0;
		this.setLayout(layout);
		///////////////////////////
		
		//////////////////////////
		// Configure Components
		// Label First ComboBox
		constraints.gridx = 0;
		JLabel labelMatrixColoring = new JLabel("Normal Matrix colors:");
		this.add(labelMatrixColoring);

		// First ComboBox
		constraints.gridx = 1;
		this.setMatrixColoringModel(new DefaultComboBoxModel<MatrixColoringStrategy>());
		this.setMatrixColoringComboBox(new JComboBox<>(this.getMatrixColoringModel()));
		ColorSelectedActionListener colorSelectAction = new ColorSelectedActionListener();
		this.getMatrixColoringComboBox().addActionListener(colorSelectAction );
		this.add(this.getMatrixColoringComboBox());
		
		// Label second ComboBox		
		constraints.gridx = 3;
		JLabel labelZoomMatrixColoring = new JLabel("Zoom Matrix colors:");
		this.add(labelZoomMatrixColoring);
		
		// First ComboBox
		constraints.gridx = 4;
		this.setZoomMatrixColoringModel(new DefaultComboBoxModel<ZoomMatrixColoringStrategy>());
		this.setZoomMatrixColoringComboBox(new JComboBox<>(this.getZoomMatrixColoringModel()));
		this.getZoomMatrixColoringComboBox().addActionListener(colorSelectAction);
		this.add(this.getZoomMatrixColoringComboBox());
		//////////////////////////////
	}
	////////////////////////////////

	////////////////////////////////
	// Auxiliary Classes
	private class ColorSelectedActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			ColoringSelectionPane.this.getViewer().ColorSelected();
			
		}
		
	}
	
}
