package utils.ProfilePlotter;

import graphics.profile.PngWriter;
import io.onelinelister.OneLineListReader;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BoxAndWhiskerRenderer;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;

import cmdGA2.CommandLine;
import cmdGA2.MultipleArgumentOption;
import cmdGA2.SingleArgumentOption;
import cmdGA2.returnvalues.InfileValue;
import cmdGA2.returnvalues.OutfileValue;
import cmdGA2.returnvalues.StringValue;

public class BoxplotFromProfileRegion {

	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		
		//////////////////////////////
		// Create Parser
		CommandLine cmd = new CommandLine();
		
		//////////////////////////////
		// Add Command line arguments
		MultipleArgumentOption<File> dataOpt = new MultipleArgumentOption<>(cmd, "--data", ',', new ArrayList<File>() , new InfileValue());
		
		MultipleArgumentOption<String> seriesTitleOpt = new MultipleArgumentOption<>(cmd, "--series", ',', new ArrayList<String>() , new StringValue());
		
		SingleArgumentOption<File> outFileOpt = new SingleArgumentOption<File>(cmd, "--out", new OutfileValue(), null);
		
		SingleArgumentOption<File> lengthsFileOpt =  new SingleArgumentOption<File>(cmd, "--lengths", new InfileValue(), null);
		
		SingleArgumentOption<File> namesFileOpt =  new SingleArgumentOption<File>(cmd, "--names", new InfileValue(), null);
		
		//////////////////////////////
		// Try to parse command line
		cmd.readAndExitOnError(args);
		
		//////////////////////////////
		// Validate Command Line
		
		if (!dataOpt.isPresent() || !outFileOpt.isPresent() || !lengthsFileOpt.isPresent() || !seriesTitleOpt.isPresent()) {
			
			System.err.println("Options: -data -series -out & -lengths are mandatory");
			
			System.exit(1);
			
		}
		
		//////////////////////////////
		// Recover parameters from 
		// command line arguments
		List<File> dataFiles = dataOpt.getValues();
		
		List<String> seriesTitle = seriesTitleOpt.getValues();
		
		File out = outFileOpt.getValue();
		
		File lengthsFile = lengthsFileOpt.getValue();
		
		File namesFile = namesFileOpt.getValue();
		
		/////////////////////////////
		// read data
		List<double[]> data = readDataFiles(dataFiles);
		
		List<Integer> lengths = readLengthsFile(lengthsFile);
		
		List<String> names = readNamesFile(namesFile);
		
		/////////////////////////////
		// Create boxplot
		BufferedImage boxplot  = drawBoxPlot(data,seriesTitle, lengths, names);
		
