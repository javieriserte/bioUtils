package pdb.contactmap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pair.Pair;
import pdb.contacts.ContactCriteria;
import pdb.structures.Chain;
import pdb.structures.Residue;
import pdb.structures.SpacePoint;

/**
 * Calculates the contact map of a pdb and returns a list.
 * A ContactCriteria must be given in order to decide if two residues are in 
 * contact.
 * @author javier
 *
 */
public class ContactMapListGenerator {
	////////////////////////////////////////////////////////////////////////////
	// Public Interface
	/**
	 * Retrieves a list of atomic coordinates in contact for a given PDB.
	 * @param pdb
	 * @param criteria
	 * @return
	 */
	public List<Pair<SpacePoint,SpacePoint>> getSpacePointsInContact(Map<Character,Chain> pdb, ContactCriteria criteria) {
		
		List<Pair<SpacePoint,SpacePoint>> result = new ArrayList<Pair<SpacePoint,SpacePoint>>();
		
		for (Chain chain : pdb.values()) {
			
			List<Residue> residues = new ArrayList<Residue>();
			residues.addAll(chain.getResiduesCollection());
			
			for (int i =0 ; i< residues.size() -1 ; i++) {
				
				for (int j =i+1 ;j< residues.size() ; j++) {

					Pair<SpacePoint, SpacePoint> pair = criteria.areInContact(residues.get(i), residues.get(j));
					
					if (pair!=null) {
						
						result.add(pair);
						
					}
					
				}
				
			}
			
		}
		
		return result;
		
	}
	////////////////////////////////////////////////////////////////////////////

}
