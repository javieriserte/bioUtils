package seqManipulation.fastamanipulator.commands.modify;

import io.onelinelister.OneLineListReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import pair.Pair;
import seqManipulation.fastamanipulator.commands.FastaCommand;
import cmdGA2.SingleArgumentOption;

public class ChangeDescriptionsCommand extends FastaCommand<SingleArgumentOption<File>> {

	public ChangeDescriptionsCommand(InputStream inputstream, PrintStream output, SingleArgumentOption<File> option) {
		super(inputstream, output, option);

	}

	@Override
	protected List<String> performAction() {
		
		File infile = this.getOption().getValue();
		
		List<Pair<String, String>> pairs = this.getSequences();
		
		List<String> result = new ArrayList<String>();
		
		try {
			
			List<String> newDescriptions = OneLineListReader.createOneLineListReaderForString().read(new BufferedReader(new FileReader(infile)));
			
			for (int i = 0; i<pairs.size(); i++) {
				
				result.add(">"+newDescriptions.get(i));
				
				result.add(pairs.get(i).getSecond());
				
			}
			
			
		} catch (IOException e) {
			
			e.printStackTrace();
			
		}   			
		
		return result;
		
	}

}
