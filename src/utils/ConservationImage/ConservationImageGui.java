package utils.ConservationImage;

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import pair.Pair;
import utils.ConservationImage.color.ColoringStrategy;
import utils.ConservationImage.renderer.DrawingLayout;
import utils.ConservationImage.renderer.Renderer;
import fileformats.readers.AlignmentReadingResult;
import fileformats.readers.GenericAlignmentReader;

public class ConservationImageGui extends JFrame {

	//////////////////////////////////////////////
	// Class Constants
	private static final long serialVersionUID = -2915260884274179118L;
	//////////////////////////////////////////////
	
	//////////////////////////////////////////////
	// Instance Variables
	private List<Pair<String,String>> alignment;
	private DrawingLayout layout;
	private boolean countGaps;
	private boolean isProtein;
	private Renderer renderer;
	private ColoringStrategy coloring; 
	//////////////////////////////////////////////
	
	//////////////////////////////////////////////
	// Components
	private JPanel imagePane;
	private OptionsPane optionPane;
	// End of Components
	///////////////////////////////////////////////

	///////////////////////////////////////////////
	// Constructor
	public ConservationImageGui() {
		super();
		this.createGUI();
	}
	////////////////////////////////////////////////
	
	////////////////////////////////////////////////
	// Private and protected Methods
	private void createGUI() {
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		
		this.setImagePane(new JPanel());
		
		this.setOptionPane(new OptionsPane(this));
		
		JSplitPane jsp1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		
		jsp1.setOpaque(true);
		
		jsp1.add(this.getOptionPane());

		jsp1.add(this.getImagePane());
		
		this.getImagePane().setOpaque(true);
		
		this.getImagePane().setBackground(new Color(230,230,230));
		
		System.out.println(jsp1.getComponentCount());
		
		this.add(jsp1);
		
		jsp1.setDividerLocation(200);
		
		this.setMinimumSize(new Dimension(600,400));
		
		this.setPreferredSize(new Dimension(600,400));
		
		BasicSplitPaneUI ui = (BasicSplitPaneUI) jsp1.getUI();
		
        BasicSplitPaneDivider divider = ui.getDivider();
        
		divider.setBackground(Color.green);
		
		divider.setBorder(BorderFactory.createLineBorder(Color.gray, 2));
		
		this.pack();
		
	}
	
	protected void loadAlignment(File file) {
		
		GenericAlignmentReader reader = new GenericAlignmentReader();
		
		List<AlignmentReadingResult> readingResult = reader.read(file);
		
		for (AlignmentReadingResult alignmentReadingResult : readingResult) {
			
			if (alignmentReadingResult.successfulRead()) {
				
				this.setAlignment(alignmentReadingResult.getAlignment());
				
				return;
				
			}
			
		}
		
	}
	// End of Private and Protected Methods
	////////////////////////////////////////////////
	
	////////////////////////////////////////////////
	// Getters And Setters
	
	protected JPanel getImagePane() {
		return imagePane;
	}


	protected void setImagePane(JPanel imagePane) {
		this.imagePane = imagePane;
	}


	protected OptionsPane getOptionPane() {
		return optionPane;
	}


	protected void setOptionPane(OptionsPane optionPane) {
		this.optionPane = optionPane;
	}

	protected List<Pair<String, String>> getAlignment() {
		return alignment;
	}

	protected void setAlignment(List<Pair<String, String>> list) {
		this.alignment = list;
	}

	protected DrawingLayout getDrawingLayout() {
		return layout;
	}

	protected void setLayout(DrawingLayout layout) {
		this.layout = layout;
	}

	protected boolean isCountGaps() {
		return countGaps;
	}

	protected void setCountGaps(boolean countGaps) {
		this.countGaps = countGaps;
	}

	protected boolean isProtein() {
		return isProtein;
	}

	protected void setProtein(boolean isProtein) {
		this.isProtein = isProtein;
	}

	protected Renderer getRenderer() {
		return renderer;
	}

	protected void setRenderer(Renderer renderer) {
		this.renderer = renderer;
	}

	protected ColoringStrategy getColoring() {
		return coloring;
	}

	protected void setColoring(ColoringStrategy coloring) {
		this.coloring = coloring;
	}

}
