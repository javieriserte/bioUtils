package utils.mutualinformation.misticmod;

import java.util.regex.Pattern;

/**
 * A class to manage data from MI_data generated by MISTIC
 * 
 * @author javier iserte
 *
 */
public class MI_Position {

	//////////////////////////////
	// Class variables
	/**
	 * 	Pattern to match the output from Mistic<br>
	 *  Sample Data:<br>
	 *  <pre>
	 *  1	M	2	A	12.182070
	 *  </pre>
	 */
	protected final static String MISTIC_FORMAT_LINE = "\\d+\\t[A-Z\\-]\\t\\d+\\t[A-Z\\-]\\t-*\\d+\\.?\\d*[eE]*[-+]*\\d*$";

	/**
	 * Pattern to match the output from Mortem Nielsen soft<br>
	 * Sample Data:<br>
	 * <pre>
	 * MI[ 1 G ][ 6 Q ] = -0.029194 0.003595 0.016705 -1.962794
	 * </pre>
	 */
	protected final static String MN_FORMAT_LINE = "MI\\[ \\d+ [A-Z\\-] \\]\\[ \\d+ [A-Z\\-] \\] = -*\\d+\\.?\\d*[eE]*[-+]*\\d* -*\\d+\\.?\\d*[eE]*[-+]*\\d* -*\\d+\\.?\\d*[eE]*[-+]*\\d* -*\\d+\\.?\\d*[eE]*[-+]*\\d*$";

	////////////////////////
	// Instance Variables
	private int pos1;
	private int pos2;
	private char aa1;
	private char aa2;
	private Double mi;
	private Double raw_mi;

	//////////////////////////////////
	// Constructor
	public MI_Position(int pos1, int pos2, char aa1, char aa2, Double mi) {
		super();
		this.setPos1(pos1);
		this.setPos2(pos2);
		this.setAa1(aa1);
		this.setAa2(aa2);
		this.setMi(mi);
	}
	public MI_Position(int pos1, int pos2, char aa1, char aa2, Double mi, Double raw_mi) {
		super();
		this.setPos1(pos1);
		this.setPos2(pos2);
		this.setAa1(aa1);
		this.setAa2(aa2);
		this.setMi(mi);
		this.setRaw_mi(raw_mi);
	}
	protected MI_Position() {
		super();

	}

	/////////////////////////
	// Public Interface 
	@Override
	public String toString() {
		return this.getPos1() + "\t" + this.getAa1() + "\t" + this.getPos2() + "\t" + this.getAa2()+ "\t" + this.getMi();
	}
	
	/**
	 * Parses MI output from MISTIC and MN soft
	 * 
	 * @param positionLine
	 * @return
	 */
	public static MI_Position valueOf(String positionLine) { 

		MI_Position new_pos = new MI_Position();
		
		MI_Position.fillPosition(new_pos,positionLine);
		
		return new_pos;
		
	}

	////////////////////////////////////////////
	// Getters and setters
	protected void setPos1(int pos1) {
		this.pos1 = pos1;
	}

	protected void setPos2(int pos2) {
		this.pos2 = pos2;
	}

	protected void setAa1(char aa1) {
		this.aa1 = aa1;
	}

	protected void setAa2(char aa2) {
		this.aa2 = aa2;
	}

	protected void setMi(Double mi) {
		this.mi = mi;
	}
	protected void setRaw_mi(Double raw_mi) {
		this.raw_mi = raw_mi;
	}

	public int getPos1() {
		return pos1;
	}

	public int getPos2() {
		return pos2;
	}

	public char getAa1() {
		return aa1;
	}

	public char getAa2() {
		return aa2;
	}

	public Double getMi() {
		return mi;
	}
	public Double getRaw_mi() {
		return raw_mi;
	}
	
	///////////////////////////////
	// Protected static Methods
	
	protected static void fillPosition(MI_Position pos, String positionLine) {
		
		int pos1;
		int pos2; 
		char aa1;
		char aa2;
		double mi;
		double raw_mi;

		//////////////////////////////////
		// Must Choose between MN format and
		// MISTIC format.
		if (Pattern.matches(MI_Position.MISTIC_FORMAT_LINE, positionLine)) {
		// If input line matches with MISTIC pattern.
		
			String[] data = positionLine.split("\t");
			
			pos1 = Integer.valueOf(data[0]);
			pos2 = Integer.valueOf(data[2]);
			aa1 = data[1].charAt(0);
			aa2 = data[3].charAt(0);
			mi = Double.valueOf(data[4]);
			raw_mi = 0;
			
		} else if (Pattern.matches(MI_Position.MN_FORMAT_LINE, positionLine)) {
		// If input line matches with Mortem Nielsen format

			String[] data = positionLine.split(" ");
			
			pos1 = Integer.valueOf(data[1]);
			pos2 = Integer.valueOf(data[4]);
			aa1 = data[2].charAt(0);
			aa2 = data[5].charAt(0);
			mi = Double.valueOf(data[11]);
			raw_mi = Double.valueOf(data[8]);

		} else {
		
			return;
	
		}
		
		pos.setPos1(pos1);
		pos.setPos2(pos2);
		pos.setAa1(aa1);
		pos.setAa2(aa2);
		pos.setMi(mi);
		pos.setRaw_mi(raw_mi);

	}
	
}

