package seqManipulation.fastamanipulator;

import java.io.File;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import seqManipulation.fastamanipulator.commands.FastaCommand;
import seqManipulation.fastamanipulator.commands.bio.ComplementaryCommand;
import seqManipulation.fastamanipulator.commands.bio.RandomBackTranslationCommand;
import seqManipulation.fastamanipulator.commands.bio.TranslateCommand;
import seqManipulation.fastamanipulator.commands.bio.TranslateWithCommand;
import seqManipulation.fastamanipulator.commands.extraction.ExtractByTitleCommand;
import seqManipulation.fastamanipulator.commands.extraction.ExtractCommand;
import seqManipulation.fastamanipulator.commands.extraction.PickRandomCommand;
import seqManipulation.fastamanipulator.commands.filter.FilterSequenceContainingCommand;
import seqManipulation.fastamanipulator.commands.filter.FilterSequenceStartingWithCommand;
import seqManipulation.fastamanipulator.commands.filter.FilterSizeGrEqThanCommand;
import seqManipulation.fastamanipulator.commands.filter.FilterSizeSmEqThanCommand;
import seqManipulation.fastamanipulator.commands.filter.FilterTitleContainingCommand;
import seqManipulation.fastamanipulator.commands.filter.FilterTitlesContainingCommand;
import seqManipulation.fastamanipulator.commands.gaps.CountGapsInRowCommand;
import seqManipulation.fastamanipulator.commands.gaps.CrushEndsCommand;
import seqManipulation.fastamanipulator.commands.gaps.DegapCommand;
import seqManipulation.fastamanipulator.commands.gaps.FlushEndsCommand;
import seqManipulation.fastamanipulator.commands.gaps.GapFrequencyInColumnsCommand;
import seqManipulation.fastamanipulator.commands.gaps.PadWithGapCommand;
import seqManipulation.fastamanipulator.commands.gaps.RemoveGappedRowsCommand;
import seqManipulation.fastamanipulator.commands.gaps.StripGappedColumnsByFreqCommand;
import seqManipulation.fastamanipulator.commands.gaps.StripGappedColumnsCommand;
import seqManipulation.fastamanipulator.commands.help.GeneticCodeHelpCommand;
import seqManipulation.fastamanipulator.commands.help.HelpCommand;
import seqManipulation.fastamanipulator.commands.identity.IdentityMatrixCommand;
import seqManipulation.fastamanipulator.commands.identity.IdentityValuesCommand;
import seqManipulation.fastamanipulator.commands.identity.MDSCommand;
import seqManipulation.fastamanipulator.commands.info.CountCommand;
import seqManipulation.fastamanipulator.commands.info.DefinitionsCommand;
import seqManipulation.fastamanipulator.commands.info.LengthCommand;
import seqManipulation.fastamanipulator.commands.info.LengthsCommand;
import seqManipulation.fastamanipulator.commands.misc.CalculateMICommand;
import seqManipulation.fastamanipulator.commands.misc.ReconstructDottedAlignmentCommand;
import seqManipulation.fastamanipulator.commands.modify.AppendCommand;
import seqManipulation.fastamanipulator.commands.modify.AppendManyCommand;
import seqManipulation.fastamanipulator.commands.modify.ChangeDescriptionsCommand;
import seqManipulation.fastamanipulator.commands.modify.ConcatenateCommand;
import seqManipulation.fastamanipulator.commands.modify.DeInterleaveCommand;
import seqManipulation.fastamanipulator.commands.modify.KeepPositionsCommand;
import seqManipulation.fastamanipulator.commands.modify.RemPositionsCommand;
import seqManipulation.fastamanipulator.commands.modify.RemoveCommand;
import seqManipulation.fastamanipulator.commands.modify.ReplaceUncommonChars;
import seqManipulation.fastamanipulator.commands.modify.SliceCommand;
import seqManipulation.fastamanipulator.commands.system.VersionCommand;
import cmdGA2.ArgOption;
import cmdGA2.CommandLine;
import cmdGA2.MultipleArgumentOption;
import cmdGA2.NoArgumentOption;
import cmdGA2.SingleArgumentOption;
import cmdGA2.returnvalues.DoubleValue;
import cmdGA2.returnvalues.InfileValue;
import cmdGA2.returnvalues.InputStreamValue;
import cmdGA2.returnvalues.IntegerValue;
import cmdGA2.returnvalues.OutfileValue;
import cmdGA2.returnvalues.PrintStreamValue;
import cmdGA2.returnvalues.StringValue;

public class CommandLineMaganerModule {

