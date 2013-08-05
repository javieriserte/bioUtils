package utils.ConservationImage;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import javax.imageio.IIOException;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import utils.ConservationImage.color.ColoringStrategy;
import utils.ConservationImage.color.RedBlueColorringStrategy;
import utils.ConservationImage.managers.CountGap;
import utils.ConservationImage.managers.DNAManager;
import utils.ConservationImage.managers.GapManager;
import utils.ConservationImage.managers.MoleculeManager;
import utils.ConservationImage.managers.NoCountGap;
import utils.ConservationImage.managers.ProteinManager;
import utils.ConservationImage.renderer.DrawingLayout;
import utils.ConservationImage.renderer.Renderer;
import utils.ConservationImage.renderer.XYPlotRenderer;

import cmdGA.NoOption;
import cmdGA.Parser;
import cmdGA.SingleOption;
import cmdGA.exceptions.IncorrectParameterTypeException;
import cmdGA.parameterType.InputStreamParameter;
import cmdGA.parameterType.IntegerParameter;
import cmdGA.parameterType.OutFileParameter;
import cmdGA.parameterType.PrintStreamParameter;

public class ConservationImageGenerator {

	////////////////////
	// Instance Variable
	private double[] 			data;
	
	//////////////
	// Constructor 
	
	public 						ConservationImageGenerator			(double[] data) {
		this.data = data;
	}
	
	public 						ConservationImageGenerator			() {
		this.data = null;
	}

	
	///////////////////
	// Public Interface
	
	public void 				printImage							(File outfile, ColoringStrategy color, int windowSize, Renderer renderer ) throws FileNotFoundException, IOException  {
		
		// notice that createGraphics returns a g2d object directly, no cast!
		
		BufferedImage bi = renderer.render(color,this.data,windowSize);
		
//		 save the image

		this.exportJPG(outfile, bi);
	}
	
	
	//////////////////
	// Private Methods

	private void 				exportJPG							(File outfile, BufferedImage bi) throws FileNotFoundException, IOException {
		
		FileOutputStream out = new FileOutputStream(outfile);
		
		ImageWriter imagewriter = ImageIO.getImageWritersByFormatName("jpg").next();
		
		ImageWriteParam writerparam = imagewriter.getDefaultWriteParam();
		
		writerparam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		
		writerparam.setCompressionQuality(1.0f);
		
		ImageOutputStream ios = ImageIO.createImageOutputStream(out);
		
		imagewriter.setOutput(ios);
		
		imagewriter.write(null, new IIOImage(bi, null, null), writerparam);
		
		imagewriter.dispose();

	}
		
	///////////////////
	// GETTERS & SETTER
	
	public double[] 			getData								() {
		return data;
	}

	public void 				setData								(double[] data) {
		this.data = data;
	}
	
	
	//////////////////
	// Executable Main
	public static void 			main								(String[] args) {

		commandlineMain(args);

	}
	
	public static void commandlineMain(String[] args) {
		
		if (args.length==0) {
			System.err.println("No option was found.\nProgram Terminated.");
			System.exit(1);
		}
		
		// STEP ONE:
		// Create a Parser.
		Parser parser = new Parser();
		
		// STEP TWO:
		// DEFINE THE POSSIBLE OPTIONS ACCEPTED IN THE COMMAND LINE. (TAKE CARE OF AVOID AMBIGUITY) 
		SingleOption in  = new SingleOption(parser, System.in, "-infile", InputStreamParameter.getParameter());
		SingleOption outfile = new SingleOption(parser, null, "-outfile", OutFileParameter.getParameter());
		
		SingleOption windowSize = new SingleOption(parser, 11, "-window", IntegerParameter.getParameter());
		NoOption isProtein = new NoOption(parser, "-protein");
		NoOption isInformationContent = new NoOption(parser, "-ic");
		SingleOption renderOpt = new SingleOption(parser, new XYPlotRenderer(),"-renderer",RendererParameter.getParameter());
		SingleOption layoutOpt = new SingleOption(parser, null, "-layout", LayoutParameter.getParameter());
		NoOption countGapOpt =  new NoOption(parser, "-countgap");
		NoOption noDrawOpt = new NoOption(parser, "-noDraw");
		SingleOption exportValuesOpt = new SingleOption(parser, System.out, "-export", PrintStreamParameter.getParameter());
		
		
		// STEP THREE
		// PARSE THE COMMAND LINE
		try {
			parser.parseEx(args);
		} catch ( IncorrectParameterTypeException e )  {
			System.err.println( "The was an error:"       );
			System.err.println(  e.getMessage()        );
			System.err.println( "Program Terminated." );
			System.exit(1);
		}
		
			
		if (outfile.getValue() == null && !noDrawOpt.isPresent()) {
			System.err.println("No outfile was given.\n.");
			System.exit(1);
		}
		
		// Program 
		ConservationImageGenerator cig = new ConservationImageGenerator();
		InputStream invalue = (InputStream) in.getValue();

		Profiler profiler;
		MoleculeManager manager;
		GapManager gap;
		
		if (isInformationContent.isPresent()) {
			profiler = new InformationProfiler();
			
		} else {
			profiler = new ClustalProfiler();
		}
		
		if (isProtein.isPresent()) {
			manager = new ProteinManager();
		} else {
			manager = new DNAManager();
		}
		
		if (countGapOpt.isPresent()) {
			gap = new CountGap();
		} else {
			gap = new NoCountGap();
		}
		
		double[] plotdata = profiler.getdata(invalue, manager, gap);
		
		if (exportValuesOpt.isPresent()) {
			
			PrintStream out = (PrintStream) exportValuesOpt.getValue();
			
			for (double d : plotdata) {
				
				out.println(d);
				
			}
			
		}
		
		if (!noDrawOpt.isPresent()) {
		
			cig.setData(plotdata);
			
			try {   
				Renderer renderer = (Renderer) renderOpt.getValue();
				
				DrawingLayout layout = (DrawingLayout) layoutOpt.getValue();
				
				if (layout ==null) { layout = renderer.getDefaultLayout() ; }
				
				renderer.setLayout(layout);
				
				cig.printImage((File)outfile.getValue(), new RedBlueColorringStrategy(),(Integer) windowSize.getValue(),renderer );   
				
				} catch (IIOException e) {
					
					System.err.println("Hubo Un error con el formato de la imagen: "+ e.getMessage());
					
				} catch (IOException e) {
					
					System.out.println("Hubo Un error con el archivo de salida: "+e.getMessage());
					
				}
		
		}
	
	}

}
