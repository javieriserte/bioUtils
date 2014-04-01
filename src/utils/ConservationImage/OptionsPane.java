package utils.ConservationImage;

import java.awt.FileDialog;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import utils.ConservationImage.color.ColoringStrategy;
import utils.ConservationImage.renderer.Renderer;

public class OptionsPane extends JPanel {
	
	private static final long serialVersionUID = -1876531238586479356L;
	
	///////////////////////////////////
	// Instance Variables
	private ConservationImageGui mainFrame;
	
	///////////////////////////////////
	
	///////////////////////////////////
	// Components
	private JComboBox<Renderer> cbRenderers;
	private JComboBox<ColoringStrategy> cbColoring;
	private JComboBox<Profiler> cbProfiler;
	
	private JRadioButton moleculeDNA;
	private JRadioButton moleculeProtein;
	private ButtonGroup moleculeType;
	
	private FileDialog fileDialog;
	
	private JButton openMSAButton;
	private JButton saveImageButton;
	private JButton loadLayoutButton;
	
	private JButton drawImageButton;
	
	///////////////////////////////////

    ///////////////////////////////////
	// Constructor
	public OptionsPane(ConservationImageGui mainFrame) {
		
		super();
		
		this.setMainFrame(mainFrame);
		
		this.setFileDialog(new FileDialog(this.getMainFrame()));
		
		this.createGUI();
		
	}
	///////////////////////////////////
	

    ///////////////////////////////////
	// Constructor
	private void createGUI() {
		
		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints constraints = new GridBagConstraints();
		
		layout.columnWeights = new double[]{1};
		layout.columnWidths = new int[]{100};
		layout.rowWeights = new double[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1};
		layout.rowHeights = new int[]    {25,25,25,25,25,25,25,25,25,25,25,25,25};
		
		this.setLayout(layout);
		
		constraints.insets = new Insets(2, 2, 2, 2);
		constraints.gridx=0;
		
		constraints.gridy=0;
		this.openMSAButton = new JButton("Open MSA");
		this.add(this.openMSAButton,constraints);
		
		constraints.gridy=1;
		this.add(new JLabel("Select Method:"),constraints);

		constraints.gridy=2;
		this.cbProfiler = new JComboBox<Profiler>();
		this.add(this.cbProfiler,constraints);
		
		constraints.gridy=3;
		this.add(new JLabel("Select Image Type:"),constraints);

		constraints.gridy=4;
		this.cbRenderers = new JComboBox<Renderer>();
		this.add(this.cbRenderers,constraints);
		
		constraints.gridy=5;
		this.add(new JLabel("Select Colors:"),constraints);

		constraints.gridy=6;
		this.cbColoring = new JComboBox<ColoringStrategy>();
		this.add(this.cbColoring,constraints);
		
		constraints.gridy=7;
		this.moleculeDNA = new JRadioButton("DNA");
		this.add(this.moleculeDNA,constraints);
		
		constraints.gridy=8;
		this.moleculeProtein = new JRadioButton("Protein");
		this.add(this.moleculeProtein,constraints);
		
		this.moleculeType = new ButtonGroup();
		
		this.moleculeType.add(this.moleculeDNA);
		this.moleculeType.add(this.moleculeProtein);
		
		constraints.gridy=9;
		this.loadLayoutButton = new JButton("Load Layout");
		this.add(this.loadLayoutButton,constraints);
		
		constraints.gridy=10;
		this.drawImageButton = new JButton("Draw Image");
		this.add(this.drawImageButton,constraints);
		
		constraints.gridy=11;
		this.saveImageButton= new JButton("Save Image");
		this.add(this.saveImageButton,constraints);

		
		
		
	}
	///////////////////////////////////

	///////////////////////////////////
	// Getters And Setters
	protected ConservationImageGui getMainFrame() {
		return mainFrame;
	}


	protected void setMainFrame(ConservationImageGui mainFrame) {
		this.mainFrame = mainFrame;
	}

	protected FileDialog getFileDialog() {
		return fileDialog;
	}


	protected void setFileDialog(FileDialog fileDialog) {
		this.fileDialog = fileDialog;
	}	///////////////////////////////////



}
