package utils.mutualinformation.misticmod.tocytoscape.labelers;

import java.util.Map;

public class CustomNodeLabeler extends NodeLabeler {

	private Map<Integer,String> labels;
	
	public CustomNodeLabeler(Map<Integer, String> labels) {
		super();
		this.labels = labels;
	}

	@Override
	public String label(int nodeIndex) {
		return this.getLabels().get(nodeIndex);
	}

	public Map<Integer, String> getLabels() {
		return labels;
	}

	public void setLabels(Map<Integer, String> labels) {
		this.labels = labels;
	}

}
