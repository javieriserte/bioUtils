package utils.onepixelalignmentdrawer;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
		onePixelAlignmentImage = getSquaredImage(squareOpt, onePixelAlignmentImage);
		
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
