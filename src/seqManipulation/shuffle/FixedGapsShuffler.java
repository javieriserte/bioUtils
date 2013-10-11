package seqManipulation.shuffle;

/**
 * Implementation of SequenceShuffler that keeps
 * the gaps symbol in the same place as the original
 * sequence
 * 
 * @author Javier Iserte
 *
 */
public class FixedGapsShuffler extends SequenceShuffler {

	@Override
	protected Character[] getSymbols(String sequence) {
		
		Character[] usedCharaters = new Character[sequence.length()];
		// Creates adata structure to store those characters
		// that are non gaps. This array will be returned by
		// this method
		
		int noGapsIndex = 0;
		// This variable keeps track of the index of the 
		// next non-gap character in the usedCharacter array
		
		for (int i=0 ;i<sequence.length();i++) {
		// Iterate over all positions of the sequece.
			
			char currentChar = sequence.charAt(i);
			// Gets the current read character 
			
			if (currentChar!='-') {
			// If is not a gap...
				
				usedCharaters[noGapsIndex] = currentChar;
				// ... then add to the result array
				
				noGapsIndex++;
				// ... and updates the index of the next 
				// character
				
			}
			
		}
		
		return usedCharaters;
		
	}

	@Override
	protected String compose(Character[] symbols, String guide) {
		
		StringBuilder newSequenceBuilder = new StringBuilder();
		// Creates a data structure to build the result sequence
		
		int noGapsIndex = 0;
		// This variable keeps track of the index of the 
		// current non-gap character in the symbols array
		
		for (int i=0 ;i<guide.length();i++) {
			// Iterate over all positions of the sequece.
				
				char currentChar = guide.charAt(i);
				// Gets the current read character 
				
				if (currentChar!='-') {
				// If is not a gap...
					
					newSequenceBuilder.append(symbols[noGapsIndex]);
					// ... then add to the result sequence builder
					
					noGapsIndex++;
					// ... and updates the index for the next 
					// symbol
					
				} else {
					
					newSequenceBuilder.append('-');
					// If is gap, then add a gap to the sequence builder
					
				}
				
			}
		
		
		return newSequenceBuilder.toString();
		
	}

}
