package seqManipulation;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

import fastaIO.FastaMultipleReader;
import fastaIO.Pair;

public class OrfFinder {

	/**
	 * Search ORFs in a given sequence
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
	
		FastaMultipleReader fmr = new FastaMultipleReader();
		
		try {
			List<Pair<String,String>> res = fmr.readFile("C:\\JAvier\\DropBox\\My Dropbox\\Investigacion\\Tesis\\Figuras\\#Intro.Filogenias\\arena_L.fas");

			System.out.println("hay " + res.size() + " secuencias en el archivo");
			
			FileWriter bfg = new FileWriter("C:\\JAvier\\DropBox\\My Dropbox\\Investigacion\\Tesis\\Figuras\\#Intro.Filogenias\\arena_L_Z.fas",false);
			FileWriter bfn = new FileWriter("C:\\JAvier\\DropBox\\My Dropbox\\Investigacion\\Tesis\\Figuras\\#Intro.Filogenias\\arena_L_L.fas",false);
			
			for (Pair<String, String> pair : res) {
				
				Object[] g = OrfFinder.firstORF(pair.getSecond(), 240);
				String prot_g = (String) g[0];
//				int st_g = (Integer) g[1];
//				int le_g = (Integer) g[2];
				
				Object[] n = OrfFinder.firstComplementatyOrfFromEnd(pair.getSecond(), 240);
				String prot_n = (String) n[0];
//				int st_n = (Integer) n[1];
//				int le_n = (Integer) n[2];

				System.out.println("found : " + prot_g);
				System.out.println("size  : " + prot_g.length());
				System.out.println("found : " + prot_n);
				System.out.println("size  : " + prot_n.length());
				
				if (prot_g.length()>240) {

					bfg.write(">" + pair.getFirst() + "\r\n");
					bfg.write(prot_g + "\r\n");
					
				}
				
				if (prot_n.length()>240) {

					bfn.write(">" + pair.getFirst() + "\r\n");
					bfn.write(prot_n + "\r\n");
					
				}
				
			}
			bfg.close();
			bfn.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	///////////////////
	// Public Interface
	/**
	 * Search ORFs in a sequence.
	 * 
	 * @param sequence A nucleotide sequence. 
	 * @param frame <code>0</code> for All, <code>1-3</code> for specific frame.
	 * @param bothStrands search in the given sequence and in the reverse complementary sequence.
	 * @param largerthan minimum size of the ORF to report.
	 * @param circular True if the sequence is from a circular DNA molecule (i.e. a plasmid). 
	 *  
	 */
	public static Object[][] allOrfs(String sequence, int largerThan, boolean bothStrands, boolean circular, int frame) {

		int[] ATGs = OrfFinder.scanATG(sequence);
		int[] STOPs = OrfFinder.scanSTOP(sequence);
		
		
		// TODO Write the code lazy guy!!!
		
		
		return null;
		
		
		
		
	}
	
	public static Object[] nextORF(String sequence, int largerThan) {
		
		int maxATG=0;
		int maxLength=0;
		
		int[] ATGs = OrfFinder.scanATG(sequence);
		int[] STOPs = OrfFinder.scanSTOP(sequence);
		
		
		for (int atg : ATGs) {
			int stopCounter=0;
			
			while(  stopCounter < STOPs.length && !((STOPs[stopCounter]>atg) && ((STOPs[stopCounter] - atg)%3 == 0))) {
				stopCounter++;
			}
			
			if (stopCounter<STOPs.length) {
				int currentLength = STOPs[stopCounter] - atg;
				if (currentLength > maxLength && currentLength >= largerThan) {
					maxATG=atg;
					maxLength=currentLength;
				}
			}
		} 
		Object[] result = {(Object) sequence.substring(maxATG, maxATG + maxLength), maxATG, maxLength};
		return result;
	}
	
	public static Object[] firstORF(String sequence, int largerThan) {
		
		int maxATG=0;
		int maxLength=0;
		
		int[] ATGs = OrfFinder.scanATG(sequence);
		int[] STOPs = OrfFinder.scanSTOP(sequence);
		
		
		for (int atg : ATGs) {
			int stopCounter=0;
			
			while(  stopCounter < STOPs.length && !((STOPs[stopCounter]>atg) && ((STOPs[stopCounter] - atg)%3 == 0))) {
				stopCounter++;
			}
			
			if (stopCounter<STOPs.length) {
				int currentLength = STOPs[stopCounter] - atg;
				if (currentLength > maxLength && currentLength >= largerThan) {
					maxATG=atg;
					maxLength=currentLength;
					Object[] result = {(Object) sequence.substring(maxATG, maxATG + maxLength), maxATG, maxLength};
					return result;
				}
			}
		} 
		Object[] result = {(Object) sequence.substring(maxATG, maxATG + maxLength), maxATG, maxLength};
		return result;
	}
	
	public static Object[] nextComplementatyOrfFromEnd(String sequence, int largerThan) {
		return nextORF(reverseComplementary(sequence),largerThan);
	}
	
	public static Object[] firstComplementatyOrfFromEnd(String sequence, int largerThan) {
		return firstORF(reverseComplementary(sequence),largerThan);
	}

	
	//////////////////
	// Private Methods

	private static String reverseComplementary(String sequence) {
		
		StringBuilder result = new StringBuilder(sequence.length());
		for (int i = sequence.length()-1; i>=0; i--) {
			result.append(getComplementaryBase(Character.toUpperCase(sequence.charAt(i))));
		}
		
		return result.toString();
	}
	
	private static char getComplementaryBase(char charAt) {
		char result;
		switch (charAt) {
			case 'A': result ='T' ; break;
			case 'C': result ='G' ; break;
			case 'T': result ='A' ; break;
			case 'G': result ='C' ; break;
			case 'W': result ='W' ; break;
			case 'S': result ='S' ; break;
			case 'R': result ='Y' ; break;
			case 'Y': result ='R' ; break;
			case 'K': result ='M' ; break;
			case 'M': result ='K' ; break;
			case 'B': result ='V' ; break;
			case 'D': result ='H' ; break;
			case 'H': result ='D' ; break;
			case 'V': result ='B' ; break;
			case 'N': result ='N' ; break;
			case '-': result ='-' ; break;
			default:  result ='N' ; break;
		};
		return result;
	}

	private static int[] scanATG(String sequence) {
		return extractMatches(sequence.toUpperCase(), "ATG");
	}
	
	private static int[] scanSTOP(String sequence) {
		String[] r = {"TGA","TAG","TAA"};
		return extractMatches(sequence.toUpperCase(), r);
	}
	
	private static int[] extractMatches(String sequence, String seqToMatch) {
		String[] r = {seqToMatch};
		return extractMatches(sequence,r);
	}
	
	protected static int[] extractMatches(String sequence, String[] seqToMatch) {
		List<Integer> result = new Vector<Integer>();
		for (int i=0; i<sequence.length();i++) {
			for (int j=0; j<seqToMatch.length; j++) {
				if (sequence.startsWith(seqToMatch[j], i)) result.add(i);
			}
		}
		int[] arrayResult = new int[result.size()];
		for (int i = 0; i<result.size();i++) {
			arrayResult[i] = result.get(i);
		}
		return arrayResult;
	}
}
