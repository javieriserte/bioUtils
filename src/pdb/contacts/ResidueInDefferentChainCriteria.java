package pdb.contacts;

import java.util.HashSet;
import java.util.Set;

import pdb.structures.Residue;

/**
 * This class represents a criteria to decide if two residues are in contact. In
 * this criteria, they must be in different chains.
 * 
 * @author javier iserte
 *
 */
public class ResidueInDefferentChainCriteria extends ChainCriteria {
  // ///////////////////////////////////////////////////////////////////////////
  // Instance variables
  private Set<Character> acceptedChains;

  // ///////////////////////////////////////////////////////////////////////////

  // ///////////////////////////////////////////////////////////////////////////
  // Constructors
  public ResidueInDefferentChainCriteria(Set<Character> acceptedChains) {
    this.setAcceptedChains(acceptedChains);
  }

  public ResidueInDefferentChainCriteria() {
    this.setAcceptedChains(new HashSet<Character>());
  }

  // ///////////////////////////////////////////////////////////////////////////

  // ///////////////////////////////////////////////////////////////////////////
  // Public interface
  /**
   * @return the acceptedChains
   */
  public Set<Character> getAcceptedChains() {
    return acceptedChains;
  }

  /**
   * @param acceptedChains
   *          the acceptedChains to set
   */
  public void setAcceptedChains(Set<Character> acceptedChains) {
    this.acceptedChains = acceptedChains;
  }

  // ///////////////////////////////////////////////////////////////////////////

  // ///////////////////////////////////////////////////////////////////////////
  // Protected methods
  @Override
  protected boolean inContact(Residue currentFirstPoint,
      Residue currentSecondPoint) {
    Character chainFirst = currentFirstPoint.getChain();
    Character chainSecond = currentSecondPoint.getChain();
    return this.acceptedChains.contains(chainFirst)
        && this.acceptedChains.contains(chainSecond)
        && chainFirst != chainSecond;
  }
  // ///////////////////////////////////////////////////////////////////////////
}
