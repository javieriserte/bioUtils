package utils.ProfilePlotter;

import graphics.profile.PngWriter;

import io.onelinelister.OneLineListReader;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BoxAndWhiskerRenderer;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.Layer;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.TextAnchor;

import cmdGA.NoOption;
import cmdGA.Parser;
import cmdGA.SingleOption;
import cmdGA.exceptions.IncorrectParameterTypeException;
import cmdGA.parameterType.FontParameter;
import cmdGA.parameterType.InFileParameter;
import cmdGA.parameterType.InputStreamParameter;
import cmdGA.parameterType.OutFileParameter;
import cmdGA.parameterType.StringParameter;

/**
 * Draws a plot, from a given set of values, as a profile.
 * Regions can be marked on the plot, with labels. Regions are described only by 
 * its lengths, are expected to be ordered as desired to be shown. Regions starts 
 * from position 1 or the next position from the last region.   
 * 
 * Only one data series is expected. Each point consist in only one y-value, 
 * the x.value is the reading order. 
 * 
 * Uses jfreechart library to create the plot.
 * 
 * @author Javier Iserte
 *
 */
public class ProfileWriter {
	
	public static void main(String[] args) {
		
		//////////////////////////////
		// Create Parser
		Parser parser =new Parser();
		
		//////////////////////////////
		// Add Command line arguments
		SingleOption dataOpt = new SingleOption(parser, System.in, "-data", InputStreamParameter.getParameter());

		SingleOption outOpt = new SingleOption(parser,null , "-outfile", OutFileParameter.getParameter());
		
		SingleOption lengthsOpt = new SingleOption(parser, null, "-lengths", InFileParameter.getParameter());
		
		SingleOption namesOpt = new SingleOption(parser, null, "-names", InFileParameter.getParameter());
		
		SingleOption titleOpt = new SingleOption(parser, "Profile", "-title", StringParameter.getParameter());
		
		SingleOption labelFontOpt = new SingleOption(parser, new Font("Arial", Font.BOLD, 24) , "-font", FontParameter.getParameter());
		
		NoOption boxplotOpt = new NoOption(parser, "-box");
		
		//Font parameter accepts: "[fontname, fontstyle, size]" or "[fontname, fontstyle]" or "[fontname]".
		
		//////////////////////////////
		// Try to parse command line
		try {
			parser.parseEx(args);
		} catch (IncorrectParameterTypeException e) {
			System.err.println("There was an error trying to parse the command line: "+ e.getMessage());
			System.exit(1);
		}
		
		//////////////////////////////
		// Recover parameters from 
		// command line arguments
		BufferedReader in = new BufferedReader(new InputStreamReader((InputStream) dataOpt.getValue()));
		
		File out = (File) outOpt.getValue();
		
		File lengthsFile = (File) lengthsOpt.getValue();
		
		File namesFile = (File) namesOpt.getValue();
		
		String title = (String) titleOpt.getValue();
		
		Font plotLabelFont = (Font) labelFontOpt.getValue();
		
		/////////////////////////////
		// read profile data
		double[] data = null;
		
		try {
			 data = readData(in);
		} catch (IOException e) {
			System.err.println("There was an error trying to read data: "+ e.getMessage());
			System.exit(1);
		} 

		/////////////////////////////////
		// Define plots colors and fonts
		Color[] colors = new Color[]{new Color(255,0, 0, 50) , new Color(0, 255, 0, 50)};
		Color plotLinesColor = Color.black;
		
		BufferedImage profileImage = null;
		
		if (!boxplotOpt.isPresent()) {
		
			//////////////////////////////
			// Create Chart and add profile
			JFreeChart chart = createBasicChart(title, data, plotLinesColor);

			/////////////////////////////////////
			// Add region markers 
			addMarkerRegions(lengthsFile, namesFile, colors, plotLabelFont,	chart);
		
			/////////////////////////////////////
			// Export Image
			profileImage = chart.createBufferedImage( data.length, 300);
		
		} else {

			List<List<Double>> regionsData = splitDataByRegion(lengthsFile, data);
	        
	        DefaultBoxAndWhiskerCategoryDataset dataset = new DefaultBoxAndWhiskerCategoryDataset();
	        
	        String[] names = null;
	        
			try {
				names = readNames(namesFile);
			} catch (NumberFormatException | IOException e) {
				e.printStackTrace();
			}
	        
	        int nameCounter = 0;
	        String name = "";
	        
	        for (List<Double> regionData : regionsData) {
	        	
	        	if (names!= null) {
	        	
	        		name = names[nameCounter];
	        				
	        	} else {
	        		
	        		name = "Region " + String.valueOf(nameCounter+1);
	        		
	        	}

	        	dataset.add(regionData, name, "");
	        	
	        	nameCounter++;
	        }
	        
	        
	        
	        final CategoryAxis xAxis = new CategoryAxis("Regions");
	        final NumberAxis yAxis = new NumberAxis("Conservation");
	        yAxis.setAutoRangeIncludesZero(false);
	        final BoxAndWhiskerRenderer renderer = new BoxAndWhiskerRenderer();
	        renderer.setFillBox(false);
	        
	        for (int i= 0; i<dataset.getRowCount(); i++) {
	        	
	        	Color currentColor = new Color(
	        			colors[i % colors.length].getRed(),
	        			colors[i % colors.length].getGreen(),
	        			colors[i % colors.length].getBlue(),
	        			200);
	        	
	        	renderer.setSeriesPaint(i, currentColor);
	        	
	        	renderer.setBasePaint(currentColor);
	        	
	        }
	        
	        renderer.setWhiskerWidth(0.8);
	        
	        renderer.setItemMargin(0.5);
	        
	        final CategoryPlot plot = new CategoryPlot(dataset, xAxis, yAxis, renderer);

	        final JFreeChart chart = new JFreeChart(
	            title,
	            plotLabelFont,
	            plot,
	            true
	        );
	        
	        profileImage = chart.createBufferedImage( 1500, 600);
	        
		}
		
		try {
			
			(new PngWriter()).write(out,profileImage);
			
		} catch (IOException e) {
			
			System.err.println("There was an error trying write png file: "+ e.getMessage());
			
			System.exit(1);
		
		}
		/////////////////////////
		// End of Main Code
		/////////////////////////
	}

