package seqManipulation.orf;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import seqManipulation.complementary.Complementary;

import cmdGA.NoOption;
import cmdGA.Parser;
import cmdGA.SingleOption;
import cmdGA.exceptions.IncorrectParameterTypeException;
import cmdGA.parameterType.InputStreamParameter;
import cmdGA.parameterType.IntegerParameter;
import cmdGA.parameterType.PrintStreamParameter;
import fileformats.fastaIO.FastaMultipleReader;
import fileformats.fastaIO.Pair;

public class OrfFinder {

	/**
	 * Search ORFs in a given sequence. 
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
	
//		Object[] g = OrfFinder.firstORF("AATG", 240);
		
		// STEP ONE:
		// Create a Parser.
		Parser parser = new Parser();
		
		// STEP TWO:
		// DEFINE THE POSSIBLE OPTIONS ACCEPTED IN THE COMMAND LINE. (TAKE CARE OF AVOID AMBIGUITY) 
		SingleOption input = new SingleOption(parser, System.in, "-in", InputStreamParameter.getParameter());
		NoOption cir = new NoOption(parser, "-circular");
		SingleOption minsize = new SingleOption(parser, 100, "-min", IntegerParameter.getParameter());
		SingleOption outfile = new SingleOption(parser, System.out, "-outfile", PrintStreamParameter.getParameter());
		SingleOption frameOpt = new SingleOption(parser, 0, "-frame", IntegerParameter.getParameter());
		NoOption largestOpt = new NoOption(parser, "-largest");
		NoOption marksOpt = new NoOption(parser, "-marks");
		NoOption inFastaOpt = new NoOption(parser, "-fasta");
		NoOption helpOpt = new NoOption(parser, "-help");
		
		
		// STEP THREE
		// PARSE THE COMMAND LINE
		try {
			parser.parseEx(args);
		} catch (IncorrectParameterTypeException e) {
			System.out.println("There was an error:");
			System.out.println(e.getMessage());
			System.out.println("Program aborted.");
			System.exit(1);
		}
		BufferedReader in = new BufferedReader(new InputStreamReader( (InputStream) input.getValue()));
		PrintStream out = (PrintStream) outfile.getValue();
		
		if (helpOpt.isPresent()) {
			
			printHelp(out);			
			
		}
		
		boolean inFasta = (Boolean) inFastaOpt.isPresent();

		if (inFasta) {
			
			FastaMultipleReader mrf; 

			mrf = new FastaMultipleReader();
			
			List<Pair<String,String>> seqs = mrf.readBuffer(in);
			
			for (Pair<String, String> pair : seqs) {
				
				if (!marksOpt.isPresent()) {

					exportFasta(out, pair.getSecond(), (Integer) minsize.getValue(), pair.getFirst(),(Boolean) cir.getValue(), (Integer) frameOpt.getValue(), largestOpt.isPresent());
					
				} else {
					
					List<List<Integer>> marks = getMarks(pair.getSecond()); 
					
					exportMarks(out,pair.getFirst(),marks);
					
				}
				
			}
			
			System.exit(0);
			
		} else {

		
		try {
			
			String line;
			
			while ((line = in.readLine()) != null ) {
				
				int counter =0;
				
				if (!marksOpt.isPresent()) {

					exportFasta(out, line, (Integer) minsize.getValue(), "",(Boolean) cir.getValue(), (Integer) frameOpt.getValue(), largestOpt.isPresent());
					
				} else {
					
					List<List<Integer>> marks = getMarks(line); 
					
					exportMarks(out,"Sequence "+counter,marks);
					
					counter++;
					
				}
				
			}
			
			System.exit(0);
			
		} catch (IOException e1) {
			
			System.err.println("There was an IO error reading the input data.");
			
		}
		
		}

	}

	private static void exportMarks(PrintStream out, String first, List<List<Integer>> marks) {
		
		out.println(first); 
		
		int counter =0;

		
		for (List<Integer> list : marks) {

			StringBuilder sb = new StringBuilder();

			if(counter%2==0) {
				sb.append("ATGs  ");
			} else {
				sb.append("Stops ");
			}
			
			sb.append("frame("+counter+")");
			
			for(int j=0 ; j<list.size() ;j++) {
				
				if (j!=0) sb.append(" ,");
				
				sb.append("[" + list.get(j) + "]");
				
			}
			
			out.println(sb.toString());
			
			counter++;
			
		}
		
		out.flush();
		
		out.close();
		
	}

	private static void exportFasta(PrintStream out,String sequence, int minSize, String baseDescription, boolean isCircular, int frame, boolean keepLargest) {
		
		List<String> r = OrfFinder.allOrfs(sequence, minSize , frame==0, isCircular , frame);
		
		if (keepLargest) {
			
			String largestString = "";
			
			int largetsSize =0;
			
			for (String string : r) {
				
				if (string.length() > largetsSize) {
					
					largestString = string;
					
					largetsSize = string.length(); 
					
				}
				
			}
			
			r.clear();
			
			r.add(largestString);
			
		}
		
		int counter =0;
		
		int w = r.size()/10 +1;

		if(!baseDescription.trim().equals("")) baseDescription = baseDescription + "|"; 

		for (String string : r) {
			counter++;
			out.print(">"+baseDescription+"ORF:");
			out.format("%0"+ w +"d%n", counter);
			out.println(string);
		}
	}

	protected static void printHelp(PrintStream out) {
		out.println("OrfFinder ver 0.0.1");
		out.println("");
		out.println("   Options:");
		out.println("   -in         : Is the path to the input fasta file to be readed. ");
		out.println("               :   If no present, standard input is assumed.");
		out.println("   -outfile    : Is the path to the output file. ");
		out.println("               :   If no present, standard output is assumed.");
		out.println("   -min        : Is the minimum size for and ORF to be informed. (Default is 100)");
		out.println("   -circular   : Assumes that the sequence is from a circular molecule. ");			
		out.println("   -frame      : Looks ORFs in a specific frame: 1, 2 o 3.");
		out.println("   -largest    : keep the largest ORF found and discard the others.");
		out.println("               :   If frame is 0, then all frames are analyzed.");
		out.println("   -fasta      : the input is a fasta file.");			
		out.println("   -help       : Show this information.");
	}

	///////////////////
	// Public Interface
	/**
	 * Search ORFs in a sequence.
	 *
	 * <pre>
     * TAGATGNNN   ->Circular TAG |ATG NNN TAG| ATG NNN                                 -> 1 COPY NEEDED 
     * TAGNATGNN   ->Circular T AGN ATG NNT AGN ATG NN                                  -> 1 COPY NEEDED 
     * TAGNNATGN   ->Circular TA GNN ATG NTA GNN ATG N                                  -> 1 COPY NEEDED
     * 
     * TAGATGNNNN  ->Circular TAG |ATG NNN NTA GAT GNN NNT AGA TGN NNN TAG| ATG NNN N   -> 3 COPY NEEDED
     * TAGNATGNNN  ->Circular T AGN |ATG NNN TAG| NAT GNN NTA GNA TGN NNT AGN ATG NNN   -> 1 COPY NEEDED
     * TAGNNATGNN  ->Circular TA GNN |ATG NNT AGN NAT GNN TAG| NNA TGN NTA GNN ATG NN   -> 2 COPY NEEDED
     * 
     * TAGATGNNNNN TAG |ATG NNN NNT AGA TGN NNN NTA GAT GNN NNN TAG| ATG NNN NN         -> 3 COPY NEEDED
     * TAGNATGNNNN T AGN |ATG NNN NTA GNA TGN NNN TAG| NAT GNN NNT AGN ATG NNN N        -> 2 COPY NEEDED
     * TAGNNATGNNN TA GNN |ATG NNN TAG| NNA TGN NNT AGN NAT GNN NTA GNN ATG NNN         -> 1 COPY NEEDED
	 * </pre> 
	 * 
	 * @param sequence A nucleotide sequence. 
	 * @param frame <code>0</code> for All, <code>1-3</code> for specific frame.
	 * @param bothStrands search in the given sequence and in the reverse complementary sequence.
	 * @param minSize minimum size of the ORF to report.
	 * @param circular True if the sequence is from a circular DNA molecule (i.e. a plasmid). 
	 * 
	 * @return a list of String that contain all the orfs in the sequence
	 */
	public static List<String> allOrfs(String sequence, int minSize, boolean bothStrands, boolean circular, int frame) {
		List<String> result = new ArrayList<String>();
		if (bothStrands) {
			result = allOrfs(sequence, minSize, circular, frame);
			result.addAll(allOrfs(Complementary.reverseComplementary(sequence), minSize, circular, frame));
		} else {
			result = allOrfs(sequence, minSize, circular, frame);
		}
		return result;
		
	}
	 
