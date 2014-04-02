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
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

import utils.ConservationImage.color.ColoringStrategy;
import utils.ConservationImage.color.RedBlueColoringStrategy;
import utils.ConservationImage.managers.CountGap;
import utils.ConservationImage.managers.DNAManager;
import utils.ConservationImage.managers.GapManager;
import utils.ConservationImage.managers.MoleculeManager;
import utils.ConservationImage.managers.NoCountGap;
import utils.ConservationImage.managers.ProteinManager;
import utils.ConservationImage.renderer.Renderer;
import utils.ConservationImage.renderer.XYPlotRenderer;

import cmdGA2.CommandLine;
import cmdGA2.NoArgumentOption;
import cmdGA2.OptionsFactory;
import cmdGA2.SingleArgumentOption;
import cmdGA2.returnvalues.IntegerValue;
import cmdGA2.returnvalues.OutfileValue;
import cmdGA2.returnvalues.PrintStreamValue;

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
	
	public void 				exportJPG							(File outfile, BufferedImage bi) throws FileNotFoundException, IOException {
		
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

		if (args.length == 0) {
		
			guiMain();
			
		} else {
			
			commandlineMain(args);
		
		}

	}
	
	////////////////////////////////
	// Private methods
	
	private static void guiMain() {
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				ConservationImageGui inst = new ConservationImageGui();
					// creates the main instance
				
				inst.setLocationRelativeTo(null);
				inst.setVisible(true);
				inst.setTitle("Conservation Image");
				inst.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				inst.pack();
				
			}
		});
		
	}
	
	private static void commandlineMain(String[] args) {
		
		if (args.length==0) {
			System.err.println("No option was found.\nProgram Terminated.");
			System.exit(1);
		}
		
		////////////////////////////////////
		// Create Command Line.
		CommandLine cmd = new CommandLine();
		
		/////////////////////////////////////
		// Add Command line options
		SingleArgumentOption<InputStream> inOpt = OptionsFactory.createBasicInputStreamArgument(cmd);
		SingleArgumentOption<File> outOpt = new SingleArgumentOption<File>(cmd, "--outfile", new OutfileValue(), null);
		
		SingleArgumentOption<Integer> windowSizeOpt = new SingleArgumentOption<Integer>(cmd, "--window", new IntegerValue(), 11);
		NoArgumentOption isProteinOpt = new NoArgumentOption(cmd, "--protein");
		NoArgumentOption isInfContOpt = new NoArgumentOption(cmd, "--ic");
		
		Renderer defaultRenderer = new XYPlotRenderer();
		defaultRenderer.setLayout(defaultRenderer.getDefaultLayout());
		SingleArgumentOption<Renderer> layoutOpt = new SingleArgumentOption<Renderer>(cmd, "--layout", new RendererValue(), defaultRenderer);
		
		NoArgumentOption countGapOpt = new NoArgumentOption(cmd,"--countgap");
		NoArgumentOption noDrawOpt = new NoArgumentOption(cmd, "--nodraw");
		
		NoArgumentOption exportValuesOpt = new NoArgumentOption(cmd, "--export");
		SingleArgumentOption<PrintStream> exportPathOpt = new SingleArgumentOption<PrintStream>(cmd, "--path", new PrintStreamValue(), System.out);
		
		SingleArgumentOption<ColoringStrategy> colorOpt = new SingleArgumentOption<ColoringStrategy>(cmd, "--color", new ColorStrategyValue(), new RedBlueColoringStrategy());
	
		
		//////////////////////////////////////
		// PARSE THE COMMAND LINE
		cmd.readAndExitOnError(args);		
		///////////////////////////////////////
		
		///////////////////////////////////////
		// Get values from command line options
		// and validates them.
		if (outOpt.getValue() == null && !noDrawOpt.isPresent()) {
			System.err.println("No outfile was given.\n.");
			System.exit(1);
		}
		InputStream invalue     = inOpt.getValue();
		Profiler profiler       = ConservationImageGenerator.getProfiler(isInfContOpt);
		MoleculeManager manager = ConservationImageGenerator.getManager(isProteinOpt);
		GapManager gap          = ConservationImageGenerator.getGapCounting(countGapOpt);
		ColoringStrategy color  = colorOpt.getValue();
		// End of get values from command line
		/////////////////////////////////////////

		/////////////////////////////////////////
		// Calculate profile Data
		double[] plotdata       = profiler.getdata(invalue, manager, gap);		
		/////////////////////////////////////////
		
		/////////////////////////////////////////
		// Export data
		exportDataIfRequired(exportValuesOpt, exportPathOpt,plotdata);
		/////////////////////////////////////////

		ConservationImageGenerator cig = new ConservationImageGenerator();
		
		if (!noDrawOpt.isPresent()) {
		
			cig.setData(plotdata);
			
			try {
			
				Renderer renderer = layoutOpt.getValue();
				
				cig.printImage(outOpt.getValue(), color,(Integer) windowSizeOpt.getValue(),renderer );   
				
				} catch (IIOException e) {
					
					System.err.println("Hubo Un error con el formato de la imagen: "+ e.getMessage());
					
				} catch (IOException e) {
					
					System.out.println("Hubo Un error con el archivo de salida: "+e.getMessage());
					
				}
			
		}
	
	}

	private static void exportDataIfRequired( NoArgumentOption exportValuesOpt, SingleArgumentOption<PrintStream> exportPathOpt, double[] plotdata) {
		
		if (exportValuesOpt.isPresent() || exportPathOpt.isPresent()) {
			
			PrintStream out = exportPathOpt.getValue();
			
			for (double d : plotdata) {
				
				out.println(d);
				
			}
			
		}
	}

	private static GapManager getGapCounting(NoArgumentOption countGapOpt) {
		
		return (countGapOpt.isPresent())?new CountGap():new NoCountGap();
		
	}

	private static MoleculeManager getManager(NoArgumentOption isProteinOpt) {

		return (isProteinOpt.isPresent())? new ProteinManager(): new DNAManager();

	}

	private static Profiler getProfiler(NoArgumentOption isInfContOpt) {

		return  (isInfContOpt.isPresent())?new InformationProfiler(): new ClustalProfiler();

	}
	// End of private methods
	////////////////////////////////////////////
}