	private static List<List<Double>> splitDataByRegion(File lengthsFile, double[] data) {

		List<List<Double>> result = new ArrayList<>();
		
		try {
			
			int[] lengths = readLengths(lengthsFile);
			
			int dataCounter = 0 ;
			
			for (int i=0 ; i< lengths.length; i++) {
				
				List<Double> regionData = new ArrayList<>();
				
				for (int j=0 ; j<lengths[i] ; j++) {
					
					regionData.add(data[dataCounter]);
					
					dataCounter++;
					
				}
				
				result.add(regionData);
				
			}
			
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
		
		return result;
		
	}

	/////////////////////////////////////////
	// Private Methods
	/**
	 * Draws region markers into a given chart. Region data must be provided in 
	 * a file. Also, add label with each region names if a source file is provided.
	 *  
	 * @param lengthsFile
	 * @param namesFile
	 * @param colors
	 * @param plotLabelFont
	 * @param chart
	 */
	private static void addMarkerRegions(File lengthsFile,
			File namesFile, Color[] colors, Font plotLabelFont, JFreeChart chart) {
		
		try {
		
			if (lengthsFile!=null) {
			
				int[] lengths = readLengths(lengthsFile);
				
				String[] names = readNames(namesFile);
				
				int begin = 0;

				for (int i = 0; i< lengths.length ; i++) {
					
					int end = begin + lengths[i];
					
					IntervalMarker marker = new IntervalMarker(begin+1, end, colors [i % colors.length]);
					
					begin = end;
				
					if (namesFile!=null) {
					
						////////////////////////////////
						// Set Label Properties
						marker.setLabel(names[i]);
						marker.setLabelFont(plotLabelFont);
						marker.setLabelAnchor(RectangleAnchor.BOTTOM);
						marker.setLabelTextAnchor(TextAnchor.BOTTOM_CENTER);
					
						/////////////////////////////////
						// Add Protein Marker
						chart.getXYPlot().addDomainMarker(marker,Layer.BACKGROUND);
						
					}
					
				}
				
			} else {
				
				System.err.println("A valid lengths file must be provided (-lengths option).");
				
				System.exit(1);
				
			}

		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Creates the basic Chart. This chart includes, profile data, main title 
	 * and x and y labels. 
	 * @param title
	 * @param data
	 * @param plotLinesColor
	 * @return
	 */
	private static JFreeChart createBasicChart(String title, double[] data, Color plotLinesColor) {
		
		XYSeries series = new XYSeries("");
		
		for (int i =0 ; i<data.length ;i++) {
			
			series.add(i, data[i]);
			
		}
		
		XYSeriesCollection dataset = new XYSeriesCollection();
		
		dataset.addSeries(series);

		JFreeChart chart = ChartFactory.createXYLineChart(
				title, // Title
				"Residue position",  // x-axis Label
				"Conservation",      // y-axis Label
				dataset,  			 // Dataset
				PlotOrientation.VERTICAL, // Plot Orientation
				false,    			// Show Legend
				false,   			// Use tooltips
				false 				// Configure chart to generate URLs?
				);
		
		chart.getXYPlot().getDomainAxis().setRange(0, data.length);
		
		chart.getXYPlot().getRenderer().setSeriesPaint(0, plotLinesColor);
		
		return chart;
		
	}
	
	/**
	 * Read names of regions from a file is a valid one is provided.
	 * @param namesFile
	 * @return a list of region names read from a file
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	private static String[] readNames(File namesFile) throws NumberFormatException, IOException {
		
		if (namesFile==null) {
		
			return null;
			
		}
		
		OneLineListReader<String> reader = OneLineListReader.createOneLineListReaderForString();
		
		List<String> results = reader.read(namesFile);
		
		String[] names = new String[results.size()];
		
		for (int i = 0; i< results.size(); i++) {
			
			names[i] = results.get(i);
			
		}

		return names;
		
	}
	
	/**
	 * Read region lengths
	 * 
	 * @param lengthsFile
	 * @return
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	private static int[] readLengths(File lengthsFile) throws NumberFormatException, IOException {
		
		OneLineListReader<Integer> reader = OneLineListReader.createOneLineListReaderForInteger();
		
		List<Integer> results = reader.read(lengthsFile);
		
		int[] ints = new int[results.size()];
		
		int i=0;
		for (Integer d : results) {
			ints[i] = d;
			i++;
		}

		return ints;
		
	}
	
	/**
	 * Read Data from a input buffer.
	 * @param in
	 * @return
	 * @throws IOException
	 */
	private static double[] readData(BufferedReader in) throws IOException {
		
		OneLineListReader<Double> reader = OneLineListReader.createOneLineListReaderForDouble();
		
		List<Double> results = reader.read(in);
		
		double[] doubles = new double[results.size()];
		
		int i=0;
		for (Double d : results) {
			doubles[i] = d;
			i++;
		}
		
		return doubles;
		
	}
	
}
