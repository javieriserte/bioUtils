package utils.ConservationImage;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import fileformats.fastaIO.FastaMultipleReader;
import pair.Pair;

import utils.ConservationImage.managers.GapManager;
import utils.ConservationImage.managers.MoleculeManager;

public abstract class Profiler {

	///////////////////////////
	// Public interface
	/**
	 * Retrieve data for each point of a conservation profile.
	 * 
	 * @param inputFastaAlignment
	 * @param manager
	 * @param gap
	 * @return
	 */
	public double[] getdata(InputStream inputFastaAlignment, MoleculeManager manager, GapManager gap){
		
		FastaMultipleReader fmr = new FastaMultipleReader();
		
		List<Pair<String, String>> alin = null;
		
		alin = fmr.readBuffer(new BufferedReader(new InputStreamReader(inputFastaAlignment)));
		
		alin = this.replaceUnexpectedCharstoGaps(alin, manager);
		
		return this.getdata(alin, manager, gap);
		
	}
	

	
	public abstract double[] getdata(List<Pair<String, String>> alin, MoleculeManager manager, GapManager gap);
	
	/**
	 * Replace chars in a sequence of DNA or Protein that are not non-degenerated bases or amino acids into gaps 
	 * 
	 * @param alin
	 * @param manager
	 * @return
	 */
	public List<Pair<String,String>> replaceUnexpectedCharstoGaps(List<Pair<String,String>> alin, MoleculeManager manager) {
		
		List<Pair<String,String>> newAlin = new ArrayList<Pair<String,String>>();
		
		for (Pair<String, String> pair : alin) {
			
			Pair<String, String> newPair = new Pair<String, String>(pair.getFirst(), pair.getSecond().replaceAll("[^"+manager.alphabet()+"\\-]", "-"));
			
			newAlin.add(newPair);
			
		}
		
		return newAlin;
		
	}
	
	/////////////////////////
	// Private methods
	

}
