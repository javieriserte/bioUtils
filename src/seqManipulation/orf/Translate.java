package seqManipulation.orf;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Translate {

	// Instance Variables
	private Map<String,String> standardGeneticCode = null;
	
	// Singleton Pattern
	private static Translate singleton = null;

	/**
	 * Returns the instance of Translate object.
	 * @return
	 */
	public static Translate getInstance() {
		
		if (singleton==null) {
			return new Translate();
		} else {
			return singleton;
		}
	}
	
	// Constructor
	private Translate() {
		this.standardGeneticCode = this.getStandardGeneticCode();
	}
	

	// Public Interface
	/**
	 * Translate a sequence according to the standard genetic code.
	 * 
	 * @param sequence is a DNA sequence.
	 * @return an amino acid sequence
	 */
	public String translateSeq(String sequence) {
		
		return this.translateSeq(sequence, standardGeneticCode);
		
	}
	
	/**
	 * Translate a sequence according to the given genetic code.
	 * 
	 * @param sequence is a DNA sequence.
	 * @param geneticCode is a Map from codon (String) to amino acid (String)
	 * @return an amino acid sequence
	 */
	public String translateSeq(String sequence, Map<String, String>geneticCode) {

		if (sequence.length() %3 != 0) return "";
		
		StringBuilder result = new StringBuilder(sequence.length()); 
		
		for (int i = 0; i<sequence.length(); i = i+3) {
				String codon = sequence.substring(i,i+3);
				result.append(this.translateCodon(codon, geneticCode));
		}
		
		return result.toString();
		
	}

	/**
	 * Translate a particular condon into one amino acid, according to the given genetic code.
	 * 
	 * @param codon a three letter string that represents a codon. Is assumed that is upper case.
	 * @return an amino acid sequence.
	 */
	private String translateCodon(String codon, Map<String,String> geneticCode) {
		
		String aminoacid = geneticCode.get(codon);
		
		if (aminoacid==null) return "X";
		
		return aminoacid;
		
	}
	
	/**
	 * Creates a Map with the Standard Genetic Code
	 * 
	 * @return a Map
	 */
	public Map<String,String> getStandardGeneticCode() {
		Map<String,String> standardGeneticCode = new HashMap<String, String>(); 
		standardGeneticCode.put("GCT", "A");
		standardGeneticCode.put("GCC", "A");
		standardGeneticCode.put("GCA", "A");
		standardGeneticCode.put("GCG", "A");
		standardGeneticCode.put("TTA", "L");
		standardGeneticCode.put("TTG", "L");
		standardGeneticCode.put("CTT", "L");
		standardGeneticCode.put("CTC", "L");
		standardGeneticCode.put("CTA", "L");
		standardGeneticCode.put("CTG", "L");
		standardGeneticCode.put("CGT", "R");
		standardGeneticCode.put("CGC", "R");
		standardGeneticCode.put("CGA", "R");
		standardGeneticCode.put("CGG", "R");
		standardGeneticCode.put("AGA", "R");
		standardGeneticCode.put("AGG", "R");
		standardGeneticCode.put("AAA", "K");
		standardGeneticCode.put("AAG", "K");
		standardGeneticCode.put("AAT", "N");
		standardGeneticCode.put("AAC", "N");
		standardGeneticCode.put("ATG", "M");
		standardGeneticCode.put("GAT", "D");
		standardGeneticCode.put("GAC", "D");
		standardGeneticCode.put("TTT", "F");
		standardGeneticCode.put("TTC", "F");
		standardGeneticCode.put("TGT", "C");
		standardGeneticCode.put("TGC", "C");
		standardGeneticCode.put("CCT", "P");
		standardGeneticCode.put("CCC", "P");
		standardGeneticCode.put("CCA", "P");
		standardGeneticCode.put("CCG", "P");
		standardGeneticCode.put("CAA", "Q");
		standardGeneticCode.put("CAG", "Q");
		standardGeneticCode.put("TCT", "S");
		standardGeneticCode.put("TCC", "S");
		standardGeneticCode.put("TCA", "S");
		standardGeneticCode.put("TCG", "S");
		standardGeneticCode.put("AGT", "S");
		standardGeneticCode.put("AGC", "S");
		standardGeneticCode.put("GAA", "E");
		standardGeneticCode.put("GAG", "E");
		standardGeneticCode.put("ACT", "T");
		standardGeneticCode.put("ACC", "T");
		standardGeneticCode.put("ACA", "T");
		standardGeneticCode.put("ACG", "T");
		standardGeneticCode.put("GGT", "G");
		standardGeneticCode.put("GGC", "G");
		standardGeneticCode.put("GGA", "G");
		standardGeneticCode.put("GGG", "G");
		standardGeneticCode.put("TGG", "W");
		standardGeneticCode.put("CAT", "H");
		standardGeneticCode.put("CAC", "H");
		standardGeneticCode.put("TAT", "Y");
		standardGeneticCode.put("TAC", "Y");
		standardGeneticCode.put("ATT", "I");
		standardGeneticCode.put("ATC", "I");
		standardGeneticCode.put("ATA", "I");
		standardGeneticCode.put("GTT", "V");
		standardGeneticCode.put("GTC", "V");
		standardGeneticCode.put("GTA", "V");
		standardGeneticCode.put("GTG", "V");
		standardGeneticCode.put("TAA", "*");
		standardGeneticCode.put("TGA", "*");
		standardGeneticCode.put("TAG", "*");
		return standardGeneticCode;
		
	}
	
	/**
	 * Creates a geneticCode from a BufferedInput. 
	 * 
	 * The input format expected for the genetic code is like:
	 * 
	 * <pre>
	 * A, GCT, GCC, GCA, GCG
	 * C, TGT, TGC
	 * D, GAT, GAC
	 * E, GAA, GAG
	 * F, TTT, TTC
	 * G, GGT, GGC, GGA, GGG
	 * H, CAT, CAC
	 * I, ATT, ATC, ATA
	 * K, AAA, AAG
	 * L, TTA, TTG, CTT, CTC, CTA, CTG
	 * M, ATG
	 * N, AAT, AAC
	 * P, CCT, CCC, CCA, CCG
	 * Q, CAA, CAG
	 * R, CGT, CGC, CGA, CGG, AGA, AGG
	 * S, TCT, TCC, TCA, TCG, AGT, AGC
	 * T, ACT, ACC, ACA, ACG
	 * V, GTT, GTC, GTA, GTG
	 * W, TGG
	 * Y, TAT, TAC
	 * *, TAA, TGA, TAG
	 * </pre>
	 * @param in the input data for the genetic code.
	 * @return a Map with the genetic code.
	 */
	public static Map<String,String> readGeneticCode(BufferedReader in) {
		
		Map<String, String> geneticCode = new HashMap<String, String>();
		
		String line;
		
		try {
			
			while((line = in.readLine()) !=  null) {
				
				String[] codons = line.split(",");

				if (codons.length>1) {
					
					for (int i =1 ; i< codons.length; i++) {
					
						geneticCode.put(codons[i], codons[0]);
					
					}
					
				}
				
			}
			
		} catch (IOException e) {
			
			System.err.println("There was an error reading the genetic code.");
			
			return Translate.getInstance().getStandardGeneticCode();
			
		}
		
		return geneticCode;
		
	}
	
}
