package seqManipulation.dottedalignment;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fileformats.fastaIO.Pair;

public class ConstructDottedAlignmentTest extends ConstructDottedAlignment {
	
	List<Pair<String,String>> al1 = new ArrayList<Pair<String,String>>();;

	Pair<String, String> s1 = new Pair<String, String>("s1",  "ATCGACTAGCATCGACTG");
		
	Pair<String, String> s2 = new Pair<String, String>("s1", ".....GATCGTAGCTGAC");

	Pair<String, String> s3 = new Pair<String, String>("s1", ".............CTGAC");
	
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
	public void testGetDottedAlignment() {
		try {
			List<Pair<String,String>> na = ConstructDottedAlignment.getDottedAlignment(al1, 0);
			
			assertEquals(al1.get(0).getSecond(), na.get(0).getSecond());
			assertEquals(al1.get(1).getSecond(), na.get(1).getSecond());
			assertEquals(al1.get(2).getSecond(), na.get(2).getSecond());
			
			
		} catch (ReferenceSequenceOutOfIndex e) {
			
			e.printStackTrace();
			
		}
		
		
	}

	@Test
	public void testGetDottedSequence() {
		
		assertEquals(".....",ConstructDottedAlignment.getDottedSequence("ATGTG", "ATGTG"));
		
		assertEquals("G....",ConstructDottedAlignment.getDottedSequence("GTGTG", "ATGTG"));

		assertEquals("TAAAA",ConstructDottedAlignment.getDottedSequence("TAAAA", "ATGTG"));

		
	}

}
