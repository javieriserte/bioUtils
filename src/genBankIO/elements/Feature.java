package genBankIO.elements;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Feature {
	// PRIVATE INSTANCE VARIABLES
	private String name;
	private String region;
	private Map<String,String> qualifiers;

	// Constructor
	public Feature(String name, String region) {
		super();
		this.name = name;
		this.region = region;
		this.qualifiers = new HashMap<String,String>();
	}
	
	// Public Interface
	
	public String name() {
		return this.name;
	}
	
	public void addQualifier(String qualifierName, String value) {
		this.qualifiers.put(qualifierName.toUpperCase().trim(), value.toUpperCase());
	}
	
	public String getQualifierValue(String qualifierName) {
		return this.qualifiers.get(qualifierName.toUpperCase());
	}
	
	public Set<String> getQualifierNames() {
		return this.qualifiers.keySet();
	}
	
	public String getRegion() {
		return this.region;
	}
}
