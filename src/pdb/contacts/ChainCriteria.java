package pdb.contacts;

import pair.Pair;
import pdb.structures.Residue;
import pdb.structures.SpacePoint;

/**
 * This class represents an abstract criteria to decide if two residues are in 
 * contact. Specific subclasses that extends from this class must decide if
 * two residue are in contact using the chain field of the residues.
 * 
 * @author javier iserte
 *
 */
public abstract class ChainCriteria extends ContactCriteria {

  // ///////////////////////////////////////////////////////////////////////////
  // Public interface
  /**
   * Check if two residues are in contact according to this criteria.
   * If they are in contact, returns a non null object. otherwise returns null.
   */
  @Override
  public Pair<SpacePoint, SpacePoint> areInContact(Residue currentFirstPoint,
      Residue currentSecondPoint) {
    boolean inContact = this.inContact(currentFirstPoint,currentSecondPoint);
    if (inContact) {
      SpacePoint firstDummy = currentFirstPoint.getAtomData().values()
          .iterator().next();
      SpacePoint secondDummy = currentSecondPoint.getAtomData().values()
          .iterator().next();
      return new Pair<SpacePoint, SpacePoint>(firstDummy, secondDummy);
    } else {
      return null;
    }
  }
  
  protected abstract boolean inContact(Residue currentFirstPoint,
      Residue currentSecondPoint);

  /**
   * Returns a value that indicates the cost of the operation
   */
  @Override
  public int cost() {
    return 1;
  }
  // ///////////////////////////////////////////////////////////////////////////

}