	////////////////////////////////////////////////////////////////////////////
	// Private class Constants
	private static final String GAPFREQ = "-gapfreq";
	private static final String CHANGE_DESC = "-changeDesc";
	private static final String REMOVE = "-remove";
	private static final String REP_UNCOMMON = "-repUncommon";
	private static final String PAD = "-pad";
	private static final String APPEND_MANY = "-appendMany";
	private static final String COUNT_GAPS_IN = "-countGapsIn";
	private static final String REM_GAP_ROWS = "-remGapRows";
	private static final String REMPOS = "-rempos";
	private static final String KEEPPOS = "-keeppos";
	private static final String EXTRACT_TITLES = "-extract_titles";
	private static final String TITLES = "-titles";
	private static final String TITLE = "-title";
	private static final String PICK = "-pick";
	private static final String MDS = "-mds";
	private static final String DE_INTER = "-deInter";
	private static final String ID_MATRIX = "-idMatrix";
	private static final String ID_VALUES = "-idValues";
	private static final String COMP = "-comp";
	private static final String CONTAINS = "-contains";
	private static final String F_START_WITH = "-fStartWith";
	private static final String F_SM_TH = "-fSmTh";
	private static final String F_GR_TH = "-fGrTh";
	private static final String REC_FROM_CON = "-recFromCon";
	private static final String MI = "-MI";
	private static final String RANDOM_RT = "-randomRT";
	private static final String FLUSH = "-flush";
	private static final String CRUSH = "-crush";
	private static final String STRIP_GAPPED_COL_FR = "-stripGappedColFr";
	private static final String STRIP_GAPPED_COLUMNS = "-stripGappedColumns";
	private static final String GEN_CODE_HELP = "-genCodeHelp";
	private static final String TRANSLATE_WITH = "-translateWith";
	private static final String TRANSLATE = "-translate";
	private static final String DEGAP = "-degap";
	private static final String SLICE = "-slice";
	private static final String CONCATENATE = "-concatenate";
	private static final String LENGTHS = "-lengths";
	private static final String LENGTH = "-length";
	private static final String APPEND = "-append";
	private static final String VER = "-ver";
	private static final String HELP = "-help";
	private static final String INVERSE_FILTER = "-inverseFilter";
	private static final String DEBUG = "-debug";
	private static final String DEF = "-def";
	private static final String COUNT = "-count";
	private static final String EXTRACT = "-extract";
	private static final String OUTFILE = "-outfile";
	private static final String INFILE = "-infile";
	////////////////////////////////////////////////////////////////////////////

	////////////////////////////////////////////////////////////////////////////
	// Public Instance variables
	private InputStream in;
	private PrintStream out;
	private boolean isDebugFlagSet;
	private boolean isHelpFlagSet;
	private HelpCommand helpCommand;
	private List<FastaCommand<? extends ArgOption<?>>> uniqueCommand;
	////////////////////////////////////////////////////////////////////////////
	
