package genBankIO.elements;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
/**
 * <code>Feature</code> objest stores data from a single features of a GenBank record with all the qualifiers.<br>
 *  
 * A <code>Feature</code> has the following data:
 * <ol>
 * <li> Name: Is a identifier of the Feature. 'CDS' or 'gene' are examples.</li>
 * <li> Region: Is the region of the genome were the Feature locates</li>
 * <li> Qualifiers: Is a list of key-value pairs that specifies data for the Features. 
 * 'Translation' is an example qualifiersfor 'CDS' Feature. Uncommonly, qualifiers have not a value. In these case an empty String is stored as value</li>
 * </ol>
 * 
 * The values for all the data are stored as <code>String</code> objects. 
 * If needed, the user must parse each <String> to a better object.
 * All the qualifiers names are stores as capital letters.  
 * 
 * @author Javier Iserte (jiserte@unq.edu.ar)
 *
 */
public class Feature {
	// PRIVATE INSTANCE VARIABLES
	private String name;
	private String region;
	private Map<String,String> qualifiers;

	// Constructor
	/**
	 * Creates a <code>Feature</code> from a name and the region.
	 * This data belongs to the first line of the feature annotation in GenBank.
	 */
	public Feature(String name, String region) {
		super();
		this.name = name;
		this.region = region;
		this.qualifiers = new HashMap<String,String>();
	}
	
	// Public Interface
	/**
	 * Returns the name of the Feature.
	 * 
	 * @return a <code>String</code> with the name of the feature.
	 */
	public String name() {
		if (this.name==null) {return "";};
		return this.name;
	}
	
	/**
	 * Stores a qualifier for the feature.
	 * 
	 * @param qualifierName is a <code>String</code> with the qualifier name.
	 * @param value is as <code>String</code> with qualifier value.
	 */
	public void addQualifier(String qualifierName, String value) {
		if(qualifierName!=null) {
			if (value==null) value = "";
			this.qualifiers.put(qualifierName.toUpperCase().trim(), value.toUpperCase());
		}
	}
	
	/**
	 * Recovers a qualifier value.
	 * 
	 * @param qualifierName is a <code>String</code> with the qualifier name.
	 * @return a <code>String</code> with the qualifier value.
	 */
	public String getQualifierValue(String qualifierName) {
		if (qualifierName!= null) {
			return this.qualifiers.get(qualifierName.toUpperCase());
		}
		return "";
	}
	
	/**
	 * Recovers the names of all the qualifiers.
	 * @return a <code>Set</code> of <code>String</code> objects with all the qualifier names.
	 */
	public Set<String> getQualifierNames() {
		return this.qualifiers.keySet();
	}
	
	/**
	 * Recovers the region data for the feature.
	 * 
	 * @return a <code>String</code> with the region data.
	 */
	public String getRegion() {
		if (this.region!=null) return this.region;
		return "";
	}
	
	/**
	 * Checks if a given qualifier is annotated for the feature.
	 * 
	 * @param qualifierName is a <code>String</code> with the qualifier name.
	 * @return <b>True</b> if the qualifier is annotated.<br>
	 *         <br>False</b> otherwise.
	 */
	public boolean existsQualifier(String qualifierName) {
		if (qualifierName!=null) return this.qualifiers.keySet().contains(qualifierName.toUpperCase());
		return false;
	}
}
