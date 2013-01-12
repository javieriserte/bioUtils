package genBankIO.elements;

import java.util.List;

public class GenBankRecord {
	private GenBankHeader header;
	private List<Feature> features;
	private Origin origin;
	
	
	// Constructor
	public GenBankRecord(GenBankHeader header, List<Feature> features,
			Origin origin) {
		super();
		this.header = header;
		this.features = features;
		this.origin = origin;
	}

	public GenBankRecord(GenBankHeader header, List<Feature> features) {
		super();
		this.header = header;
		this.features = features;
	}

	
	// Public Interface
	
	public boolean hasOrigin() {
		return origin != null;
	}
	
	// Getters And Setters

	public GenBankHeader getHeader() {
		return header;
	}

	public List<Feature> getFeatures() {
		return features;
	}

	public Origin getOrigin() {
		return origin;
	}
	
	
	
	
}
