package seqManipulation.shuffle;

/**
 * Implementation of SequenceShuffler that considers 
 * gaps as onether symbol and shuffles it.
 * 
 * @author Javier Iserte
 *
 */
public class NoFixedShuffler extends SequenceShuffler {

	@Override
	protected Character[] getSymbols(String sequence) {
		
		Character[] result = new Character[sequence.length()];
		
		for (int i=0; i<sequence.length(); i++) {
			
			result[i] = sequence.charAt(i);
			
		}
		
		return result;
		
	}

	@Override
	protected String compose(Character[] symbols, String guide) {

		StringBuilder newSequenceBuilder = new StringBuilder();
		
		for (int i=0; i<guide.length(); i++) {
			
			newSequenceBuilder.append(symbols[i]);
			
		}
		
		return newSequenceBuilder.toString();
		
	}

}
