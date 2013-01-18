package seqManipulation;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import fileformats.fastaIO.FastaMultipleReader;
import fileformats.fastaIO.Pair;

public class OrfFinder {

	/**
	 * Search ORFs in a given sequence 
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
	
		Object[] g = OrfFinder.firstORF("AATG", 240);
		System.out.println();
		
//		FastaMultipleReader fmr = new FastaMultipleReader();
//		
//		try {
//			List<Pair<String,String>> res = fmr.readFile("C:\\JAvier\\DropBox\\My Dropbox\\Investigacion\\Tesis\\Figuras\\#Intro.Filogenias\\arena_L.fas");
//
//			System.out.println("hay " + res.size() + " secuencias en el archivo");
//			
//			FileWriter bfg = new FileWriter("C:\\JAvier\\DropBox\\My Dropbox\\Investigacion\\Tesis\\Figuras\\#Intro.Filogenias\\arena_L_Z.fas",false);
//			FileWriter bfn = new FileWriter("C:\\JAvier\\DropBox\\My Dropbox\\Investigacion\\Tesis\\Figuras\\#Intro.Filogenias\\arena_L_L.fas",false);
//			
//			for (Pair<String, String> pair : res) {
//				
//				Object[] g = OrfFinder.firstORF(pair.getSecond(), 240);
//				String prot_g = (String) g[0];
////				int st_g = (Integer) g[1];
////				int le_g = (Integer) g[2];
//				
//				Object[] n = OrfFinder.firstComplementatyOrfFromEnd(pair.getSecond(), 240);
//				String prot_n = (String) n[0];
////				int st_n = (Integer) n[1];
////				int le_n = (Integer) n[2];
//
//				System.out.println("found : " + prot_g);
//				System.out.println("size  : " + prot_g.length());
//				System.out.println("found : " + prot_n);
//				System.out.println("size  : " + prot_n.length());
//				
//				if (prot_g.length()>240) {
//
////					bfg.write(">" + pair.getFirst() + "\r\n");
////					bfg.write(prot_g + "\r\n");
//					
//				}
//				
//				if (prot_n.length()>240) {
//
////					bfn.write(">" + pair.getFirst() + "\r\n");
////					bfn.write(prot_n + "\r\n");
//					
//				}
//				
//			}
//			bfg.close();
//			bfn.close();
//			
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
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
	@SuppressWarnings("unused")
	public static Object[][] allOrfs(String sequence, int largerThan, boolean bothStrands, boolean circular, int frame) {

		int[] ATGs = OrfFinder.scanATG(sequence);
		int[] STOPs = OrfFinder.scanSTOP(sequence);
		
		Arrays.sort(ATGs);
		Arrays.sort(STOPs);
		
		if (circular) {
			
			if (sequence.length()%3 == 0) {
				
				int[] nATGs = new int[2*ATGs.length];
				int[] nSTOPs = new int[2*STOPs.length];
				for (int i=0 ; i < 2*ATGs.length  ; i++) nATGs[i]  = ATGs[i % ATGs.length] + sequence.length() * ((int)(i / ATGs.length));
				for (int i=0 ; i < 2*STOPs.length ; i++) nSTOPs[i] = STOPs[i % STOPs.length]+ sequence.length() * ((int)(i / STOPs.length));
				
				ATGs = nATGs;
				STOPs = nSTOPs;
				
			} else {
				int[] nATGs = new int[4*ATGs.length];
				int[] nSTOPs = new int[4*STOPs.length];
				for (int i=0 ; i < 4*ATGs.length  ; i++) nATGs[i]  = ATGs[i % ATGs.length] + sequence.length() * ((int)(i / ATGs.length));
				for (int i=0 ; i < 4*STOPs.length ; i++) nSTOPs[i] = STOPs[i % STOPs.length]+ sequence.length() * ((int)(i / STOPs.length));
				
				ATGs = nATGs;
				STOPs = nSTOPs;			
			}
		
		}
		
		System.out.println(ATGs);
		System.out.println(STOPs);
		
		// TAGATGNNN  ->Circular TAG |ATG NNN TAG| ATG NNN -> 1 COPY NEEDED 
		// TAGNATGNN  ->Circular T AGN ATG NNT AGN ATG NN  -> 1 COPY NEEDED 
		// TAGNNATGN  ->Circular TA GNN ATG NTA GNN ATG N  -> 1 COPY NEEDED
		// 
		// TAGATGNNNN TAG |ATG NNN NTA GAT GNN NNT AGA TGN NNN TAG| ATG NNN N   -> 3 COPY NEEDED
		// TAGNATGNNN T AGN |ATG NNN TAG| NAT GNN NTA GNA TGN NNT AGN ATG NNN   -> 1 COPY NEEDED
		// TAGNNATGNN TA GNN |ATG NNT AGN NAT GNN TAG| NNA TGN NTA GNN ATG NN   -> 2 COPY NEEDED
		//
		// TAGATGNNNNN TAG |ATG NNN NNT AGA TGN NNN NTA GAT GNN NNN TAG| ATG NNN NN  -> 3 COPY NEEDED
		// TAGNATGNNNN T AGN |ATG NNN NTA GNA TGN NNN TAG| NAT GNN NNT AGN ATG NNN N -> 2 COPY NEEDED
		// TAGNNATGNNN TA GNN |ATG NNN TAG| NNA TGN NNT AGN NAT GNN NTA GNN ATG NNN  -> 1 COPY NEEDED
		
		
		
//		int[] maxLengthATGForSTOP = new int[STOPs.length];
//		
//
//		for (int stop : STOPs) {
//			// Look in every Stop mark.
//			int seqLenght = sequence.length();
//			Integer previousStop = OrfFinder.getPreviousStop(stop,STOPs,circular,seqLenght);
//				// get the previous stop in the same frame.
//
//			Integer previousATG = getPreviousATG(circular, ATGs, stop, seqLenght, previousStop);
//				// an ATG in the same frame must exits between the two STOPs, 
//				// if more of one is found, the closest is recovered.
//			
//		}
		
		//
		//
		//
		//
		//
		//
		
		
		return null;
		
		
		
		
	}

	/**
	 * Given two consecutive stop marks, returns the position of the an ATG between these two 
	 * that is nearest to the first stop.
	 *   
	 * @param circular
	 * @param ATGs
	 * @param stop
	 * @param seqLenght
	 * @param previousStop
	 * @return
	 */
	protected static Integer getPreviousATG(boolean circular, int[] ATGs, int stop, int seqLenght, Integer previousStop) {
		Integer previousATG=null;
		for (int atg : ATGs) {
			// Look for every ATG
		if (atg<stop) {
				// The ATG is before the current Stop
				if (atg%3 == stop%3) {
					// They are in the same frame
					if (previousATG==null || (atg<previousATG || previousATG>stop)) {
						// |-------------|-------------|-------------|<
						//           a p         s                    < this      (p =previousATG, s= current stop
			            //                       s       a  p         < or this   (a = atg being analyzed 	
						if (previousATG > previousStop) {
							previousATG = atg;
						}
					}
				}
			} else
			// This stop mark is after the current stop mark.
			if (circular) {
				// if circular may be useful
				if ((stop + seqLenght - atg)%3 ==0) {
					// they are in the same frame
					if (previousATG==null || (atg<previousATG && previousATG>=stop));
					  previousATG = atg;
						// |-------------|-------------|-------------|<					  
 					    //                  s     a    p              < this      (p =previous, s= current stop
   			            //                                                        (a = atg being analyzed 
					if ((previousATG > previousStop) || (previousATG<stop && previousStop>=stop)) {
						// |-------------|-------------|-------------|<					  
 					    //     ps  pa       s                         < this      (pa =previousAtg, s= current stop
 					    //                  s      ps  pa             < this      (ps =previousStop
 					    //          pa      s      ps                 < this      						
   			            //                                                         
						previousATG = atg;
					}
				}
			}
			// if no circular, the stop must do not be recovered.
		}
		return previousATG;
	}