	/**
	 * Search ORFs in a sequence.
	 *
	 * <pre>
	 * For circular sequences, an ORF could start at the end of the sequence and 
	 * end at the beginning. For this, the sequence needs to be replicated.
	 * 
     * TAGATGNNN   ->Circular TAG |ATG NNN TAG| ATG NNN                                 -> 1 COPY NEEDED 
     * TAGNATGNN   ->Circular T AGN ATG NNT AGN ATG NN                                  -> 1 COPY NEEDED 
     * TAGNNATGN   ->Circular TA GNN ATG NTA GNN ATG N                                  -> 1 COPY NEEDED
     * 
     * TAGATGNNNN  ->Circular TAG |ATG NNN NTA GAT GNN NNT AGA TGN NNN TAG| ATG NNN N   -> 3 COPY NEEDED
     * TAGNATGNNN  ->Circular T AGN |ATG NNN TAG| NAT GNN NTA GNA TGN NNT AGN ATG NNN   -> 1 COPY NEEDED
     * TAGNNATGNN  ->Circular TA GNN |ATG NNT AGN NAT GNN TAG| NNA TGN NTA GNN ATG NN   -> 2 COPY NEEDED
     * 
     * TAGATGNNNNN ->Circular TAG |ATG NNN NNT AGA TGN NNN NTA GAT GNN NNN TAG| ATG NNN NN         -> 3 COPY NEEDED
     * TAGNATGNNNN ->Circular T AGN |ATG NNN NTA GNA TGN NNN TAG| NAT GNN NNT AGN ATG NNN N        -> 2 COPY NEEDED
     * TAGNNATGNNN ->Circular TA GNN |ATG NNN TAG| NNA TGN NNT AGN NAT GNN NTA GNN ATG NNN         -> 1 COPY NEEDED
	 * </pre> 
	 * 
	 * @param sequence A nucleotide sequence. 
	 * @param frame <code>0</code> for All, <code>1-3</code> for specific frame.
	 * @param minSize minimum size of the ORF to report, the size is measured in bases, no amino acids.
	 * @param circular True if the sequence is from a circular DNA molecule (i.e. a Plasmid). 
	 * 
	 * @return a list of String that contain all the ORFs in the sequence
	 */
	public static List<String> allOrfs(String sequence, int minSize, boolean circular, int frame) {

		int[] ATGs = OrfFinder.scanATG(sequence);
		int[] STOPs = OrfFinder.scanSTOP(sequence);
		int unitLength = sequence.length();
		
		@SuppressWarnings("unchecked")
		List<Integer>[] ATGsAndSTOPsByFrame = (List<Integer>[]) new List[3];
		ATGsAndSTOPsByFrame[0] = new ArrayList<Integer>();
		ATGsAndSTOPsByFrame[1] = new ArrayList<Integer>();
		ATGsAndSTOPsByFrame[2] = new ArrayList<Integer>();

		@SuppressWarnings("unchecked")
		List<Boolean>[] ATGorStop = (List<Boolean>[]) new List[3];
		ATGorStop[0] = new ArrayList<Boolean>();
		ATGorStop[1] = new ArrayList<Boolean>();
		ATGorStop[2] = new ArrayList<Boolean>();

		List<String> result = new ArrayList<String>();

		Arrays.sort(ATGs);
		Arrays.sort(STOPs);
		
		if (circular) {
			
			if (sequence.length()%3 == 0) {
				
				int by = 2;
				
				ATGs = replicateArray(sequence, ATGs, by);
				
				STOPs = replicateArray(sequence, STOPs, by);
				
				sequence = sequence + sequence;
				
			} else {
				
				int by = 4;
				
				ATGs = replicateArray(sequence, ATGs, by);

				STOPs = replicateArray(sequence, STOPs, by);
				
				sequence = sequence + sequence + sequence + sequence;
			}
		
		}
		
		separateByFrame(frame, ATGs, STOPs, ATGsAndSTOPsByFrame, ATGorStop);
		
		excludeAdjacentATGorSTOP(ATGsAndSTOPsByFrame, ATGorStop);
		
		retriveORFs(sequence, unitLength, ATGsAndSTOPsByFrame, ATGorStop, result, minSize);
		
		return result;
		
	}
	
