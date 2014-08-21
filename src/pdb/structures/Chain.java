package pdb.structures;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class to represent chains of a pdb file
 * 
 * @author javier
 *
 */
public class Chain {
	////////////////////////////////////////////////////////////////////////////
	// Instance Variables
	private char chainIdentifier;
	private Map<Integer,Residue> residues;
	////////////////////////////////////////////////////////////////////////////
	
	////////////////////////////////////////////////////////////////////////////
	// Constructor
	public Chain(char chainIdentifier) {
		
		this.setChainIdentifier(chainIdentifier);
		
		this.setResidues(new HashMap<Integer, Residue>());
		
	}
	////////////////////////////////////////////////////////////////////////////

	////////////////////////////////////////////////////////////////////////////
	// Public Interface
	public Collection<Residue> getResiduesCollection() {

		return this.getResidues().values();
		
	}
	
	public void addResidue(Residue residue) {
		
		this.getResidues().put(residue.getResNumber(), residue);
		
	}
	
	public char getChainIdentifier() {
		return chainIdentifier;
	}

	public void setChainIdentifier(char chainIdentifier) {
		this.chainIdentifier = chainIdentifier;
	}

	public Map<Integer,Residue> getResidues() {
		return residues;
	}

	public void setResidues(Map<Integer,Residue> residues) {
		this.residues = residues;
	}
	
}
