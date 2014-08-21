package pdb.structures;

import io.onelinelister.OneLineListReader.LineParser;

public class SpacePointLineParser implements LineParser<SpacePoint>{

	private static final String HETATM_RECORDNAME = "HETATM";
	private static final String ATOM_RECORDNAME = "ATOM";
	private static final int FULLRECORDLENGHTH = 80;

	@Override
	public SpacePoint parse(String line) {
		
		if (line.length()<=FULLRECORDLENGHTH) {
			
			if (line.length()<FULLRECORDLENGHTH) {
				
				StringBuilder rest = new StringBuilder();
				
				int left = FULLRECORDLENGHTH - line.length();
				
				while (left>0) {
					
					rest.append(" ");
					
					left--;
					
				}
				
				line = line + rest.toString();
				
			}
			
			String type = line.substring(0,6).trim();
			
			SpacePoint point = null;
			
			switch (type) {
			
			case ATOM_RECORDNAME:
				
				point = new Atom();
				
				break;
				
			case HETATM_RECORDNAME:
				
				point = new HetAtom();
			
				break;

			}
			
			point.setAtomSerialNumber(Integer.valueOf(line.substring(6, 11).trim()));
			
			point.setAtomName(line.substring(12, 16).toCharArray());
			
			point.setAlternateLocationIndicator(line.charAt(16));
			
			point.setResidueName(line.substring(17,20).toCharArray());
			
			point.setChainIdentifier(line.charAt(21));
			
			point.setResidueSequenceNumber(Integer.valueOf(line.substring(22, 26).trim()));
			
			point.setResidueInsertionCode(line.charAt(26));
			
			point.setX(Double.valueOf(line.substring(30,38).trim()));
			
			point.setY(Double.valueOf(line.substring(38,46).trim()));
			
			point.setZ(Double.valueOf(line.substring(46,54).trim()));
			
			point.setOccupancy(Double.valueOf(line.substring(54,60).trim()));
			
			point.setTemperatureFactor(Double.valueOf(line.substring(60,66).trim()));
			
			point.setSegmentIdentifier(line.substring(72,76).toCharArray());
			
			point.setElementSymbol(line.substring(76,78).toCharArray());
			
			point.setCharge(line.substring(78,FULLRECORDLENGHTH).toCharArray());
			
			return point;
			
		} else {
			
			return null;
			
		}
		
	}

}
