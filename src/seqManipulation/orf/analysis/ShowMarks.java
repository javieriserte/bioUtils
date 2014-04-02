package seqManipulation.orf.analysis;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import seqManipulation.orf.OrfFinder;
import seqManipulation.orf.datastructures.OrfComposer;
import seqManipulation.orf.replication.Replicate;

import pair.Pair;

public class ShowMarks extends OrfAnalysis {

	
	public ShowMarks(Replicate replicator, int minSize, Integer[] frames) {
			super();
			this.replicator= replicator;
			this.minSize = minSize;
			this.frames = frames;
		}
	
	@Override
	public void exportResults(PrintStream out, Pair<String, String> sequence) {
		
		List<List<Integer>> marks = getMarks(sequence.getSecond(), this.replicator, this.frames);
		
		exportMarks(out, sequence.getFirst(), marks);

	}
	
	public List<List<Integer>> getMarks(String sequence, Replicate replicator, Integer[] frames) {
		
		List<Pair<List<Integer>, String>> order = getFrameIterateOrder(this.frames, sequence);
		
		List<List<Integer>> result = new ArrayList<List<Integer>>();
		
		for (Pair<List<Integer>, String> pair : order) {
			
			Integer[] newarray = new Integer[pair.getFirst().size()];
			
			result.addAll(getStrandMarks(pair.getSecond(), replicator, pair.getFirst().toArray(newarray)));
			
		}
	
	return result;
	
}

	/**
	 * Extracts the ATGs and Stop positions from a sequence.
	 * Note that the ATG at position i of List index 0 and stop at position i of List index 1 
	 * (both are in the same frame) may no correspond to the same ORF.  
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
	private static List<List<Integer>> getStrandMarks(String sequence, Replicate replicator, Integer[] frames) {
		
		OrfComposer composer = OrfFinder.getOrfComposer(sequence, replicator, frames);
		
		List<List<Integer>> result = new ArrayList<List<Integer>>();
	
		for (int i=0; i<6;i++) result.add(new ArrayList<Integer>());
		
		for (int f=0; f<3;f++) {
			
			for (int i=0; i<composer.getMarks().getATGsAndSTOPsByFrame()[f].size();i++) {
				
				result.get(2*f + ((composer.getMarks().getATGorStop()[f].get(i))?0:1)).add(composer.getMarks().getATGsAndSTOPsByFrame()[f].get(i));
				
			}
			
		}
		
		return result;
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
		
	}

}
