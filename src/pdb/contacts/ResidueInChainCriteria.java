package pdb.contacts;

import annotations.NeverUsed;
import pdb.structures.Residue;


/**
 * This class represents a criteria to decide if two residues are in contact. In
 * this criteria, they must be in a given chain.
 * 
 * @author javier iserte
 *
 */
@NeverUsed
public class ResidueInChainCriteria extends ChainCriteria {
  // ///////////////////////////////////////////////////////////////////////////
  // Instance variables
  private char chain;
  // ///////////////////////////////////////////////////////////////////////////

  // ///////////////////////////////////////////////////////////////////////////
  // Constructor
  public ResidueInChainCriteria(char chain) {
    this.setChain(chain);
  }
  public ResidueInChainCriteria() {
    this.setChain('A');
  }
  // ///////////////////////////////////////////////////////////////////////////

  // ///////////////////////////////////////////////////////////////////////////  
  // Public interface
  /**
   * @return the chain
   */
  public char getChain() {
    return chain;
  }

  /**
   * @param chain
   *          the chain to set
   */
  public void setChain(char chain) {
    this.chain = chain;
  }
  // ///////////////////////////////////////////////////////////////////////////

  // ///////////////////////////////////////////////////////////////////////////
  // Protected interface
  @Override
  protected boolean inContact(Residue currentFirstPoint,
      Residue currentSecondPoint) {
    return currentFirstPoint.getChain() == this.getChain()
        && currentSecondPoint.getChain() == this.getChain();
  }
  // ///////////////////////////////////////////////////////////////////////////

}
