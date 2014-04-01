package utils.ConservationImage;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import utils.ConservationImage.managers.GapManager;
import utils.ConservationImage.managers.MoleculeManager;
import fileformats.fastaIO.Pair;

public class ClustalProfiler extends Profiler {
	
	private static String[] majorSets = new String[]{"STA","NEQK", "NHQK", "NDEQ", "QHRK", "MILV","MILF","HY", "FYW"};
	
	private static String[] minorSets = new String[]{"CSA","ATV", "SAG", "STNK", "STPA", "SGND","SNDEQK","NDEQHK", "NEQHRK","FVLIM","HFY"};

	///////////////////////
	// Public interface
	/**
	 * Retrieves the values for the plot 
	 */
	public double[] getdata(List<Pair<String, String>> alin, MoleculeManager manager, GapManager gap) {
		
		String clustalProfile = manager.getProfile(this,alin, gap);
		
		return this.getDataFromClustal(clustalProfile);

	}
	
	
	/**
	 * Creates a profile for a DNA molecule
	 * @param alin
	 * @return
	 */
	public String profileForDNA(List<Pair<String, String>> alin, GapManager gap) {
		
		StringBuilder sb = new StringBuilder();
		
		int Len = alin.get(0).getSecond().length();
		
		for (int x=0; x<Len;x++) {
			
			Set<Character> cs = getCharsAtColumn(alin,x,gap);
			
			if (cs.size()==1) {
				
				sb.append("*");
				
			} else {
				
				sb.append(" ");
				
			}; 
		}
		return sb.toString();
	}

/**
 * Given an array of String, converts it to a List of Sets. Where each set represents one of the strings and the elements within the set are the characters from the string. 
 * @param strings an array of strings
 * @return
 */
private List<Set<Character>> getSetListFromStringArray(String[] strings) {
	List<Set<Character>> setslist = new Vector<Set<Character>>();
	for(String s: strings) {
		Set<Character> sc = new HashSet<Character>();
		for (char c: s.toCharArray()) { sc.add(c); }
		setslist.add(sc);
	}
	return setslist;
}

/**
 * Given a List<Pair<String, String>> that represents a multiple sequence alignment, this method returns the set of Characters from a single column. 
 * @param alin List<Pair<String, String>> that represents an alignment.
 * @param column is the position number of the column.
 * @return an array of Character with all the characters in the column
 */
private Set<Character> getCharsAtColumn(List<Pair<String, String>> alin, int column, GapManager gap) {
	
	Set<Character> set = new HashSet<Character>();
	
	for (int x=0;x<alin.size(); x++) {
		
		set.add(alin.get(x).getSecond().charAt(column));
		
	}
	
	return gap.attempToDeleteGaps(set) ;
	
}

private double[]			getDataFromClustal					(String line) {
	double[] data = new double[line.length()];
	
	for (int i = 0; i < data.length; i++) {
		switch (line.charAt(i)) {
		case '*': data[i] = 1; 	 	break;

		case ':': data[i] = 0.5; 	break;

		case '.': data[i] = 0.25; 	break;

		case ' ': data[i] = 0; 		break;
			
		default:   					break;
		}
	}
	
	return data;
}


/**
 * A line above the alignment is used to mark strongly conserved positions. Three characters ('*', ':' and '.') are used:<br>
 * <b>'*'</b> indicates positions which have a single, fully conserved residue.<br>
 * <b>':'</b> indicates that one of the following 'strong' groups is fully conserved: <br>
 * <ol type=disc>
 * <li>STA</li>
 * <li>NEQK</li>
 * <li>NHQK</li>
 * <li>NDEQ</li>
 * <li>QHRK</li>
 * <li>MILV</li>
 * <li>MILF</li>
 * <li>HY</li>
 * <li>FYW</li>
 * </ol>
 * '.' indicates that one of the following 'weaker' groups is fully conserved:<br>
 * <ol type=disc>
 * <li>CSA</li>
 * <li>ATV</li>
 * <li>SAG</li>
 * <li>STNK</li>
 * <li>STPA</li>
 * <li>SGND</li>
 * <li>SNDEQK</li>
 * <li>NDEQHK</li>
 * <li>NEQHRK</li>
 * <li>FVLIM</li>
 * <li>HFY</li>
 * </ol>
 * These are all the positively scoring groups that occur in the Gonnet Pam250 matrix. The strong and weak groups are defined as strong score >0.5 and weak score =<0.5 respectively.
 */
	public String profileForProtein(List<Pair<String, String>> alin,GapManager gap) {
		
		List<Set<Character>> majorSetsList = getSetListFromStringArray(majorSets);
		
		List<Set<Character>> minorSetsList = getSetListFromStringArray(minorSets);
		
		StringBuilder sb = new StringBuilder();
		int Len = alin.get(0).getSecond().length();
		
		for (int x=0; x<Len;x++) {
			Set<Character> cs = getCharsAtColumn(alin,x, gap);
			if (cs.size()==1) {
				// check for identity
				sb.append("*");
			} else {
				// check major groups
				boolean found = false;
				for (Set<Character> group : majorSetsList) {
					if (!found && group.equals(cs)) {
						found = true;
					}
				}
				if (found) {
					sb.append(":");
				} else {
					// check minor groups
					for (Set<Character> group : minorSetsList) {
						if (!found && group.equals(cs)) {
							found = true;
						}
					}
					if (found) {
						sb.append(".");
					} else {
						sb.append(" ");					
					}
				}
			}
		}
		return sb.toString();
	}
	
	public String toString() {
		
		return "Clustal Profiler";
		
	}

}
