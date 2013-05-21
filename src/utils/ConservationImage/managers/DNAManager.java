package utils.ConservationImage.managers;

import java.util.List;

import utils.ConservationImage.ClustalProfiler;

import fileformats.fastaIO.Pair;

public class DNAManager extends MoleculeManager{

	public static String alphabet = "ACTG";
	
	@Override
	public int alphabetSize() {
		return 4;
	}

	@Override
	public String alphabet() {
		return DNAManager.alphabet;
	}

	@Override
	public String getProfile(ClustalProfiler clustalProfiler, List<Pair<String, String>> alin, GapManager gap) {
		return clustalProfiler.profileForDNA(alin,gap);
	}

}
