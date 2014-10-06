package utils.mutualinformation.mimatrixviewer;

import io.bufferreaders.UncommenterBufferedReader;
import io.onelinelister.OneLineListReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import utils.mutualinformation.misticmod.datastructures.MI_Position;
import utils.mutualinformation.misticmod.datastructures.MI_PositionLineParser;

/**
 * Reads MI data.
 * Can interpret Morten format and mistic format.
 * Also can read zipped files.
 * 
 * @author Javier
 *
 */
public class MIMatrixReader {

	public MI_Matrix read(BufferedReader in) throws IOException {
		
		MI_PositionLineParser parser = new MI_PositionLineParser();
		
		List<MI_Position> values = (new OneLineListReader<MI_Position>(parser)).read(new UncommenterBufferedReader(in));
		
		List<Integer> residueLabels = this.getResidueLabels(values);
		
		char[] refSeq = this.getReferenceSequence(values);

		int matrixSize = (int)(Math.sqrt(values.size()*8+1)+1)/2;
		
		MI_Matrix matrix = new MI_Matrix(matrixSize,true,true,true,residueLabels);
		
		for (MI_Position mi_Position : values) {
			
			matrix.setReferenceSequence(refSeq);
			
			int posX = mi_Position.getPos1();
			
			int posY = mi_Position.getPos2();
			
			matrix.setZscoreValue(mi_Position.getMi(), posX, posY);
			
			matrix.setApcValue(mi_Position.getRaw_mi(), posX, posY);
			
			matrix.setMIValue(MI_Matrix.UNDEFINED, posX, posY);
			
		}
		
		return matrix;

	}
	
	private char[] getReferenceSequence(List<MI_Position> values) {
		
		Map<Integer,Character> chars = new HashMap<>();
		
		for (MI_Position pair : values) {
			
			chars.put(pair.getPos1(), pair.getAa1());
			
			chars.put(pair.getPos2(), pair.getAa2());
			
		}
		
		List<Integer> order = new ArrayList<>(chars.keySet());
		Collections.sort(order);
		
		char[] result = new char[order.size()];
		
		int counter=0;
		for (Integer integer : order) {
			
			result[counter] = chars.get(integer);
			
			counter++;
			
		}
		
		return result;
		
	}

	private List<Integer> getResidueLabels(List<MI_Position> values) {
		
		Set<Integer> labels = new TreeSet<Integer>();
		
		for (MI_Position pair : values) {
			
			labels.add(pair.getPos1());
			
			labels.add(pair.getPos2());
			
		}
		
		List<Integer> result = new ArrayList<>();
		
		result.addAll(labels);
		
		Collections.sort(result);
		
		return result;
		
	}

	public MI_Matrix read(Reader in) throws IOException {
		
		return this.read(new BufferedReader(in));
		
	}
	
	public MI_Matrix read(File in) throws IOException {

		return this.read(new BufferedReader(new FileReader(in)));
		
	}
	
	public MI_Matrix readZipped(File infile){
	
	ZipFile zf;
	
	try {
		zf = new ZipFile(infile);
		
		Enumeration<? extends ZipEntry> entries = zf.entries();
		
		InputStream fis = zf.getInputStream(entries.nextElement());
		
		MI_Matrix a = this.read(new BufferedReader(new InputStreamReader(fis)));
		
		zf.close();
		
		return a;
			
		
		
	} catch (ZipException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	}

	return null;
	
	}
		
}
