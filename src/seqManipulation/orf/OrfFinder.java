package seqManipulation.orf;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import seqManipulation.orf.analysis.ExtractSequences;
import seqManipulation.orf.analysis.LargestOrf;
import seqManipulation.orf.analysis.OrfAnalysis;
import seqManipulation.orf.analysis.ShowMarks;
import seqManipulation.orf.analysis.ShowPositions;
import seqManipulation.orf.datastructures.OrfComposer;
import seqManipulation.orf.datastructures.OrfMarks;
import seqManipulation.orf.replication.Replicate;
import seqManipulation.orf.replication.ReplicateCircular;
import seqManipulation.orf.replication.ReplicateLinear;
import seqManipulation.orf.replication.Replicon;
import seqManipulation.orf.seqsource.FastaSequenceSource;
import seqManipulation.orf.seqsource.SequenceSource;
import seqManipulation.orf.seqsource.TextSequenceSource;

import cmdGA.MultipleOption;
import cmdGA.NoOption;
import cmdGA.Parser;
import cmdGA.SingleOption;
import cmdGA.exceptions.IncorrectParameterTypeException;
import cmdGA.parameterType.InputStreamParameter;
import cmdGA.parameterType.IntegerParameter;
import cmdGA.parameterType.PrintStreamParameter;
import pair.Pair;

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
		MultipleOption frameOpt = new MultipleOption(parser, 0, "-frame", ',',IntegerParameter.getParameter());
		NoOption largestOpt = new NoOption(parser, "-largest");
		NoOption marksOpt = new NoOption(parser, "-marks");
		NoOption inFastaOpt = new NoOption(parser, "-fasta");
		NoOption positOpt = new NoOption(parser, "-positions");
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
			
			System.exit(0);
		}
		
		SequenceSource source = getSource(inFastaOpt, in);
		
		Replicate replicate = getReplicate(cir);
		
		OrfAnalysis analysis = getAnalysis(minsize, frameOpt, largestOpt, marksOpt, positOpt, replicate);
		

		for (Pair<String, String> pair : source.getSequences()) {

			analysis.exportResults(out, pair);
			
		}

		out.flush();
		
		out.close();


	}

	private static OrfAnalysis getAnalysis(SingleOption minsize, MultipleOption frameOpt, NoOption largestOpt, NoOption marksOpt, 	NoOption positOpt, Replicate replicate) {
		OrfAnalysis a = null;
		
		Integer[] frames = getFrames(frameOpt);
		
		if (marksOpt.isPresent()) a = new ShowMarks(replicate,(Integer)minsize.getValue(),frames);
		else
		if (largestOpt.isPresent()) a = new LargestOrf(replicate, (Integer)minsize.getValue(), frames);
		else
		if (positOpt.isPresent()) a = new ShowPositions(replicate, (Integer)minsize.getValue(), frames);
		else 
		a = new ExtractSequences(replicate, (Integer)minsize.getValue(), frames);
		return a;
	}

	private static Integer[] getFrames(MultipleOption frameOpt) {
		Integer[] frames;
		
		if (frameOpt.getValues().length==1 && (Integer)frameOpt.getValues()[0]==0) {
			
			frames = new Integer[]{1,2,3,4,5,6};
			
		} else {
			
			frames = new Integer[frameOpt.count()];
			int counter = 0;
			for (int i = 0; i< frameOpt.count() ; i++) {
				
				Integer currentvalue = (Integer) frameOpt.getValue(i);
				
				if (currentvalue>0 && currentvalue<=6) {
					frames[counter++] = currentvalue;
				}
				
			}
			
			frames = Arrays.copyOf(frames, counter);
			
		}

		return frames;
	}

	private static Replicate getReplicate(NoOption cir) {
		Replicate replicate = null; 
				
		if (cir.isPresent()) {
			
			replicate = new ReplicateCircular(); 
						
		} else {
			
			replicate = new ReplicateLinear(); 
			
		}
		return replicate;
	}

	private static SequenceSource getSource(NoOption inFastaOpt, BufferedReader in) {
		SequenceSource source = null;
		
		if (inFastaOpt.isPresent()) {
			
			source = new FastaSequenceSource(in);
			
		} else {
			
			source = new TextSequenceSource(in);
			
		}
		return source;
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
		out.println("   -marks      : show the ATG and STOP positions for each orf found and each frame.");
		out.println("   -help       : Show this information.");
	}

	///////////////////
	// Public Interface
	
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
	 * @param isCircular True if the sequence is from a circular DNA molecule (i.e. a Plasmid). 
	 * 
	 * @return a list of String that contain all the ORFs in the sequence
	 */
	public static List<String> allOrfsInThisStrand(String sequence, int minSize, Replicate replicator, Integer[] frames) {

		OrfComposer composer = getOrfComposer(sequence, replicator, frames);
						
		return retriveORFs(composer.getSequence(), composer.getUnitlength(), composer.getMarks(), minSize);
		
//		return retriveORFs(rep.getSequence(), unitLength, marks, minSize);
		
	}

	public static OrfComposer getOrfComposer(String sequence, Replicate replicator, Integer[] frames) {
		
		int unitLength = sequence.length();
		
		int[] ATGs = OrfFinder.scanATG(sequence);
		
		int[] STOPs = OrfFinder.scanSTOP(sequence);
		
		Arrays.sort(ATGs);
		
		Arrays.sort(STOPs);
		
		Replicon rep = replicator.attempToReplicateSequence(sequence, unitLength, ATGs, STOPs);
		
		OrfMarks marks = separateByFrame(frames, rep.getATG(), rep.getStop());
		
		marks = excludeAdjacentATGorSTOP(marks);
		
		OrfComposer composer = new OrfComposer(marks, unitLength, sequence);
		
		return composer;
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
	 * Retrieve the ORFs from a sequence, given a list of ATGs and STOPs mark position.
	 * There is expected that there are not two ATGs or two STOPs adjacent in the list.
	 * Usually the method <code>excludeAdjacentATGorSTOP</code> is called before this.
	 *   
	 * @param sequence the sequence from which the ORFs will be extracted.  
	 * @param unitLength is the Sequence minimum unit length.
	 * @param ATGsAndSTOPsByFrame an <code>array</code> containing the ATGs and STOPs mark positions.
	 * @param ATGorStop is a list of boolean values that is a reference of which elements in ATGsAnsSTOPsByFrame list are ATG o STOP.
	 *        When ATGorSTOP[index] is true, then ATGsAnsSTOPsByFrame[index] is the position of an ATG.
	 *        When ATGorSTOP[index] is false, then ATGsAnsSTOPsByFrame[index] is the position of a STOP.  
	 * @param result is the array in which the results will be stored.
	 */
	protected static List<String> retriveORFs(String sequence, int unitLength, OrfMarks marks, int minSize) {
		
		List<Integer>[] ATGsAndSTOPsByFrame = marks.getATGsAndSTOPsByFrame();
		
		List<Boolean>[] ATGorStop = marks.getATGorStop();
		
		List<String> result = new ArrayList<String>();
		
		for (int cframe = 0; cframe<3;cframe++ ) {
			
			for (int i=0; i<ATGsAndSTOPsByFrame[cframe].size();i++) {
				// Iterate over all positions

				if (ATGorStop[cframe].get(i) && (i+1)<ATGsAndSTOPsByFrame[cframe].size()) {
					// The two conditions are:
					//    1 - The position analyzed if of and ATG
					//    2 - An Stop exits for this ATG 
					
					int atgpos = ATGsAndSTOPsByFrame[cframe].get(i);
					
					int endIndex = ATGsAndSTOPsByFrame[cframe].get(i+1);
					
					if (atgpos<unitLength && (endIndex - atgpos) >= minSize ) {
						// checks that the ATG belongs to the first repeating unit
						// checks that the length of the orf is greater or equal than minsize
						
						result.add(sequence.substring(atgpos, endIndex+3));
						
					}
					
				}
			
			}
			
		}
		return result;
	}
	
	/**
	 * Detect, in a list of ATG and STOP, two adjacent ATG or two adjacent STOP and keeps the lower one, removing the other.
	 * 
	 * @param ATGsAnsSTOPsByFrame is a list of ATG and STOP mark positions
	 * @param ATGorStop is a list of boolean values that is a reference of which elements in ATGsAnsSTOPsByFrame list are ATG o STOP.
	 *        When ATGorSTOP[index] is true, then ATGsAnsSTOPsByFrame[index] is the position of an ATG.
	 *        When ATGorSTOP[index] is false, then ATGsAnsSTOPsByFrame[index] is the position of a STOP.    
	 */
	protected static OrfMarks excludeAdjacentATGorSTOP( OrfMarks marks) {
			
		List<Integer>[] ATGsAnsSTOPsByFrame = marks.getATGsAndSTOPsByFrame();
		
		List<Boolean>[] ATGorStop = marks.getATGorStop();
		
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
		
		return marks;
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
	protected static OrfMarks separateByFrame(Integer[] frames, int[] ATGs, int[] STOPs) {
		// Separate atg and stops by frame
		
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
		
			for (int frame : frames) {
				
				int cframe = frame-1;
				
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
							ATGsAndSTOPsByFrame[cframe].add(currentATG);
							ATGorStop[cframe].add(true);
							atgIndex++;
							if (atgIndex<ATGs.length) currentATG = ATGs[atgIndex];
						} else
						if ( atgIndex==ATGs.length && StopIndex<STOPs.length)  {
							//Stop reach the end and ATGs do not.				
							ATGsAndSTOPsByFrame[cframe].add(currentSTOP);
							ATGorStop[cframe].add(false);
							StopIndex++;
							if (StopIndex<STOPs.length) currentSTOP = STOPs[StopIndex];
						} else
						// Stops nor ATGs reach the end.				
						if (currentATG<currentSTOP) {
							// current atg is lower and atg is in frame
							ATGsAndSTOPsByFrame[cframe].add(currentATG);
							atgIndex++;
							ATGorStop[cframe].add(true);
							if (atgIndex<ATGs.length) currentATG = ATGs[atgIndex];
							
						} else {
							// current stop is lower and stop is in frame
							ATGsAndSTOPsByFrame[cframe].add(currentSTOP);
							ATGorStop[cframe].add(false);
							StopIndex++;				
							if (StopIndex<STOPs.length) currentSTOP = STOPs[StopIndex];				
						}

					}

				}

		}
		
		OrfMarks result = new OrfMarks(ATGsAndSTOPsByFrame, ATGorStop);
		
		return result;

	}

}
