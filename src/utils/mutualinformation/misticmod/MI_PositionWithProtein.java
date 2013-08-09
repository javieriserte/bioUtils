package utils.mutualinformation.misticmod;

import java.util.List;

public class MI_PositionWithProtein extends MI_Position {

	////////////////////////
	// Instance
	private int protein_1;

	private int protein_2;
	
	public MI_PositionWithProtein(int pos1, int pos2, char aa1, char aa2, Double mi) {

		super(pos1, pos2, aa1, aa2, mi);
		
	}

	public int getProtein_1() {
		return protein_1;
	}

	public void setProtein_1(int prot1) {
		this.protein_1 = prot1;
	}

	public int getProtein_2() {
		return protein_2;
	}

	public void setProtein_2(int prot2) {
		this.protein_2 = prot2;
	}
	
	/**
	 * Parses MI output from MISTIC
	 * 
	 * @param positionLine
	 * @return
	 */
	public static MI_PositionWithProtein valueOf(String positionLine) { 
		
		String[] data = positionLine.split("\t");

		return new MI_PositionWithProtein(Integer.valueOf(data[0]), Integer.valueOf(data[2]), data[1].charAt(0), data[3].charAt(0), Double.valueOf(data[4]));
		
		
	}
	
	/**
	 * Sets the protein data that correspond for each protein
	 * 
	 * @param lengths
	 */
	public void assignProteinNumber(List<Integer> lengths) {
		
		this.setProtein_1(this.getProteinNumberFromPos(this.pos1,lengths));
		
		this.setProtein_2(this.getProteinNumberFromPos(this.pos2,lengths));
		
	}

	/**
	 * Get the protein value from the position
	 * 
	 * @param position
	 * @param proteinLengths
	 * @return
	 */
	private int getProteinNumberFromPos(int position, List<Integer> proteinLengths) {
		
		for (int i = 0; i<proteinLengths.size();i++) {
			
			position = position - proteinLengths.get(i);
			
			if (position<=0) return i;
			
		}
		
		return 0;
		
	}

}
