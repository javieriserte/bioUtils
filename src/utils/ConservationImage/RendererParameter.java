package utils.ConservationImage;

import cmdGA.parameterType.ParameterType;

public class RendererParameter extends ParameterType  {

	public static final String XY = "XY";
	public static final String LINES = "LINES";
	
	protected static ParameterType  singleton = new RendererParameter();
	/**
	 * No instance variables are used, so there is no need of more than one instance.
	 * A 'singleton' pattern is implemented. 

	 * @return the only one instance RendererParameter
	 */
	public static RendererParameter getParameter() {
		return (RendererParameter) singleton;
	}
	/**
	 * Parse Method.
	 * 
	 * @return a Renderer object depending on the value of <code>parameter</code> variable
	 */
	protected Object parse(String parameter) {
		parameter = parameter.trim().toUpperCase();
		System.err.println("parameter: "+parameter + " - XY: "+ XY);
		if (parameter.equals(XY)) {
			return (Object) new XYPlotRenderer();
		} else 
		if (parameter.equals(LINES)) {
			return (Object) new ColoredLinesRenderer();
		}
		return null;
		
	}

}
