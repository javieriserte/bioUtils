package utils.mutualinformation.misticmod;

import io.onelinelister.OneLineListReader;

public class MI_PositionLineParser implements OneLineListReader.LineParser<MI_Position>{

	@Override
	public MI_Position parse(String line) {

		MI_Position pos = MI_Position.valueOf(line);
			
		// pos.assignProteinNumber(lengths);
		
		return pos;
	
	}
	
}
