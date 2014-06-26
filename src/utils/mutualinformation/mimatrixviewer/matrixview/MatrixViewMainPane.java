package utils.mutualinformation.mimatrixviewer.matrixview;

import java.awt.LayoutManager;

import javax.swing.JPanel;

import utils.mutualinformation.mimatrixviewer.MI_Matrix;

public class MatrixViewMainPane extends JPanel{

	/////////////////////////////////////////
	// Instance variables
	private MI_Matrix data;
	/////////////////////////////////////////
	
	/////////////////////////////////////////
	// Components
	private MIMatrixPane            matrixPane;
	private ZoomPanel                zoomPanel;
	private ColoringSelectionPane coloringPane;
	/////////////////////////////////////////

	/////////////////////////////////////////
	// Class constants
	private static final long serialVersionUID = -1703838873771510760L;
	/////////////////////////////////////////

	public MatrixViewMainPane() {
		super();
		this.setMatrixPane(new MIMatrixPane(viewer));
		
		
	}

	/**
	 * @return the data
	 */
	protected MI_Matrix getData() {
		return data;
	}

	/**
	 * @param data the data to set
	 */
	protected void setData(MI_Matrix data) {
		this.data = data;
	}

	/**
	 * @return the matrixPane
	 */
	protected MIMatrixPane getMatrixPane() {
		return matrixPane;
	}

	/**
	 * @param matrixPane the matrixPane to set
	 */
	protected void setMatrixPane(MIMatrixPane matrixPane) {
		this.matrixPane = matrixPane;
	}

	/**
	 * @return the zoomPanel
	 */
	protected ZoomPanel getZoomPanel() {
		return zoomPanel;
	}

	/**
	 * @param zoomPanel the zoomPanel to set
	 */
	protected void setZoomPanel(ZoomPanel zoomPanel) {
		this.zoomPanel = zoomPanel;
	}

	/**
	 * @return the coloringPane
	 */
	protected ColoringSelectionPane getColoringPane() {
		return coloringPane;
	}

	/**
	 * @param coloringPane the coloringPane to set
	 */
	protected void setColoringPane(ColoringSelectionPane coloringPane) {
		this.coloringPane = coloringPane;
	}

}
