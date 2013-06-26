package seqManipulation.fastamanipulator.commands;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import cmdGA.NoOption;

/**
 * Generates help to build a custom genetic code file.
 * 
 * @author javier iserte
 *
 */
public class GeneticCodeHelpCommand extends FastaCommand<NoOption> {

	public GeneticCodeHelpCommand(InputStream inputstream, PrintStream output, NoOption option) {
		
		super(inputstream, output, option);
		
	}

	@Override
	protected List<String> performAction() {
		
		List<String> results = new ArrayList<String>();
		results.add("The File Format for the genetic code is like:");
		results.add("");
		results.add("A, GCT, GCC, GCA, GCG");
		results.add("C, TGT, TGC");
		results.add("D, GAT, GAC");
		results.add("E, GAA, GAG");
		results.add("F, TTT, TTC");
		results.add("G, GGT, GGC, GGA, GGG");
		results.add("H, CAT, CAC");
		results.add("I, ATT, ATC, ATA");
		results.add("K, AAA, AAG");
		results.add("L, TTA, TTG, CTT, CTC, CTA, CTG");
		results.add("M, ATG");
		results.add("N, AAT, AAC");
		results.add("P, CCT, CCC, CCA, CCG");
		results.add("Q, CAA, CAG");
		results.add("R, CGT, CGC, CGA, CGG, AGA, AGG");
		results.add("S, TCT, TCC, TCA, TCG, AGT, AGC");
		results.add("T, ACT, ACC, ACA, ACG");
		results.add("V, GTT, GTC, GTA, GTG");
		results.add("W, TGG");
		results.add("Y, TAT, TAC");
		results.add("*, TAA, TGA, TAG");
		
		return results;
		
	}

}
