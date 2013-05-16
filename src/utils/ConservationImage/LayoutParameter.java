package utils.ConservationImage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import utils.ConservationImage.renderer.DefaultLayoutFactory;
import utils.ConservationImage.renderer.DrawingLayoutLines;
import utils.ConservationImage.renderer.DrawingLayoutXYPlot;

import cmdGA.exceptions.IncorrectParameterTypeException;
import cmdGA.parameterType.InFileParameter;
import cmdGA.parameterType.ParameterType;

public class LayoutParameter extends ParameterType {

	protected static LayoutParameter singleton = new LayoutParameter();
	/**
	 * No instance variables are used, so there is no need of more than one instance.
	 * A 'singleton' pattern is implemented. 

	 * @return the only one instance InFileParameter
	 */	
	public static LayoutParameter getParameter() {
		return (LayoutParameter) singleton;
	}
	
	
	@Override
	protected Object parse(String parameter) {
		
		InFileParameter ifp = InFileParameter.getParameter();
		
		try {
			
			File configfile = (File) ifp.parseParameter(parameter);
			
			BufferedReader in = new BufferedReader(new FileReader(configfile));
			
			String line = null;
			
			String renderer = null;
			
			if ((line = in.readLine())!=null && !line.trim().equals("")) {
				// parse first line
					
				if (line.trim().toUpperCase().startsWith("#RENDERER:")) {
				
					renderer = line.trim().toUpperCase().substring(10); 
						
				} else {
					in.close();
					return new DefaultLayoutFactory();
						
				}
				
			}
			
			if (renderer.equals("XYPLOY")) {
				return parse_xyplot(in);
			} else 
			if (renderer.equals("LINES")) {
				return parse_lines(in);
			}
			in.close();
			return new DefaultLayoutFactory();
			
		} catch (IncorrectParameterTypeException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
	private Object parse_lines(BufferedReader in) {
		DrawingLayoutLines dll = new DrawingLayoutLines(); 
		try {
			String line = null;
			
			while((line = in.readLine())!=null) {
				
				line =line.trim().toUpperCase();
				String val = this.readValue(line);
				
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
	
	
	
	private String readValue(String line) {
		
		return line.replaceFirst("^[A-Z]+.", "");
		
	}
	private Object parse_xyplot(BufferedReader in) {
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


}
