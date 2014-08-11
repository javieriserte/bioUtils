package seqManipulation.fastamanipulator.commands.modify;

import java.io.File;
import java.io.InputStream;
import java.io.PrintStream;

import cmdGA2.SingleArgumentOption;
import pair.Pair;

/**
 * Removes a set of columns from a MSA and keeps the rest.
 * 
 * 
 * @author javier
 *
 */
public class RemPositionsCommand extends KeepPositionsCommand {

	public RemPositionsCommand(InputStream inputstream, PrintStream output, SingleArgumentOption<File> option) {
		
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
