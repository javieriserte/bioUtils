package utils.mutualinformation.misticmod.tocytoscape.labelers;

public class NumericNodeLabeler extends NodeLabeler {

	@Override
	public String label(int nodeIndex) {

		return String.valueOf(nodeIndex);
		
	}

}
