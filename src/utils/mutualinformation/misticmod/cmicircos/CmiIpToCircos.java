package utils.mutualinformation.misticmod.cmicircos;

import io.resources.ResourceContentAsString;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import utils.mutualinformation.misticmod.CircosChromosome;
import cmdGA.MultipleOption;
import cmdGA.NoOption;
import cmdGA.Parser;
import cmdGA.SingleOption;
import cmdGA.exceptions.IncorrectParameterTypeException;
import cmdGA.parameterType.InFileParameter;
import cmdGA.parameterType.InputStreamParameter;
import cmdGA.parameterType.IntegerParameter;
import cmdGA.parameterType.StringParameter;

public class CmiIpToCircos {

	////////////////////////////////////////////////////////////////////////////
	// Private constants
	private static final String KARIO_EXTENSION = ".kario";
	private static final String CONF_EXTENSION = ".conf";
	private static final String PNG_EXTENSION = ".png";
	private static final String LINKS_EXTENSION = ".links";
	private static final String HELP_RESOURCE_FILE = "cmi_ip_circos_help";
	private static final String CIRCOS_TEMPLATE_FILE = "circos.conf.template";
	
	private static final String CIRCOS_KARIO_TAG= "#KARIOFILE#";
	private static final String CIRCOS_DIR_TAG = "#DIR#";
	private static final String CIRCOS_IMAGE_TAG = "#IMAGEFILE#";
	private static final String CIRCOS_LINKS_TAG = "#LINKSFILE";
	////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Creates the files for creating a circos graph from CMI interprotein data
	 * 
	 * @param args
	 * @throws IncorrectParameterTypeException
	 * @throws IOException
	 */
	public static void main(String[] args)
			throws IncorrectParameterTypeException, IOException {

		////////////////////////////////////////////////////////////////////////
		// Create Parser
		Parser parser = new Parser();
		////////////////////////////////////////////////////////////////////////

		////////////////////////////////////////////////////////////////////////
		// Define Command line arguments
		SingleOption inOpt = new SingleOption(parser, System.in, "-infile",
				InputStreamParameter.getParameter());

		MultipleOption lengthsOpt = new MultipleOption(parser, null,
				"-lengths", ',', IntegerParameter.getParameter());

		SingleOption outOpt = new SingleOption(parser, "", "-prefix",
				StringParameter.getParameter());

		NoOption countAllPairsOpt = new NoOption(parser, "-countall");

		SingleOption labelFileOpt = new SingleOption(parser, null, "-names",
				InFileParameter.getParameter());

		SingleOption pngDirOpt = new SingleOption(parser, ".", "-pngdir",
				StringParameter.getParameter());
		
		NoOption helpOpt = new NoOption(parser, "-help");
		////////////////////////////////////////////////////////////////////////

		////////////////////////////////////////////////////////////////////////
		// Parse comman line
		parser.parseEx(args);
		////////////////////////////////////////////////////////////////////////

		/////////////////////////////////////////////////////////////////////////
		// Get values from command line
		String outPrefix = (String) outOpt.getValue();

		String pngDir = (String) pngDirOpt.getValue();

		InputStream infile = (InputStream) inOpt.getValue();

		File labelsFile = (File) labelFileOpt.getValue();

		boolean helpRequired = helpOpt.isPresent();
		////////////////////////////////////////////////////////////////////////
		
		////////////////////////////////////////////////////////////////////////
		// Check for help
		if (helpRequired) {
			
			String helpText = new ResourceContentAsString().readContents(
					CmiIpToCircos.HELP_RESOURCE_FILE, CmiIpToCircos.class);
			System.err.println(helpText);
			System.exit(1);
			
		}
		////////////////////////////////////////////////////////////////////////
		
		////////////////////////////////////////////////////////////////////////
		// Program
		InterProteinCumulativeMIMatrix ipcm = 
				new InterProteinCumulativeMIMatrix();

		Integer[] lengths = 
				InterProteinCumulativeMIMatrix.getLengths(lengthsOpt);

		List<String> labels = readLabels(labelsFile);

		ipcm.readMiData(infile);

		ipcm.assignProteinNumber(lengths);

		Normalizer normalizer = countAllPairsOpt.isPresent() ? (
				new NormalizeWithAll(lengths)) : 
				(new NormalizeWithPositives(lengths.length,
				ipcm.data, lengths));

		Double[][] cmi_inter = ipcm.calculateCMIInter(lengths.length, lengths, 
				normalizer, ipcm.data);

		CmiIpToCircos citc = new CmiIpToCircos();

		List<String> chromosomeNames = citc.getChromosomeNames(lengths.length);

		citc.generateKaryotypeFile(outPrefix, lengths.length, chromosomeNames, 
				labels);

		citc.generateLinksFile(outPrefix, cmi_inter, chromosomeNames);

		citc.generateConfigFile(
				outPrefix + CmiIpToCircos.CONF_EXTENSION, 
				outPrefix + CmiIpToCircos.KARIO_EXTENSION,
				outPrefix + CmiIpToCircos.PNG_EXTENSION,
				outPrefix + CmiIpToCircos.LINKS_EXTENSION, 
				pngDir);
		////////////////////////////////////////////////////////////////////////

	}

