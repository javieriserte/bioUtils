package seqManipulation.colpairfeq;

import io.onelinelister.OneLineListReader;
import io.resources.ResourceContentAsString;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import pair.Pair;
import cmdGA2.CommandLine;
import cmdGA2.MultipleArgumentOption;
import cmdGA2.NoArgumentOption;
import cmdGA2.OptionsFactory;
import cmdGA2.SingleArgumentOption;
import cmdGA2.returnvalues.InfileValue;
import cmdGA2.returnvalues.IntegerValue;
import fileformats.readers.AlignmentReadingResult;
import fileformats.readers.GenericAlignmentReader;

/**
 * ColPairFreq gets an alignment and two position in it an return a list of 
 * all pairs of residues of the alignment that corresponds to these positions
 * and the frequency of each pair. 
 * 
 * @author javier
 *
 */
public class ColPairFreq {

	public static void main(String[] args) {
		
		////////////////////////////////////////////////////////////////////////
		// Create Command Line Object
		CommandLine cmd = new CommandLine();
		////////////////////////////////////////////////////////////////////////
		
		////////////////////////////////////////////////////////////////////////
		// Add options to the command line
		SingleArgumentOption<InputStream> inOpt = OptionsFactory.createBasicInputStreamArgument(cmd);
		SingleArgumentOption<PrintStream> outOption = OptionsFactory.createBasicPrintStreamArgument(cmd);
		SingleArgumentOption<File> listOpt = new SingleArgumentOption<File>(cmd, "--list_file", new InfileValue(), null);
		MultipleArgumentOption<Integer> posOpt = new MultipleArgumentOption<Integer>(cmd, "--positions", ',', new ArrayList<Integer>(), new IntegerValue());
		NoArgumentOption helpOpt = new NoArgumentOption(cmd, "--help");
		////////////////////////////////////////////////////////////////////////
		
		////////////////////////////////////////////////////////////////////////
		// Parse command line
		cmd.readAndExitOnError(args);
		////////////////////////////////////////////////////////////////////////
		
		////////////////////////////////////////////////////////////////////////
		// Check For Help 
		if (helpOpt.isPresent()) {
			String helpText = new ResourceContentAsString().readContents("help", ColPairFreq.class);
			System.err.println(helpText);
			System.exit(0);
		}
		////////////////////////////////////////////////////////////////////////
		
		
		////////////////////////////////////////////////////////////////////////
		// Validate command line
		if (listOpt.isPresent() == posOpt.isPresent()) {
			
			System.err.println("--list_file and --positions can not be present or absent at the same time");
			System.exit(0);
			
		}
		////////////////////////////////////////////////////////////////////////
		
		////////////////////////////////////////////////////////////////////////
		// Get Values from Command line
		BufferedReader in = new BufferedReader(new InputStreamReader(inOpt.getValue()));
		PrintStream out = outOption.getValue();
		////////////////////////////////////////////////////////////////////////
		
		////////////////////////////////////////////////////////////////////////
		// Read Input Alignment 
		GenericAlignmentReader reader = new GenericAlignmentReader();
		List<AlignmentReadingResult> res = reader.read(in);
		List<Pair<String,String>> seqs = (!res.isEmpty() && res.get(0).successfulRead())?res.get(0).getAlignment():new ArrayList<Pair<String,String>>();
		////////////////////////////////////////////////////////////////////////
		
		////////////////////////////////////////////////////////////////////////
		// Get Positions to analyze
		List<Pair<Integer,Integer>> positions = getPositionsToAnalyze(listOpt,posOpt);
		////////////////////////////////////////////////////////////////////////
		
		////////////////////////////////////////////////////////////////////////
		// Get Frequencies for each pair of columns
		Map<Pair<Integer,Integer>, Map<String,Double>> frequencies = new HashMap<Pair<Integer,Integer>, Map<String,Double>>();
		
		for (Pair<Integer, Integer> pair : positions) {
			
			Map<String,Double> currentFreq  = new HashMap<String,Double>();
			
			int count = 0;
			
			for (Pair<String,String> seq : seqs) {
				
				String currentPair = String.valueOf(seq.getSecond().charAt(pair.getFirst()-1)) + String.valueOf(seq.getSecond().charAt(pair.getSecond()-1));

				if (!currentFreq.containsKey(currentPair)) {
					
					currentFreq.put(currentPair, 0d);
					
				}
				
				count++;
				
				currentFreq.put(currentPair, currentFreq.get(currentPair) +1d);
				
			}
			
			for (String currentPair : currentFreq.keySet()) {
				
				currentFreq.put(currentPair, currentFreq.get(currentPair) / count);
				
			}
			
			frequencies.put(pair, currentFreq);
			
		}
		////////////////////////////////////////////////////////////////////////
		
		////////////////////////////////////////////////////////////////////////
		// Print Out results
		String spacerLine="";
		for (Pair<Integer, Integer> pair : frequencies.keySet()) {
			
			String header = pair.toString() + " ";
			
			boolean first = true;
			
			out.print(spacerLine);
			
			spacerLine = "\n";

			////////////////////////////////////////////////////////////////////
			// Sort frequencies before printing out
			Map<String, Double> currentFrequencies = frequencies.get(pair);

			ValueComparator bvc =  new ValueComparator(currentFrequencies);

			TreeMap<String,Double> sortedFrequencies = new TreeMap<String,Double>(bvc);
			
			sortedFrequencies.putAll(currentFrequencies);
			////////////////////////////////////////////////////////////////////
			
			for (String currentPair : sortedFrequencies.keySet()) {
				
				out.println(header + currentPair + ": " + String.format(Locale.US,"%.4f", frequencies.get(pair).get(currentPair)));
				
				if (first) {
					
					header = header.replaceAll(".", " ");
					
					first = false;
					
				}
				
			} 
			
		}
		
		////////////////////////////////////////////////////////////////////////
		
	}

	private static List<Pair<Integer, Integer>> getPositionsToAnalyze(
			SingleArgumentOption<File> listOpt,
			MultipleArgumentOption<Integer> posOpt) {
		
		List<Pair<Integer, Integer>> result = new ArrayList<Pair<Integer, Integer>>();

		if (listOpt.isPresent()) {

			OneLineListReader<String> reader = OneLineListReader.createOneLineListReaderForString();
				
			List<String> lines = reader.read(listOpt.getValue());
			
			for (String line : lines) {
				
				String[] parts = line.split("\\s");
				
				if (parts.length==2) {
					
					result.add(new Pair<Integer, Integer>(Integer.valueOf(parts[0]), Integer.valueOf(parts[1])));
					
				}
				
			}
			
		} 
		else if (posOpt.isPresent()) {
			
			List<Integer> positions = posOpt.getValues();
			
			if (positions.size() % 2 == 0) {
				
				for (int i = 0; i< positions.size() ; i=i+2) {
				
					result.add(new Pair<Integer, Integer>(positions.get(i), positions.get(i+1)));
					
				}
				
			} else {
				
				System.err.println("An even number of positions must be passed.");
				
			}
			
		}
		
		return result;
		
	}
	
}

class ValueComparator implements Comparator<String> {

    Map<String, Double> base;
    public ValueComparator(Map<String, Double> base) {
        this.base = base;
    }

    // Note: this comparator imposes orderings that are inconsistent with equals.    
    public int compare(String a, String b) {
        if (base.get(a) >= base.get(b)) {
            return -1;
        } else {
            return 1;
        } // returning 0 would merge keys
    }
}
