package fileformats.conversion;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.List;

import cmdGA.NoOption;
import cmdGA.Parser;
import cmdGA.SingleOption;
import cmdGA.exceptions.IncorrectParameterTypeException;
import cmdGA.parameterType.InFileParameter;
import cmdGA.parameterType.PrintStreamParameter;
import fileformats.genBankIO.GenBankFormatException;
import fileformats.genBankIO.GenBankReader;
import fileformats.genBankIO.GenBankRecord;

public class GenBankToFasta {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

	// Step One : Create the parser
		
		Parser parser = new Parser();
		
		// Step Two : Add Command Line Options
		
		SingleOption inputFileOpt = new SingleOption(parser, null, "-infile", InFileParameter.getParameter());
		
		SingleOption printStreamOpt = new SingleOption(parser, System.out, "-outfile", PrintStreamParameter.getParameter());
		
		NoOption countOpt = new NoOption(parser, "-count"); 

		// Step Three : Try to parse the command line
		
		
		
		try {
			parser.parseEx(args);
		} catch (IncorrectParameterTypeException e) {
			System.err.println("There was a error parsing the command line");
			System.exit(1);
		}

		File in = (File) inputFileOpt.getValue();
		
		PrintStream out = (PrintStream) printStreamOpt.getValue();
		
		if (in== null || !in.exists()) System.exit(1);
		
		if(countOpt.isPresent()) {
			
			try {
				List<GenBankRecord> a = GenBankReader.readFile(in);
				out.println(a.size());
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (GenBankFormatException e) {
				e.printStackTrace();
			}
			
			
		} else {
			
			try {
				List<GenBankRecord> a = GenBankReader.readFile(in);
				
				for (GenBankRecord genBankRecord : a) {
					out.println(">" + genBankRecord.getHeader().getDefinition());
					out.println(genBankRecord.getOrigin().getSequence());

				}
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (GenBankFormatException e) {
				e.printStackTrace();
			}
			
			
		}
		
	}

}
