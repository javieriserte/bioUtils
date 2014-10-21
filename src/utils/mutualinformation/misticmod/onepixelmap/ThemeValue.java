package utils.mutualinformation.misticmod.onepixelmap;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import utils.mutualinformation.misticmod.onepixelmap.themes.BlueAndRedTheme;
import utils.mutualinformation.misticmod.onepixelmap.themes.MatrixColoringTheme;
import utils.mutualinformation.misticmod.onepixelmap.themes.YellowAndRedTheme;
import cmdGA2.returnvalues.ReturnValueParser;

public class ThemeValue extends ReturnValueParser<MatrixColoringTheme> {
    private static String THEME_REGEX = "(?<name>[A-Z]+)(:(?<cutoff>[0-9]+))*"; 
	//private static String THEME_REGEX = "(<name>[A-Z]+)";
	@Override
	public MatrixColoringTheme parse(String token) {
		
		Pattern p = Pattern.compile(THEME_REGEX);
		
		Matcher matcher = p.matcher(token.trim());
		
		double cutoff = 0;
		
		if (matcher.matches()) {

			String name = matcher.group("name");
			
			String cutOffString = matcher.group("cutoff");

			switch (name.toUpperCase()) {
			case "YELLOW":
				cutoff = (cutOffString==null) ? 6.5 : Double.valueOf(cutOffString);
				return new YellowAndRedTheme(cutoff);

			case "BLUERED":
				cutoff = (cutOffString==null) ? 0 : Double.valueOf(cutOffString);
				return new BlueAndRedTheme(cutoff);
				
			default:
				cutoff = (cutOffString==null) ? 0 : Double.valueOf(cutOffString);
				return new BlueAndRedTheme(cutoff);
			}
			
		}
		
		return new BlueAndRedTheme(cutoff);
		
		
	}

}