	/**
	 * Extracts the ATGs and Stop positions from a sequence.
	 * 
	 * @param sequence a nucleotide sequence
	 * @return a List of twelve List. Each one of these list contains info of ATG and STOP by frame.
	 * 			<ol>
	 *          <li>List index 0  = ATG at frame 0.</li>
	 *          <li>List index 1  = stop at frame 0.</li>
	 *          <li>List index 2  = ATG at frame 1.</li>
	 *          <li>List index 3  = stop at frame 1.</li>
	 *          <li>List index 4  = ATG at frame 2.</li>
	 *          <li>List index 5  = stop at frame 2.</li>
	 *          <li>List index 6  = ATG at frame -0.</li>
	 *          <li>List index 7  = stop at frame -0.</li>
	 *          <li>List index 8  = ATG at frame -1.</li>
	 *          <li>List index 9  = stop at frame -1.</li>
	 *          <li>List index 10 = ATG at frame -2.</li>
	 *          <li>List index 11 = stop at frame -2.</li>
	 *          </ol><br>
	 *  Frames starting with '-', indicates that the ORF correspond to the reverse complementary sequence.
	 */
	public static List<List<Integer>> getMarks(String sequence) {

		
		
		String[] seqs = new String[2];
		
		seqs[0] =sequence;
		
		seqs[1] = Complementary.reverseComplementary(sequence);
		
		List<List<Integer>> result = new ArrayList<List<Integer>>();
		
		for (int i =0 ;i<2;i++) {
		
			result.addAll(getStrandMarks(seqs[i]));
			
		}
		
		return result;
		
	}

	