	private static List<String> readLabels(File labelsFile) throws IOException {

		if (labelsFile == null) {

			return null;

		} else {

			List<String> results = new ArrayList<>();

			BufferedReader br = new BufferedReader(new FileReader(labelsFile));

			String currentline = null;

			while ((currentline = br.readLine()) != null) {

				results.add(currentline.trim());

			}

			br.close();

			return results;

		}

	}

	private void generateLinksFile(String outPrefix, Double[][] cmi_inter,
			List<String> chromosomeNames) {

		PrintStream out = null;

		List<CircosLink> links = new ArrayList<CircosLink>();

		try {

			out = new PrintStream(
					new File(outPrefix + CmiIpToCircos.LINKS_EXTENSION));

			double max = getMax(cmi_inter);

			getLinks(cmi_inter, chromosomeNames, links, max);

			sortLinks(links);

			for (CircosLink circosLink : links) {

				out.println(circosLink);

			}

		} catch (FileNotFoundException e) {

			System.err.println("No pudo abrirse el archivo: " + outPrefix
					+ ".kario");

		} finally {

			out.flush();

			out.close();

		}

	}

	public void sortLinks(List<CircosLink> links) {
		Collections.sort(links, new Comparator<CircosLink>() {

			@Override
			public int compare(CircosLink o1, CircosLink o2) {

				int len_2 = Math.abs(o2.pos1_chr1 - o2.pos2_chr1);

				int len_1 = Math.abs(o1.pos1_chr1 - o1.pos2_chr1);

				return len_1 - len_2;

			}

		});
		
	}

	public void getLinks(Double[][] cmi_inter, List<String> chromosomeNames,
			List<CircosLink> links, double max) {

		for (int i = 0; i < cmi_inter.length - 1; i++) {

			for (int j = i + 1; j < cmi_inter.length; j++) {

				int maxWidth = 50;

				int width = (int) (maxWidth * (cmi_inter[i][j] / max));

				int pos1 = 26 + (maxWidth - width) / 2;

				int pos2 = 75 - (maxWidth - width) / 2;

				CircosLink cl = new CircosLink(chromosomeNames.get(i),
						chromosomeNames.get(j), pos1, pos2, pos1, pos2);

				links.add(cl);

			}

		}
	}

	public double getMax(Double[][] cmi_inter) {
		double max = 0;

		for (int i = 0; i < cmi_inter.length - 1; i++) {

			for (int j = i + 1; j < cmi_inter.length; j++) {

				max = Math.max(cmi_inter[i][j], max);

			}

		}
		return max;
	}

	private void generateKaryotypeFile(String outPrefix, int length,
			List<String> chromosomeNames, List<String> chromosomeLabels) {

		PrintStream out = null;

		try {

			out = new PrintStream(new File(outPrefix + ".kario"));

			for (int i = 0; i < chromosomeNames.size(); i++) {

				String chrName = chromosomeNames.get(i);

				String chrLabel = (
						chromosomeLabels != null && i < chromosomeLabels.size())
						? chromosomeLabels.get(i) : chrName;

				CircosChromosome c = new CircosChromosome(chrName, chrLabel, 1,
						100, "green");

				out.println(c);

			}

		} catch (FileNotFoundException e) {

			System.err.println("No pudo abrirse el archivo: " + outPrefix
					+ ".kario");

		} finally {

			out.flush();

			out.close();

		}

	}

	private List<String> getChromosomeNames(int length) {

		List<String> results = new ArrayList<String>();

		for (int i = 0; i < length; i++) {

			results.add("protein" + (i + 1));

		}

		return results;

	}

	private void generateConfigFile(String outfile, String kariotype,
			String image, String links, String dir) {

		try {

			PrintStream out = new PrintStream(new File(outfile));

			BufferedReader a = new BufferedReader(new InputStreamReader(this
					.getClass().getResourceAsStream(
							CmiIpToCircos.CIRCOS_TEMPLATE_FILE)));
			
			Map<String,String> replacements = new HashMap<String, String>();
			
			replacements.put(CmiIpToCircos.CIRCOS_DIR_TAG, dir);		
			replacements.put(CmiIpToCircos.CIRCOS_LINKS_TAG, links);
			replacements.put(CmiIpToCircos.CIRCOS_KARIO_TAG, kariotype);
			replacements.put(CmiIpToCircos.CIRCOS_IMAGE_TAG, image);

			Set<String> replacementKeys = replacements.keySet(); 
			
			String currentLine = null;

			while ((currentLine = a.readLine()) != null) {

				for (String key : replacementKeys) {
					
					currentLine = currentLine.replaceAll(key, 
						replacements.get(key));
					
				}

				out.println(currentLine);

			}

			out.flush();

			out.close();

		} catch (IOException e) {

			System.err.println("There was an error writing the output file:" + e.getMessage());

		}

	}

}
