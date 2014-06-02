package utils.onepixelalignmentdrawer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import utils.color.ColorStrategy;
import utils.color.DnaColorStrategy;
import utils.color.ProteinColorStrategy;
import cmdGA.NoOption;
import cmdGA.Parser;
import cmdGA.SingleOption;
import cmdGA.exceptions.IncorrectParameterTypeException;
import cmdGA.parameterType.InputStreamParameter;
import cmdGA.parameterType.OutFileParameter;
import fileformats.fastaIO.FastaMultipleReader;
import pair.Pair;

/**
 * Draws an one-pixel representation of a Multiple
 * Sequence Alignment
 * 
 * @author Javier Iserte
 *
 */
public class OnePixel {
	private static final int ColumnsBreak = 30;

	public static void main(String[] args) {
		
		////////////////////////////////
		// Create Parser
		Parser parser = new Parser();
		
		////////////////////////////////
		// Add Parser Options
		SingleOption inOpt = new SingleOption(parser, System.in, "-infile", InputStreamParameter.getParameter());

		SingleOption outOpt = new SingleOption(parser, null, "-out", OutFileParameter.getParameter());
		
		NoOption isprotOpt = new NoOption(parser, "-isprotein");
		
		NoOption squareOpt = new NoOption(parser, "-square");
		
		NoOption helpOpt = new NoOption(parser, "-help");
		
		NoOption verOpt = new NoOption(parser, "-v");
		
		////////////////////////////////
		// Parse Command Line
		try {
			
			parser.parseEx(args);
		
		} catch (IncorrectParameterTypeException e) {
			
			System.err.println("There was an error trying to parse the command line:" + e.getMessage());
			
			System.exit(1);
			
		}
		
		//////////////////////////////
		// Get Command Line Values
		BufferedReader in = new BufferedReader(new InputStreamReader((InputStream) inOpt.getValue()));
		
		File out = (File) outOpt.getValue();
		
		boolean isProtein = isprotOpt.isPresent();
		
		boolean help = helpOpt.isPresent();
		
		boolean ver = verOpt.isPresent();
		
		//////////////////////////////
		// Checks for help
		if (help) {
			
			printHelp();
			
			System.exit(1);
			
		}
		
		//////////////////////////////
		// Checks if output is valid
		if (out==null) {
			
			System.err.println("There Was an error with the selected output file");
			
			System.exit(1);
			
		}

		if (ver) {
			
			System.err.println("# Output file: " + out.getAbsolutePath());
			
			System.err.println("# Is Protein : " + String.valueOf(isProtein));
			
		}
		
		//////////////////////////////
		// Create Fasta Reader
		FastaMultipleReader fmr = new FastaMultipleReader();

		//////////////////////////////
		// Read Input Fasta Alignment
		long before = System.currentTimeMillis();
		List<Pair<String, String>> alignment = fmr.readBuffer(in);
		
		if (ver) {
		
			System.err.println("# Input MSA loaded in " + (System.currentTimeMillis() - before ) + "ms.");
			
			System.err.println("# Input MSA is " + alignment.get(0).getSecond().length()+ "bp long.");
			
			System.err.println("# Input MSA contains " + alignment.size() + " sequences.");
			
		}
		
		//////////////////////////////
		// Create Coloring Scheme
		ColorStrategy color = (isProtein)?(new ProteinColorStrategy()):(new DnaColorStrategy());
		
		//////////////////////////////
		// Create OnePixel Object
		OnePixel onePixel = new OnePixel();
		
		//////////////////////////////
		// Create Image
		before = System.currentTimeMillis();
		BufferedImage onePixelAlignmentImage = onePixel.drawImage(alignment, color);
		if (ver) {
			
			System.err.println("# Image created in " + (System.currentTimeMillis() - before) +"ms.");
			
		}
		
		//////////////////////////////
		// Make the image Squared
		List<BufferedImage> panels= getSquaredImages(squareOpt, onePixelAlignmentImage);
		
		//////////////////////////////
		// Add Ticks
		panels = addTicksToPanels(panels,100,10);
		
		//////////////////////////////
		// Append all panels
		
		if (squareOpt.isPresent()) {
			
			panels = addSpacerToPanels (panels);
			
		}
		
		onePixelAlignmentImage = appendPanels(panels);
		
		
		//////////////////////////////
		// Export Image
		try {
			before = System.currentTimeMillis();
			onePixel.writePNG(onePixelAlignmentImage, out);
			
			if (ver) {
				
				System.err.println("# Image written in file in " + (System.currentTimeMillis() - before) +"ms.");
				
			}
			
		} catch (IOException e) {
			
			System.err.println("There was an error trying to write PNG file: "+e.getMessage());
			
		}
		
	}

	


