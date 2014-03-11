package fileformats.readers;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.junit.Test;

import fileformats.readers.faults.AlignmentReadingFault;
import fileformats.readers.faults.BlankAlignmentFault;
import pair.Pair;

public class AlignmentReadingResultDepthComparatorTest extends
		AlignmentReadingResultDepthComparator {

	@Test
	public void testCompare() {

		List<pair.Pair<String, String>> al1 = new ArrayList<Pair<String,String>>();
		al1.add(new Pair<String, String>("al1_s1", "AAA"));
		al1.add(new Pair<String, String>("al1_s2", "BBB"));
		List<pair.Pair<String, String>> al2 = new ArrayList<Pair<String,String>>();
		al2.add(new Pair<String, String>("al2_s1", "CCC"));
		al2.add(new Pair<String, String>("al2_s2", "DDD"));
		
		AlignmentReadingResult a1 = null;
		AlignmentReadingResult a2 = null;
		AlignmentReadingResult a3 = new AlignmentReadingResult();
		a3.setAlignment(al1);
		AlignmentReadingResult a4 = new AlignmentReadingResult();
		a4.setAlignment(al2);

		AlignmentReadingResult a5 =  new AlignmentReadingResult();
		AlignmentReadingFault f1 = new BlankAlignmentFault();
		f1.setWrongLineNumber(5);
		a5.setFault(f1);
		AlignmentReadingResult a6 = new AlignmentReadingResult();;
		AlignmentReadingFault f2 = new BlankAlignmentFault();
		f2.setWrongLineNumber(15);
		a6.setFault(f2);
		
		Comparator<AlignmentReadingResult> comp = new AlignmentReadingResultDepthComparator();
		
		assertEquals(0, comp.compare(a1, a2));
		assertEquals(0, comp.compare(a2, a1));
		
		assertTrue( comp.compare(a3, a1)>0);
		assertFalse( comp.compare(a1, a3)>0);
		
		assertEquals(0, comp.compare(a3, a4));
		assertEquals(0, comp.compare(a4, a3));
		
		assertTrue( comp.compare(a5, a4)>0);
		assertTrue( comp.compare(a5, a3)>0);
		
		assertTrue( comp.compare(a6, a5)>0);
		assertFalse( comp.compare(a5, a6)>0);

	}

}
