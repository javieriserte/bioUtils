package utils.mutualinformation.misticmod.tocytoscape.labelers;

public class MiEdgeLabeler extends EdgeLabeler {

	private NodeLabeler nodelabeler;
	
	
	
	public MiEdgeLabeler(NodeLabeler nodelabeler) {
		super();
		this.setNodelabeler(nodelabeler);
	}

	@Override
	public String label(int edgeIndex1, int edgeIndex2) {
		
		return getNodelabeler().label(edgeIndex1) + " MI " + getNodelabeler().label(edgeIndex2);
		
	}

	/**
	 * @return the nodelabeler
	 */
	protected NodeLabeler getNodelabeler() {
		return nodelabeler;
	}

	/**
	 * @param nodelabeler the nodelabeler to set
	 */
	protected void setNodelabeler(NodeLabeler nodelabeler) {
		this.nodelabeler = nodelabeler;
	}

}