		////////////////////////////////
		// Write Image To File
		try {
			
			(new PngWriter()).write(out,boxplot);
			
		} catch (IOException e) {
			
			System.err.println("There was an error trying write png file: "+ e.getMessage());
			
			System.exit(1);
		
		}
		/////////////////////////
		// End of Main Code
		/////////////////////////

	}

	private static BufferedImage drawBoxPlot(List<double[]> data,
			List<String> seriesTitle, List<Integer> lengths, List<String> names) {

		List<List<List<Double>>> seriesData = splitDataBySerie(lengths, data);
        
        DefaultBoxAndWhiskerCategoryDataset dataset = new DefaultBoxAndWhiskerCategoryDataset();
        
		//Color[] colors = new Color[]{new Color(255,0, 0, 50) , new Color(0, 255, 0, 50), new Color(0, 0, 255, 50) };
		
		Color[] colors = createRandomColors(lengths.size());
        
        int seriesCounter = 0;
        
        for (List<List<Double>> serieData : seriesData) {
        	
        	int nameCounter = 0;
        	
        	String name = "";
        	
	        for (List<Double> regionData : serieData) {
	        	
	        	if (!names.isEmpty()) {
	        	
	        		name = names.get(nameCounter);
	        				
	        	} else {
	        		
	        		name = "Region " + String.valueOf(nameCounter+1);
	        		
	        	}
	
	        	dataset.add(regionData, name, seriesTitle.get(seriesCounter));
	        	
	        	nameCounter++;
	        }
	        
	        seriesCounter++;
        
        }
        
        final CategoryAxis xAxis = new CategoryAxis("Regions");
        final NumberAxis yAxis = new NumberAxis("Conservation");
        yAxis.setAutoRangeIncludesZero(false);
        final BoxAndWhiskerRenderer renderer = new BoxAndWhiskerRenderer();
        renderer.setFillBox(false);
        
        for (int i= 0; i<dataset.getRowCount(); i++) {
        	
        	Color currentColor = colors[ i % colors.length];
        	
        	renderer.setSeriesPaint(i, currentColor);
        	
//        	renderer.setBasePaint(currentColor);
        	renderer.setArtifactPaint(currentColor);
        	
        	renderer.setSeriesItemLabelFont(i, new Font("Arial", 1, 30));
        	
        }
        
        renderer.setWhiskerWidth(0.8);
        
        renderer.setItemMargin(0.5);
        
        yAxis.setLabelFont(new Font("Arial", 1, 35));
        
        xAxis.setLabel("");
        
        xAxis.setTickLabelFont(new Font("Arial", 1, 35));
        
        yAxis.setTickLabelFont(new Font("Arial", 1, 20));
        
        final CategoryPlot plot = new CategoryPlot(dataset, xAxis, yAxis, renderer);
        
        plot.setBackgroundPaint(new Color(225,225,225));

        final JFreeChart chart = new JFreeChart(
            "",
            new Font("Arial", 1, 45),
            plot,
            true
        );
        
        
        
        renderer.setDefaultLegendTextFont(new Font("Arial", 1, 25));
        
		return chart.createBufferedImage(3000, 1200);
		
	}



	private static Color[] createRandomColors(int size) {
		
		Color[] colors = new Color[size];
		
		for (int i = 0; i<size; i++) {
			
			int full = (int) (150 + Math.random()*550);
			
			int r = (int)Math.max(Math.min((full * Math.random()),255),0);
			
			int g = (int)Math.max(Math.min(((full -r) * Math.random()),255),0) ;
			
			int b = Math.max(Math.min(full -r -g,255),0);
			
			colors[i] = new Color(r,b,g,200);
			
		}
		

		return colors;
	}

	private static List<Integer> readLengthsFile(File lengthsFile) {
		
		if (lengthsFile == null) {
			
			return new ArrayList<Integer>();
			
		} else {
		
			return OneLineListReader.createOneLineListReaderForInteger().read(lengthsFile);
			
		}
		
	}
	
	private static List<String> readNamesFile(File namesFile) {
		
		if (namesFile == null) {
			
			return new ArrayList<String>();
			
		} else {
		
			return OneLineListReader.createOneLineListReaderForString().read(namesFile);
			
		}
		
	}

	private static List<double[]> readDataFiles(List<File> dataFiles) {
		
		List<double[]> seriesData = new ArrayList<>();
		
		for (File dataFile : dataFiles) {
			
			List<Double> dataList = OneLineListReader.createOneLineListReaderForDouble().read(dataFile);
			
			double[] dataArray = new double[dataList.size()];
			
			int counter = 0;
			
			for (Double value : dataList) {
				
				dataArray[counter++] =value;
				
			}
			
			seriesData.add(dataArray);
			
		}
		
		return seriesData;
		
	}
	
	private static List<List<List<Double>>> splitDataBySerie(
			List<Integer> lengths, List<double[]> data) {
		List<List<List<Double>>> result = new ArrayList<List<List<Double>>>();
		
		for (double [] serieData : data) {
			
			result.add(splitDataByRegion(lengths, serieData));
			
		}
		
		return result;
		
	}
	
	
	private static List<List<Double>> splitDataByRegion(List<Integer> lengths, double[] data) {

		List<List<Double>> result = new ArrayList<>();
		
		int dataCounter = 0 ;
			
		for (int i=0 ; i< lengths.size(); i++) {
				
			List<Double> regionData = new ArrayList<>();
				
			for (int j=0 ; j<lengths.get(i) ; j++) {
				
				regionData.add(data[dataCounter]);
				
				dataCounter++;
					
			}
				
			result.add(regionData);
				
		}

		return result;
		
	}

}
