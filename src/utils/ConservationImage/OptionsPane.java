package utils.ConservationImage;

import java.awt.Color;
import java.awt.FileDialog;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.BevelBorder;

import utils.ConservationImage.color.BlackColoringStrategy;
import utils.ConservationImage.color.ColoringStrategy;
import utils.ConservationImage.color.LightRedBlueColoringStrategy;
import utils.ConservationImage.color.RedBlueColoringStrategy;
import utils.ConservationImage.renderer.ColoredLinesRenderer;
import utils.ConservationImage.renderer.Renderer;
import utils.ConservationImage.renderer.XYPlotRenderer;

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
	private JCheckBox   countGaps;
	
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
		
		this.setOpaque(true);
		
		this.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED, Color.lightGray, Color.gray));
		
		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints constraints = new GridBagConstraints();
		
		layout.columnWeights = new double[]{1};
		layout.columnWidths = new int[]{100};
		layout.rowWeights = new double[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1};
		layout.rowHeights = new int[]    {25,25,25,25,25,25,25,25,25,25,25,25,25,25};
		
		this.setLayout(layout);
		
		constraints.insets = new Insets(2, 2, 2, 2);
		constraints.gridx=0;
		
		constraints.gridy=0;
		this.openMSAButton = new JButton("Open MSA");
		this.openMSAButton.addActionListener(new openMSAActionListener());
		this.add(this.openMSAButton,constraints);
		
		constraints.gridy=1;
		this.add(new JLabel("Select Method:"),constraints);

		constraints.gridy=2;
		this.cbProfiler = new JComboBox<Profiler>();
		this.cbProfiler.setRenderer(new CustomRenderer());
		DefaultComboBoxModel<Profiler> profilerModel = new DefaultComboBoxModel<Profiler>(new Profiler[]{new ClustalProfiler(), new InformationProfiler()}); 
		this.cbProfiler.setModel(profilerModel);
		this.add(this.cbProfiler,constraints);
		
		constraints.gridy=3;
		this.add(new JLabel("Select Image Type:"),constraints);

		constraints.gridy=4;
		this.cbRenderers = new JComboBox<Renderer>();
		this.cbRenderers.setRenderer(new CustomRenderer());
		DefaultComboBoxModel<Renderer> renderersModel = new DefaultComboBoxModel<Renderer>(new Renderer[]{new XYPlotRenderer(), new ColoredLinesRenderer()}); 
		this.cbRenderers.setModel(renderersModel);		
		this.add(this.cbRenderers,constraints);
		
		constraints.gridy=5;
		this.add(new JLabel("Select Colors:"),constraints);

		constraints.gridy=6;
		this.cbColoring = new JComboBox<ColoringStrategy>();
		this.cbColoring.setRenderer(new CustomRenderer());
		DefaultComboBoxModel<ColoringStrategy> coloringModel = new DefaultComboBoxModel<ColoringStrategy>(new ColoringStrategy[]{new RedBlueColoringStrategy(), new LightRedBlueColoringStrategy(), new BlackColoringStrategy()}); 
		this.cbColoring.setModel(coloringModel);		
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
		this.moleculeDNA.setSelected(true);
		
		constraints.gridy=9;
		this.countGaps = new JCheckBox("Count Gaps");
		this.countGaps.setSelected(false);
		this.add(this.countGaps,constraints);
		
		constraints.gridy=10;
		this.loadLayoutButton = new JButton("Load Layout");
		this.add(this.loadLayoutButton,constraints);
		
		constraints.gridy=11;
		this.drawImageButton = new JButton("Draw Image");
		this.add(this.drawImageButton,constraints);
		
		constraints.gridy=12;
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

	///////////////////////////////////////////////////////////////////
	//Auxiliary classes
	class openMSAActionListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			OptionsPane.this.getFileDialog().setModal(true);
			OptionsPane.this.getFileDialog().setMode(FileDialog.LOAD);
			OptionsPane.this.getFileDialog().setVisible(true);
		
		}
		
	}
	
	class loadLayoutActionListener implements ActionListener {
	
		@Override
		public void actionPerformed(ActionEvent e) {
		
		}
	
	}
	
	class drawImageActionListener implements ActionListener {
	
		@Override
		public void actionPerformed(ActionEvent e) {
		
		}
		
	}
	
	class saveImageActionListener implements ActionListener {
	
		@Override
		public void actionPerformed(ActionEvent e) {
		
		}
		
		}
	}
	


