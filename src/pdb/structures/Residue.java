package pdb.structures;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * A class to represent residues of a PDB protein.
 * 
 * @author javier
 *
 */
public class Residue {
	////////////////////////////////////////////////////////////////////////////
	// Instance Variables
	private String resName;
	private Character chain;
	private Integer resNumber;
	private Map<Integer,SpacePoint> atomData;
	////////////////////////////////////////////////////////////////////////////
	
	////////////////////////////////////////////////////////////////////////////
	// Constructor
	/**
	 * Creates a new Residue
	 * @param id
	 * @param atomData
	 */
	public Residue(String id, int resNumber, char chain) {

		this.setId(id);
		
		this.setResNumber(resNumber);
		
		this.setChain(chain);

		this.setAtomData(new HashMap<Integer, SpacePoint>());
		
	}
	////////////////////////////////////////////////////////////////////////////

	////////////////////////////////////////////////////////////////////////////
	// Public Interface
	public Collection<SpacePoint> getSpacePointCollection() {
		
		return this.getAtomData().values();
		
	}
	
	public void addSpacePoint(SpacePoint spacePoint) {
		
		this.getAtomData().put(spacePoint.getAtomSerialNumber(), spacePoint);
		
	}
	
	public String getId() {
		return resName;
	}

	public void setId(String id) {
		this.resName = id;
	}

	public Map<Integer,SpacePoint> getAtomData() {
		return atomData;
	}

	public void setAtomData(Map<Integer,SpacePoint> atomData) {
		this.atomData = atomData;
	}
	/**
	 * @return the resNumber
	 */
	public Integer getResNumber() {
		return resNumber;
	}

	/**
	 * @param resNumber the resNumber to set
	 */
	public void setResNumber(Integer resNumber) {
		this.resNumber = resNumber;
	}

	public Character getChain() {
		return chain;
	}

	public void setChain(Character chain) {
		this.chain = chain;
	}
	////////////////////////////////////////////////////////////////////////////



	
}
