package seqManipulation.fastamanipulator.commands;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.List;

import cmdGA.Option;
import fileformats.fastaIO.FastaMultipleReader;
import fileformats.fastaIO.Pair;

public abstract class FastaCommand <Op extends Option> {

	////////////////////////////
	// Instance Variables
	InputStream inputstream;
	
	PrintStream output;
	
	Op option;
	
	
	///////////////////////////
	// Constructor
	public FastaCommand(InputStream inputstream, PrintStream output, Op option) {
		super();
		this.inputstream = inputstream;
		this.output = output;
		this.option = option;
	}
	
	///////////////////////////
	// Public Interface
	public void execute() {

		List<String> result = this.performAction();
		
		for (String resultLine : result) {
			
			this.getOutput().println(resultLine);
			
		}
		
	}

	///////////////////////
	// Getters And Setters
	public InputStream getInputstream() {
		return inputstream;
	}

	public void setInputstream(InputStream inputstream) {
		this.inputstream = inputstream;
	}

	public PrintStream getOutput() {
		return output;
	}

	public void setOutput(PrintStream output) {
		this.output = output;
	}

	public Op getOption() {
		return option;
	}

	public void setOption(Op option) {
		this.option = option;
	}
	
	/////////////////////////////
	// Private Methods
	
	protected abstract List<String> performAction();

	protected List<Pair<String,String>> getSequences() {
		
		FastaMultipleReader fmr = new FastaMultipleReader();
		
		BufferedReader in = new BufferedReader(new InputStreamReader(this.getInputstream()));
		
		return fmr.readBuffer(in); 
		
	}
	
	
}
