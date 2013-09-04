package utils.ConservationImage;

import utils.ConservationImage.color.BlackColoringStrategy;
import utils.ConservationImage.color.LightRedBlueColoringStrategy;
import utils.ConservationImage.color.RedBlueColoringStrategy;

import cmdGA.parameterType.ParameterType;

public class ColorStrategyParameter extends ParameterType {
	public final String LIGHTREDBLUE = "LIGHTREDBLUE";
	public final String REDBLUE = "REDBLUE";
	public final String BLACK = "BLACK";
	
	protected static ParameterType singleton = new ColorStrategyParameter();
	/**
	 * No instance variables are used, so there is no need of more than one instance.
	 * A 'singleton' pattern is implemented. 

	 * @return the only one instance InFileParameter
	 */	
	public static ColorStrategyParameter getParameter() {
		return (ColorStrategyParameter) singleton;
	}

	@Override
	protected Object parse(String parameter) {

		String st = parameter.trim();
		
		st = st.replaceAll("\"", "");
		st = st.replaceAll("\'", "");
		
		if (st.equalsIgnoreCase(BLACK)) {
			
			return new BlackColoringStrategy();
			
		} else 
			
		if (st.equalsIgnoreCase(LIGHTREDBLUE)) {
			
			return new LightRedBlueColoringStrategy();
			
		} else 
			
		if (st.equalsIgnoreCase(REDBLUE)) {
			
			return new RedBlueColoringStrategy();
			
		} else {
			
			return null;
			
		}
		
	}

}
