package utils.mutualinformation.misticmod.sepsubnets;

import io.onelinelister.OneLineListReader;
import io.resources.ResourceContentAsString;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import utils.mutualinformation.misticmod.MI_Position;
import utils.mutualinformation.misticmod.MI_PositionLineParser;
import cmdGA2.CommandLine;
import cmdGA2.NoArgumentOption;
import cmdGA2.OptionsFactory;
import cmdGA2.SingleArgumentOption;
import cmdGA2.returnvalues.IntegerValue;
import cmdGA2.returnvalues.OutfileValue;


/**
 * Read a MI data file and generates several files.
 * Each file corresponds to a subnet of the MI data. 
 * 
 * @author Javier Iserte
 *
 */
public class SeparateMISubnets {

	public static void main(String[] args) {

		
		/////////////////////////////////////////////
		// Create command line
		CommandLine cmd = new CommandLine();
		/////////////////////////////////////////////
		
		/////////////////////////////////////////////
		// Add commmand line options
		SingleArgumentOption<InputStream> inOpt        = OptionsFactory.createBasicInputStreamArgument(cmd);
		SingleArgumentOption<String>      outPrefixOpt = OptionsFactory.createBasicStringArgument(cmd, "--prefix", "mi_data_");
		SingleArgumentOption<String>      outSufixOpt  = OptionsFactory.createBasicStringArgument(cmd, "--suffix", "_subnet");
		SingleArgumentOption<File>        outOpt       = new SingleArgumentOption<File>(cmd, "--outfolder", new OutfileValue(),  new File(""));
		SingleArgumentOption<Integer>     removeOpt    = new SingleArgumentOption<Integer>(cmd, "--remove", new IntegerValue(), 0);
		NoArgumentOption                  helpOpt      = new NoArgumentOption(cmd, "--help");
		/////////////////////////////////////////////
		
		/////////////////////////////////////////////
		// Parse command line
		cmd.readAndExitOnError(args);		
		/////////////////////////////////////////////
		
		/////////////////////////////////////////////
		// Get Command line arguments
		BufferedReader in = new BufferedReader(new InputStreamReader(inOpt.getValue()));
		String prefix = outPrefixOpt.getValue();
		String sufix  = outSufixOpt.getValue();
		File out = outOpt.getValue();
		Integer removeCutOff = removeOpt.getValue();
		/////////////////////////////////////////////
		
		/////////////////////////////////////////////
		// Print Help
		if (helpOpt.isPresent()) {
			
			System.err.println(new ResourceContentAsString().readContents("help"));
			
			System.exit(0);
			
		}
		/////////////////////////////////////////////
		
		try {
			
			//////////////////////////////////////////////
			// Read MI Data
			OneLineListReader<MI_Position> reader = new OneLineListReader<MI_Position>(new MI_PositionLineParser());
			List<MI_Position> positions = reader.read(in);
			//////////////////////////////////////////////
			
			//////////////////////////////////////////////
			// Create data structures for edges
			// This will keep the results
			List<Set<MI_Position>> edgesBySubnet = new ArrayList<Set<MI_Position>>();
			/////////////////////////////////////////////
			
			/////////////////////////////////////////////////
			// Separates the subnets
			MiSubnetSplitter splitter = new MiSubnetSplitter();
			edgesBySubnet = splitter.split(positions);
			///////////////////////////////////////////////
			
			///////////////////////////////////////////////
			// Filter small subnets by its size in edges
			edgesBySubnet = new MiSubnetFilterBySize(removeCutOff).filter(edgesBySubnet);
			///////////////////////////////////////////////
			
			///////////////////////////////////////////////
			// Finally, exports the results
			exportResults(prefix, sufix, out, edgesBySubnet);
			///////////////////////////////////////////////
			
			
		} catch (Exception e) {
			
			System.err.println("There was an error while reading MI data: "+ e .getMessage());
			
		}

	}


	private static void exportResults(String prefix, String sufix, File out,
			List<Set<MI_Position>> edgesBySubnet) throws FileNotFoundException {
		
		for (int i = 0; i<edgesBySubnet.size(); i++) {
			
			Set<MI_Position> currentEdgeSet = edgesBySubnet.get(i);
			
			File currentFile = new File(out.getAbsoluteFile(),prefix+String.valueOf(i)+sufix );
			
			PrintStream ps = new PrintStream(currentFile);
			
			for (MI_Position pos : currentEdgeSet) {
				
				ps.println(pos);
				
			}
			
			ps.close();
			
		}
	}

}