	@Deprecated
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
	
	@Deprecated
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
	
	@Deprecated
	public static Object[] nextComplementatyOrfFromEnd(String sequence, int largerThan) {
		return nextORF(Complementary.reverseComplementary(sequence),largerThan);
	}
	
	@Deprecated
	public static Object[] firstComplementatyOrfFromEnd(String sequence, int largerThan) {
		return firstORF(Complementary.reverseComplementary(sequence),largerThan);
	}

	
	//////////////////
	// Private Methods

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
	
	
	/**
	 * Extracts the ATGs and Stop positions from a sequence.
	 * 
	 * @param sequence a nucleotide sequence
	 * @return a List of six List. Each one of these list contains info of ATG and STOP by frame.
	 * 			<ol>
	 *          <li>List index 0 = ATG at frame 0.</li>
	 *          <li>List index 1 = stop at frame 0.</li>
	 *          <li>List index 2 = ATG at frame 1.</li>
	 *          <li>List index 3 = stop at frame 1.</li>
	 *          <li>List index 4 = ATG at frame 2.</li>
	 *          <li>List index 5 = stop at frame 2.</li>
	 *          </ol>
	 *          
	 */
	private static List<List<Integer>> getStrandMarks(String sequence) {
		
		List<List<Integer>> result = new ArrayList<List<Integer>>();
		int[] ATGs = OrfFinder.scanATG(sequence);
		int[] STOPs = OrfFinder.scanSTOP(sequence);
		
		@SuppressWarnings("unchecked")
		List<Integer>[] ATGsAndSTOPsByFrame = (List<Integer>[]) new List[3];
		ATGsAndSTOPsByFrame[0] = new ArrayList<Integer>();
		ATGsAndSTOPsByFrame[1] = new ArrayList<Integer>();
		ATGsAndSTOPsByFrame[2] = new ArrayList<Integer>();

		@SuppressWarnings("unchecked")
		List<Boolean>[] ATGorStop = (List<Boolean>[]) new List[3];
		ATGorStop[0] = new ArrayList<Boolean>();
		ATGorStop[1] = new ArrayList<Boolean>();
		ATGorStop[2] = new ArrayList<Boolean>();


		Arrays.sort(ATGs);
		Arrays.sort(STOPs);
		
		separateByFrame(0, ATGs, STOPs, ATGsAndSTOPsByFrame, ATGorStop);
		
		excludeAdjacentATGorSTOP(ATGsAndSTOPsByFrame, ATGorStop);

		for (int i=0; i<6;i++) result.add(new ArrayList<Integer>());
		
		for (int f=0; f<3;f++) {
			
			for (int i=0; i<ATGsAndSTOPsByFrame[f].size();i++) {
				
				result.get(2*f + ((ATGorStop[f].get(i))?0:1)).add(ATGsAndSTOPsByFrame[f].get(i));
				
			}
			
		}
		
		return result;
	}
	
