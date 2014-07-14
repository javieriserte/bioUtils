package utils.mutualinformation.misticmod.datastructures;

import utils.mutualinformation.misticmod.datastructures.format.FormatContainer;
import io.onelinelister.OneLineListReader;

public class MI_PositionLineParser implements OneLineListReader.LineParser<MI_Position>{

	private FormatContainer container = new FormatContainer();
	
	@Override
	public MI_Position parse(String line) {

		MI_Position pos = MI_Position.valueOf(line,container);
		
		return pos;
	
	}
	
}
