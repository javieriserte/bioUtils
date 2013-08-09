package utils.mutualinformation.misticmod;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import utils.mutualinformation.misticmod.InterProteinCumulativeMIMatrix.Normalizer;

import cmdGA.MultipleOption;
import cmdGA.NoOption;
import cmdGA.Parser;
import cmdGA.SingleOption;
import cmdGA.exceptions.IncorrectParameterTypeException;
import cmdGA.parameterType.InputStreamParameter;
import cmdGA.parameterType.IntegerParameter;
import cmdGA.parameterType.StringParameter;

public class CmiIpToCircos {

	/**
	 * Creates the files for creating a circos graph from CMI interprotein data
	 * @param args
	 * @throws IncorrectParameterTypeException 
	 */
	public static void main(String[] args) throws IncorrectParameterTypeException {

		Parser parser = new Parser();
		
		SingleOption inOpt = new SingleOption(parser, System.in, "-infile", InputStreamParameter.getParameter());
		
		MultipleOption lengthsOpt = new MultipleOption(parser, null, "-lengths", ',', IntegerParameter.getParameter());
		
		SingleOption outOpt = new SingleOption(parser, "", "-prefix", StringParameter.getParameter());
		
		NoOption countAllPairsOpt = new NoOption(parser, "-countall");
		
		SingleOption pngDirOpt=  new SingleOption(parser, ".", "-pngdir", StringParameter.getParameter());
		
		parser.parseEx(args);
		
		String outPrefix = (String)outOpt.getValue();
		
		String pngDir = (String) pngDirOpt.getValue();

		InterProteinCumulativeMIMatrix ipcm = new InterProteinCumulativeMIMatrix();

		Integer[] lengths = InterProteinCumulativeMIMatrix.getLengths(lengthsOpt);
		
		ipcm.readMiData((InputStream) inOpt.getValue());
		
		ipcm.assignProteinNumber(lengths);
		
		Normalizer normalizer = countAllPairsOpt.isPresent()?(ipcm.new NormalizeWithAll(lengths)):(ipcm.new NormalizeWithPositives(lengths.length, ipcm.data, lengths));
		
		Double[][] cmi_inter = ipcm.calculateCMIInter(lengths.length, lengths, normalizer, ipcm.data);
		
		CmiIpToCircos citc = new CmiIpToCircos();
		
		List<String> chromosomeNames = citc.getChromosomeNames(lengths.length);
		
		citc.generateKaryotypeFile(outPrefix, lengths.length, chromosomeNames);
		
		citc.generateLinksFile(outPrefix, cmi_inter, chromosomeNames);
		
		citc.generateConfigFile(outPrefix+".conf", outPrefix+".kario", outPrefix+".png", outPrefix+".links", pngDir);
		
	}

	private void generateLinksFile(String outPrefix, Double[][] cmi_inter, List<String> chromosomeNames) {

		PrintStream out = null;
		
		List<CircosLink> links = new ArrayList<CircosLink>();

		
		try {

			out = new PrintStream(new File(outPrefix + ".links"));

			double max = getMax(cmi_inter);
			
			getLinks(cmi_inter, chromosomeNames, links, max);
			
			sortLinks(links);

			for (CircosLink circosLink : links) {
				
				out.println(circosLink);
				
			}
			
		
		} catch (FileNotFoundException e) {

			System.err.println("No pudo abrirse el archivo: "+outPrefix + ".kario" );
			
		} finally {
			
			out.flush();
			
			out.close();
			
		}
		
	}



	public void sortLinks(List<CircosLink> links) {
		Collections.sort(links, new Comparator<CircosLink>(){

			@Override
			public int compare(CircosLink o1, CircosLink o2) {
				
				int len_2 = Math.abs(o2.pos1_chr1-o2.pos2_chr1);
				
				int len_1 = Math.abs(o1.pos1_chr1-o1.pos2_chr1);
				
				return len_1 - len_2;
				
			}
			
		});
	}



	public void getLinks(Double[][] cmi_inter, List<String> chromosomeNames, List<CircosLink> links, double max) {
		
		for (int i = 0; i< cmi_inter.length-1; i++) {
			
			for (int j = i+1; j < cmi_inter.length; j++) {
				
				int maxWidth = 50;
				
				int width = (int)(maxWidth * (cmi_inter[i][j]/max));
				
				int pos1 = 26 + (maxWidth - width) / 2;
				
				int pos2 = 75 - (maxWidth - width) / 2;
				
				CircosLink cl = new CircosLink(chromosomeNames.get(i), chromosomeNames.get(j), pos1, pos2, pos1, pos2);
				
				links.add(cl);
				
			}
			
		}
	}



	public double getMax(Double[][] cmi_inter) {
		double max = 0;
		
		for (int i = 0; i< cmi_inter.length-1; i++) {
			
			for (int j = i+1; j < cmi_inter.length; j++) {
				
				max = Math.max(cmi_inter[i][j],max);
				
			}
			
		}
		return max;
	}



	private void generateKaryotypeFile(String outPrefix, int length, List<String> chromosomeNames) {
		
		PrintStream out = null;
		
		try {

			out = new PrintStream(new File(outPrefix + ".kario"));
			
			for (String string : chromosomeNames) {
				
				CircosChromosome c = new CircosChromosome(string, string, 1, 100, "green");
				
				out.println(c);
				
			}
		
		} catch (FileNotFoundException e) {

			System.err.println("No pudo abrirse el archivo: "+outPrefix + ".kario" );
			
		} finally {
			
			out.flush();
			
			out.close();
			
		}
		
		
	}

	private List<String> getChromosomeNames(int length) {
		
		List<String> results = new ArrayList<String>();
		
		for (int i=0; i<length; i++) {
			
			results.add("protein"+(i+1));
			
		}
		
		return results;
		
	}
	
	private void generateConfigFile(String outfile,String kariotype, String image, String links, String dir) {
		
		try {
			
			PrintStream out = new PrintStream(new File(outfile));
			
			BufferedReader a = new BufferedReader( new InputStreamReader( this.getClass().getResourceAsStream("circos.conf.template")));
			
			String l = null;
		
			while ((l=a.readLine())!=null) {

				if (l.contains("#KARIOFILE#")) {
					
					l = l.replaceAll("#KARIOFILE#", kariotype);
					
				} else 
					
				if (l.contains("#DIR#")) {
						
					l = l.replaceAll("#DIR#", dir);
						
				} else 

					if (l.contains("#IMAGEFILE#")) {
					
				l = l.replaceAll("#IMAGEFILE#", image);
					
				} else 
				
				if (l.contains("#LINKSFILE")) {
					
					l = l.replaceAll("#LINKSFILE#", links);
					
				}
				
				out.println(l);
				
			}
			
			out.flush();
			
			out.close();
			
		} catch (IOException e) {
			
			System.err.println("There was an error writing the output file:" + e.getMessage());
			
		}
		
	}

}