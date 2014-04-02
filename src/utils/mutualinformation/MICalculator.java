package utils.mutualinformation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import seqManipulation.AlignmentSequenceEditor;
import seqManipulation.GapToolbox;
import pair.Pair;
import utils.mutualinformation.MICorrections;

public class MICalculator {
	
	///////////////////
	// Public Interface
	
	public static Map<Pair<Integer, Integer>, Double> calculateGenericMIMatrix(List<Pair<String,String>> alignment, MICorrections correction, FrequencyConverter converter, MolType molecule ) {
		
		int alphabetSize = getAlphabetSize(molecule);
		
		switch (correction) {
		
		case Uncorrected:
			
			return calculateMIMatrix(alignment, alphabetSize, converter);
			
		case MIr:
			
			return calculateMIrMatrix(alignment, alphabetSize, converter);
			
		case MIp:
			
			return calculateMIpMatrix(alignment, alphabetSize, converter);
			
		}
		
		return null;
		
	}
	
	/**
	 * Calculate the Mutual information matrix for a protein alignment.
	 * 
	 * @param alignment
	 * @return
	 */
	public static Map<Pair<Integer, Integer>, Double> calculateProteinMIMatrix(List<Pair<String,String>> alignment) {
		
		return MICalculator.calculateMIMatrix(alignment,20, new SimpleFrecuencyConverter());
		
	}
	
	/**
	 * Calculate the Mutual information matrix for a nucleic acid alignment.
	 * 
	 * @param alignment
	 * @return
	 */
	public static Map<Pair<Integer, Integer>, Double> calculateNucleicMIMatrix(List<Pair<String,String>> alignment) {
		
		return MICalculator.calculateMIMatrix(alignment,4, new SimpleFrecuencyConverter());
		
	}
	
	/**
	 * Calculate the Mutual information matrix for a protein alignment.
	 * 
	 * @param alignment
	 * @return
	 */
	public static Map<Pair<Integer, Integer>, Double> calculateProteinMIrMatrix(List<Pair<String,String>> alignment) {
		
		return MICalculator.calculateMIrMatrix(alignment,20, new SimpleFrecuencyConverter());
		
	}
	
	/**
	 * Calculate the Mutual information matrix for a nucleic acid alignment.
	 * 
	 * @param alignment
	 * @return
	 */
	public static Map<Pair<Integer, Integer>, Double> calculateNucleicMIrMatrix(List<Pair<String,String>> alignment) {
		
		return MICalculator.calculateMIrMatrix(alignment,4, new SimpleFrecuencyConverter());
		
	}
	
	/**
	 * Calculate the Mutual information matrix for a protein alignment.
	 * 
	 * @param alignment
	 * @return
	 */
	public static Map<Pair<Integer, Integer>, Double> calculateProteinMIpMatrix(List<Pair<String,String>> alignment) {
		
		return MICalculator.calculateMIpMatrix(alignment,20, new SimpleFrecuencyConverter());
		
	}
	
	/**
	 * Calculate the Mutual information matrix for a nucleic acid alignment.
	 * 
	 * @param alignment
	 * @return
	 */
	public static Map<Pair<Integer, Integer>, Double> calculateNucleicMIpMatrix(List<Pair<String,String>> alignment) {
		
		return MICalculator.calculateMIpMatrix(alignment,4, new SimpleFrecuencyConverter());
		
	}
	
