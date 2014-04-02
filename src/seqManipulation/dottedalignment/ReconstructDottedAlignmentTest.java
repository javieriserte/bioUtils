package seqManipulation.dottedalignment;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pair.Pair;

public class ReconstructDottedAlignmentTest {

	Pair<String, String> s1 = new Pair<String, String>("s1",  "ATCGACTAGCATCGACTG");
	
	Pair<String, String> s2 = new Pair<String, String>("s1",  ".....GATCGTAGCTGAC");

	Pair<String, String> s3 = new Pair<String, String>("s1", ".............CTGAC");
	
	List<Pair<String,String>> al1 = new ArrayList<Pair<String,String>>();
	
	@Before
	public void setUp() throws Exception {
		
		al1.add(s1);
		al1.add(s2);
		al1.add(s3);
		
		
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testReconstructListOfPairOfStringStringInt() {

		List<Pair<String, String>> na = ReconstructDottedAlignment.reconstruct(al1, 0);
		
		assertEquals(3, na.size());

		assertEquals("ATCGACTAGCATCGACTG",na.get(0).getSecond());
		
		assertEquals("ATCGAGATCGTAGCTGAC",na.get(1).getSecond());
		
		assertEquals("ATCGACTAGCATCCTGAC",na.get(2).getSecond());

		
	}

	@Test
	public void testReconstructListOfPairOfStringString() {

		List<Pair<String, String>> na = ReconstructDottedAlignment.reconstruct(al1);
		
		assertEquals(3, na.size());

		assertEquals("ATCGACTAGCATCGACTG",na.get(0).getSecond());
		
		assertEquals("ATCGAGATCGTAGCTGAC",na.get(1).getSecond());
		
		assertEquals("ATCGACTAGCATCCTGAC",na.get(2).getSecond());
		
	}

	@Test
	public void testReconstructSequence() {
		
		assertEquals("ATCGAGATCGTAGCTGAC", ReconstructDottedAlignment.reconstructSequence(".....GATCGTAGCTGAC", "ATCGACTAGCATCGACTG"));
		
		assertEquals("ATCGACTAGCATCGACTG", ReconstructDottedAlignment.reconstructSequence("..................", "ATCGACTAGCATCGACTG"));

		assertEquals("NNNNNGATCGTAGCTGAC", ReconstructDottedAlignment.reconstructSequence("NNNNNGATCGTAGCTGAC", "ATCGACTAGCATCGACTG"));
		
	}

}
