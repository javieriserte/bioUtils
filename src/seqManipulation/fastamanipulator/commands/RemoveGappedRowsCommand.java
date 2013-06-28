package seqManipulation.fastamanipulator.commands;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import cmdGA.NoOption;
import fileformats.fastaIO.Pair;

public class RemoveGappedRowsCommand extends FastaCommand<NoOption> {

	public RemoveGappedRowsCommand(InputStream inputstream, PrintStream output, NoOption option) {
		
		super(inputstream, output, option);

	}

	@Override
	protected List<String> performAction() {
		
		RemoveGappedRows rgr = new RemoveGappedRows();
		
		List<Pair<String, String>> seqs = this.getSequences();
		
		List<String> results = new ArrayList<String>();
				
		seqs = rgr.removeAllGapRows(seqs);
		
		for (Pair<String, String> pair : seqs) {
			
			results.add(">"+pair.getFirst());
			
			results.add(pair.getSecond());
			
		}
		
		return results;
	}

}
