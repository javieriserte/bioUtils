package pdb.structures;

import io.onelinelister.OneLineListReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Is a simple PDB reader.
 * Do not interprets a entire PDB file, just ATOM, and HETATOM registers.
 * @author javier iserte
 */
public class SimplePdbReader {

	public Map<Character,Chain> readPdb(BufferedReader in) {

		try {
			List<SpacePoint> pdb = new OneLineListReader<SpacePoint>(new SpacePointLineParser()).read(in);
			
			Map<Character,Chain> data = new HashMap<Character, Chain>();
			
			for (SpacePoint spacePoint : pdb) {
				
				char currentChainIdentifier = spacePoint.getChainIdentifier();
				
				int currentResNumber = spacePoint.getResidueSequenceNumber();
				
				String currentResId = spacePoint.getResidueName().toString();
				
				if (!data.containsKey(currentChainIdentifier)) {
					
					data.put(currentChainIdentifier, new Chain(currentChainIdentifier));
					
				}
				
				Chain currentChain = data.get(currentChainIdentifier); 
				
				if (!currentChain.getResidues().containsKey(currentResNumber)) {
					
					currentChain.addResidue(new Residue(currentResId, currentResNumber, currentChainIdentifier));
					
				}
				
				Residue currentResidue = currentChain.getResidues().get(currentResId);
				
				currentResidue.addSpacePoint(spacePoint);

			}
			
			return data;
			
		} catch (IOException e) {
			System.err.println("There was an error while trying to read pdb file.");
			e.printStackTrace();
			System.exit(0);
		}
		
		return null;
		
	}
	
}
