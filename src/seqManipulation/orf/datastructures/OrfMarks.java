package seqManipulation.orf.datastructures;

import java.util.List;

public class OrfMarks {

	//////////////////////////
	// Instance Variables
	private List<Integer>[] ATGsAndSTOPsByFrame;
	
	
	private List<Boolean>[] ATGorStop;

	
    //////////////////////////
	// Public interface
	public OrfMarks(List<Integer>[] aTGsAndSTOPsByFrame,
			List<Boolean>[] aTGorStop) {
		super();
		ATGsAndSTOPsByFrame = aTGsAndSTOPsByFrame;
		ATGorStop = aTGorStop;
	}

    ///////////////////////////
	// Getters and setters
	public List<Integer>[] getATGsAndSTOPsByFrame() {
		return ATGsAndSTOPsByFrame;
	}


	public void setATGsAndSTOPsByFrame(List<Integer>[] aTGsAndSTOPsByFrame) {
		ATGsAndSTOPsByFrame = aTGsAndSTOPsByFrame;
	}


	public List<Boolean>[] getATGorStop() {
		return ATGorStop;
	}


	public void setATGorStop(List<Boolean>[] aTGorStop) {
		ATGorStop = aTGorStop;
	}
	
	
	
	
}
