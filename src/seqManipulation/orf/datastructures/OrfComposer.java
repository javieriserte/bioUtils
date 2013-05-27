package seqManipulation.orf.datastructures;

public class OrfComposer {
	
	private OrfMarks marks;
	
	private int unitlength;
	
	private String sequence;

	public OrfComposer(OrfMarks marks, int unitlength, String sequence) {
		super();
		this.marks = marks;
		this.unitlength = unitlength;
		this.sequence = sequence;
	}

	public OrfMarks getMarks() {
		return marks;
	}

	public void setMarks(OrfMarks marks) {
		this.marks = marks;
	}

	public int getUnitlength() {
		return unitlength;
	}

	public void setUnitlength(int unitlength) {
		this.unitlength = unitlength;
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}
	
	
	

}
