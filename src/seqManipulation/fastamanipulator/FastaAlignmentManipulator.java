package seqManipulation.fastamanipulator;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import seqManipulation.fastamanipulator.commands.AppendCommand;
import seqManipulation.fastamanipulator.commands.AppendManyCommand;
import seqManipulation.fastamanipulator.commands.CalculateMICommand;
import seqManipulation.fastamanipulator.commands.ChangeDescriptionsCommand;
import seqManipulation.fastamanipulator.commands.ComplementaryCommand;
import seqManipulation.fastamanipulator.commands.ConcatenateCommand;
import seqManipulation.fastamanipulator.commands.CountCommand;
import seqManipulation.fastamanipulator.commands.CountGapsInRowCommand;
import seqManipulation.fastamanipulator.commands.DeInterleaveCommand;
import seqManipulation.fastamanipulator.commands.DefinitionsCommand;
import seqManipulation.fastamanipulator.commands.DegapCommand;
import seqManipulation.fastamanipulator.commands.ExtractByTitleCommand;
import seqManipulation.fastamanipulator.commands.ExtractCommand;
import seqManipulation.fastamanipulator.commands.FastaCommand;
import seqManipulation.fastamanipulator.commands.FilterSequenceContainingCommand;
import seqManipulation.fastamanipulator.commands.FilterSequenceStartingWithCommand;
import seqManipulation.fastamanipulator.commands.FilterSizeGrEqThanCommand;
import seqManipulation.fastamanipulator.commands.FilterSizeSmEqThanCommand;
import seqManipulation.fastamanipulator.commands.FilterTitleContainingCommand;
import seqManipulation.fastamanipulator.commands.FilterTitlesContainingCommand;
import seqManipulation.fastamanipulator.commands.FlushEndsCommand;
import seqManipulation.fastamanipulator.commands.GapFrequencyInColumnsCommand;
import seqManipulation.fastamanipulator.commands.GeneticCodeHelpCommand;
import seqManipulation.fastamanipulator.commands.HelpCommand;
import seqManipulation.fastamanipulator.commands.IdentityMatrixCommand;
import seqManipulation.fastamanipulator.commands.IdentityValuesCommand;
import seqManipulation.fastamanipulator.commands.KeepPositionsCommand;
import seqManipulation.fastamanipulator.commands.LengthCommand;
import seqManipulation.fastamanipulator.commands.LengthsCommand;
import seqManipulation.fastamanipulator.commands.MDSCommand;
import seqManipulation.fastamanipulator.commands.PadWithGapCommand;
import seqManipulation.fastamanipulator.commands.PickRandomCommand;
import seqManipulation.fastamanipulator.commands.RandomBackTranslationCommand;
import seqManipulation.fastamanipulator.commands.ReconstructDottedAlignmentCommand;
import seqManipulation.fastamanipulator.commands.RemPositionsCommand;
import seqManipulation.fastamanipulator.commands.RemoveCommand;
import seqManipulation.fastamanipulator.commands.RemoveGappedRowsCommand;
import seqManipulation.fastamanipulator.commands.ReplaceUncommonChars;
import seqManipulation.fastamanipulator.commands.SliceCommand;
import seqManipulation.fastamanipulator.commands.StripGappedColumnsByFreqCommand;
import seqManipulation.fastamanipulator.commands.StripGappedColumnsCommand;
import seqManipulation.fastamanipulator.commands.TranslateCommand;
import seqManipulation.fastamanipulator.commands.TranslateWithCommand;
import seqManipulation.fastamanipulator.commands.VersionCommand;
import cmdGA.MultipleOption;
import cmdGA.NoOption;
import cmdGA.Option;
import cmdGA.Parser;
import cmdGA.SingleOption;
import cmdGA.exceptions.IncorrectParameterTypeException;
import cmdGA.parameterType.FloatParameter;
import cmdGA.parameterType.InFileParameter;
import cmdGA.parameterType.InputStreamParameter;
import cmdGA.parameterType.IntegerParameter;
import cmdGA.parameterType.OutFileParameter;
import cmdGA.parameterType.PrintStreamParameter;
import cmdGA.parameterType.StringParameter;

public class FastaAlignmentManipulator {

	public static final String VERSION = "0.1.1";
	
	private static final String DEF = "-def";
	private static final String COUNT = "-count";
	private static final String EXTRACT = "-extract";
	private static final String OUTFILE = "-outfile";
	private static final String INFILE = "-infile";
	

