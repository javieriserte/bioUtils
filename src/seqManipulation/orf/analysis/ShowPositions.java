package seqManipulation.orf.analysis;

import java.io.PrintStream;
import java.util.List;

import seqManipulation.orf.replication.Replicate;

import fileformats.fastaIO.Pair;

public class ShowPositions extends OrfAnalysis{

	
	public ShowPositions(Replicate replicator, int minSize, Integer[] frames) {
		super();
		this.replicator= replicator;
		this.minSize = minSize;
		this.frames = frames;
	}
	
	
	@Override
	public void exportResults(PrintStream out, Pair<String, String> sequence) {

		ShowMarks sm = new ShowMarks(this.replicator,this.minSize, this.frames);
		
		List<List<Integer>> marks = sm.getMarks(sequence.getSecond(), this.replicator, this.frames);
		
		
		this.printoutPositions(out,marks, this.minSize);
		
	}

	private void printoutPositions(PrintStream out,List<List<Integer>> marks, int minSize) {
		
		for(int i=0; i<12;i=i+2) {
			
			if (marks.get(i+1).get(0) < marks.get(i).get(0)) marks.get(i+1).remove(0);
			
			while (marks.get(i).size()>0 && marks.get(i+1).size()>0) {

				int atg = marks.get(i).remove(0);
				
				int stop = marks.get(i+1).remove(0);
				
				if (stop-atg>=minSize) {
				
					StringBuilder sb = new StringBuilder();

					int frame = ((int)i/2) + 1;
					
					sb.append("Frame " + String.valueOf(frame) + " ");
				
					sb.append("["+atg +" - "+ stop +"] " + String.valueOf(stop - atg));
					
					out.println(sb.toString());
				
				}
				
			}
			
		}
		
	}

}
