package seqManipulation.fastamanipulator.commands;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import cmdGA.SingleOption;
import pair.Pair;

/**
 * Eliminates columns of a multiple sequence alignment that contains more than a threshold fraction of gaps.
 * 
 * @author javier Iserte
 *
 */
public class StripGappedColumnsByFreqCommand extends FastaCommand<SingleOption> {
	
	SingleOption reference;
	
	public StripGappedColumnsByFreqCommand(InputStream inputstream, PrintStream output, SingleOption option, SingleOption reference) {
		
		super(inputstream, output, option);
		
		this.setReference(reference);

	}

	@Override
	protected List<String> performAction() {

		double fr = (Float) this.getOption().getValue();
		
		List<Pair<String, String>> seqs = this.getSequences();
		
		File outreference = null;
		
		if (this.getReference().isPresent()) {
			
			outreference = (File) this.getReference().getValue();
			
		}
		
		return stripGappedColumnsCommand(seqs, fr, outreference);
		
	}
	
	private static List<String> stripGappedColumnsCommand(List<Pair<String, String>> seqs, double fr, File outreference) {
		// removes the columns of the alignment that contain a gap
		double[] gapFreq = new double[seqs.get(0).getSecond().length()];
		
		boolean[] keepers = new boolean[seqs.get(0).getSecond().length()];
		
		List<String> results = new ArrayList<String>();
		
		PrintStream ref = null;
		try {
			if (outreference!=null) {
				
				ref = new PrintStream(outreference);
			}
		} catch (FileNotFoundException e) {
			
			System.err.println("There was an error trying to open reference file: " +e.getMessage() );

			System.exit(1);
			
		}
		
		int N = seqs.size();
		
		for (Pair<String, String> seq : seqs) {
			
			for (int j = 0; j< seq.getSecond().length();j++) {
				
				gapFreq[j] = gapFreq[j] + ((seq.getSecond().charAt(j) == '-')?1:0);
				
			}
			
		}
		
		for (int j = 0; j< seqs.get(0).getSecond().length();j++) {
		
			keepers[j] = gapFreq[j]/N <= fr;
			
			if (!keepers[j] && outreference!=null) {
				
				ref.println(j);
			
			}
			
		}
		
		
		for (Pair<String, String> seq : seqs) {
			
			StringBuilder nseq = new StringBuilder();
			
			String oldSeq = seq.getSecond();
			
			for (int j = 0; j< oldSeq.length();j++) {
				
				if (keepers[j]) {
					
					nseq.append(oldSeq.charAt(j));
					
				}
				
			}
			
			results.add(">" + seq.getFirst());

			results.add(nseq.toString());
			
		}
		
		if (ref!=null) {
		
			ref.close();
		
		}
		
		return results;		
	}
	

	///////////////////////////
	// Getters And Setters

	public SingleOption getReference() {
		return reference;
	}

	public void setReference(SingleOption reference) {
		this.reference = reference;
	}

	
}
