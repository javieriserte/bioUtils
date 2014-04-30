package utils.mutualinformation.misticmod.top;

import cmdGA2.returnvalues.ReturnValueParser;

/**
 * Parse command line text and creates corresponding MiFilter objects. 
 * 
 * @author javier iserte
 */
public class MIFilterReturnValue extends ReturnValueParser<MiFilter>{

	@Override
	public MiFilter parse(String token) {
		if (token.length()>1) {
		
			char lastChar = token.charAt(token.length()-1);
			
			String number = token.substring(0, token.length()-1);

			switch (Character.toLowerCase(lastChar)) {
				case 'p': // For Top Percentage filter
					return new SortedPercentageFilter(Double.valueOf(number));
				case 'v': // For Top Value filter
					return new SortedValuesFilter(Integer.valueOf(number));
				case 'c': // For above cutoff filter
					return new CutOffFilter(Double.valueOf(number));
				default:
					return null;
			}
			
		} else {

			return null;
			
		}
	}

}
