package utils.mutualinformation.misticmod.splitdata;

import io.bufferreaders.UncommenterBufferedReader;
import io.onelinelister.OneLineListReader;
import io.resources.ResourceContentAsString;

import java.util.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

import utils.mutualinformation.misticmod.datastructures.MI_Position;
import utils.mutualinformation.misticmod.datastructures.MI_PositionLineParser;
import utils.mutualinformation.misticmod.datastructures.MI_Position_MortemPrinter;

import cmdGA2.CommandLine;
import cmdGA2.MultipleArgumentOption;
import cmdGA2.NoArgumentOption;
import cmdGA2.OptionsFactory;
import cmdGA2.SingleArgumentOption;
import cmdGA2.returnvalues.IntegerValue;
import cmdGA2.returnvalues.OutfileValue;

public class MIDataSplitterCli {

	public static void main(String[] args) {

		////////////////////////////////////////////////////////////////////////
		// Create Command line object
		CommandLine cmd = new CommandLine();
		////////////////////////////////////////////////////////////////////////
		
		////////////////////////////////////////////////////////////////////////
		// Add options to the command line
		SingleArgumentOption<InputStream> inOpt = OptionsFactory.createBasicInputStreamArgument(cmd);
		MultipleArgumentOption<File> outOpt = new MultipleArgumentOption<File>(cmd, "--out", ',', null, new OutfileValue());
		SingleArgumentOption<Integer> cutOpt = new SingleArgumentOption<Integer>(cmd, "--cut", new IntegerValue(), 0);
		NoArgumentOption helpOpt = new NoArgumentOption(cmd, "--help");
		////////////////////////////////////////////////////////////////////////

		////////////////////////////////////////////////////////////////////////
		// Parse command line
		cmd.readAndExitOnError(args);
		////////////////////////////////////////////////////////////////////////
		
        ////////////////////////////////////////////////////////////////////////
		// Print Help
		if (helpOpt.isPresent()) {
			
			System.err.println(new ResourceContentAsString().readContents("help",MIDataSplitterCli.class));
			
			System.exit(0);
			
		}
        ////////////////////////////////////////////////////////////////////////
		
		////////////////////////////////////////////////////////////////////////
		// Get values from command line
		BufferedReader in = new UncommenterBufferedReader(new InputStreamReader(inOpt.getValue()));
		List<File> out= outOpt.getValues();
		int cutPosition = cutOpt.getValue(); 
		////////////////////////////////////////////////////////////////////////
		
		////////////////////////////////////////////////////////////////////////
		// Read Mi Data
		OneLineListReader<MI_Position> reader = new OneLineListReader<MI_Position>(new MI_PositionLineParser());
		List<MI_Position> data = null;
		try {
			data = reader.read(in);
		} catch (IOException e1) {
			System.out.println("There was a problem trying to read data");
			System.exit(1);
		}
		////////////////////////////////////////////////////////////////////////
		
		////////////////////////////////////////////////////////////////////////
		// Create splitter object
		MIDataSplitter splitter = new MIDataSplitter();
		splitter.splitAfter(cutPosition, data);
		////////////////////////////////////////////////////////////////////////
		
		////////////////////////////////////////////////////////////////////////
		// Export results
		List<MI_Position> first = splitter.getFirstIntraMiDataList();
		List<MI_Position> second = splitter.getSecondIntraMiDataList();
		List<MI_Position> inter = splitter.getInterMiDataList();
		try {
			MIDataSplitterCli.export(out.get(0),first);
			MIDataSplitterCli.export(out.get(1),second);
			MIDataSplitterCli.export(out.get(2),inter);
		} catch (FileNotFoundException e) {
			System.out.println("There was a problem trying to export results");
			System.exit(1);

		}
		////////////////////////////////////////////////////////////////////////

		
	}

	private static void export(File file, List<MI_Position> data) throws FileNotFoundException {

		PrintStream ps =new PrintStream(file );
		
		MI_Position_MortemPrinter printer= new MI_Position_MortemPrinter();
		
		for (MI_Position pair : data) {
			
			ps.println(printer.print(pair));
			
		}
		ps.flush();
		ps.close();
		
	}

}
