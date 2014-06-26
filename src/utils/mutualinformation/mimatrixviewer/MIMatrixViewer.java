package utils.mutualinformation.mimatrixviewer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Rectangle;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import utils.mutualinformation.mimatrixviewer.matrixview.BlackAndWhiteZoomMatrixColoringStrategy;
import utils.mutualinformation.mimatrixviewer.matrixview.ColoringSelectionPane;
import utils.mutualinformation.mimatrixviewer.matrixview.MIMatrixPane;
import utils.mutualinformation.mimatrixviewer.matrixview.MatrixColoringStrategyFactory;
import utils.mutualinformation.mimatrixviewer.matrixview.ZoomMatrixColoringStrategyFactory;
import utils.mutualinformation.mimatrixviewer.matrixview.ZoomPanel;


public class MIMatrixViewer extends JFrame{

	////////////////////////////////
	// Class Constants
	private static final long serialVersionUID = -4648080458360903060L;
	////////////////////////////////
	
	////////////////////////////////
	// Components
	private OptionsPane optionPane;
	private MIMatrixPane matrixPane;
	private ZoomPanel zoomPanel;
	private ColoringSelectionPane coloringPane;
	////////////////////////////////

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				MIMatrixViewer inst = new MIMatrixViewer();
					// creates the main instance
				
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
				inst.setTitle("Mi Matrix Viewer");
				inst.setOptionPane(new OptionsPane(inst));
				inst.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				
				MIMatrixPane matrixPane = new MIMatrixPane(inst);
				
				inst.setZoomPanel(new ZoomPanel(inst));
				
				inst.setMatrixPane(matrixPane);
				
				inst.setLayout(new BorderLayout());
				
				JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
				splitPane.add(inst.getZoomPanel());
				splitPane.add(inst.getMatrixPane());
				inst.getZoomPanel().setColoringStrategy(new BlackAndWhiteZoomMatrixColoringStrategy(10));
				inst.getContentPane().add(splitPane, BorderLayout.CENTER);
				inst.getContentPane().add(inst.getOptionPane(), BorderLayout.NORTH);
				splitPane.setDividerLocation(200);
				
				inst.setColoringPane(new ColoringSelectionPane(inst));
				inst.getContentPane().add(inst.getColoringPane(),BorderLayout.SOUTH);
				inst.pack();
				inst.getGlassPane();
				
					// set swing properties of MainFASDPD
			}
		});

	}
	
	///////////////////////
	// CONSTRUCTOR
	public MIMatrixViewer() {
		super();
		createGUI();
	}

	private void createGUI() {
		
		try {

			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	        	// Set System L&F
			this.loadMainPane();
				// Brings the main pane to screen

		} catch (Exception e) {
			e.printStackTrace();
		}

		
	}
	////////////////////////////

	///////////////////////////////
	// Public interface
	public void processFile(File file) {
		
		if(file.isDirectory() || !file.canRead() || file.length() < 4) {
			return;
		}
		DataInputStream in;
    	boolean isZipFile =  false;

		try {
			in = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
			int test = in.readInt();
		    in.close();
		    isZipFile =  (test == 0x504b0304);
		} catch (IOException e) {
			e.printStackTrace();
		}
		MI_Matrix matrix = null;
		if (isZipFile) {
			matrix = MI_Matrix.loadFromZippedFile(file);
		} else {
			matrix = MI_Matrix.loadFromFile(file);
		}
		
		this.getColoringPane().getMatrixColoringModel().removeAllElements();
		this.getColoringPane().addMatrixColoringStrategy(MatrixColoringStrategyFactory.createRedBlueGradient(matrix));
		this.getColoringPane().addMatrixColoringStrategy(MatrixColoringStrategyFactory.createRedBlueGradientGt10(matrix));
		this.getColoringPane().addMatrixColoringStrategy(MatrixColoringStrategyFactory.createRedBlueGradientNoCutOff(matrix));
		
		this.getColoringPane().getZoomMatrixColoringModel().removeAllElements();
		this.getColoringPane().addZoomMatrixColoringStrategy(ZoomMatrixColoringStrategyFactory.BlackAndWhiteWithStdCutoff());
		this.getColoringPane().addZoomMatrixColoringStrategy(ZoomMatrixColoringStrategyFactory.BlackAndWhiteWithCutoff10());
		
		int[] protLengths = new int[]{matrix.getSize()};
		this.getMatrixPane().setMatrix(matrix);
		this.getMatrixPane().setProteinLengths(protLengths);
		this.getMatrixPane().accomodateSize();
		this.getMatrixPane().resetImage();
		this.getMatrixPane().updateUI();
	}
	
	
	//////////////////////////////////////
	// Getters and Setters
	public OptionsPane getOptionPane() {
		return optionPane;
	}
	public void setOptionPane(OptionsPane optionPane) {
		this.optionPane = optionPane;
	}
	public MIMatrixPane getMatrixPane() {
		return matrixPane;
	}

	public void setMatrixPane(MIMatrixPane matrixPane) {
		this.matrixPane = matrixPane;
	}

	
	public ZoomPanel getZoomPanel() {
		return zoomPanel;
	}

	public void setZoomPanel(ZoomPanel zoomPanel) {
		this.zoomPanel = zoomPanel;
	}

	public ColoringSelectionPane getColoringPane() {
		return coloringPane;
	}

	protected void setColoringPane(ColoringSelectionPane coloringPane) {
		this.coloringPane = coloringPane;
	}

	/////////////////////////////////////
	// protected and private methods
	private void loadMainPane() {
	    this.setContentPane(new JPanel());
		Insets cpinsets = getContentPane().getInsets();
		this.getContentPane().setSize(800, 600);
		setSize(400, 300);
		Insets frameinsets = this.getInsets();
		this.setMinimumSize(new Dimension(1024 + cpinsets.left   + frameinsets.left  + 
				                                 cpinsets.right  + frameinsets.right  
				                          ,768 + cpinsets.top    + frameinsets.top   +
				                                 cpinsets.bottom + frameinsets.bottom));
		
	}

	public void zoomArea(Rectangle rect, double[][] values, char[] hChars, char[] vChars) {
		this.getZoomPanel().renderImage(values, hChars, vChars);
		
	}

	public void ColorSelected() {
		this.getMatrixPane().resetImage();
		this.getMatrixPane().updateUI();
		
	}
}
