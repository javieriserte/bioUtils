package clustering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import seqManipulation.identity.IndentityMatrixCalculator;

import fileformats.fastaIO.Pair;

/**
 * Clustering Algorithm from the paper:
 * 
 * Hobohm U. et al. Selection of representative protein data sets.
 * Protein Science (1992), I , 409-417. Cambridge University Press. Printed in the USA.
 * 
 * Produces clusters from a sequence alignment.
 * This class implements the first algorithm proposed from Uwe Hobohm.  
 * 
 * Outline of the algorithm extracted from the paper:
 * 
 * Given a sorted list of candidate proteins, process each protein in turn by selecting or
 * discarding it according to the following criteria: 
 * <lo>
 * <li> discard proteins that are similar to already selected proteins.
 * <li> discard proteins that fail to meet additional user specified standards.
 * </lo>
 * 
 * The first algorithm proceeds by simultaneous processing
 * of three lists of protein identifiers, the test list of all 
 * candidate proteins (or protein chains), the skip list, and the
 * select list. The test list can be sorted according to user-defined 
 * criteria, such as resolution (for proteins of known 3D structure),
 * so that certain types of proteins have a higher probability 
 * of being selected. The skip list contains proteins that are 
 * similar to a previously processed protein from the test list 
 * and may also contain a priori unwanted proteins. 
 * The select list (initially empty) contains proteins
 * chosen as part of the non redundant data set.
 * In detail, the three lists are processed as follows: (1)
 * read one protein identifier from the test list and check if
 * this protein is a member of the skip list; if so, process the
 * next protein in the test list, i.e., repeat step 1; otherwise
 * (2) check if the protein satisfies user-specified requirements, 
 * such as minimum sequence length, maximum
 * number of unknown residues, and the like. If the requirements 
 * are satisfied, append the protein to the select list;
 * otherwise, process the next protein in the test list, i.e., 
 * repeat step 1 ; (3) with the selected protein, start a FASTA
 * search (Pearson & Lipman, 1988) against all remaining
 * sequences in the test list; (4) scan the FASTA output file
 * and append to the list proteins with a higher similarity skip 
 * than the specified threshold (e.g., five percentage points 
 * above the threshold for structural homology corresponding
 * to the length of the FASTA alignment [Sander & Schneider, 19911).
 * Finally, step 1 is repeated until all proteins in the test 
 * list are processed.
 * 
 * @author Javier Iserte (jiserte@unq.edu.ar, jiserte@leloir.org.ar)
 *
 */
public class HobohmClusteringM1 {
	
	/////////////////////////////
	// Public Interface
	
	/**
	 * Generates cluster from a sequence alignment using the
	 * guidelines from the Hobohm method 1. There are some implementations
	 * differences with the published guidelines.
	 * <ol>
	 * 	<li>Identity fraction is used to search related sequences, instead a 
	 * FASTA search.</li>
	 *  <li>Skip list is always empty at the beggining</li>
	 *  <li>Maps and Sets are used in additions to lists</li>
	 * </ol> 
	 * 
	 * @param sequences a MSA to be clustered. Unique sequence identifiers are assumed
	 * @param identityThreshold a value from 0 to 1, that is the minimum fraction of identities
	 *         that two sequences must have to be considered related.
	 * @return
	 */
	public Set<List<Pair<String,String>>> clusterize ( List<Pair<String,String>> sequences , double identityThreshold ) {
		
		Set<List<Pair<String,String>>>result = new HashSet<>();
		// Data structure to store clusters.
		// Each List<Pair<String,String>> represents a cluster.
		
		Map<String,String> testMap = fillTestMap(sequences);
		// Creates a map data structure to store all the sequences with 
		// descriptions as keys.
		
		List<String> testList = new ArrayList<>(testMap.keySet());
		// Creates a data structure to iterate the sequences
		
		Set<String> skipSet = new HashSet<>();
		// Data structure that contains 
		// descriptions of sequences that will be
		// ignored.
		
		Set<String> selectSet = new HashSet<>();
		// Data structure to store descriptions of the
		// current growing cluster

		for (int i =0; i< testList.size(); i++) {
		// Iterates over each sequence in the test list
			
			String currentDescription = testList.get(i);
			// Store the description of the sequence being compared. 
			
			if (! skipSet.contains(currentDescription)) {
			// If current sequence is in the description list,
			// then do nothing with it and continue with the next sequence.

				selectSet.add(currentDescription);
				// Add the description of the sequence being analyzed
				// into the select set
				
				for (int j=i+1; j< testList.size(); j++) {
				// Now, iterates over the following sequences
				// in the test set looking for those that are more closely 
				// related.

					String currentFollowingDescription = testList.get(j);
					// Get the description of the next following sequence
					
					if (!skipSet.contains(currentFollowingDescription)) {
					// If the current following sequence is in the 
					// skip list, ignore it. 
					
						double identity = IndentityMatrixCalculator.calculateIdentity(
								testMap.get(currentDescription), 
								testMap.get(currentFollowingDescription));
						// Calculated identity fraction between the current sequence,
						// and the current following sequence
						
						if (identity >= identityThreshold) {
						// Is identity is above the given threshold value...
							
							selectSet.add(currentFollowingDescription);
							// ... the the following sequence into the 
							// select set. i.e. the following sequence will be 
							// in the same cluster that current sequence
							
							skipSet.add(currentFollowingDescription);
							// ... also, adds the current following description
							// to the skip list. So, this sequence will not be 
							// computed again
							
						}
						
					}
					
				}
				
				result.add(this.createNewCluster(testMap, selectSet));
				// Creates a new cluster with the sequences in the select set
				// Add this cluster to the list of resulting clusters.
				
				selectSet.clear();
				// Finally, clear the select set, in order to be 
				// used for a new cluster
				
			}
			
		}
		
		return result;
		
	}

	//////////////////////////////////////////
	// Private Methods
	/**
	 * Converts the list of sequences (as description and sequence pairs) into a Map,
	 * where the key is the description and the value is the sequence itself.
	 * @param sequences the list of sequence. Assumes that sequence has different descriptions. 
	 * @return a map with the sequences. 
	 */
	private Map<String, String> fillTestMap(List<Pair<String, String>> sequences) {
		
		Map<String,String> testMap = new HashMap<>();
		
		for (Pair<String, String> pair : sequences) {
			
			testMap.put(pair.getFirst(), pair.getSecond());
			
		}

		return testMap;
		
	}
	
	/**
	 * Creates a new list a sequences (that represents a cluster of sequences) 
	 * from a set of description and a Map that contains descriptions as keys 
	 * and sequences as values.
	 * 
	 * @param testMap is the map with all the descriptions and sequences
	 * @param selectSet is a subset of testMap keys which will be in the new cluster
	 * @return
	 */
	private List<Pair<String, String>> createNewCluster( 
			Map<String, String> testMap, Set<String> selectSet) {
		
		List<Pair<String,String>> newCluster = new ArrayList<>();
		// Creates a data structure to store the new cluster
		
		for (String selected : selectSet) {
		// Iterates over each selected description
			
			newCluster.add(new Pair<String,String>(selected, testMap.get(selected)));
			// Put the new sequence into the cluster.
			
		}
		
		return newCluster;
	}

}
