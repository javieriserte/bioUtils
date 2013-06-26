package utils.ConservationImage;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.IIOException;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;

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
		
		ImageWriter imagewriter = ImageIO.getImageWritersByFormatName("png").next();
		
		ImageWriteParam writerparam = imagewriter.getDefaultWriteParam();
		
		writerparam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		
		writerparam.setCompressionQuality(1.0f);
	
		imagewriter.setOutput(out);
		
		imagewriter.write(null, new IIOImage(bi, null, null), writerparam);
		
		imagewriter.dispose();
//		ImageIO.write(bi, "jpeg", out);
		
//		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
//		JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bi);
//		param.setQuality(1.0f, false);
//		encoder.setJPEGEncodeParam(param);
//		encoder.encode(bi);
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
		
			
		if (outfile.getValue() == null) {
			System.err.println("No outfile was given.\n.");
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
		
		cig.setData(plotdata);
		
		try {   
			Renderer renderer = (Renderer) renderOpt.getValue();
			
			DrawingLayout layout = (DrawingLayout) layoutOpt.getValue();
			
			if (layout ==null) { layout = renderer.getDefaultLayout() ; }
			
			renderer.setLayout(layout);
			
			cig.printImage((File)outfile.getValue(), new RedBlueColorringStrategy(),(Integer) windowSize.getValue(),renderer );   		
			} catch (IIOException e) { System.out.println("Hubo Un error con el formato de la imagen."); 
				e.printStackTrace();  
			} catch (IOException e) {          System.out.println("Hubo Un error con el archivo de salida.");
				e.printStackTrace(); }
	}

}
