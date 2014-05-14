package seqManipulation.alignmentclusterweight;

import java.io.BufferedReader;
import java.util.LinkedHashMap;
import java.util.List;

import pair.Pair;
import fileformats.readers.AlignmentReadingResult;
import fileformats.readers.GenericAlignmentReader;

/**
 * This class reads an alignment and returns a map from 
 * description to sequence. The map returned is a 
 * LinkedHashMap, so the order of iteration over the map is the same
 * order of the sequences in the alignment.
 * 
 * @author javier
 *
 */
public class AlignmentToMapConverter {

	public static LinkedHashMap<String, String> read(BufferedReader in) {
		
		GenericAlignmentReader reader = new GenericAlignmentReader();
		
		List<AlignmentReadingResult> seq = reader.read(in);
		
		for (AlignmentReadingResult alignmentReadingResult : seq) {
			
			if (alignmentReadingResult.successfulRead()) {
				
				LinkedHashMap<String, String> sequences = new LinkedHashMap<>();
				
				for (Pair<String, String> pair : alignmentReadingResult.getAlignment()) {
					
					sequences.put(pair.getFirst(), pair.getSecond());
					
				}
				
				return sequences;
				
			}
			
		}
		
		return new LinkedHashMap<String, String>() ;
		
	}

	
}
