package utils.mutualinformation.misticmod.datastructures;

import java.util.List;

public class MI_PositionWithProtein extends MI_Position {

	////////////////////////
	// Instance Variables
	private int protein_1;

	private int protein_2;
	
	///////////////////////////////////////
	// Constructors
	public MI_PositionWithProtein(int pos1, int pos2, char aa1, char aa2, Double mi) {

		super(pos1, pos2, aa1, aa2, mi);
		
	}
	public MI_PositionWithProtein(int pos1, int pos2, char aa1, char aa2, Double mi, Double raw_mi) {

		super(pos1, pos2, aa1, aa2, mi, raw_mi);
		
	}

	private MI_PositionWithProtein() {
		super();
	}

	//////////////////////////////////////
	// Public Interface
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
	 * Parses MI output from MISTIC and MN soft
	 * 
	 * @param positionLine each line of the output mi calculation
	 * @return
	 */
	public static MI_PositionWithProtein valueOf(String positionLine) { 

		MI_PositionWithProtein new_pos = new MI_PositionWithProtein();
		
		MI_Position.fillPosition(new_pos,positionLine);
		
		return new_pos;
		
	}
	
	/**
	 * Sets the protein data that correspond for each protein
	 * 
	 * @param lengths
	 */
	public void assignProteinNumber(List<Integer> lengths) {
		
		this.setProtein_1(this.getProteinNumberFromPos(this.getPos1(),lengths));
		
		this.setProtein_2(this.getProteinNumberFromPos(this.getPos2(),lengths));
		
	}

	/////////////////////////////////////////
	// Private methods
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