	/**
	 * Retrieve the ORFs from a sequence, given a list of ATGs and STOPs mark position.
	 * There is expected that there are not two ATGs or two STOPs adjacent in the list.
	 * Usually the method <code>excludeAdjacentATGorSTOP</code> is called before this.
	 *   
	 * @param sequence the sequence from which the ORFs will be extracted.  
	 * @param unitLength is the Sequence minimun unit length.
	 * @param ATGsAnsSTOPsByFrame an <code>array</code> containing the ATGs and STOPs mark positions.
	 * @param ATGorStop is a list of boolean values that is a reference of which elements in ATGsAnsSTOPsByFrame list are ATG o STOP.
	 *        When ATGorSTOP[index] is true, then ATGsAnsSTOPsByFrame[index] is the position of an ATG.
	 *        When ATGorSTOP[index] is false, then ATGsAnsSTOPsByFrame[index] is the position of a STOP.  
	 * @param result is the array in which the results will be stored.
	 */
	protected static void retriveORFs(String sequence, int unitLength,
			List<Integer>[] ATGsAnsSTOPsByFrame, List<Boolean>[] ATGorStop,
			List<String> result, int minSize) {
		for (int cframe = 0; cframe<3;cframe++ ) {
			
			for (int i=0; i<ATGsAnsSTOPsByFrame[cframe].size();i++) {

				if (ATGorStop[cframe].get(i) && (i+1)<ATGsAnsSTOPsByFrame[cframe].size()) {
					
					int atgpos = ATGsAnsSTOPsByFrame[cframe].get(i);
					
					int endIndex = ATGsAnsSTOPsByFrame[cframe].get(i+1);
					
					if (atgpos<unitLength && (endIndex - atgpos) >= minSize ) {
						
						result.add(sequence.substring(atgpos, endIndex+3));
						
					}
					
				}
			
			}
			
		}
	}
	
	/**
	 * Detect, in a list of ATG and STOP, two adjacent ATG or two adjacent STOP and keeps the lower one, removing the other.
	 * 
	 * @param ATGsAnsSTOPsByFrame is a list of ATG and STOP mark positions
	 * @param ATGorStop is a list of boolean values that is a reference of which elements in ATGsAnsSTOPsByFrame list are ATG o STOP.
	 *        When ATGorSTOP[index] is true, then ATGsAnsSTOPsByFrame[index] is the position of an ATG.
	 *        When ATGorSTOP[index] is false, then ATGsAnsSTOPsByFrame[index] is the position of a STOP.    
	 */
	protected static void excludeAdjacentATGorSTOP(
			List<Integer>[] ATGsAnsSTOPsByFrame, List<Boolean>[] ATGorStop) {
		for (int cframe=0;cframe<3;cframe++) {
			
			if (ATGsAnsSTOPsByFrame[cframe].size()>1) {
				
				int lastIndex = 0;
				
				boolean lastWasATG = ATGorStop[cframe].get(lastIndex);
				
				for (int i = 1; i<ATGsAnsSTOPsByFrame[cframe].size(); i++) {
					
					if (ATGorStop[cframe].get(i)==lastWasATG) {
						
						ATGsAnsSTOPsByFrame[cframe].remove(i);
						
						ATGorStop[cframe].remove(i);
						
						i--;
						
					} else {
					
					lastWasATG = ATGorStop[cframe].get(i);
					
					}
					
				}
				
			}
			
		}
	}

