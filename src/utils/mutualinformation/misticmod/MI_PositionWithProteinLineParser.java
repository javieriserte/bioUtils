package utils.mutualinformation.misticmod;

import io.onelinelister.OneLineListReader;

import java.util.List;

public class MI_PositionWithProteinLineParser implements OneLineListReader.LineParser<MI_PositionWithProtein>{

	List<Integer> lengths;
		
	protected void setLengths(List<Integer> lengths) {
			
		this.lengths = lengths;
			
	}

	@Override
	public MI_PositionWithProtein parse(String line) {

		MI_PositionWithProtein pos = MI_PositionWithProtein.valueOf(line);
			
		pos.assignProteinNumber(lengths);
		
		return pos;
	
	}
	
}