	private static BufferedImage appendPanels(List<BufferedImage> panels) {
		
		int totalWidth = 0;
		
		int height = 0;
		
		for (BufferedImage panel :panels) {
			
			totalWidth += panel.getWidth();
			
			height = Math.max(panel.getHeight() , height);
			
		}
		
		BufferedImage result = new BufferedImage(totalWidth, height, BufferedImage.TYPE_INT_RGB);

		Graphics2D graphics = (Graphics2D) result.getGraphics();  
		
		graphics.setColor(Color.black);
		
		graphics.fillRect(0, 0, totalWidth, height);
		
		int x = 0;
		
		int y = 0;
		
		for (BufferedImage panel :panels) {
			
			int currentWidth = panel.getWidth();
			
			int currentHeight = panel.getHeight();
			
			graphics.drawImage(panel, x, y, currentWidth, currentHeight, null);
			
		}
		
		return result;
		
	}


	private static List<BufferedImage> addSpacerToPanels (List<BufferedImage> panels) {
		
		int alignmentSpacer = 30;
		
		List<BufferedImage> result = new ArrayList<>();
		
		BufferedImage spacerImage = new BufferedImage(alignmentSpacer, panels.get(0).getHeight(), BufferedImage.TYPE_INT_RGB);
		
		Graphics2D graphics = (Graphics2D) spacerImage.getGraphics();
		graphics.setColor(Color.white);
		graphics.fillRect(0, 0, spacerImage.getWidth(), spacerImage.getHeight());
		
		boolean first = true;
		
		for (BufferedImage currentPanel : panels) {
			
			if (!first) {
				
				result.add(spacerImage);
				
			}
			
			result.add(currentPanel);
			
		}
		
		return result;
	}


	private static List<BufferedImage> addTicksToPanels( List<BufferedImage> panels, int majorTickSpacer,int minorTickSpacer) {
		
		int topSpacer = 30;
		int leftSpacer = 50;
		int rightSpacer = 50;
		int bottomSpacers = 10;

		List<BufferedImage> result = new ArrayList<>();
		
		for (BufferedImage currentPanel : panels) {
			
			BufferedImage newImage = new BufferedImage(
					leftSpacer+rightSpacer+currentPanel.getWidth(), // Width 
					topSpacer+bottomSpacers+currentPanel.getHeight(), // Height 
					BufferedImage.TYPE_INT_RGB);
			
			Graphics2D graphics = (Graphics2D) newImage.getGraphics();
			
			graphics.drawImage(currentPanel, leftSpacer, topSpacer, currentPanel.getWidth(), currentPanel.getHeight(), null);
			
			////////////////////////////
			// Draw Minor Ticks
			int minorTickLength = 5;
			graphics.setStroke(new BasicStroke(1));
			graphics.setColor(Color.black);
			List<Integer> ticks = new ArrayList<>();
			ticks.add(1);
			for (int x=minorTickSpacer-1;x<currentPanel.getWidth();x=x+minorTickSpacer) {

				ticks.add(x+1);
				
			}
			
			for (Integer integer : ticks) {
			
				graphics.drawLine(leftSpacer + integer-1, topSpacer, leftSpacer + integer-1, topSpacer-minorTickLength);
				
			}
			
			////////////////////////////
			// Draw Large Ticks
			int majorTickLength = 10;
			graphics.setStroke(new BasicStroke(2));
			graphics.setColor(Color.black);
			ticks = new ArrayList<>();
			ticks.add(1);
			for (int x=majorTickSpacer-1;x<currentPanel.getWidth();x=x+majorTickSpacer) {

				ticks.add(x+1);
				
			}
			
			for (Integer integer : ticks) {
			
				graphics.drawLine(leftSpacer + integer, topSpacer, leftSpacer+integer, topSpacer-majorTickLength);
				
			}
			
			////////////////////////////
			// Draw Text Labels
			
			graphics.setColor(Color.black);
			graphics.setFont(new Font("Arial",0,10));
			for (Integer tick: ticks) {
			
				String label = String.valueOf(tick);
				
				int textAdvance = graphics.getFontMetrics().stringWidth(label);
				
				graphics.drawString(label, leftSpacer + tick -1 - textAdvance/2, 17);
				
			}
			
			result.add(newImage);
			
		} 
		
		return result;
	}


