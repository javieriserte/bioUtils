package seqManipulation.orf.analysis;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import seqManipulation.complementary.Complementary;
import seqManipulation.orf.replication.Replicate;

import fileformats.fastaIO.Pair;

public abstract class OrfAnalysis {
	
	protected Replicate replicator;
	
	protected int minSize;
	
	protected Integer[] frames;

	public abstract void exportResults(PrintStream out, Pair<String, String> sequence);
	
	protected List<Pair<List<Integer>,String>> getFrameIterateOrder(Integer[] frames, String sequence) {
		
		List<Integer> fwframe = new ArrayList<Integer>();
		List<Integer> rvframe = new ArrayList<Integer>();
		List<List<Integer>> framelists = new ArrayList<List<Integer>>();
		
		List<Pair<List<Integer>,String>> order = new ArrayList<Pair<List<Integer>,String>>(); 
		
		framelists.add(fwframe);
		framelists.add(rvframe);
		
		for (int frame : frames) {
			
			if (frame<3) fwframe.add(frame);
			else rvframe.add(frame-3);
		}
		
		
		for (List<Integer> framelist : framelists) {
			
			if (framelist==rvframe) sequence = Complementary.reverseComplementary(sequence);
			
			if (framelist.size() > 0) {
				
				order.add(new Pair<List<Integer>, String>(framelist, sequence));
				
			}
			
		}
		
		return order;
	}
	
	

}
