package pdb.contactmap;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pdb.contacts.AndContactCriteria;
import pdb.contacts.ClosestAtomPairContactCriteria;
import pdb.contacts.ContactCriteria;
import pdb.contacts.ResidueInChainCriteria;
import pdb.contacts.ResidueInDifferentChainCriteria;
import pdb.contacts.ResidueInManyChainsCriteria;
import cmdGA2.returnvalues.ReturnValueParser;

public class ContactCriteriaValue extends ReturnValueParser<ContactCriteria> {

  // ///////////////////////////////////////////////////////////////////////////
  // Class Constants
  public static final String CRITERIA_CLOSEST = "closest";
  public static final String CRITERIA_INCHAIN = "chain";
  public static final String CRITERIA_MANYCHAINS = "chains";
  public static final String CRITERIA_DIFF_CHAIN = "diffchain";

  // ///////////////////////////////////////////////////////////////////////////

  // ///////////////////////////////////////////////////////////////////////////
  // Public interface
  @Override
  public ContactCriteria parse(String token) {
    List<ContactCriteria> criterias = new ArrayList<>();
    token = token.replaceAll(" ", "");
    // Read all criterias from input text and split them individually
    String[] criteriaStrings = token.split("\\+");
    // Iterate over each criteria text string.
    for (int i = 0; i < criteriaStrings.length; i++) { 
      // Slipt fields into criteria
      String[] data = criteriaStrings[i].split(",");
      // Check for every individual criteria
      switch (data[0].toLowerCase()) {
      case CRITERIA_CLOSEST:
        double distance;
        if (data.length > 1) {
          distance = Double.valueOf(data[1]);
        } else {
          distance = 6d;
        }
        criterias.add(new ClosestAtomPairContactCriteria(distance));
        break;
      case CRITERIA_INCHAIN:
        char chain;
        if (data.length > 1) {
          chain = data[1].toUpperCase().charAt(0);
        } else {
          chain = 'A';
        }
        ResidueInChainCriteria currentCriteria = new ResidueInChainCriteria();
        currentCriteria.setChain(chain);
        criterias.add(currentCriteria);
        break;
      case CRITERIA_DIFF_CHAIN:
        Set<Character> chains = new HashSet<>();
        if (data.length > 1) {
          for (int j  = 1; j < data.length ; j++) {
            chains.add(data[j].toUpperCase().charAt(0));
          }
        } else {
          chains.add('A');
        }
        criterias.add(new ResidueInDifferentChainCriteria(chains));
        break;
      case CRITERIA_MANYCHAINS:
        Set<Character> chainsb = new HashSet<>();
        if (data.length > 1) {
          for (int j  = 1; j < data.length ; j++) {
            chainsb.add(data[j].toUpperCase().charAt(0));
          }
        } else {
          chainsb.add('A');
        }
        criterias.add(new ResidueInManyChainsCriteria(chainsb));
        break;
      }
    }
    // Finally return an adecuate criteria object as result or null
    int numberOfCriterias = criterias.size();
    if (numberOfCriterias == 0) {
      return null;
    } else if (numberOfCriterias == 1) {
      return criterias.get(0);
    } else {
      return new AndContactCriteria(criterias);
    }

  }
  // ///////////////////////////////////////////////////////////////////////////

}
