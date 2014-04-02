package utils.ConservationImage;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import pair.Pair;
import utils.ConservationImage.color.ColoringStrategy;
import utils.ConservationImage.managers.CountGap;
import utils.ConservationImage.managers.DNAManager;
import utils.ConservationImage.managers.GapManager;
import utils.ConservationImage.managers.MoleculeManager;
import utils.ConservationImage.managers.NoCountGap;
import utils.ConservationImage.managers.ProteinManager;
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
	private Profiler profiler;
	private BufferedImage image;
	private int windowSize;
	//////////////////////////////////////////////
	
	//////////////////////////////////////////////
	// Components
	private ImagePanel imagePane;
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
		
		
		this.setImagePane(new ImagePanel());
		
		JScrollPane scrollPane = new JScrollPane();
		
		scrollPane.setViewportView(this.getImagePane());
		
		this.getImagePane().setOpaque(true);
		
		this.setOptionPane(new OptionsPane(this));
		
		JSplitPane jsp1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		
		jsp1.setOpaque(true);
		
		jsp1.add(this.getOptionPane());

		jsp1.add(scrollPane);
		
		this.getImagePane().setOpaque(true);
		
		this.getImagePane().setBackground(new Color(230,230,230));
		
		System.out.println(jsp1.getComponentCount());
		
		this.add(jsp1);
		
		jsp1.setDividerLocation(200);
		
		this.setMinimumSize(new Dimension(800,600));
		
		this.setPreferredSize(new Dimension(800,600));
		
		BasicSplitPaneUI ui = (BasicSplitPaneUI) jsp1.getUI();
		
        BasicSplitPaneDivider divider = ui.getDivider();
		
		divider.setBorder(BorderFactory.createLineBorder(Color.gray, 2));
		
		this.pack();
		
	}
	
	protected boolean loadAlignment(File file) {
		
		GenericAlignmentReader reader = new GenericAlignmentReader();
		
		List<AlignmentReadingResult> readingResult = reader.read(file);
		
		for (AlignmentReadingResult alignmentReadingResult : readingResult) {
			
			if (alignmentReadingResult.successfulRead()) {
				
				this.setAlignment(alignmentReadingResult.getAlignment());
				
				return true;
				
			}
			
		}
		
		return false;
		
	}
	
	public void drawImage() {
		
		MoleculeManager molManager = (this.isProtein)?new ProteinManager(): new DNAManager();
		
		GapManager gapManager = (this.countGaps)?new CountGap():new NoCountGap();
		
		this.setAlignment(this.getProfiler().replaceUnexpectedCharstoGaps(this.getAlignment(), molManager));
		
		double[] data = this.getProfiler().getdata(this.getAlignment(), molManager, gapManager);
		
		BufferedImage bi = renderer.render(this.getColoring(),data , this.getWindowSize());
		
		this.setImage(bi);
		
		this.getImagePane().setBi(bi);
		
	}
	

	public void exportImage(File imageFile) {
	
		try {

			FileOutputStream out = new FileOutputStream(imageFile);
		
			ImageWriter imagewriter = ImageIO.getImageWritersByFormatName("jpg").next();
		
			ImageWriteParam writerparam = imagewriter.getDefaultWriteParam();
		
			writerparam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		
			writerparam.setCompressionQuality(1.0f);
		
			ImageOutputStream ios = ImageIO.createImageOutputStream(out);
		
			imagewriter.setOutput(ios);
		
			imagewriter.write(null, new IIOImage(this.getImage(), null, null), writerparam);
			
			imagewriter.dispose();

			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		
	}


	// End of Private and Protected Methods
	////////////////////////////////////////////////
	
	////////////////////////////////////////////////
	// Getters And Setters
	
	protected ImagePanel getImagePane() {
		return imagePane;
	}


	protected void setImagePane(ImagePanel imagePane) {
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

	public Profiler getProfiler() {
		return profiler;
	}

	public void setProfiler(Profiler profiler) {
		this.profiler = profiler;
	}


	protected BufferedImage getImage() {
		return image;
	}


	protected void setImage(BufferedImage image) {
		this.image = image;
	}

	public int getWindowSize() {
		return windowSize;
	}


	public void setWindowSize(int windowSize) {
		this.windowSize = windowSize;
	}




}