	/**
	 * Generic alphabet calculator of mutual information matrix.
	 * According to the formula:
	 * <pre>
	 * MI(a,b) = H(a) + H(b) - H(a,b)
	 * 
	 * Where H is Shannon's entropy.
	 * Extracted from : 
	 * Dunn, S. D. and Wahl, L. M. and Gloor, G. B. 
	 * Mutual information without the influence of phylogeny or entropy 
	 * dramatically improves residue contact prediction. Bioinformatics. 2008.
	 * </pre>
	 * 
	 * @param alignment
	 * @param alphabetsize
	 * @return
	 */
	public static Map<Pair<Integer, Integer>, Double> calculateMIMatrix(List<Pair<String,String>> alignment, int alphabetsize, FrequencyConverter converter) {
		
		// Creates a new alignment without gapped columns
		List<Pair<String, String>> nAlign = removeGaps(alignment);
		
		// Calculate Entropy for each column
		AlignmentSequenceEditor nAlignASE = new AlignmentSequenceEditor(nAlign);
		
		// get the columns of the alignment
		List<Character[]> columns = nAlignASE.getColumns();
		
		double[] columnEntropies = MICalculator.getColumnsEntropies(alphabetsize, nAlign, columns, converter);
	
		// Calculate Entropy for each column pair
		Map<Pair<Integer,Integer>,Double> dicolumnEntropyMatrix = MICalculator.getDicolumnsEntropies(alphabetsize, columns, columnEntropies, converter);

		// Calculate Mutual Info
		Map<Pair<Integer,Integer>,Double> mutualInfoMatrix = MICalculator.getMutualInfoMatrix(columnEntropies, dicolumnEntropyMatrix);
		
		return mutualInfoMatrix;
		
	}


	/**

	 * Correction of Mutual Info by Dunn et al (Dunn:2008)
	 * 
	 * <pre>
	 * 
	 * MIp(a,b) = MI(a,b) - MI(a,x_mean) * MI(b,x_mean) / MI_mean
	 * 
	 * </pre>
	 * Dunn, S. D. and Wahl, L. M. and Gloor, G. B.
	 * Mutual information without the influence of phylogeny or entropy 
	 * dramatically improves residue contact prediction.
	 * Bioinformatics. 2008.
	 * 
	 */
	public static Map<Pair<Integer, Integer>, Double> calculateMIpMatrix(List<Pair<String,String>> alignment, int alphabetsize, FrequencyConverter converter) {
		
		// Creates a new alignment without gapped columns
		List<Pair<String, String>> nAlign = removeGaps(alignment);
		
		// Calculate Entropy for each column
		AlignmentSequenceEditor nAlignASE = new AlignmentSequenceEditor(nAlign);
		
		// get the columns of the alignment
		List<Character[]> columns = nAlignASE.getColumns();
		
		double[] columnEntropies = MICalculator.getColumnsEntropies(alphabetsize, nAlign, columns, converter);
	
		// Calculate Entropy for each column pair
		Map<Pair<Integer,Integer>,Double> dicolumnEntropyMatrix = MICalculator.getDicolumnsEntropies(alphabetsize, columns, columnEntropies, converter);

		// Calculate Mutual Info
		Map<Pair<Integer,Integer>,Double> mutualInfoMatrix = MICalculator.getMutualInfoMatrix(columnEntropies, dicolumnEntropyMatrix);
		
		// Calculate APC correction Matrix
		Map<Pair<Integer,Integer>,Double> APCMatrix = MICalculator.APCCorrectionMatrix(mutualInfoMatrix, nAlignASE.getRowsSize());
		
		// Correct Mutual Info by MIp (Dunn;2008)
		Map<Pair<Integer,Integer>,Double> correctedMIMatrix = MICalculator.getMIpMatrix(mutualInfoMatrix,APCMatrix,nAlignASE.getRowsSize());
		
		return correctedMIMatrix;
		
		
	}


	/**

	 * Correction of Mutual Info by Martin et al (2005)
	 * 
	 * <pre>
	 * MIr(a,b) = MI(a.b) / (H(a.b),
	 * 
	 * where H es Shannon Entropy.
	 * </pre>
	 * 
	 * Martin, L. C. and Gloor, G. B. and Dunn, S. D. and Wahl, L. M. 
	 * Using information theory to search for co-evolving residues in proteins}}.
	 * Bioinformatics. 2005.
	 */
	public static Map<Pair<Integer, Integer>, Double> calculateMIrMatrix(List<Pair<String,String>> alignment, int alphabetsize, FrequencyConverter converter) {
		
		// Creates a new alignment without gapped columns
		List<Pair<String, String>> nAlign = removeGaps(alignment);
		
		// Calculate Entropy for each column
		AlignmentSequenceEditor nAlignASE = new AlignmentSequenceEditor(nAlign);
		
		// get the columns of the alignment
		List<Character[]> columns = nAlignASE.getColumns();
		
		double[] columnEntropies = MICalculator.getColumnsEntropies(alphabetsize, nAlign, columns, converter);
	
		// Calculate Entropy for each column pair
		Map<Pair<Integer,Integer>,Double> dicolumnEntropyMatrix = MICalculator.getDicolumnsEntropies(alphabetsize, columns, columnEntropies, converter);

		// Calculate Mutual Info
		Map<Pair<Integer,Integer>,Double> mutualInfoMatrix = MICalculator.getMutualInfoMatrix(columnEntropies, dicolumnEntropyMatrix);
		
		// Correct Mutual Info by MIr (Martin:2005)
		Map<Pair<Integer,Integer>,Double> correctedMIMatrix = MICalculator.performMartin2005Correction(mutualInfoMatrix,dicolumnEntropyMatrix,nAlignASE.getRowsSize());
		
		return correctedMIMatrix;
		
		
	}
	