	/**
	 * Replicate the elements of and <code>int</code> array a given number of times.
	 * The replicated elements are put at the end of the array and its value is modified 
	 * too add the length of the sequence.
	 * 
	 * <pre>
	 * Example:
	 *         Initial Array = [1, 4 , 6 , 9]
	 *         sequence length = 10;
	 *         Replicate 3 times;
	 *         ...
	 *         Result Array = [1, 4 , 6 , 9, 1 + (10*1), 4 + (10*1), 6 + (10*1), 9 + (10*1), 1 + (10*2), 4 + (10*2), 6 + (10*2), 9 + (10*2)]
	 *         Result Array = [1, 4 , 6 , 9, 11, 14, 16, 19, 21, 24, 26, 29]
	 * 
	 * </pre>
	 * @param sequence is the sequence for which the array positions belongs. The positions are ATG or STOPs mark positions.
	 * @param array contain the positions of ATG and STOP marks from the sequence.
	 * @param by is the number of times that the array will be replicated. 
	 * @return a new array with elements of the first replicated
	 */
	protected static int[] replicateArray(String sequence, int[] array, int by) {
		int[] nArray = new int[by*array.length];
		for (int i=0 ; i < by*array.length  ; i++) nArray[i]  = array[i % array.length] + sequence.length() * ((int)(i / array.length));
		return nArray; 
	}

	/**
	 * Given a list of ATGs positions and a list of STOPs positions corresponding 
	 * to a sequence, separates them by its frame. 
	 * 
	 * @param frame is the reading frame of the ATGs and STOPs positions to be retrieved. 
	 *        frame can be 0,1,2 or 3. 1 to 3 for each reading frame and 0 for all.  
	 * @param ATGs an array of ATG marks positions.
	 * @param STOPs an array of STOP marks positions.
	 * @param ATGsAnsSTOPsByFrame
	 * @param ATGorStop
	 */
	protected static void separateByFrame(int frame, int[] ATGs, int[] STOPs,
			List<Integer>[] ATGsAnsSTOPsByFrame, List<Boolean>[] ATGorStop) {
		// Separate atg and stops by frame
		
		for(int cframe=0;cframe<3;cframe++) {
		
			if ((cframe+1)==frame || frame == 0) {
			int atgIndex = 0;
			int StopIndex = 0;
			int currentATG=0;
			if (atgIndex<ATGs.length) currentATG = ATGs[atgIndex];
			int currentSTOP=0;
			if (StopIndex<STOPs.length) currentSTOP = STOPs[StopIndex];
			while(atgIndex<ATGs.length || StopIndex<STOPs.length) {
			
					while(atgIndex<ATGs.length && currentATG%3 != cframe) {
						// tries to get the next atg in the current frame
						atgIndex++;
						if (atgIndex<ATGs.length) currentATG = ATGs[atgIndex];
					}
					
					while(StopIndex<STOPs.length && currentSTOP%3 != cframe ){
						// tries to get the next atg in the current frame				
						StopIndex++;
						if (StopIndex<STOPs.length) currentSTOP = STOPs[StopIndex];
					}
					
					if ( atgIndex==ATGs.length && StopIndex==STOPs.length)  {
						// both STops and ATG reach the end
						// do nothing. The next loop will exit.
						continue;
					} else 
					if ( StopIndex==STOPs.length && atgIndex<ATGs.length)  {
						//Stop reach the end and ATGs do not.
						ATGsAnsSTOPsByFrame[cframe].add(currentATG);
						ATGorStop[cframe].add(true);
						atgIndex++;
						if (atgIndex<ATGs.length) currentATG = ATGs[atgIndex];
					} else
					if ( atgIndex==ATGs.length && StopIndex<STOPs.length)  {
						//Stop reach the end and ATGs do not.				
						ATGsAnsSTOPsByFrame[cframe].add(currentSTOP);
						ATGorStop[cframe].add(false);
						StopIndex++;
						if (StopIndex<STOPs.length) currentSTOP = STOPs[StopIndex];
					} else
					// Stops nor ATGs reach the end.				
					if (currentATG<currentSTOP) {
						// current atg is lower and atg is in frame
						ATGsAnsSTOPsByFrame[cframe].add(currentATG);
						atgIndex++;
						ATGorStop[cframe].add(true);
						if (atgIndex<ATGs.length) currentATG = ATGs[atgIndex];
						
					} else {
						// current stop is lower and stop is in frame
						ATGsAnsSTOPsByFrame[cframe].add(currentSTOP);
						ATGorStop[cframe].add(false);
						StopIndex++;				
						if (StopIndex<STOPs.length) currentSTOP = STOPs[StopIndex];				
					}
				}
			}
		}
	}

}
