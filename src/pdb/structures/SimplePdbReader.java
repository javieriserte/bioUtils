package pdb.structures;

import io.onelinelister.OneLineListReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pdb.structures.SpacePoint.RecordName;

/**
 * Is a simple PDB reader. Do not interprets a entire PDB file, just ATOM, and
 * HETATOM registers. If PDB file contains many models, only read the first one.
 * 
 * @author javier iserte
 */
public class SimplePdbReader {

  public Map<Character, Chain> readPdb(BufferedReader in) {

    try {
      List<SpacePoint> pdb = new OneLineListReader<SpacePoint>(
          new SpacePointLineParser()).read(in);

      Map<Character, Chain> data = new HashMap<Character, Chain>();

      int modelCounter = 0;

      for (SpacePoint spacePoint : pdb) {

        if (spacePoint == null) {
          continue;
        }
        
        // Models appears as ModelDummyPoint objects
        // Check if the current spacePoint object correspond to a new Model.
        // If so, increment the model counter and continue with the next 
        // spacePoint
        if (spacePoint.getType() == RecordName.Model ) {
          modelCounter++;
          continue;
        }
        
        // Check if the current element correspond to a header data element,
        // if so, ignore it.
        if ( spacePoint.getType() == RecordName.Header) {
          continue;
        }

        if (modelCounter <= 1) {

          char currentChainIdentifier = spacePoint.getChainIdentifier();

          int currentResNumber = spacePoint.getResidueSequenceNumber();

          String currentResId = String.valueOf(spacePoint.getResidueName());

          if (!data.containsKey(currentChainIdentifier)) {

            data.put(currentChainIdentifier, new Chain(currentChainIdentifier));

          }

          Chain currentChain = data.get(currentChainIdentifier);

          if (!currentChain.getResidues().containsKey(currentResNumber)) {

            currentChain.addResidue(new Residue(currentResId, currentResNumber,
                currentChainIdentifier));

          }

          Residue currentResidue = currentChain.getResidues().get(
              currentResNumber);

          currentResidue.addSpacePoint(spacePoint);

        }

      }

      return data;

    } catch (IOException e) {
      System.err.println("There was an error while trying to read pdb file.");
      e.printStackTrace();
      System.exit(1);
    }

    return null;

  }

}
