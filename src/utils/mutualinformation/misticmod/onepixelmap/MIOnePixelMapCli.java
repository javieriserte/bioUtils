package utils.mutualinformation.misticmod.onepixelmap;

import graphics.profile.PngWriter;
import io.bufferreaders.UncommenterBufferedReader;
import io.onelinelister.OneLineListReader;
import io.resources.ResourceContentAsString;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import utils.mutualinformation.misticmod.datastructures.MI_PositionWithProtein;
import utils.mutualinformation.misticmod.onepixelmap.themes.BlueAndRedTheme;
import utils.mutualinformation.misticmod.onepixelmap.themes.MatrixColoringTheme;
import cmdGA2.CommandLine;
import cmdGA2.MultipleArgumentOption;
import cmdGA2.NoArgumentOption;
import cmdGA2.OptionsFactory;
import cmdGA2.SingleArgumentOption;
import cmdGA2.returnvalues.OutfileValue;
import cmdGA2.returnvalues.StringValue;

public class MIOnePixelMapCli {

	public static void main(String[] args) {
		////////////////////////////////////////////////////////////////////////
		// Create Command Line
		CommandLine cmd = new CommandLine();
		////////////////////////////////////////////////////////////////////////
		
		////////////////////////////////////////////////////////////////////////
		// Add Command Line Options
		SingleArgumentOption<InputStream> inOpt = OptionsFactory.createBasicInputStreamArgument(cmd);
		
		SingleArgumentOption<File> outOpt = new SingleArgumentOption<File>(cmd, "--out", new OutfileValue() , null);
		
		SingleArgumentOption<MatrixColoringTheme> colOpt = new SingleArgumentOption<MatrixColoringTheme>(cmd, "--color", new ThemeValue() , new BlueAndRedTheme(0));
		
		MultipleArgumentOption<Integer> lenOpt = OptionsFactory.createBasicCommaSeparatedIntegersArgument(cmd, "--lengths");
		
		MultipleArgumentOption<String> namesOpt = new MultipleArgumentOption<>(cmd, "--names", ',', new ArrayList<String>(), new StringValue());
		
		NoArgumentOption helpOpt = new NoArgumentOption(cmd, "--help");
		////////////////////////////////////////////////////////////////////////

		////////////////////////////////////////////////////////////////////////
		// Parse Command Line
		cmd.readAndExitOnError(args);
		////////////////////////////////////////////////////////////////////////

		////////////////////////////////////////////////////////////////////////
		// Parse Command Line
		if (helpOpt.isPresent()) {
			String helpText = new ResourceContentAsString().readContents(
					"help", 
					MIOnePixelMapCli.class);
			System.err.println(helpText);
			System.exit(0);
		}
		////////////////////////////////////////////////////////////////////////
		
		////////////////////////////////////////////////////////////////////////
		// Get Values From Command Line
		InputStream in = inOpt.getValue();
		
		File out = outOpt.getValue();
		
		List<Integer> lengths = lenOpt.getValues();
		
		List<String> names = namesOpt.getValues();
		
		MatrixColoringTheme colorer = colOpt.getValue();
		
		////////////////////////////////////////////////////////////////////////
		
		////////////////////////////////////////////////////////////////////////
		// Validate Options
		if (!outOpt.isPresent() || out==null) {
			
			System.err.println("There was an error with the output file");
			
			System.exit(1);
			
		}
		
		if (!lenOpt.isPresent() || lengths.isEmpty()) {
			
			System.err.println("There was an error with the protein lengths");
			
			System.exit(1);
			
		}
		
		if (namesOpt.isPresent() && (names.size() != lengths.size()) ) {

			System.err.println("There must be the same number of name labels that region lengths");
			
			System.exit(1);
			
		}
		////////////////////////////////////////////////////////////////////////
		
		////////////////////////////////////////////////////////////////////////
		// Program
		MI_PosLinePaser mi_parser = new MI_PosLinePaser();
		
		MiDataAnalitics analitics =new MiDataAnalitics();
		
		mi_parser.setLengths(lengths);
		
		OneLineListReader<MI_PositionWithProtein> reader = new OneLineListReader<>(mi_parser);
		
		MIOnePixelMap mapper = new MIOnePixelMap();
		
		try {
			
			List<MI_PositionWithProtein> positions = reader.read(new UncommenterBufferedReader(new InputStreamReader(in)));

			int max = analitics.getMaxResidueNumber(positions);
			
			double maxMIValue = analitics.getMaxValue(positions);
			
			double minMIValue = analitics.getMinValue(positions);

			colorer.setMaxMI(maxMIValue);
			
			colorer.setMinMI(minMIValue);
			
			BufferedImage bi = mapper.getImage(lengths, names,  positions, max, maxMIValue, minMIValue, colorer);
			
			PngWriter writer = new PngWriter();
			
			writer.write(out, bi);
			
		} catch (IOException e) {

			System.err.println("There was an error, trying to read the input data: "+ e.getMessage());
			
			System.exit(1);
			
		}


	}

}
