package utils.mutualinformation.mimatrixviewer.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import utils.mutualinformation.mimatrixviewer.MI_Matrix;

public class MI_MatrixTest extends MI_Matrix {

	
	public MI_MatrixTest() {
		super(0, false, false, false, new ArrayList<Integer>());
	}
	
	MI_Matrix mat ;
	
	@Before
	public void setUp() throws Exception {
		
		List<Integer> labels = new ArrayList<>();
		
		labels.add(11);
		labels.add(12);
		labels.add(13);
		labels.add(14);
		labels.add(15);	
		
		this.mat = new MI_Matrix(5, true, true, true, labels);
		
		this.mat.setReferenceSequence("MQWER");
		
		this.mat.setZscoreValue(1, 11, 12);
		this.mat.setZscoreValue(MI_Matrix.UNDEFINED, 11, 13);
		this.mat.setZscoreValue(2, 11, 14);
		this.mat.setZscoreValue(3, 11, 15);
		this.mat.setZscoreValue(4, 12, 13);
		this.mat.setZscoreValue(5, 12, 14);
		this.mat.setZscoreValue(6, 12, 15);
		this.mat.setZscoreValue(7, 13, 14);
		this.mat.setZscoreValue(8, 13, 15);
		this.mat.setZscoreValue(9, 14, 15);
		
	}

	@Test
	public void testCount() {

		assertEquals(10, this.mat.count());
		
	}

	@Test
	public void testCountDefinedValues() {
		assertEquals(9, this.mat.countDefinedValues());
	}

	@Test
	public void testGetReferenceSequenceCharAt() {
		assertEquals('M', this.mat.getReferenceSequenceCharAt(11));
		assertEquals('Q', this.mat.getReferenceSequenceCharAt(12));
		assertEquals('W', this.mat.getReferenceSequenceCharAt(13));
		assertEquals('E', this.mat.getReferenceSequenceCharAt(14));
		assertEquals('R', this.mat.getReferenceSequenceCharAt(15));
		
	}

	@Test
	public void testGetReferenceSequenceAsString() {
		assertEquals("MQWER", this.mat.getReferenceSequenceAsString());	}

	@Test
	public void testGetZscoreValue() {

		assertEquals(1, this.mat.getZscoreValue(11, 12),0.1);
		assertEquals(4, this.mat.getZscoreValue(13, 12),0.1);
		assertEquals(5, this.mat.getZscoreValue(12, 14),0.1);
		assertEquals(8, this.mat.getZscoreValue(13, 15),0.1);
		assertEquals(9, this.mat.getZscoreValue(15, 14),0.1);

	}

	@Test
	public void testGetSize() {
		assertEquals(5, this.mat.getSize());
	}

	@Test
	public void testGetMaxZscore() {
		assertEquals(9, this.mat.getMaxZscore(),0.1);
	}

	@Test
	public void testGetMinZscore() {
		assertEquals(1, this.mat.getMinZscore(),0.1);
	}

}
