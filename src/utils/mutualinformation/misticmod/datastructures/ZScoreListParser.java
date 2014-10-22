package utils.mutualinformation.misticmod.datastructures;


import utils.mutualinformation.misticmod.datastructures.format.FormatContainer;
import io.onelinelister.OneLineListReader;
public class ZScoreListParser implements OneLineListReader.LineParser<Double> {

	private FormatContainer container = new FormatContainer();
	
	@Override
	public Double parse(String line) {
		
		if (container.isNullFormat()) {
			
			container.setCorrespondingFormat(line);
			
		}
		
		double zscore = container.format.getZscoreFrom(line);
		
		return zscore;
	}
	
}