	////////////////////////////////////////////////////////////////////////////
	// Public interface
	public void dealWithCommandLine(String[] args) {
		
		////////////////////////////////////////////////////////////////////////
		// Step One : Create the Command line object
		CommandLine cmd = new CommandLine();
		////////////////////////////////////////////////////////////////////////

		List<FastaCommand<? extends ArgOption<?>>> uniqueCommands = new ArrayList<FastaCommand<? extends ArgOption<?>>>();

		////////////////////////////////////////////////////////////////////////
		// Step Two : Add Command Line Options
		////////////////////////////////////////////////////////////////////////
		// System options
		SingleArgumentOption<InputStream> inputStreamOpt = new SingleArgumentOption<InputStream>(cmd, INFILE, new InputStreamValue(), System.in);
		SingleArgumentOption<PrintStream> printStreamOpt = new SingleArgumentOption<PrintStream>(cmd, OUTFILE, new PrintStreamValue(), System.out);
		NoArgumentOption debugOpt = new NoArgumentOption(cmd, DEBUG);
		NoArgumentOption versionOpt = new NoArgumentOption(cmd, VER);
		VersionCommand versionCommand = new VersionCommand(null, null, versionOpt);
		uniqueCommands.add(versionCommand);
		////////////////////////////////////////////////////////////////////////
		// Help Options
		NoArgumentOption helpOpt = new NoArgumentOption(cmd, HELP);
		HelpCommand helpCommand = new HelpCommand(null, null, helpOpt);
		NoArgumentOption geneticCodeHelpOpt = new NoArgumentOption(cmd, GEN_CODE_HELP);
		GeneticCodeHelpCommand geneticCodeHelpCommand = new GeneticCodeHelpCommand(null, null, geneticCodeHelpOpt);
		uniqueCommands.add(geneticCodeHelpCommand);
		////////////////////////////////////////////////////////////////////////
		// Info options
		uniqueCommands.add(new CountCommand(null, null, new NoArgumentOption(cmd, COUNT)));
		uniqueCommands.add(new DefinitionsCommand(null, null, new NoArgumentOption(cmd, DEF)));
		uniqueCommands.add(new LengthCommand(null, null, new NoArgumentOption(cmd, LENGTH)));
		uniqueCommands.add(new LengthsCommand(null, null, new NoArgumentOption(cmd, LENGTHS)));
		////////////////////////////////////////////////////////////////////////
		// Modify
		uniqueCommands.add(new AppendCommand(null, null, new SingleArgumentOption<File>(cmd, APPEND, new InfileValue() ,null) )); 
		uniqueCommands.add(new ConcatenateCommand(null, null, new MultipleArgumentOption<File>(cmd, CONCATENATE, ',', null, new InfileValue())));
		uniqueCommands.add(new SliceCommand(null, null, new MultipleArgumentOption<Integer>(cmd, SLICE, ',', null, new IntegerValue())));
		uniqueCommands.add(new DeInterleaveCommand(null, null, new NoArgumentOption(cmd, DE_INTER)));
		uniqueCommands.add(new AppendManyCommand(null, null, new MultipleArgumentOption<File>(cmd, APPEND_MANY, ',', null,new InfileValue())));
		uniqueCommands.add(new RemoveCommand(null,null,new MultipleArgumentOption<Integer>(cmd, REMOVE, ',',null, new IntegerValue())));
		uniqueCommands.add(new ReplaceUncommonChars(null, null, new MultipleArgumentOption<String>(cmd, REP_UNCOMMON, ',', null, new StringValue())));
		uniqueCommands.add(new ChangeDescriptionsCommand(null, null, new SingleArgumentOption<File>(cmd, CHANGE_DESC, new InfileValue(),null)));
		uniqueCommands.add(new RemPositionsCommand(null, null, new SingleArgumentOption<File>(cmd, REMPOS, new InfileValue(), null)));
		uniqueCommands.add(new KeepPositionsCommand(null, null, new SingleArgumentOption<File>(cmd, KEEPPOS, new InfileValue(), null)));
		////////////////////////////////////////////////////////////////////////
		// Filter
		NoArgumentOption invertFilterOpt = new NoArgumentOption(cmd, INVERSE_FILTER);
		uniqueCommands.add(new FilterSizeGrEqThanCommand(null, null, new SingleArgumentOption<Integer>(cmd, F_GR_TH, new IntegerValue(), null), invertFilterOpt));
		uniqueCommands.add(new FilterSizeSmEqThanCommand(null, null, new SingleArgumentOption<Integer>(cmd, F_SM_TH, new IntegerValue(), null), invertFilterOpt));
		uniqueCommands.add(new FilterSequenceStartingWithCommand(null, null, new SingleArgumentOption<String>(cmd, F_START_WITH, new StringValue(), null), invertFilterOpt));
		uniqueCommands.add(new FilterSequenceContainingCommand(null, null, new SingleArgumentOption<String>(cmd, CONTAINS,new StringValue(), null), invertFilterOpt));
		uniqueCommands.add(new FilterTitleContainingCommand(null, null, new SingleArgumentOption<String>(cmd, TITLE, new StringValue(), null), invertFilterOpt));
		uniqueCommands.add(new FilterTitlesContainingCommand(null, null, new MultipleArgumentOption<String>(cmd, TITLES, ',',null,new StringValue()), invertFilterOpt));
		////////////////////////////////////////////////////////////////////////
		// Extraction
		uniqueCommands.add(new ExtractCommand(null, null, new MultipleArgumentOption<Integer>(cmd,EXTRACT, ',', null, new IntegerValue())));
		uniqueCommands.add(new PickRandomCommand(null, null, new SingleArgumentOption<Integer>(cmd, PICK,  new IntegerValue(), 1)));
		uniqueCommands.add(new ExtractByTitleCommand(null, null, new MultipleArgumentOption<String>(cmd, EXTRACT_TITLES, ',',null,new StringValue() )));
		////////////////////////////////////////////////////////////////////////
		// Bio
		uniqueCommands.add(new TranslateCommand(null, null, new NoArgumentOption(cmd, TRANSLATE)));
		uniqueCommands.add(new TranslateWithCommand(null, null, new SingleArgumentOption<File>(cmd, TRANSLATE_WITH, new InfileValue(), null)));
		uniqueCommands.add(new RandomBackTranslationCommand(null, null, new NoArgumentOption(cmd, RANDOM_RT)));
		uniqueCommands.add(new ComplementaryCommand(null, null, new NoArgumentOption(cmd, COMP)));
		////////////////////////////////////////////////////////////////////////
		// Identity
		uniqueCommands.add(new IdentityValuesCommand(null, null, new NoArgumentOption(cmd, ID_VALUES)));
		uniqueCommands.add(new IdentityMatrixCommand(null, null, new NoArgumentOption(cmd, ID_MATRIX)));
		uniqueCommands.add(new MDSCommand(null, null, new SingleArgumentOption<Integer>(cmd, MDS, new IntegerValue(),2)));
		////////////////////////////////////////////////////////////////////////
		// Gap
		uniqueCommands.add(new DegapCommand(null, null, new NoArgumentOption(cmd, DEGAP)));
		uniqueCommands.add(new FlushEndsCommand(null, null, new NoArgumentOption(cmd, FLUSH)));
		uniqueCommands.add(new CrushEndsCommand(null, null, new NoArgumentOption(cmd, CRUSH)));
		uniqueCommands.add(new PadWithGapCommand(null, null, new NoArgumentOption(cmd, PAD)));
		uniqueCommands.add(new GapFrequencyInColumnsCommand(null,null,new NoArgumentOption(cmd, GAPFREQ)));
		uniqueCommands.add(new RemoveGappedRowsCommand(null, null, new NoArgumentOption(cmd, REM_GAP_ROWS)));
		uniqueCommands.add(new StripGappedColumnsCommand(null, null, new NoArgumentOption(cmd, STRIP_GAPPED_COLUMNS)));
		uniqueCommands.add(new StripGappedColumnsByFreqCommand(null, null, new SingleArgumentOption<Double>(cmd,STRIP_GAPPED_COL_FR, new DoubleValue(),1d ), new SingleArgumentOption<File>(cmd,  "-reference", new OutfileValue(),null)));
		uniqueCommands.add(new CountGapsInRowCommand(null, null, new SingleArgumentOption<Integer>(cmd, COUNT_GAPS_IN,  new IntegerValue(),1)));
		////////////////////////////////////////////////////////////////////////
		// Misc
		uniqueCommands.add(new CalculateMICommand(null, null, new NoArgumentOption(cmd, MI)));
		uniqueCommands.add(new ReconstructDottedAlignmentCommand(null, null, new SingleArgumentOption<Integer>(cmd,REC_FROM_CON, new IntegerValue(), 0 )));
		////////////////////////////////////////////////////////////////////////
		
		////////////////////////////////////////////////////////////////////////
		// Step Three : Read The Command Line Options
		cmd.readAndExitOnError(args);
		////////////////////////////////////////////////////////////////////////
		
		this.setDebugFlagSet(debugOpt.isPresent());
		this.setHelpFlagSet(helpOpt.isPresent());
		this.setHelpCommand(helpCommand);
		this.setIn(inputStreamOpt.getValue());
		this.setOut(printStreamOpt.getValue());
		this.setUniqueCommand(uniqueCommands);
	}
	////////////////////////////////////////////////////////////////////////////

