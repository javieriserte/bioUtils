package utils.mutualinformation.mimatrixviewer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;


public class MIMatrixViewer extends JFrame{

	private static final long serialVersionUID = -4648080458360903060L;

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				MIMatrixViewer inst = new MIMatrixViewer();
					// creates the main instance
				
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
				inst.setTitle("Mi Matrix Viewer");
				inst.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				
				
				Graphics2D graphics = (Graphics2D) inst.getContentPane().getGraphics();
				
				graphics.setColor(Color.black);
				graphics.fillRect(0, 0, 400, 300);
				
				inst.getContentPane().repaint();
			
				inst.setLayout(new FlowLayout());
				
				File infile = new File("/home/javier/Dropbox/Posdoc/HIV.Segundo.Analisis/mi_mf_cl/mi_cl_lc_data");
				
				MI_Matrix matrix = MI_Matrix.loadFromFile(infile);
				
				MIMatrixPane matrixPane = new MIMatrixPane();
				MatrixColoringStrategy color = new RedAndBlueMatrixColoringStrategy(-10,50);
				
				matrixPane.setColor(color);
				matrixPane.setMatrix(matrix);
				matrixPane.setSize(matrix.getSize(), matrix.getSize());
				matrixPane.setPreferredSize(new Dimension(matrix.getSize(), matrix.getSize()));
				JScrollPane jsp1 = new JScrollPane(matrixPane, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
				inst.setLayout(new BorderLayout());
				inst.getContentPane().add(jsp1, BorderLayout.CENTER);
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

}
