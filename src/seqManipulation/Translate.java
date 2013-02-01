package seqManipulation;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import cmdGA.Parser;

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
	private Map<String,String> getStandardGeneticCode() {
		Map<String,String> standardGeneticCode = new HashMap<String, String>(); 
		standardGeneticCode.put("A", "GCT");
		standardGeneticCode.put("A", "GCC");
		standardGeneticCode.put("A", "GCA");
		standardGeneticCode.put("A", "GCG");
		standardGeneticCode.put("L", "TTA");
		standardGeneticCode.put("L", "TTG");
		standardGeneticCode.put("L", "CTT");
		standardGeneticCode.put("L", "CTC");
		standardGeneticCode.put("L", "CTA");
		standardGeneticCode.put("L", "CTG");
		standardGeneticCode.put("R", "CGT");
		standardGeneticCode.put("R", "CGC");
		standardGeneticCode.put("R", "CGA");
		standardGeneticCode.put("R", "CGG");
		standardGeneticCode.put("R", "AGA");
		standardGeneticCode.put("R", "AGG");
		standardGeneticCode.put("K", "AAA");
		standardGeneticCode.put("K", "AAG");
		standardGeneticCode.put("N", "AAT");
		standardGeneticCode.put("N", "AAC");
		standardGeneticCode.put("M", "ATG");
		standardGeneticCode.put("D", "GAT");
		standardGeneticCode.put("D", "GAC");
		standardGeneticCode.put("F", "TTT");
		standardGeneticCode.put("F", "TTC");
		standardGeneticCode.put("C", "TGT");
		standardGeneticCode.put("C", "TGC");
		standardGeneticCode.put("P", "CCT");
		standardGeneticCode.put("P", "CCC");
		standardGeneticCode.put("P", "CCA");
		standardGeneticCode.put("P", "CCG");
		standardGeneticCode.put("Q", "CAA");
		standardGeneticCode.put("Q", "CAG");
		standardGeneticCode.put("S", "TCT");
		standardGeneticCode.put("S", "TCC");
		standardGeneticCode.put("S", "TCA");
		standardGeneticCode.put("S", "TCG");
		standardGeneticCode.put("S", "AGT");
		standardGeneticCode.put("S", "AGC");
		standardGeneticCode.put("E", "GAA");
		standardGeneticCode.put("E", "GAG");
		standardGeneticCode.put("T", "ACT");
		standardGeneticCode.put("T", "ACC");
		standardGeneticCode.put("T", "ACA");
		standardGeneticCode.put("T", "ACG");
		standardGeneticCode.put("G", "GGT");
		standardGeneticCode.put("G", "GGC");
		standardGeneticCode.put("G", "GGA");
		standardGeneticCode.put("G", "GGG");
		standardGeneticCode.put("W", "TGG");
		standardGeneticCode.put("H", "CAT");
		standardGeneticCode.put("H", "CAC");
		standardGeneticCode.put("Y", "TAT");
		standardGeneticCode.put("Y", "TAC");
		standardGeneticCode.put("I", "ATT");
		standardGeneticCode.put("I", "ATC");
		standardGeneticCode.put("I", "ATA");
		standardGeneticCode.put("V", "GTT");
		standardGeneticCode.put("V", "GTC");
		standardGeneticCode.put("V", "GTA");
		standardGeneticCode.put("V", "GTG");
		standardGeneticCode.put("*", "TAA");
		standardGeneticCode.put("*", "TGA");
		standardGeneticCode.put("*", "TAG");
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
					
						geneticCode.put(codons[0], codons[i]);
					
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
