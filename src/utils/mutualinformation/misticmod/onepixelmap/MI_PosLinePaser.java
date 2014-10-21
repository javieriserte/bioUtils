package utils.mutualinformation.misticmod.onepixelmap;

import io.onelinelister.OneLineListReader;

import java.util.List;

import utils.mutualinformation.misticmod.datastructures.MI_PositionWithProtein;

////////////////////////////////////////////////////////////////////////////
// Auxiliary classes
class MI_PosLinePaser implements OneLineListReader.LineParser<MI_PositionWithProtein>{

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
////////////////////////////////////////////////////////////////////////////