	/**
	 * Given a Stop mark position, retrieves the position of the previous one in the same frame.
	 * If none is recovered, the result is null.
	 * 
	 * @param stop the current stop position 
	 * @param STOPs an array with all the stop mark position in an sequence
	 * @param circular is the sequence is the be considered circular, like a plasmid
	 * @param seqLenght the length of the sequence
	 * @return
	 */
	private static Integer getPreviousStop(int stop, int[] STOPs, boolean circular, int seqLenght) {
		
		Integer previous = null;

		for (int i : STOPs) {

			if (i<stop) {
				// This Stop mark is before the current stop mark.
				if (i%3 == stop%3) {
					// both stops mark are in the same frame
					if (previous==null || (i>previous || stop<=previous)) {
						// |-------------|-------------|-------------|
						//             p    i    s                    < this      (p =previous, s= current stop
//			            //                  i    s          p         < or this   (i = stop being analyzed 
						previous = i;
					}
				}
			} else {
				// This stop mark is after the current stop mark.
				if (circular) {
					// if circular may be useful
					if ((stop + seqLenght - i)%3 ==0) {
						// they are in the same frame
						if (previous==null || (i>previous && previous>=stop));
						previous = i;
						//                 s     p    i               < this      (p =previous, s= current stop
//			            //                                                        (i = stop being analyzed 
						
					}
				}
				// if no circular, the stop must do not be recovered.
			}
			
		}
		
		return previous;
		
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
