package pdb.contacts;

import pair.Pair;
import pdb.structures.Residue;
import pdb.structures.SpacePoint;

/**
 * Is a criteria to decide if two residues are in a protein are in contact.
 * If the distance of the two closest atoms if two residues is less than a given
 * cutoff value then is assumed as contact.
 * 
 * @author javier
 *
 */
public class ClosestAtomPairContactCriteria extends ContactCriteria {

	////////////////////////////////////////////////////////////////////////////
	// Instance Variables
	private double cutoffDistance;
	////////////////////////////////////////////////////////////////////////////
	
	////////////////////////////////////////////////////////////////////////////
	// Constructor
	/**
	 * Creates a new ClosestAtomPairContactCriteria with a given cut-off value.
	 * The distance is measured in Angstroms.
	 * @param cutoffDistance
	 */
	public ClosestAtomPairContactCriteria(double cutoffDistance) {
		this.setCutoffDistance(cutoffDistance);
	}
	////////////////////////////////////////////////////////////////////////////
	
	////////////////////////////////////////////////////////////////////////////
	// Public Interface
	/**
	 * Check if two residues are in contact. Is so, the closest atom pair is
	 * returned. Otherwise null is returned.
	 */
	@Override
	public Pair<SpacePoint, SpacePoint> areInContact(Residue currentFirstPoint,
			Residue currentSecondPoint) {

		double cutoff = this.getCutoffDistance();
		
		double minDistance = Double.POSITIVE_INFINITY;
		
		SpacePoint closestAtomInFirst = null;
		SpacePoint closestAtomInSecond = null;

		for (SpacePoint atomInFirst : currentFirstPoint.getSpacePointCollection()) {

			for (SpacePoint atomInSecond : currentSecondPoint.getSpacePointCollection()) {
				
				double currentDistance = atomInFirst.distanceTo(atomInSecond);
				
				if (currentDistance<=minDistance) {
					
					minDistance = currentDistance; 
					closestAtomInFirst = atomInFirst;
					closestAtomInSecond = atomInSecond; 
					
				}
				
			}
			
		}
		
		if (minDistance<=cutoff) {
			
			return new Pair<SpacePoint, SpacePoint>(closestAtomInFirst, closestAtomInSecond);

		}
		
		return null;
		
	}
	
	/**
	 * Returns a value that indicates the cost of the operation
	 */
  @Override
  public int cost() {
    return 1000;
  }
	
	/**
	 * Gets the cut-off value to decide if two residues are in contact.
	 * @return
	 */
	public double getCutoffDistance() {
		return cutoffDistance;
	}
	/**
	 * Sets the cut-off value to decide if two residues are in contact.
	 * @return
	 */	
	public void setCutoffDistance(double cutoffDistance) {
		this.cutoffDistance = cutoffDistance;
	}
	////////////////////////////////////////////////////////////////////////////



}