	////////////////////
	// Private Methods 
	
	//////////////////
	// private methods
	
	private static Map<Pair<Integer,Integer>,Double> getMIpMatrix(Map<Pair<Integer,Integer>,Double> mutualInfoMatrix, Map<Pair<Integer,Integer>,Double> APCCorrectedMatrix, int matrixSize) {
		
		Map<Pair<Integer,Integer>,Double> MIpMatrix = new HashMap<Pair<Integer,Integer>, Double>();
		
		for (int i=0; i<matrixSize-1; i++) {
			
			for (int j=i+1; j<matrixSize;j++) {
				
				Pair<Integer, Integer> currentPos = new Pair<Integer, Integer>(i, j);
				
				MIpMatrix.put(currentPos, mutualInfoMatrix.get(currentPos) - APCCorrectedMatrix.get(currentPos));
				
			}
			
		}
		
		return MIpMatrix;
		
	}
	

	private static Map<Pair<Integer,Integer>,Double> APCCorrectionMatrix(Map<Pair<Integer,Integer>,Double> mutualInfoMatrix, int matrixSize) {
		
		double[] meansColumsMIArray = MICalculator.getMeansColumsMIArray(mutualInfoMatrix, matrixSize);
		
		double meanMI = MICalculator.getMeanMI(mutualInfoMatrix, matrixSize);

		Map<Pair<Integer,Integer>,Double> APCCorrectionMatrix = new HashMap<Pair<Integer,Integer>, Double>();
		
		for (int i=0; i<matrixSize-1; i++) {
			
			for (int j=i+1; j<matrixSize;j++) {
				
				APCCorrectionMatrix.put(new Pair<Integer, Integer>(i, j), meansColumsMIArray[i]*meansColumsMIArray[j]/meanMI);
				
			}
			
		}
		
		return APCCorrectionMatrix;
		
	}
	

	private static double[] getMeansColumsMIArray(Map<Pair<Integer,Integer>,Double> mutualInfoMatrix, int matrixSize) {
		
		double[] meansColumsMIArray = new double[matrixSize];
		
		for (int i =0 ;i<matrixSize; i++) {
		
			meansColumsMIArray[i] = MICalculator.getMeanColumnMI(mutualInfoMatrix, i, matrixSize);
			

		}
		
		return meansColumsMIArray;
		
	}
	
	
	private static double getMeanColumnMI(Map<Pair<Integer,Integer>,Double> mutualInfoMatrix, int columnPos, int matrixSize) {
		
		double sumMI=0;
		
		for (int i=0; i < matrixSize; i++) {
			
			if (i != columnPos) {
				
				int pa = Math.min(i, columnPos);
				
				int pb = Math.max(i, columnPos);
				
				sumMI += mutualInfoMatrix.get(new Pair<Integer,Integer>(pa,pb));
				
			}
			
		}
		
		return sumMI / (matrixSize-1);
		
	}
	

	private static double getMeanMI(Map<Pair<Integer,Integer>,Double> mutualInfoMatrix, int matrixSize) {
		
		double sumMI=0;
		
		for (int i=0; i < matrixSize-1; i++) {
			
			for (int j=0; j<matrixSize;j++) {
			
				int pa = Math.min(i, j);
					
				int pb = Math.max(i, j);
					
				sumMI += mutualInfoMatrix.get(new Pair<Integer,Integer>(pa,pb));
				
			}
			
		}
		
		return sumMI * 2 / (matrixSize * (matrixSize-1));
		
	}


