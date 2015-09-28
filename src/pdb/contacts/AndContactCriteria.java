package pdb.contacts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import annotations.NeverUsed;
import pair.Pair;
import pdb.structures.Residue;
import pdb.structures.SpacePoint;

/**
 * This class represents a list of criteria must must be all true for two
 * residues to consider them in contact.
 * 
 * @author javier
 *
 */
@NeverUsed
public class AndContactCriteria extends ContactCriteria {
  // ///////////////////////////////////////////////////////////////////////////
  // instance variables
  private List<ContactCriteria> criterias;
  private List<ContactCriteria> sortedCriterias;

  // ///////////////////////////////////////////////////////////////////////////

  // ///////////////////////////////////////////////////////////////////////////
  // Constructor
  public AndContactCriteria(List<ContactCriteria> criterias) {
    super();
    this.setCriterias(criterias);
  }

  public AndContactCriteria() {
    super();
    this.setCriterias(new ArrayList<ContactCriteria>());
  }

  // ///////////////////////////////////////////////////////////////////////////

  // ///////////////////////////////////////////////////////////////////////////
  // Public interface
  @Override
  public Pair<SpacePoint, SpacePoint> areInContact(Residue currentFirstPoint,
      Residue currentSecondPoint) {
    boolean inContact = true;
    Map<ContactCriteria, Pair<SpacePoint, SpacePoint>> contactResultMap = new HashMap<>();

    for (ContactCriteria criteria : this.getSortedCriterias()) {
      Pair<SpacePoint, SpacePoint> result = criteria.areInContact(
          currentFirstPoint, currentSecondPoint);
      inContact = inContact && result != null;
      if (inContact) {
        contactResultMap.put(criteria, result);
      }
    }
    if (inContact) {
      for (ContactCriteria criteria : this.getCriterias()) {
        Pair<SpacePoint, SpacePoint> currentResult = contactResultMap
            .get(criteria);
        if (currentResult.getFirst() != null
            && currentResult.getSecond() != null) {
          return currentResult;
        }
      }
      return new Pair<SpacePoint, SpacePoint>(null, null);
    } else {
      return null;
    }
  }

  @Override
  public int cost() {
    int cost = 0;
    for (ContactCriteria criteria : this.getCriterias()) {
      cost += criteria.cost();
    }
    return cost;
  }

  /**
   * @return the criterias
   */
  public List<ContactCriteria> getCriterias() {
    return criterias;
  }

  /**
   * @param criterias
   *          the criterias to set
   */
  public void setCriterias(List<ContactCriteria> criterias) {
    this.criterias = criterias;
    this.sortedCriterias = new ArrayList<>(this.getCriterias());
    Collections.sort(this.sortedCriterias, new CriteriaCostComparator());
  }

  // ///////////////////////////////////////////////////////////////////////////

  // ///////////////////////////////////////////////////////////////////////////
  // Private methods
  private List<ContactCriteria> getSortedCriterias() {
    return this.sortedCriterias;
  }
  // ///////////////////////////////////////////////////////////////////////////

  @Override
  public boolean useDistance() {
    return false;
  }

}
