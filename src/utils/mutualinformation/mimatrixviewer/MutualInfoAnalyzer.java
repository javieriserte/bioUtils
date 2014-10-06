package utils.mutualinformation.mimatrixviewer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import utils.mutualinformation.mimatrixviewer.matrixview.MatrixViewMainPane;

public class MutualInfoAnalyzer extends JFrame {

	private static final long serialVersionUID = -4941234241763767073L;
	
	private GeneralDataPane generalDataPane;
	private GraphicViewerPane graphicViewerPane;
	private List<MI_Matrix> data;
	private MI_Matrix currentData;
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				MutualInfoAnalyzer inst = new MutualInfoAnalyzer();
					// creates the main instance
				
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
				inst.setTitle("Mutual Info Analyzer");
//				inst.setOptionPane(new OptionsPane(inst));
				inst.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				
//				MatrixViewMainPane matrixPane = new MatrixViewMainPane();
				
//				inst.setLayout(new BorderLayout());
				
//				inst.getContentPane().add(inst.getOptionPane(), BorderLayout.NORTH);

	//			inst.setMatrixView(matrixPane);
				
//				inst.getContentPane().add(inst.getMatrixPane(),BorderLayout.CENTER);

				inst.pack();
//				inst.getGlassPane();
				
					// set swing properties of MainFASDPD
			}
		});

	}
	
	
	////////////////////////////////////////////////////////////////////////////
	// CONSTRUCTOR
	public MutualInfoAnalyzer() {
		super();
		createGUI();
	}


	private void createGUI() {
		
		try {

			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	        	// Set System L&F
			this.createPanes();
				// Brings the main pane to screen

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}


	private void createPanes() {
		
		GridBagLayout layout = new GridBagLayout();
		layout.columnWeights = new double[]{0,1};
		layout.columnWidths = new int[]{300,0};
		layout.rowHeights = new int[]{0};
		layout.rowWeights = new double[]{1};
		
		this.setLayout(layout);
		
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridheight = 1;
		constraints.gridwidth = 1;
		constraints.gridy = 0;
		constraints.gridx = 0;
		
		this.generalDataPane = new GeneralDataPane();
		
	    this.add(this.generalDataPane,constraints);
		
//		this.setMinimumSize(new Dimension(512 ,300 ));
		this.setPreferredSize(new Dimension(1024 ,768 ));

		constraints.gridx = 1;
		this.graphicViewerPane = new GraphicViewerPane();
		
		this.add(this.graphicViewerPane,constraints);
		
		
		
	}
}
