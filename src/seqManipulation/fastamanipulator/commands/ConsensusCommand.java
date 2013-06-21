package seqManipulation.fastamanipulator.commands;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;

import cmdGA.NoOption;

public class ConsensusCommand extends FastaCommand<NoOption> {

	public ConsensusCommand(InputStream inputstream, PrintStream output, NoOption option) {
		
		super(inputstream, output, option);
		
	}

	@Override
	protected List<String> performAction() {
		
		return null;
		
	}

}
