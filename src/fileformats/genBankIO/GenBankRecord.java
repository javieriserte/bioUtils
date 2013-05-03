package fileformats.genBankIO;

import fileformats.genBankIO.elements.Feature;
import fileformats.genBankIO.elements.GenBankHeader;
import fileformats.genBankIO.elements.Origin;

import java.io.Serializable;
import java.util.List;

/**
 * This class manages a single GenBank record.<br>
 * Each GenBank record is organized in three sections:<br>
 * <ol>
 * <li> Header: Information about the definition of the record, publications, authors, etc, </li>
 * <li> Feature List: A list of annotations of the sequence data, like Coding sequences, Promoter regions, etc</li>
 * <li> Origin: Full plain sequences of the record. </li>
 * </ol>
 * For every of these three sections there is an appropriate object to store the data.
 * <ol type=disc>
 * <li> For Header there is a <code>GenBankHeader</code> object.</li>
 * <li> For each Feature there is a <code>Feature</code> object.</li>
 * <li> For Origin there is a <code>Origin</code> object.</li>
 * </ol>
 * <code>Origin</code> object can be present or not in the GenBankReader.
 * 
 * @author Javier Iserte (jiserte@unq.edu.ar)
 */
public class GenBankRecord implements Serializable {

	//////////////////////////////
	// Private Instance Variables
	private static final long serialVersionUID = -8284380031577259687L;
	private GenBankHeader header;
	private List<Feature> features;
	private Origin origin;
	
	
	//////////////////////////////
	// Constructor
	/**
	 * Builds a GenBankRecord object from header, features and origin data. 
	 */
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

	
	/////////////////////////////
	// Public Interface
	/**
	 * GenBank Origin data can be voliminous and sometimes is not needed to be parsed 
	 * or stored, so <code>GenBankRecord</code> objects permits to do not store it.<br> 
	 * This method tests if the record has origin data.
	 * 
	 * @return <b>True</b> if origin data was added to the record.<br>
	 *         <b>False</b> Otherwise.
	 * 
	 */
	public boolean hasOrigin() {
		return origin != null;
	}
	
	///////////////////////////////
	// Getters And Setters

	/**
	 * Recovers the header information.
	 * 
	 * @see GenBankHeader
	 * @return a GenBankHeader object
	 */
	public GenBankHeader getHeader() {
		return header;
	}
	
	/**
	 * Recovers the features information as a <code>List</code> of <code>Feature</code> objects.
	 * 
	 * @see Feature
	 * @return a <code>List&lt;Feature&gt;</code> objects.
	 *  
	 */
	public List<Feature> getFeatures() {
		return features;
	}

	/**
	 * Recovers the origin information from a GenBank Record.
	 * 
	 * @see Origin
	 * @return an <b><code>Origin<code></b> object if there is data stored.<br>
	 *         <b>null</b> object otherwise.
	 */
	public Origin getOrigin() {
		return origin;
	}
	
	
	
	
}
