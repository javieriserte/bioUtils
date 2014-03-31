package utils.ConservationImage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import utils.ConservationImage.renderer.ColoredLinesRenderer;
import utils.ConservationImage.renderer.DrawingLayout;
import utils.ConservationImage.renderer.DrawingLayoutLines;
import utils.ConservationImage.renderer.DrawingLayoutXYPlot;
import utils.ConservationImage.renderer.Renderer;
import utils.ConservationImage.renderer.XYPlotRenderer;

import cmdGA2.returnvalues.InfileValue;
import cmdGA2.returnvalues.ReturnValueParser;

public class RendererValue extends ReturnValueParser<Renderer> {
	///////////////////////////////////////
	// Class constants
	public static final String XY_PLOT_RENDERER = "XY";
	public static final String VERTICAL_LINES_RENDERER = "LINES";
	///////////////////////////////////////
	
	@Override
	public Renderer parse(String token) {
		Renderer renderer = null;
		if (token.equals(RendererValue.XY_PLOT_RENDERER)) {
			renderer = new XYPlotRenderer();
			renderer.setLayout(renderer.getDefaultLayout());
		} else 
		if (token.equals(RendererValue.VERTICAL_LINES_RENDERER)){
			renderer = new ColoredLinesRenderer();
			renderer.setLayout(renderer.getDefaultLayout());
		} else {
			File infile = (new InfileValue()).parse(token);
			if ( infile.exists() ) {
				
				renderer = this.parse_file(infile);
				
			}
		
		}
		return renderer;

	}
	
	private Renderer parse_file(File infile) {
		
		try {
			
			BufferedReader in = new BufferedReader(new FileReader(infile));
			
			String line = null;
			
			String rendererString = null;
			
			if ((line = in.readLine())!=null && !line.trim().equals("")) {
				// parse first line
				if (line.trim().toUpperCase().startsWith("#RENDERER:")) {
				
					rendererString = line.trim().toUpperCase().substring(10); 
						
				} else {
					in.close();
					return null;
				}
			}
			
			if (rendererString.equals(RendererValue.XY_PLOT_RENDERER)) {
				Renderer renderer = new XYPlotRenderer();
				renderer.setLayout(this.parse_xyplot(in));
				return renderer;
			} else 
			if (rendererString.equals(RendererValue.VERTICAL_LINES_RENDERER)) {
				Renderer renderer = new ColoredLinesRenderer();
				renderer.setLayout(this.parse_lines(in));
				return renderer;
			}
			in.close();
			return null;
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	
	private DrawingLayout parse_xyplot(BufferedReader in) {
		DrawingLayoutXYPlot dlXY = new DrawingLayoutXYPlot(); 
		try {
			String line = null;
			
			while((line = in.readLine())!=null) {
				
				line =line.trim().toUpperCase();
				String val = this.readValue(line);
				
				if (line.startsWith("PADDINGV")) dlXY.setPaddingV(Integer.valueOf(val));
				if (line.startsWith("PADDINGW")) dlXY.setPaddingW(Integer.valueOf(val));
				if (line.startsWith("PLOTHEIGHT")) dlXY.setPlotHeight(Integer.valueOf(val));
				if (line.startsWith("SPACE_1")) dlXY.setSpace_1(Integer.valueOf(val));
				if (line.startsWith("RULERHEIGHT")) dlXY.setRulerHeight(Integer.valueOf(val));
				if (line.startsWith("RULERLINESVSPACE")) dlXY.setRulerLinesVspace(Integer.valueOf(val));
				if (line.startsWith("RULERNUMBERSVSPACE")) dlXY.setRulerNumbersVspace(Integer.valueOf(val));
				
			}
			
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return dlXY;
	}
	
	private String readValue(String line) {
		
		return line.replaceFirst("^[^\\s]+=", "");
		
	}
	
	private DrawingLayout parse_lines(BufferedReader in) {
		DrawingLayoutLines dll = new DrawingLayoutLines(); 
		try {
			String line = null;
			
			while((line = in.readLine())!=null) {
				
				line =line.trim().toUpperCase();
				String val = this.readValue(line).trim();
				
				if (line.startsWith("PADDINGV")) dll.setPaddingV(Integer.valueOf(val));
				if (line.startsWith("PADDINGW")) dll.setPaddingW(Integer.valueOf(val));
				if (line.startsWith("LINEHEIGHT")) dll.setLineHeight(Integer.valueOf(val));
				if (line.startsWith("SPACE_1")) dll.setSpace_1(Integer.valueOf(val));
				if (line.startsWith("SPACE_2")) dll.setSpace_2(Integer.valueOf(val));
				if (line.startsWith("RULERHEIGHT")) dll.setRulerHeight(Integer.valueOf(val));
				if (line.startsWith("RULERLINESVSPACE")) dll.setRulerLinesVspace(Integer.valueOf(val));
				if (line.startsWith("RULERNUMBERSVSPACE")) dll.setRulerNumbersVspace(Integer.valueOf(val));
				if (line.startsWith("BARSPERLINE")) dll.setBarsPerLine(Integer.valueOf(val));
				
			}
			
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return dll;
	}
	

}
