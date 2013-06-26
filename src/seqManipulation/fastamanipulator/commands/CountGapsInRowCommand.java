package seqManipulation.fastamanipulator.commands;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import seqManipulation.GapToolbox;

import cmdGA.SingleOption;
import fileformats.fastaIO.Pair;

/***
 * Count the number of gaps in a given row of a sequence alignment
 * 
 * @author javier iserte
 *
 */
public class CountGapsInRowCommand extends FastaCommand<SingleOption> {

	private Integer rowToCounts;

	public CountGapsInRowCommand(InputStream inputstream, PrintStream output, SingleOption option) {
		
		super(inputstream, output, option);
		
		this.rowToCounts = (Integer) this.getOption().getValue();
		
	}

	@Override
	protected List<String> performAction() {
		
		List<Pair<String, String>> seqs = this.getSequences();
		
		List<String> results = new ArrayList<String>();
		
		GapToolbox gtb = new GapToolbox();
		
		ArrayList<Pair<String, String>> newalign = new ArrayList<Pair<String,String>>();
		
		newalign.add(seqs.get(this.rowToCounts-1));
				
		boolean[] keepers = gtb.getKeepers(newalign);
		
		Integer[] posWithGaps = gtb.getPositionsWithGaps(keepers);
		
		results.add(String.valueOf(posWithGaps.length));
		
		return results;
		
	}

}
