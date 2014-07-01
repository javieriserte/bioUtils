package utils.mutualinformation.mimatrixviewer.matrixview;

import java.awt.BorderLayout;
import java.awt.Rectangle;

import javax.swing.JPanel;
import javax.swing.JSplitPane;

import utils.mutualinformation.mimatrixviewer.MI_Matrix;

public class MatrixViewMainPane extends JPanel{


	/////////////////////////////////////////
	// Class constants
	private static final long serialVersionUID = -1703838873771510760L;
	/////////////////////////////////////////
	
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


	public MatrixViewMainPane() {
		super();
		this.setMatrixPane(new MIMatrixPane(this));
		this.setZoomPanel(new ZoomPanel(this));
		this.setColoringPane(new ColoringSelectionPane(this));

		this.setLayout(new BorderLayout());
		
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		
		splitPane.add(this.getZoomPanel());
		splitPane.add(this.getMatrixPane());
		
		this.getZoomPanel().setColoringStrategy(new BlackAndWhiteZoomMatrixColoringStrategy(10));

		this.add(splitPane, BorderLayout.CENTER);
		this.add(this.getColoringPane(),BorderLayout.SOUTH);
		
		splitPane.setDividerLocation(200);

		
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
	public MIMatrixPane getMatrixPane() {
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
	

	public void zoomArea(Rectangle rect, double[][] values, char[] hChars, char[] vChars) {
		this.getZoomPanel().renderImage(values, hChars, vChars);
		
	}

	public void setMatrix(MI_Matrix matrix) {
		this.getColoringPane().getMatrixColoringModel().removeAllElements();
		this.getColoringPane().addMatrixColoringStrategy(MatrixColoringStrategyFactory.createRedBlueGradient(matrix));
		this.getColoringPane().addMatrixColoringStrategy(MatrixColoringStrategyFactory.createRedBlueGradientGt10(matrix));
		this.getColoringPane().addMatrixColoringStrategy(MatrixColoringStrategyFactory.createRedBlueGradientNoCutOff(matrix));
		
		this.getColoringPane().getZoomMatrixColoringModel().removeAllElements();
		this.getColoringPane().addZoomMatrixColoringStrategy(ZoomMatrixColoringStrategyFactory.BlackAndWhiteWithStdCutoff());
		this.getColoringPane().addZoomMatrixColoringStrategy(ZoomMatrixColoringStrategyFactory.BlackAndWhiteWithCutoff10());
		
	}

	public void setProteinLengths(int[] protLengths) {
		// TODO Auto-generated method stub
		
	}

}
