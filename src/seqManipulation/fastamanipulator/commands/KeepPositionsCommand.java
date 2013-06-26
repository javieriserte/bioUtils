package seqManipulation.fastamanipulator.commands;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import cmdGA.SingleOption;
import fileformats.fastaIO.Pair;

/**
 * Keeps a set of columns from a MSA and removes the rest.
 * 
 * 
 * @author javier
 *
 */
public class KeepPositionsCommand extends FastaCommand<SingleOption> {

	List<Integer> markedPositions;
	
	public KeepPositionsCommand(InputStream inputstream, PrintStream output, SingleOption option) {
		
		super(inputstream, output, option);
						
	}

	public List<Integer> readPositionsToKeep(File in) {
		
		List<Integer> positionsToKeep = new ArrayList<Integer>();
		
		if (in!=null && in.exists()) {

			String line = null;
					
			BufferedReader br;
					
			try {
						
				br = new BufferedReader(new FileReader(in));

				while ((line = br.readLine())!=null) {
							
					positionsToKeep.add(Integer.valueOf(line)); 
							
				}
				
			} catch (IOException e) {
				
				System.err.println("There was an error reading: "+in.getName());
				
			} 
			
		}
		
		return positionsToKeep;
		
	}

	@Override
	protected List<String> performAction() {

		List<Pair<String, String>> seqs = this.getSequences();
		
		List<String> results = new ArrayList<String>();
		
		File in = (File) this.getOption().getValue();
		
		this.markedPositions = readPositionsToKeep(in);
		
		for (Pair<String, String> pair : seqs) {
				
			StringBuilder newseq = new StringBuilder();
					
			iterateSequenceAndProcess(pair, newseq);
					
		results.add(">" + pair.getFirst() );
					
		results.add(newseq.toString());
					
		}
		
		return results;
		
	}

	public void iterateSequenceAndProcess(Pair<String, String> pair, StringBuilder newseq) {
		
		for (Integer integer : this.markedPositions) {
					
			newseq.append(pair.getSecond().charAt(integer));
					
		}
	}

}
