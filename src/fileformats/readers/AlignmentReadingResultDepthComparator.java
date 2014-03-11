package fileformats.readers;

import java.util.Comparator;

public class AlignmentReadingResultDepthComparator  implements Comparator<AlignmentReadingResult> {

	@Override
	public int compare(AlignmentReadingResult o1, AlignmentReadingResult o2) {
		
		/////////////////////////////////
		// If one or both are null
		int r1 = (o1==null)?1:0;
		int r2 = (o2==null)?2:0;

		if (r1+r2!=0) {
			return r2 - 2*r1;
		}
		////////////////////////////////
		
		/////////////////////////////////
		// If one or both are successful
		int s1 = (o1.successfulRead())?1:0;
		int s2 = (o2.successfulRead())?2:0;

		if (s1+s2!=0) {
			return s2 - 2*s1;
		}
		////////////////////////////////

		/////////////////////////////////
		// If none or both are successful
		int x1 = o1.getFault().getWrongLineNumber();
		int x2 = o2.getFault().getWrongLineNumber();

		return x1 - x2;
		
		////////////////////////////////
	}
	
};

