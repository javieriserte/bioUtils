package clustering;

/**
 * 
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
 * The first algorithm proceedsby simultaneous processing
 * of three lists of protein identifiers, the test list of all 
 * candidate proteins (or protein chains), the skip list, and the
 * select list. The test list can be sorted according to user-defined 
 * criteria, such as resolution (for proteins of known 3D structure),
 * so that certain types of proteins have a higher probability 
 * of being selected. The skip list contains proteins that are 
 * similar to a previously processed protein from the test list 
 * and may also contain a priori unwanted proteins. 
 * The select list (initially empty) contains proteins
 * chosen as part of the nonredundant data set.
 * In detail, the three lists are processed as follows: (1)
 * read one proteinidentifier from thetest list and check if
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
 * than thespecified threshold (e.g., five percentage points 
 * above the threshold for structural homology corresponding
 * to the length of the FASTA alignment [Sander & Schneider, 19911).
 * Finally, step 1 is repeated until all proteins in the test 
 * list are processed.
 * 
 * @author Javier Iserte (jiserte@unq.edu.ar, jiserte@leloir.org.ar)
 *
 */
public class HobohmClusteringM1 {
	
	

}
