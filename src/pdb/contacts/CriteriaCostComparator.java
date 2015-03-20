package pdb.contacts;

import java.util.Comparator;

import annotations.NeverUsed;

/**
 * This class compares two ContactCriteria by its cost.
 * @author javier
 *
 */
@NeverUsed
public class CriteriaCostComparator implements Comparator<ContactCriteria> {

  // ///////////////////////////////////////////////////////////////////////////
  // Public Interface
  @Override
  public int compare(ContactCriteria o1, ContactCriteria o2) {
    return o1.cost() - o2.cost();
  }
  // ///////////////////////////////////////////////////////////////////////////

}