	/**
	 * A simple program to manipulate Alignments given in fasta format.
	 * 
	 * @param args
	 */
	public static void main(String[] args)  {
		
		// Step One : Create the parser
		
		long time0 = System.currentTimeMillis();
		
		Parser parser = new Parser();
		
		// Step Two : Add Command Line Options
		
		SingleOption inputStreamOpt = new SingleOption(parser, System.in, INFILE, InputStreamParameter.getParameter());
		
		SingleOption printStreamOpt = new SingleOption(parser, System.out, OUTFILE, PrintStreamParameter.getParameter());
		
		NoOption debugOpt = new NoOption(parser, "-debug");

		List<FastaCommand<? extends Option>> uniqueCommands = new ArrayList<FastaCommand<? extends Option>>();

		NoOption invertFilterOpt = new NoOption(parser, "-inverseFilter");
		
		uniqueCommands.add(new ExtractCommand(null, null, new MultipleOption(parser, 0, EXTRACT, ' ', IntegerParameter.getParameter())));

		NoOption countOpt = new NoOption(parser, COUNT); 
		FastaCommand<NoOption> countCommand = new CountCommand(null, null, countOpt);
		uniqueCommands.add(countCommand);
		NoOption defOpt = new NoOption(parser, DEF); 
		FastaCommand<NoOption> defCommand = new DefinitionsCommand(null, null, defOpt);
		uniqueCommands.add(defCommand);
		NoOption helpOpt = new NoOption(parser, "-help");
		FastaCommand<NoOption> helpCommand = new HelpCommand(null, null, helpOpt);

		NoOption versionOpt = new NoOption(parser, "-ver");
		FastaCommand<NoOption> versionCommand = new VersionCommand(null, null, versionOpt);
		uniqueCommands.add(versionCommand);
		
		uniqueCommands.add(new AppendCommand(null, null, new SingleOption(parser, null, "-append", InFileParameter.getParameter()))); 

		uniqueCommands.add(new LengthCommand(null, null, new NoOption(parser, "-length")));

		uniqueCommands.add(new LengthsCommand(null, null, new NoOption(parser, "-lengths")));

		uniqueCommands.add(new ConcatenateCommand(null, null, new MultipleOption(parser, null, "-concatenate", ',', InFileParameter.getParameter())));

		uniqueCommands.add(new SliceCommand(null, null, new MultipleOption(parser, null, "-slice", ',', IntegerParameter.getParameter())));

		uniqueCommands.add(new DegapCommand(null, null, new NoOption(parser, "-degap")));

		uniqueCommands.add(new TranslateCommand(null, null, new NoOption(parser, "-translate")));

		uniqueCommands.add(new TranslateWithCommand(null, null, new SingleOption(parser, null,"-translateWith",InFileParameter.getParameter())));

		NoOption geneticCodeHelpOpt = new NoOption(parser, "-genCodeHelp");
		GeneticCodeHelpCommand geneticCodeHelpCommand = new GeneticCodeHelpCommand(null, null, geneticCodeHelpOpt);
		uniqueCommands.add(geneticCodeHelpCommand);

		uniqueCommands.add(new StripGappedColumnsCommand(null, null, new NoOption(parser, "-stripGappedColumns")));

		uniqueCommands.add(new StripGappedColumnsByFreqCommand(null, null, new SingleOption(parser,null, "-stripGappedColFr", FloatParameter.getParameter()), new SingleOption(parser, null, "-reference", OutFileParameter.getParameter())));

		uniqueCommands.add(new FlushEndsCommand(null, null, new NoOption(parser, "-flush")));

		uniqueCommands.add(new RandomBackTranslationCommand(null, null, new NoOption(parser, "-randomRT")));

		uniqueCommands.add(new CalculateMICommand(null, null, new NoOption(parser, "-MI")));

		uniqueCommands.add(new ReconstructDottedAlignmentCommand(null, null, new SingleOption(parser, 0 ,"-recFromCon", IntegerParameter.getParameter())));

		uniqueCommands.add(new FilterSizeGrEqThanCommand(null, null, new SingleOption(parser, null, "-fGrTh", IntegerParameter.getParameter()), invertFilterOpt));

		uniqueCommands.add(new FilterSizeSmEqThanCommand(null, null, new SingleOption(parser, null, "-fSmTh", IntegerParameter.getParameter()), invertFilterOpt));

		uniqueCommands.add(new FilterSequenceStartingWithCommand(null, null, new SingleOption(parser, null, "-fStartWith", StringParameter.getParameter()), invertFilterOpt));

		uniqueCommands.add(new FilterSequenceContainingCommand(null, null, new SingleOption(parser, null, "-contains", StringParameter.getParameter()), invertFilterOpt));

		uniqueCommands.add(new ComplementaryCommand(null, null, new NoOption(parser, "-comp")));

		uniqueCommands.add(new IdentityValuesCommand(null, null, new NoOption(parser, "-idValues")));

		uniqueCommands.add(new IdentityMatrixCommand(null, null, new NoOption(parser, "-idMatrix")));

		uniqueCommands.add(new DeInterleaveCommand(null, null, new NoOption(parser, "-deInter")));

		uniqueCommands.add(new MDSCommand(null, null, new SingleOption(parser,2, "-mds", IntegerParameter.getParameter())));

		uniqueCommands.add(new PickRandomCommand(null, null, new SingleOption(parser, 1, "-pick", IntegerParameter.getParameter())));

		uniqueCommands.add(new FilterTitleContainingCommand(null, null, new SingleOption(parser, 1, "-title", StringParameter.getParameter()), invertFilterOpt));

		uniqueCommands.add(new FilterTitlesContainingCommand(null, null, new MultipleOption(parser, 1, "-titles", ',',StringParameter.getParameter()), invertFilterOpt));

		uniqueCommands.add(new ExtractByTitleCommand(null, null, new MultipleOption(parser, 1, "-extract_titles", ',',StringParameter.getParameter())));
		
		uniqueCommands.add(new KeepPositionsCommand(null, null, new SingleOption(parser, 1, "-keeppos", InFileParameter.getParameter())));

		uniqueCommands.add(new RemPositionsCommand(null, null, new SingleOption(parser, 1, "-rempos", InFileParameter.getParameter())));

		uniqueCommands.add(new RemoveGappedRowsCommand(null, null, new NoOption(parser, "-remGapRows")));

		uniqueCommands.add(new CountGapsInRowCommand(null, null, new SingleOption(parser,1, "-countGapsIn", IntegerParameter.getParameter())));

		uniqueCommands.add(new AppendManyCommand(null, null, new MultipleOption(parser,1, "-appendMany", ',', InFileParameter.getParameter())));
		
		uniqueCommands.add(new PadWithGapCommand(null, null, new NoOption(parser, "-pad")));
		
		uniqueCommands.add(new ReplaceUncommonChars(null, null, new MultipleOption(parser, null, "-repUncommon", ',', StringParameter.getParameter())));
		
		uniqueCommands.add(new RemoveCommand(null,null,new MultipleOption(parser,null, "-remove", ',',IntegerParameter.getParameter())));
		
		uniqueCommands.add(new ChangeDescriptionsCommand(null, null, new SingleOption(parser,1, "-changeDesc", InFileParameter.getParameter())));
		
		uniqueCommands.add(new GapFrequencyInColumnsCommand(null,null,new NoOption(parser, "-gapfreq")));

		// Step Three : Try to parse the command line
		
		long time1 = System.currentTimeMillis(); 
		
		try {

			parser.parseEx(args);

		} catch (IncorrectParameterTypeException e) {

			System.err.println("There was a error parsing the command line");

			System.exit(1);
			
		}
		
		if (debugOpt.isPresent()) {
			
			System.err.println("Creation of command object after: "+ (time1 - time0) + "ms.");
			
			System.err.println("Parsing arguments after: "+ (System.currentTimeMillis() - time0) + "ms.");
			
		}

		
		// Program Itself
		
		
		int parametersUsed = getNumberOfParametersUsed(uniqueCommands);
		
		PrintStream out = (PrintStream) printStreamOpt.getValue();
		
		InputStream input = (InputStream) inputStreamOpt.getValue();
		
		if (parametersUsed==0 || helpOpt.isPresent()) {
			
			helpCommand.setOutput(out);
			
			helpCommand.execute();

			System.exit(0);
		} else 	if (parametersUsed>1) {
			System.err.println("Only one option is expected");
			System.exit(0);			
		}
		
		if (versionOpt.isPresent()) {
			
			versionCommand.setOutput(out);
			
			versionCommand.execute();
			
			System.exit(0);
		}
		
		if (geneticCodeHelpOpt.isPresent()) {
			
			geneticCodeHelpCommand.setOutput(out);
			
			geneticCodeHelpCommand.execute();
			
			System.exit(0);
			
		}
		
		FastaCommand<? extends Option> selectedCommand = getSelectedCommand(uniqueCommands);
		
		selectedCommand.setInputstream(input);
		
		selectedCommand.setOutput(out);
		
		if (debugOpt.isPresent()) {
			
			System.err.println("Starting execute option after: "+ (System.currentTimeMillis() - time0) + "ms.");
			
		}
		
		selectedCommand.execute();
		
		if (debugOpt.isPresent()) {
			
			System.err.println("Ending execute option after: "+ (System.currentTimeMillis() - time0) + "ms.");
			
		}
		
		if (debugOpt.isPresent()) {
			
			System.err.println("End Program after: "+ (System.currentTimeMillis() - time0) + "ms.");
			
		}

	}

	///////////////////////////
	// Private Methods
	private static FastaCommand<? extends Option> getSelectedCommand( List<FastaCommand<? extends Option>> uniqueCommands) {
		
		for (FastaCommand<? extends Option> fastaCommand : uniqueCommands) {

			if (fastaCommand.getOption().isPresent()) {
				
				return fastaCommand;
				
			}
			
		}
		
		return null;
		
	}

	private static int getNumberOfParametersUsed(
			List<FastaCommand<? extends Option>> uniqueCommands) {
		int parametersUsed = 0;
		
		for (FastaCommand<? extends Option> command : uniqueCommands) { 		
			if (command.getOption().isPresent()) parametersUsed++; 
		}
		return parametersUsed;
	}


	
}
