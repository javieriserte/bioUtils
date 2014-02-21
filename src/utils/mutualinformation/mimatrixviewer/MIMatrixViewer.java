package utils.mutualinformation.mimatrixviewer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Rectangle;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;


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
				
//				File infile = new File("/home/javier/Dropbox/Posdoc/HIV.Segundo.Analisis/mi_mf_cl/mi_cl_lc_data");
//				MI_Matrix matrix = MI_Matrix.loadFromFile(infile);

//				File infile = new File("b:\\javier\\Dropbox\\Posdoc\\HIV.Segundo.Analisis\\mi_mf_cl\\mi_cl_lc_data");
//				int[] protLengths = new int[]{578, 211, 106, 334, 67, 55, 103, 38, 68};
				
				
				MIMatrixPane matrixPane = new MIMatrixPane(inst);
				
				inst.setZoomPanel(new ZoomPanel());
				
				inst.setMatrixPane(matrixPane);
//				inst.getMatrixPane().setProteinLengths(protLengths);
				MatrixColoringStrategy color = new RedBlueGradientMatrixColoringStrategy(-10,50,6.5);
				
				inst.getMatrixPane().setColor(color);
				
//				inst.getMatrixPane().setMatrix(matrix);
//				inst.getMatrixPane().setPreferredSize(inst.getMatrixPane().getMinimumSize());
				
				inst.setLayout(new BorderLayout());
				
				JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
				splitPane.add(inst.getZoomPanel());
				splitPane.add(inst.getMatrixPane());
				inst.getZoomPanel().setColoringStrategy(new BlackAndWhiteZoomMatrixColoringStrategy());
				inst.getContentPane().add(splitPane, BorderLayout.CENTER);
				inst.getContentPane().add(inst.getOptionPane(), BorderLayout.NORTH);
				splitPane.setDividerLocation(200);
				inst.pack();
				
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
		MI_Matrix matrix = MI_Matrix.loadFromFile(file);
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
}