	/**
	 * @return the in
	 */
	protected InputStream getIn() {
		return in;
	}

	/**
	 * @param in the in to set
	 */
	protected void setIn(InputStream in) {
		this.in = in;
	}

	/**
	 * @return the out
	 */
	protected PrintStream getOut() {
		return out;
	}

	/**
	 * @param out the out to set
	 */
	protected void setOut(PrintStream out) {
		this.out = out;
	}

	/**
	 * @return the isDebugFlagSet
	 */
	protected boolean isDebugFlagSet() {
		return isDebugFlagSet;
	}

	/**
	 * @param isDebugFlagSet the isDebugFlagSet to set
	 */
	protected void setDebugFlagSet(boolean isDebugFlagSet) {
		this.isDebugFlagSet = isDebugFlagSet;
	}

	/**
	 * @return the isHelpFlagSet
	 */
	protected boolean isHelpFlagSet() {
		return isHelpFlagSet;
	}

	/**
	 * @param isHelpFlagSet the isHelpFlagSet to set
	 */
	protected void setHelpFlagSet(boolean isHelpFlagSet) {
		this.isHelpFlagSet = isHelpFlagSet;
	}

	/**
	 * @return the helpCommand
	 */
	protected HelpCommand getHelpCommand() {
		return helpCommand;
	}

	/**
	 * @param helpCommand the helpCommand to set
	 */
	protected void setHelpCommand(HelpCommand helpCommand) {
		this.helpCommand = helpCommand;
	}


	/**
	 * @return the uniqueCommand
	 */
	protected List<FastaCommand<? extends ArgOption<?>>> getUniqueCommand() {
		return uniqueCommand;
	}

	/**
	 * @param uniqueCommand the uniqueCommand to set
	 */
	protected void setUniqueCommand(
			List<FastaCommand<? extends ArgOption<?>>> uniqueCommand) {
		this.uniqueCommand = uniqueCommand;
	}



	
}
