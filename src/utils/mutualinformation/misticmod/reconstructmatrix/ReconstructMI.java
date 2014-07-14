package utils.mutualinformation.misticmod.reconstructmatrix;

import io.onelinelister.OneLineListReader;
import io.resources.ResourceContentAsString;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import utils.mutualinformation.misticmod.datastructures.MI_Position;
import utils.mutualinformation.misticmod.datastructures.MI_PositionLineParser;
import utils.mutualinformation.misticmod.datastructures.MI_Position_MortemPrinter;
import utils.mutualinformation.misticmod.datastructures.MI_Position_Printer;
import cmdGA2.CommandLine;
import cmdGA2.MultipleArgumentOption;
import cmdGA2.NoArgumentOption;
import cmdGA2.OptionsFactory;
import cmdGA2.SingleArgumentOption;
import cmdGA2.returnvalues.InfileValue;
import cmdGA2.returnvalues.IntegerValue;

/**
 * Reads several MI_Data files and creates one that contains all the data.<br>
 * Given a data set of N proteins (each protein is P<sub>i</sub>, 
 * with 0 < i <= N), the program reads (N*(N+1)) files of MI data between all 
 * proteins, including every protein with itself (each MI data file corresponds 
 * to MI(P<sub>i</sub>,P<sub>j</sub>), with j >= i). Files must given in the 
 * right order, for data files for MI(P<sub>i</sub>,P<sub>j</sub>):
 * <ol>
 * <li> index j >= index i.
 * <li> first, order by i index.
 * <li> then, order data sets with the same index i, with the index j. 
 * </ol>
 * Residue enumeration for both proteins in every data file must start with 1.
 * The first residue of every MI pair must correspond to P<sub>i</sub>, and the 
 * second to P<sub>j</sub>, for a data file of MI(P<sub>i</sub>,P<sub>j</sub>).
 * 
 * @author Javier Iserte
 */
public class ReconstructMI {

	public static void main(String[] args) {

		////////////////////////////////////
		// Create Command Line
		CommandLine cmd = new CommandLine();
		////////////////////////////////////
		
		////////////////////////////////////
		// Add command line arguments
		MultipleArgumentOption<File> infilesOpt = new MultipleArgumentOption<>(cmd, "--files", ',',new ArrayList<File>() , new InfileValue()); 
	    MultipleArgumentOption<Integer> lenOpt = new MultipleArgumentOption<Integer>(cmd, "--lengths", ',', new ArrayList<Integer>(), new IntegerValue());
	    NoArgumentOption                helpOpt = new NoArgumentOption(cmd, "--help");
	    SingleArgumentOption<PrintStream> outOpt= OptionsFactory.createBasicPrintStreamArgument(cmd);
		////////////////////////////////////
	    
	    ////////////////////////////////////
	    // Read command line
	    cmd.readAndExitOnError(args);
	    ////////////////////////////////////
	    
	    ////////////////////////////////////
	    // Get Values from Command line
	    List<File> infiles = infilesOpt.getValues();
	    List<Integer> proteinLengths = lenOpt.getValues();
	    int numberOfProteins = proteinLengths.size();
	    PrintStream out = outOpt.getValue();
	    ////////////////////////////////////
	    
	    ////////////////////////////////////
	    // Check for help flag
	    if (helpOpt.isPresent()) {
	    	System.out.println(new ResourceContentAsString().readContents("help", ReconstructMI.class));
	    	System.exit(0);
	    }
	    ////////////////////////////////////
	    
	    ////////////////////////////////////
	    // Validate input data
	    if (infiles.size()!= numberOfProteins* (numberOfProteins+1)/2) {
	    	
	    	System.err.println("The number of files given and the number of proteins do not match.");
	    	System.exit(1);
	    	
	    }
	    ////////////////////////////////////
	    
	    ////////////////////////////////////
	    // Create reader for MI data files
	    OneLineListReader<MI_Position> reader = new OneLineListReader<>(new MI_PositionLineParser());
	    ////////////////////////////////////
	    
	    ////////////////////////////////////
	    // Create printer for MI data
	    MI_Position_Printer printer = new MI_Position_MortemPrinter();
	    ////////////////////////////////////
	    
	    ////////////////////////////////////
	    // Create an array with the starting point
	    // of each protein.
	    Integer[] startingPoint = new Integer[proteinLengths.size()];
	    int acumm=0;
	    for (int i = 0; i<startingPoint.length;i++) {
	    	
	    	startingPoint[i] = acumm;
	    	acumm = acumm + proteinLengths.get(i);
	    	
	    }
	    ////////////////////////////////////
	    
	    ////////////////////////////////////
	    // Create a data structure to store
	    // the entire matrix
	    MI_Position[][] newMatrix = new MI_Position[acumm][acumm];
	    ////////////////////////////////////
	    
	    ////////////////////////////////////
	    // Reorder the input data
	    int fileCounter = 0;
	    for (int i = 0; i<numberOfProteins;i++) {
	    	
	    	for (int j=i;j<numberOfProteins;j++) {
	    		
	    		File currentFile = infiles.get(fileCounter);
	    		fileCounter++;
	    		
	    		List<MI_Position> data = reader.read(currentFile);
	    		
	    		for (MI_Position mi_Position : data) {
					
	    			MI_Position newPos = new MI_Position(mi_Position.getPos1() + startingPoint[i], 
	    					                             mi_Position.getPos2() + startingPoint[j],
	    					                             mi_Position.getAa1(), 
	    					                             mi_Position.getAa2(),
	    					                             mi_Position.getMi(),
	    					                             mi_Position.getRaw_mi());
	    			newPos.setMean_mi(mi_Position.getMean_mi());
	    			newPos.setSd_mi(mi_Position.getSd_mi());
	    			
	    			newMatrix[mi_Position.getPos1() + startingPoint[i]-1][mi_Position.getPos2() + startingPoint[j]-1] = newPos; 
	    			
				}
	    		
	    	}
	    	
	    }
	    ////////////////////////////////////
		
	    
	    ////////////////////////////////////
	    // Print out the matrix result
	    for (int c=0; c<acumm-1;c++) {
	    	
		    for (int d=c+1; d<acumm;d++) {
		    	
		    	out.println(printer.print(newMatrix[c][d]));
		    	
		    }
	    	
	    }
	    ////////////////////////////////////
	}

}
