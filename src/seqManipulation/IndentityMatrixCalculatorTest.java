package seqManipulation;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import fileformats.fastaIO.Pair;

public class IndentityMatrixCalculatorTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void calculateIdentityMatrixTest() {
		
		String s1 = "AAAA";
		String s2 = "AABB";
		String s3 = "BBBB";
		String s4 = "B--B";
		String s5 = "B--C";
		
		
		
		List<Pair<String,String>> alignment = new ArrayList<Pair<String,String>>();
		
		alignment.add(new Pair<String,String>("s1",s1));
		alignment.add(new Pair<String,String>("s2",s2));
		alignment.add(new Pair<String,String>("s3",s3));
		alignment.add(new Pair<String,String>("s1_2",s1));
		alignment.add(new Pair<String,String>("s4",s4));
		alignment.add(new Pair<String,String>("s4",s5));
		
		
		
		
		Map<Pair<Integer,Integer>,Double> res = IndentityMatrixCalculator.calculateIdentityMatrix(alignment);
		
		assertEquals(0.5, res.get(new Pair<Integer,Integer>(0,1)), 0.01);
		assertEquals(0.0, res.get(new Pair<Integer,Integer>(0,2)), 0.01);
		assertEquals(1.0, res.get(new Pair<Integer,Integer>(0,3)), 0.01);
		assertEquals(0.5, res.get(new Pair<Integer,Integer>(1,2)), 0.01);
		
		assertEquals(0.0, res.get(new Pair<Integer,Integer>(0,4)), 0.01);
		assertEquals(0.25, res.get(new Pair<Integer,Integer>(1,4)), 0.01);
		assertEquals(0.5, res.get(new Pair<Integer,Integer>(2,4)), 0.01);
		assertEquals(0.5, res.get(new Pair<Integer,Integer>(4,5)), 0.01);

		
		
		
	}
	

}