	private static Map<Pair<Integer, Integer>, Double> performMartin2005Correction (Map<Pair<Integer, Integer>, Double> mutualInfoMatrix, 	Map<Pair<Integer, Integer>, Double> dicolumnEntropyMatrix, int matrixSize) {
		
		Map<Pair<Integer, Integer>, Double> corrected;
		corrected = new HashMap<Pair<Integer,Integer>, Double>();
		
		for(int i = 0 ; i < matrixSize-1;i++) {
			
			for (int j = i+1 ; j<matrixSize;j++) {
				
				Pair<Integer, Integer> currentPos= new Pair<Integer, Integer>(i, j);
				
				corrected.put(currentPos , mutualInfoMatrix.get(currentPos) / dicolumnEntropyMatrix.get(currentPos));
					
			}
			
		}
		return corrected;

	}


	private static Map<Pair<Integer, Integer>, Double> getMutualInfoMatrix(double[] columnEntropies, 	Map<Pair<Integer, Integer>, Double> dicolumnEntropyMatrix) { 

		Map<Pair<Integer, Integer>, Double> mutualInfoMatrix;
		
		mutualInfoMatrix = new HashMap<Pair<Integer,Integer>, Double>();
		
		for(int i = 0 ; i < columnEntropies.length-1;i++) {
			
			for (int j = i+1 ; j<columnEntropies.length;j++) {
				
				
				double mutualInfo = columnEntropies[i] + columnEntropies[j] - dicolumnEntropyMatrix.get(new Pair<Integer,Integer>(i,j));
					
				mutualInfoMatrix.put(new Pair<Integer,Integer>(i,j) , mutualInfo);
					
			}
			
		}
		
		return mutualInfoMatrix;
	}


	private static Map<Pair<Integer, Integer>, Double> getDicolumnsEntropies(int alphabetsize, List<Character[]> columns, double[] columnEntropies, FrequencyConverter converter) {
		
		Map<Pair<Integer, Integer>, Double> dicolumnEntropyMatrix;
		
		dicolumnEntropyMatrix = new HashMap<Pair<Integer,Integer>, Double>();
		
		for(int i = 0 ; i < columnEntropies.length-1;i++) {
			
			for (int j = i+1 ; j<columnEntropies.length;j++) {
				
				
				try {
					
					double dicolumnEntropy = EntropyCalculator.calculateEntropy(columns.get(i), columns.get(j), alphabetsize, false, converter);
					
					dicolumnEntropyMatrix.put(new Pair<Integer,Integer>(i,j) , dicolumnEntropy);
					
					
				} catch (CharGroupSizeException e) {
		
					e.printStackTrace();

				}
				
			}
			
		}
		
		return dicolumnEntropyMatrix;
		
	}


	private static List<Pair<String, String>> removeGaps(	List<Pair<String, String>> alignment) {

		GapToolbox gtb = new GapToolbox();
		
		// Look for columns with gaps in the alignment
		boolean[] keepers = gtb.getKeepers(alignment);
		
		// Remove gaps
		List<Pair<String,String>> nAlign = gtb.removeGappedColumns(alignment, keepers);
		
		return nAlign;
		
	}


	private static double[] getColumnsEntropies(int alphabetsize, List<Pair<String, String>> nAlign, List<Character[]> columns, FrequencyConverter converter) {
		
		double[] columnEntropies;
		
		columnEntropies = new double[nAlign.get(0).getSecond().length()];
		
		for (int i=0; i<columnEntropies.length; i++) {
			
			columnEntropies[i] = EntropyCalculator.calculateEntropy(columns.get(i), alphabetsize, false, converter);
			
		}
		
		return columnEntropies;
		
	}


	private static int getAlphabetSize(MolType molecule) {
		int alphabetSize = 0;
		
		switch (molecule) {
		case Protein:	
			alphabetSize = 20;
			break;

		case Nucleic:
			alphabetSize = 4;
			break;
			
		default:
			break;
		}
		return alphabetSize;
	}
	
	
}
