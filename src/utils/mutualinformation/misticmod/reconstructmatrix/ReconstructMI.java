package utils.mutualinformation.misticmod.reconstructmatrix;

import io.onelinelister.OneLineListReader;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import utils.mutualinformation.misticmod.MI_Position;
import utils.mutualinformation.misticmod.MI_PositionLineParser;
import utils.mutualinformation.misticmod.MI_Position_MortemPrinter;
import utils.mutualinformation.misticmod.MI_Position_Printer;
import cmdGA2.CommandLine;
import cmdGA2.MultipleArgumentOption;
import cmdGA2.OptionsFactory;
import cmdGA2.SingleArgumentOption;
import cmdGA2.returnvalues.InfileValue;
import cmdGA2.returnvalues.IntegerValue;

public class ReconstructMI {

	public static void main(String[] args) {

		////////////////////////////////////
		// Create Command Line
		CommandLine cmd = new CommandLine();
		////////////////////////////////////
		
		////////////////////////////////////
		// Add command line arguments
		MultipleArgumentOption<File> infilesOpt = new MultipleArgumentOption<>(cmd, "--files", ',',new ArrayList<File>() , new InfileValue()); 
	    SingleArgumentOption<Integer> nProtOpt = new SingleArgumentOption<Integer>(cmd, "--nprot", new IntegerValue(), 1);
	    SingleArgumentOption<Integer> sizeOpt = new SingleArgumentOption<Integer>(cmd, "--size", new IntegerValue(), 1);
	    MultipleArgumentOption<Integer> lenOpt = new MultipleArgumentOption<Integer>(cmd, "--lengths", ',', new ArrayList<Integer>(), new IntegerValue());
	    SingleArgumentOption<PrintStream> outOpt= OptionsFactory.createBasicPrintStreamArgument(cmd);
		////////////////////////////////////
	    
	    ////////////////////////////////////
	    // Read command line
	    cmd.readAndExitOnError(args);
	    ////////////////////////////////////
	    
	    ////////////////////////////////////
	    // Get Values from Command line
	    List<File> infiles = infilesOpt.getValues();
	    int numberOfProteins = nProtOpt.getValue();
	    List<Integer> proteinLengths = lenOpt.getValues();
	    int size =sizeOpt.getValue();
	    PrintStream out = outOpt.getValue();
	    ////////////////////////////////////
	    
	    ////////////////////////////////////
	    // Create data structure to store de result matrix
	    MI_Position[][] newMatrix = new MI_Position[size][size];
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
	    int fileCounter = 0;
	    for (int i = 0; i<numberOfProteins-1;i++) {
	    	
	    	for (int j=i+1;j<numberOfProteins;j++) {
	    		
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
	    			
	    			out.println(printer.print(newPos));
	    			
				}
	    		
	    	}
	    	
	    }
		
	}

}
