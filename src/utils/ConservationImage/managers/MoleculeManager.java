package utils.ConservationImage.managers;

import java.util.List;

import utils.ConservationImage.ClustalProfiler;

import fileformats.fastaIO.Pair;

public abstract class MoleculeManager {

	public abstract int alphabetSize();
	
	public abstract String alphabet();

	public abstract String getProfile(ClustalProfiler clustalProfiler, List<Pair<String, String>> alin, GapManager gap);
	
}
