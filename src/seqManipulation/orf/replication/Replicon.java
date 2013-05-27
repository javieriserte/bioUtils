package seqManipulation.orf.replication;

public class Replicon {
	
	////////////////////////
	// Instance variable
	int[] ATG;
	int[] Stop;
	String sequence;
	
	////////////////////////
	// Constructor
	public Replicon(int[] aTG, int[] stop, String sequence) {
		super();
		ATG = aTG;
		Stop = stop;
		this.sequence = sequence;
	}
	
	///////////////////////
	// Getters and setters
	public int[] getATG() {
		return ATG;
	}
	public void setATG(int[] aTG) {
		ATG = aTG;
	}
	public int[] getStop() {
		return Stop;
	}
	public void setStop(int[] stop) {
		Stop = stop;
	}
	public String getSequence() {
		return sequence;
	}
	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	
	
	
}
