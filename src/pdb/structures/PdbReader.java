package pdb.structures;

import io.onelinelister.OneLineListReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import annotations.NeverUsed;
import pdb.structures.SpacePoint.RecordName;

@NeverUsed
public class PdbReader {

  public PdbEntity readPdb(BufferedReader in) {

    try {

      List<HeaderData> header = new ArrayList<>();
      Map<Integer, Map<Character, Chain>> models = new HashMap<>();

      List<SpacePoint> pdb = new OneLineListReader<SpacePoint>(
          new SpacePointLineParser()).read(in);

      int modelCounter = 0;

      for (SpacePoint spacePoint : pdb) {
        if (spacePoint == null) {
          continue;
        }

        // Models appears as ModelDummyPoint objects
        // Check if the current spacePoint object correspond to a new Model.
        // If so, increment the model counter and continue with the next
        // spacePoint
        if (spacePoint.getType() == RecordName.Model) {
          modelCounter++;
          continue;
        }

        // Check if the current element correspond to a header data element,
        // if so, ignore it.
        if (spacePoint.getType() == RecordName.Header) {
          header.add((HeaderData) spacePoint);
          continue;
        }

        // If modelcounter is one or zero, then the model is 1.
        // Modelcounter equals zero means that the pdb do not contains
        // MODEL tags, and therefore there are only one model.
        int modelNumber = modelCounter > 1 ? modelCounter : 1;

        // Get the chain id from pdb
        char currentChainIdentifier = spacePoint.getChainIdentifier();

        // Get res number from pdb
        int currentResNumber = spacePoint.getResidueSequenceNumber();

        // Get coordinates and properties from pdb
        String currentResId = String.valueOf(spacePoint.getResidueName());

        // Create the model data structure if not exists already
        if (!models.containsKey(modelNumber)) {
          models.put(modelNumber, new HashMap<Character, Chain>());
        }
        Map<Character, Chain> currentModel = models.get(modelNumber);

        // Create the chain data structure if not exists already
        if (!currentModel.containsKey(currentChainIdentifier)) {
          currentModel.put(currentChainIdentifier, new Chain(
              currentChainIdentifier));
        }
        Chain currentChain = currentModel.get(currentChainIdentifier);

        // Create residue data structure if not exists already
        if (!currentChain.getResidues().containsKey(currentResNumber)) {
          currentChain.addResidue(new Residue(currentResId, currentResNumber,
              currentChainIdentifier));
        }
        Residue currentResidue = currentChain.getResidues().get(
            currentResNumber);

        // Add a new atomic data to the hierarchy
        currentResidue.addSpacePoint(spacePoint);

      }

      return new PdbEntity(header, models);

    } catch (IOException e) {
      System.err.println("There was an error while trying to read pdb file.");
      e.printStackTrace();
      System.exit(1);
    }

    return null;

  }
}