	private static List<BufferedImage> getSquaredImages(NoOption squareOpt, BufferedImage onePixelAlignmentImage) {
		
		List<BufferedImage> result = new ArrayList<>();
		
		if (squareOpt.isPresent()) {
			int length = onePixelAlignmentImage.getHeight();
			int width =  onePixelAlignmentImage.getWidth();
			int numberOfRegions = (int) Math.round(Math.sqrt(length/width));
			
			if (numberOfRegions>1) {
				
				int height  =  ((int) length /numberOfRegions) -1;
				
				for (int i = 0; i<numberOfRegions; i++) {
					
					BufferedImage temp = new BufferedImage(width, Math.min(height, length - i*height),  BufferedImage.TYPE_INT_RGB);
		
					((Graphics2D) temp.getGraphics()).drawImage(onePixelAlignmentImage, 0, -i*height, null);
					
					result.add(temp);
					
					((Graphics2D) temp.getGraphics()).dispose();
					
				}
				
			} else {
				
				result.add(onePixelAlignmentImage);
			
			}
			
		} else {
			
			result.add(onePixelAlignmentImage);
			
		}
		
		return result;
		
	}

	@Deprecated
	@SuppressWarnings("unused")
	private static BufferedImage getSquaredImage(NoOption squareOpt,
			BufferedImage onePixelAlignmentImage) {
		if (squareOpt.isPresent()) {
			int length = onePixelAlignmentImage.getHeight();
			int width =  onePixelAlignmentImage.getWidth();
			int numberOfRegions = (int) Math.round(Math.sqrt(length/width));
			
			if (numberOfRegions>1) {
				
				int height  =  ((int) length /numberOfRegions) -1;
				
				BufferedImage newImage = new BufferedImage(width * numberOfRegions +  OnePixel.ColumnsBreak*(numberOfRegions-1), height, BufferedImage.TYPE_INT_RGB);
				
				for (int i = 0; i<numberOfRegions; i++) {
					
					BufferedImage temp = new BufferedImage(width, Math.min(height, length - i*height),  BufferedImage.TYPE_INT_RGB);
		
					((Graphics2D) temp.getGraphics()).drawImage(onePixelAlignmentImage, 0, -i*height, null);
					
					((Graphics2D) temp.getGraphics()).dispose();
					
					newImage.getGraphics().drawImage(temp, i* (width+OnePixel.ColumnsBreak), 0,null);
					
				}
				
				onePixelAlignmentImage = newImage;
				
			}
			
		}
		return onePixelAlignmentImage;
	}


	/**
	 * Prints Program Help into standard error.
	 * 
	 */
	private static void printHelp() {

		InputStream isHelp = OnePixel.class.getResourceAsStream("help");
		
		BufferedReader brHelp = new BufferedReader(new InputStreamReader(isHelp));
		
		String currentline = null;
		
		try {
			
			while((currentline=brHelp.readLine())!=null) {
				
				System.err.println(currentline);
				
			}
		} catch (IOException e) {

			System.err.println("Error: "+ e .getMessage());
			
			System.exit(1);
			
		}
		
	}



	/////////////////////////////////////
	// Public Interface
	/***
	 * Creates an Image representation of a sequence alignment 
	 * using one pixel per base/amino acid
	 * @param alignment
	 * @param color
	 * @return
	 */
	public BufferedImage drawImage(List<Pair<String, String>> alignment, ColorStrategy color) {
		
		int height = alignment.size();
		
		int width = alignment.get(0).getSecond().length();
		
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
//		Graphics2D graphics = (Graphics2D) image.getGraphics();

		for (int i = 0; i< height; i++) {
			
			for (int j=0; j<width; j++) {
				
				char residue = alignment.get(i).getSecond().charAt(j);
		
				Color currentColor;
				
				if (residue=='-') {
					
					currentColor= new Color(245,245,245);
					
				} else {
				
					currentColor= color.getColor(residue);
					
				}
				
				image.setRGB(j, i, currentColor.getRGB());
				
			}
		
		}
		
		return image;
		
	}
	
	/**
	 * Writes a BufferedImage into a PNG file.
	 * @param onePixelAlignmentImage
	 * @param out
	 * @throws IOException 
	 */
	public void writePNG(BufferedImage onePixelAlignmentImage, File outfile) throws IOException {
		
		FileOutputStream out = new FileOutputStream(outfile);
		
		ImageWriter imagewriter = ImageIO.getImageWritersByFormatName("png").next();
		
		ImageWriteParam writerparam = imagewriter.getDefaultWriteParam();
		
		ImageOutputStream ios = ImageIO.createImageOutputStream(out);
		
		imagewriter.setOutput(ios);
		
		imagewriter.write(null, new IIOImage(onePixelAlignmentImage, null, null), writerparam);
		
		imagewriter.dispose();
		
	}
	
}
