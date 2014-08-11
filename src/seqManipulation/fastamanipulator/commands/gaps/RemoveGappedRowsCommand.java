package seqManipulation.fastamanipulator.commands.gaps;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import cmdGA2.NoArgumentOption;
import pair.Pair;
import seqManipulation.fastamanipulator.commands.FastaCommand;

public class RemoveGappedRowsCommand extends FastaCommand<NoArgumentOption> {

	public RemoveGappedRowsCommand(InputStream inputstream, PrintStream output, NoArgumentOption option) {
		
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
