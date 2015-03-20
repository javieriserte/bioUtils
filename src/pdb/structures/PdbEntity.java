package pdb.structures;

import java.util.List;
import java.util.Map;

/**
 * This class contains data of pdb files organized in a hierarchical way.
 * Model->Chain->Residue->Atoms Also, contains all extra data.
 * 
 * @author javier
 *
 */
public class PdbEntity {
  // ///////////////////////////////////////////////////////////////////////////
  // Instance variables
  /**
   * Extra data in pdb
   */
  private List<HeaderData> header;

  /**
   * Models, represented as a map from numbers (the model number) to a map of
   * chains.
   */
  private Map<Integer, Map<Character, Chain>> models;
  // ///////////////////////////////////////////////////////////////////////////

  // ///////////////////////////////////////////////////////////////////////////
  // Constructor
  public PdbEntity(List<HeaderData> header, Map<Integer, Map<Character, Chain>> models) {
    super();
    this.header = header;
    this.models = models;
  }
  // ///////////////////////////////////////////////////////////////////////////

  // ///////////////////////////////////////////////////////////////////////////
  // Public interface
  /**
   * @return the header
   */
  public List<HeaderData> getHeader() {
    return header;
  }

  /**
   * @param header
   *          the header to set
   */
  public void setHeader(List<HeaderData> header) {
    this.header = header;
  }

  /**
   * @return the models
   */
  public Map<Integer, Map<Character, Chain>> getModels() {
    return models;
  }

  /**
   * @param models
   *          the models to set
   */
  public void setModels(Map<Integer, Map<Character, Chain>> models) {
    this.models = models;
  }
  // ///////////////////////////////////////////////////////////////////////////
}
