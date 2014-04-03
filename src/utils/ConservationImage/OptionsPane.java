package utils.ConservationImage;

import java.awt.Color;
import java.awt.FileDialog;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.BevelBorder;

import utils.ConservationImage.color.BlackColoringStrategy;
import utils.ConservationImage.color.ColoringStrategy;
import utils.ConservationImage.color.LightRedBlueColoringStrategy;
import utils.ConservationImage.color.RedBlueColoringStrategy;
import utils.ConservationImage.renderer.ColoredLinesRenderer;
import utils.ConservationImage.renderer.Renderer;
import utils.ConservationImage.renderer.RendererReader;
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
	
	private JSpinner windowSize;
	
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
		
		Color lightRed = new Color(255,230,230);
		Color mateRed = new Color(200,130,130);
		Color lightGreen = new Color(230,255,230);
		Color mateGreen= new Color(20,130,130);
		
		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints constraints = new GridBagConstraints();
		
		layout.columnWeights = new double[]{0,1};
		layout.columnWidths = new int[]{50,100};
		layout.rowWeights = new double[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1};
		layout.rowHeights = new int[]    {25,25,25,25,25,25,25,25,25,25,25,25,25,25,25,25};
		
		this.setLayout(layout);
		
		// Adding Labels
		constraints.insets = new Insets(10, 10, 10, 2);
		constraints.gridx=0;
		constraints.gridy=0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.fill   = GridBagConstraints.BOTH;
		
		JLabel lbl01 = new JLabel("1");
		lbl01.setOpaque(true);
		lbl01.setBackground(lightRed);
		lbl01.setHorizontalAlignment(JLabel.CENTER);
		lbl01.setVerticalAlignment(JLabel.CENTER);
		lbl01.setBorder(BorderFactory.createLineBorder(mateRed ,1, false));
		this.add(lbl01,constraints);
		
		constraints.gridy=1;
		constraints.gridheight=12;
		JLabel lbl02 = new JLabel("2");
		lbl02.setOpaque(true);
		lbl02.setBackground(lightGreen);
		lbl02.setHorizontalAlignment(JLabel.CENTER);
		lbl02.setVerticalAlignment(JLabel.CENTER);
		lbl02.setBorder(BorderFactory.createLineBorder(mateGreen ,1, false));
		this.add(lbl02,constraints);
		

		constraints.gridy=13;
		constraints.gridheight=1;
		JLabel lbl03 = new JLabel("3");
		lbl03.setOpaque(true);
		lbl03.setBackground(lightRed);
		lbl03.setHorizontalAlignment(JLabel.CENTER);
		lbl03.setVerticalAlignment(JLabel.CENTER);
		lbl03.setBorder(BorderFactory.createLineBorder(mateRed ,1, false));
		this.add(lbl03,constraints);

		constraints.gridy=14;
		JLabel lbl04 = new JLabel("4");
		lbl04.setOpaque(true);
		lbl04.setBackground(lightGreen);
		lbl04.setHorizontalAlignment(JLabel.CENTER);
		lbl04.setVerticalAlignment(JLabel.CENTER);
		lbl04.setBorder(BorderFactory.createLineBorder(mateGreen ,1, false));
		this.add(lbl04,constraints);	
		// End Adding labels
		
		// Addind components
		constraints.insets = new Insets(10, 2, 10, 10);
		constraints.gridx=1;
		constraints.gridy=0;
		this.openMSAButton = new JButton("Open MSA");
		this.openMSAButton.addActionListener(new openMSAActionListener());
		this.add(this.openMSAButton,constraints);

		constraints.insets = new Insets(10, 2, 2, 10);
		constraints.gridy=1;
		this.add(new JLabel("Select Method:"),constraints);

		constraints.insets = new Insets(2, 2, 2, 10);
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
		this.add(new JLabel("Window size:"),constraints);
		
		constraints.gridy=8;
		SpinnerNumberModel spinnerModel = new SpinnerNumberModel();
		spinnerModel.setMinimum(1);
		spinnerModel.setStepSize(1);
		spinnerModel.setValue(11);
		this.windowSize = new JSpinner(spinnerModel);
		//((DefaultEditor)this.windowSize.getEditor()).getTextField().setEditable(false);
		
		this.add(this.windowSize,constraints);

		
		constraints.gridy=9;
		this.moleculeProtein = new JRadioButton("Protein");
		this.add(this.moleculeProtein,constraints);
		
		constraints.gridy=10;
		this.moleculeDNA = new JRadioButton("DNA");
		this.add(this.moleculeDNA,constraints);
		
		this.moleculeType = new ButtonGroup();
		
		this.moleculeType.add(this.moleculeDNA);
		this.moleculeType.add(this.moleculeProtein);
		this.moleculeDNA.setSelected(true);
		
		constraints.gridy=11;
		this.countGaps = new JCheckBox("Count Gaps");
		this.countGaps.setSelected(false);
		this.add(this.countGaps,constraints);
		
		constraints.insets = new Insets(2, 2, 10, 10);
		constraints.gridy=12;
		this.loadLayoutButton = new JButton("Load Layout");
		this.loadLayoutButton.addActionListener(new loadLayoutActionListener());
		this.add(this.loadLayoutButton,constraints);
		
		constraints.insets = new Insets(10, 2, 10, 10);
		constraints.gridy=13;
		this.drawImageButton = new JButton("Draw Image");
		this.drawImageButton.addActionListener(new drawImageActionListener()); 
		
		this.add(this.drawImageButton,constraints);
		
		constraints.gridy=14;
		
		this.saveImageButton= new JButton("Save Image");
		this.saveImageButton.addActionListener(new saveImageActionListener());
		this.add(this.saveImageButton,constraints);
		
		this.setGuiStage(1);
		
	}
	
	private void setGuiStage(int i) {
		
		switch(i) {
			case 1:
				this.cbColoring.setEnabled(false);
				this.cbRenderers.setEnabled(false);
				this.cbProfiler.setEnabled(false);
				this.countGaps.setEnabled(false);
				this.countGaps.setEnabled(false);
				this.loadLayoutButton.setEnabled(false);
				this.windowSize.setEnabled(false);
				this.moleculeDNA.setEnabled(false);
				this.moleculeProtein.setEnabled(false);
				this.drawImageButton.setEnabled(false);
				this.saveImageButton.setEnabled(false);
				break;
			case 2:
				this.cbColoring.setEnabled(true);
				this.cbRenderers.setEnabled(true);
				this.cbProfiler.setEnabled(true);
				this.countGaps.setEnabled(true);
				this.countGaps.setEnabled(true);
				this.loadLayoutButton.setEnabled(true);
				this.windowSize.setEnabled(true);
				this.moleculeDNA.setEnabled(true);
				this.moleculeProtein.setEnabled(true);
				this.drawImageButton.setEnabled(true);
				this.saveImageButton.setEnabled(false);
				break;
			case 3:
				this.cbColoring.setEnabled(true);
				this.cbRenderers.setEnabled(true);
				this.cbProfiler.setEnabled(true);
				this.countGaps.setEnabled(true);
				this.countGaps.setEnabled(true);
				this.loadLayoutButton.setEnabled(true);
				this.windowSize.setEnabled(true);
				this.moleculeDNA.setEnabled(true);
				this.moleculeProtein.setEnabled(true);
				this.drawImageButton.setEnabled(true);
				this.saveImageButton.setEnabled(true);
				break;				
		
		}
		
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
			
			File alnFile = null;
			
			try {
			
				OptionsPane.this.getFileDialog().setTitle("Select an alignment");
				OptionsPane.this.getFileDialog().setModal(true);
				OptionsPane.this.getFileDialog().setMultipleMode(false);
				OptionsPane.this.getFileDialog().setMode(FileDialog.LOAD);
				OptionsPane.this.getFileDialog().setVisible(true);
				
				if (OptionsPane.this.getFileDialog().getFiles().length>0) {
				
					alnFile = OptionsPane.this.getFileDialog().getFiles()[0];
					
					boolean successfulRead = OptionsPane.this.getMainFrame().loadAlignment(alnFile);
					
					if (successfulRead) {
						
						OptionsPane.this.setGuiStage(2);
						
					} else {
						
						OptionsPane.this.setGuiStage(1);
						
					}
	
					
				}
			
			} catch (Exception e1) {
				
				System.err.println(e1.getMessage());
				
				System.err.println(OptionsPane.this.getFileDialog().getFiles().length);
				
				
			}
			
		}
		
	}
	
	class loadLayoutActionListener implements ActionListener {
	
		@Override
		public void actionPerformed(ActionEvent e) {
			
			File layoutFile = null;
			
			OptionsPane.this.getFileDialog().setTitle("Select a layout file");
			OptionsPane.this.getFileDialog().setModal(true);
			OptionsPane.this.getFileDialog().setMultipleMode(false);
			OptionsPane.this.getFileDialog().setMode(FileDialog.LOAD);
			OptionsPane.this.getFileDialog().setVisible(true);
			
			if (OptionsPane.this.getFileDialog().getFiles().length>0) {
				
				layoutFile = OptionsPane.this.getFileDialog().getFiles()[0];
				
				Renderer renderer = (new RendererReader()).parse_file(layoutFile);
				
				renderer.setDefault(false);
				
				if (renderer != null) {
					
					DefaultComboBoxModel<Renderer> renderersModel = new DefaultComboBoxModel<Renderer>(
							new Renderer[]{renderer,
									       new XYPlotRenderer(), 
							               new ColoredLinesRenderer()});
					
					renderersModel.setSelectedItem(renderer);					
					OptionsPane.this.cbRenderers.setModel(renderersModel);

				}

				
			}
			
		
		}
	
	}
	
	class drawImageActionListener implements ActionListener {
	
		@Override
		public void actionPerformed(ActionEvent e) {
			
			OptionsPane optionsPane = OptionsPane.this;
			
			ConservationImageGui mainFrame = optionsPane.getMainFrame();
			
			mainFrame.setColoring( optionsPane.cbColoring.getItemAt(optionsPane.cbColoring.getSelectedIndex()));
			
			Renderer renderer = optionsPane.cbRenderers.getItemAt(optionsPane.cbRenderers.getSelectedIndex());
			
			if (renderer.isDefault()) {
				renderer.setLayout(renderer.getDefaultLayout());
			}
			
			mainFrame.setRenderer(renderer);
			
			mainFrame.setProfiler(optionsPane.cbProfiler.getItemAt(optionsPane.cbProfiler.getSelectedIndex()));
			
			mainFrame.setProtein(optionsPane.moleculeProtein.isSelected());
			
			mainFrame.setCountGaps(optionsPane.countGaps.isSelected());
			
			mainFrame.setWindowSize((Integer)optionsPane.windowSize.getValue());
			
			mainFrame.drawImage();
			
			optionsPane.setGuiStage(3);
			
		}
		
	}
	
	class saveImageActionListener implements ActionListener {
	
		@Override
		public void actionPerformed(ActionEvent e) {
	
			File imageFile = null;
			
			OptionsPane.this.getFileDialog().setTitle("Select an output jpg file");
			OptionsPane.this.getFileDialog().setModal(true);
			OptionsPane.this.getFileDialog().setMultipleMode(false);
			OptionsPane.this.getFileDialog().setMode(FileDialog.SAVE);
			OptionsPane.this.getFileDialog().setVisible(true);
			
			if (OptionsPane.this.getFileDialog().getFiles().length>0) {
				
				imageFile = OptionsPane.this.getFileDialog().getFiles()[0];
				
				if (imageFile.getAbsolutePath().length()<4 || !imageFile.getAbsolutePath().substring(imageFile.getAbsolutePath().length() - 4).equalsIgnoreCase(".JPG")) {
					imageFile = new File(imageFile.getAbsolutePath()+".jpg");
				}
				
				OptionsPane.this.getMainFrame().exportImage(imageFile);
				
			}
			
			
		}
		
	}
	
}
	


