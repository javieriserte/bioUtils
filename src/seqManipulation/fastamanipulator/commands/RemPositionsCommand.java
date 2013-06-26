package seqManipulation.fastamanipulator.commands;

import java.io.InputStream;
import java.io.PrintStream;

import cmdGA.SingleOption;
import fileformats.fastaIO.Pair;

/**
 * Removes a set of columns from a MSA and keeps the rest.
 * 
 * 
 * @author javier
 *
 */
public class RemPositionsCommand extends KeepPositionsCommand {

	public RemPositionsCommand(InputStream inputstream, PrintStream output, SingleOption option) {
		
		super(inputstream, output, option);

	}
	
	public void iterateSequenceAndProcess(Pair<String, String> pair, StringBuilder newseq) {
	
		for(int i = 0; i < pair.getSecond().length(); i++) {
			
			if (!this.markedPositions.contains(i)) { 
			
				newseq.append(pair.getSecond().charAt(i));
			
			}
			
		}
		
	}
	
}
