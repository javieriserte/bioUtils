package seqManipulation.shuffle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import cmdGA2.CommandLine;
import cmdGA2.NoArgumentOption;
import cmdGA2.OptionsFactory;
import cmdGA2.SingleArgumentOption;
import fileformats.fastaIO.FastaMultipleReader;
import fileformats.fastaIO.FastaWriter;
import pair.Pair;
import math.random.FischerYatesShuffle;

/**
 * Given a sequence generates a new one that contains the same
 * characters of the original in different order. Gaps characters 
 * can be optionally treated in a special way, indicating that 
 * thay must remain in the same position as the original sequence. 
 * This option is implementation specific.
 * 
 * @author Javier Iserte 
 *
 */
public abstract class SequenceShuffler {

	///////////////////////
	// Public Interface
	/**
	 * This method is the only one in the public interface.
	 * Takes a sequence and returns it shuffled
	 *  
	 * @param sequence the input sequence to be shuffled
	 * @return a new sequence, that conatains the same characters
	 *         from the input sequence in an altered order.
	 */
	public String shuffle(String sequence) {

		//////////////////////////////
		// This method is implemented
		// as a template method
		// pattern. The first and the third
		// steps are subclass specific
		
		Character[] symbols = this.getSymbols(sequence);
		// Retrieves the symbols used in
		// the impit sequence
		
		this.shuffleSymbols(symbols);
		// Shuffles the symbol
		
		return this.compose(symbols, sequence);
		// Creates the new sequence
		// and returns it
		
	}
	
	////////////////////////
	// Protected Methods
	protected abstract Character[] getSymbols(String sequence);
	
	protected abstract String compose(Character[] symbols, String guide);
	
	protected void shuffleSymbols (Character[] symbols) {
		
		FischerYatesShuffle.shuffle(symbols.length, symbols);
		
	}
	
	////////////////////////
	// Main executable method
	public static void main(String[] args) {
		
		/////////////////////////
		// Create Command Line object
		CommandLine cmdline = new CommandLine();
		
		/////////////////////////
		// Adds Options to command line
		SingleArgumentOption<InputStream> inOpt = OptionsFactory.createBasicInputStreamArgument(cmdline);
		
		SingleArgumentOption<PrintStream> outOpt = OptionsFactory.createBasicPrintStreamArgument(cmdline);
		
		NoArgumentOption fixedGapOpt = new NoArgumentOption(cmdline, "--fixedGaps");
		
		NoArgumentOption helpOpt = new NoArgumentOption(cmdline, "--help");
		
		/////////////////////////////
		// Parse command line
		cmdline.readAndExitOnError(args);
		
		//////////////////////////////
		// Checks for help option
		if (helpOpt.isPresent()) {
			
			SequenceShuffler.printHelp();
			
			System.exit(1);
			
		}
		
		/////////////////////////////
		// Gets command line values
		BufferedReader in = new BufferedReader(new InputStreamReader( inOpt.getValue()));
		
		PrintStream out = outOpt.getValue();
		
		boolean fixedGap = fixedGapOpt.isPresent();
		
		////////////////////////////
		// Checks if --fixedGaps option
		// is present in the command line
		// and creates the corresponding 
		// sequence shuffler.
		SequenceShuffler seqShuffler = fixedGap ? 
				                       (new FixedGapsShuffler()):
				                       (new NoFixedShuffler());
			
		/////////////////////////////
        // Read the input sequences
        List<Pair<String, String>> sequences = (new FastaMultipleReader()).readBuffer(in);
        
        /////////////////////////////
        // Shuffle sequences 
        
        List<Pair<String,String>> shuffled = new ArrayList<>();
        
        for (Pair<String, String> sequence : sequences) {
			
        	shuffled.add(new Pair<String, String>(sequence.getFirst(), seqShuffler.shuffle(sequence.getSecond())));
        	
		}

        ////////////////////////////
        // and prints them out.
        FastaWriter writer = new FastaWriter();
        
    	writer.print(out, shuffled);
		
	}

	private static void printHelp() {
		
		InputStream helpStream = SequenceShuffler.class.getResourceAsStream("Help");
		
		PrintStream ps = new PrintStream(System.err);
		
		byte current = -1;

		try {
			
			while ((current = (byte) helpStream.read())!=-1) {
			
				ps.write(current);
			
			}
			
		} catch (IOException e) {
			
			System.err.println("There was an error trying to print the help.");
			
		}
		
	}
	
	
}
