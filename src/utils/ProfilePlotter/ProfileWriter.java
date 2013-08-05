package utils.ProfilePlotter;

import graphics.profile.PngWriter;
import graphics.profile.Profiler;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import cmdGA.Parser;
import cmdGA.SingleOption;
import cmdGA.exceptions.IncorrectParameterTypeException;
import cmdGA.parameterType.InFileParameter;
import cmdGA.parameterType.InputStreamParameter;
import cmdGA.parameterType.OutFileParameter;

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
		
		/////////////////////////////
		// read profile data
		double[] data = null;
		try {
			 data = readData(in);
		} catch (IOException e) {
			System.err.println("There was an error trying to read data: "+ e.getMessage());
			System.exit(1);
		} 
		
		double max = getMax(data);
		
		double min = getMin(data);
		
		System.err.println("# Min: "+ min);
		System.err.println("# Max: "+ max);
		
		int[] lengths = null;
		
		Color[] colors = new Color[]{Color.red,Color.green};
		
		try {
			lengths = readLengths(lengthsFile);
		} catch (NumberFormatException | IOException e) {
			System.err.println("There was an error trying read lengths file: "+ e.getMessage());
			System.exit(1);
		}
		
		BufferedImage image = new BufferedImage(data.length, (int) 220, BufferedImage.TYPE_INT_RGB);
		
		BufferedImage profile = (new Profiler()).draw(data, data.length, 200, max, min);
		
		Graphics2D g = (Graphics2D) image.getGraphics();
		
		g.drawImage(profile, 0, 0, null);
		
		
		int lastx = 0;
		
		for(int i=0;i<lengths.length;i++) {
			
			g.setColor(colors[i % 2]);
			
			g.fillRect(lastx, 200, lengths[i], 20);
			
			lastx = lastx + lengths[i];
			
		}
		
		try {
			(new PngWriter()).write(out,image);
		} catch (IOException e) {
			System.err.println("There was an error trying write png file: "+ e.getMessage());
			System.exit(1);
			
		}
		
	}
	
	

	private static int[] readLengths(File lengthsFile) throws NumberFormatException, IOException {
		
		String currentline = null;
		
		List<Integer> results = new ArrayList<>();
		
		BufferedReader in = new BufferedReader(new FileReader(lengthsFile));
		
		while((currentline=in.readLine())!=null) {
			
			results.add(Integer.valueOf(currentline));
			
		}
		
		int[] ints = new int[results.size()];
		
		int i=0;
		for (Integer d : results) {
			ints[i] = d;
			i++;
		}

		in.close();

		return ints;
		
	}



	private static double getMin(double[] data) {
		
		double min = 0;
		
		for (double d : data) {
			
			min = Math.min(min,d);
			
		}
		
		return min;
		
	}

	private static double getMax(double[] data) {
		
		double max = Double.NEGATIVE_INFINITY;
		
		for (double d : data) {
			
			max = Math.max(max,d);
			
		}
		
		return max;
		
	}

	private static double[] readData(BufferedReader in) throws IOException {
		
		String currentline = null;
		
		List<Double> results = new ArrayList<>();
		
		while((currentline=in.readLine())!=null) {
			
			results.add(Double.valueOf(currentline));
			
		}
		
		double[] doubles = new double[results.size()];
		
		int i=0;
		for (Double d : results) {
			doubles[i] = d;
			i++;
		}
		
		return doubles;
		
	}

}
