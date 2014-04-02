package utils.ConservationImage.managers;

import java.util.List;

import utils.ConservationImage.ClustalProfiler;

import pair.Pair;

public class ProteinManager extends MoleculeManager{

	public static String alphabet = "ACDEFGHIKLMNPQRSTVWY";
	
	@Override
	public int alphabetSize() {
		return 20;
	}

	@Override
	public String alphabet() {
		return ProteinManager.alphabet;
	}

	@Override
	public String getProfile(ClustalProfiler clustalProfiler, List<Pair<String, String>> alin, GapManager gap) {
		return clustalProfiler.profileForProtein(alin,gap);
	}

}
