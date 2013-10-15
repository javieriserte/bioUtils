package seqManipulation.gapstripper;

import static org.junit.Assert.*;

import java.util.LinkedHashMap;

import org.junit.Test;

public class GapstripperTest extends Gapstripper {

	@Test
	public void testRemoveColumnsAndRows() {
		fail("Not yet implemented");
	}

	@Test
	public void testCalculateGapFrequency() {
		Gapstripper stripper = new Gapstripper();
		
		String s1 ="AAAAAAAAAA";
		String s2 ="AAAAAAAA--";
		String s3 ="AAAAAA----";
		String s4 ="AAAA------";
		String s5 ="AA--------";
		String s6 ="----------";
		
		double[]  f = new double[6];
		
		f[0] = stripper.calculateGapFrequency(s1);
		f[1] = stripper.calculateGapFrequency(s2);
		f[2] = stripper.calculateGapFrequency(s3);
		f[3] = stripper.calculateGapFrequency(s4);
		f[4] = stripper.calculateGapFrequency(s5);
		f[5] = stripper.calculateGapFrequency(s6);
		
		assertArrayEquals(new double[]{0,0.2,0.4,0.6,0.8,1.0}, f, 0.00001);
		
		
	}

	@Test
	public void testMaskAnd() {
		
		boolean[] array_1 = new boolean[]{true, true,false,false};
		boolean[] array_2 = new boolean[]{true, false,true,false};
		
		array_1 = (new Gapstripper()).MaskAnd(array_2, array_1);
		
		assertTrue(array_1[0]);
		assertFalse(array_1[1]);
		assertFalse(array_1[2]);
		assertFalse(array_1[3]);
		
		
	}

	@Test
	public void testGetReferenceSequenceMask() {
		Gapstripper stripper = new Gapstripper();
		
		String s1 ="AAAAAAAAAA";
		String s2 ="AAAAAAAA--";
		String s3 ="AAAAAA----";
		String s4 ="AAAA------";
		String s5 ="AA--------";
		String s6 ="----------";
		
		LinkedHashMap<String, String> sequences = new LinkedHashMap<>();
		
		sequences.put("s1", s1);
		sequences.put("s2", s2);
		sequences.put("s3", s3);
		sequences.put("s4", s4);
		sequences.put("s5", s5);
		sequences.put("s6", s6);

		
		boolean[] b1 = stripper.getReferenceSequenceMask(sequences, "s1");
		boolean[] b2 = stripper.getReferenceSequenceMask(sequences, "s2");
		boolean[] b3 = stripper.getReferenceSequenceMask(sequences, "s3");
		boolean[] b4 = stripper.getReferenceSequenceMask(sequences, "s4");
		boolean[] b5 = stripper.getReferenceSequenceMask(sequences, "s5");
		boolean[] b6 = stripper.getReferenceSequenceMask(sequences, "s6");
		
		assertArrayEquals(
				new Boolean[]{true,true,true,true,true,true,true,true,true,true}, 
				new Boolean[]{b1[0],b1[1],b1[2],b1[3],b1[4],b1[5],b1[6],b1[7],b1[8],b1[9]}
				);

		assertArrayEquals(
				new Boolean[]{true,true,true,true,true,true,true,true,false,false}, 
				new Boolean[]{b2[0],b2[1],b2[2],b2[3],b2[4],b2[5],b2[6],b2[7],b2[8],b2[9]}
				);

		assertArrayEquals(
				new Boolean[]{true,true,true,true,true,true,false,false,false,false}, 
				new Boolean[]{b3[0],b3[1],b3[2],b3[3],b3[4],b3[5],b3[6],b3[7],b3[8],b3[9]}
				);

		assertArrayEquals(
				new Boolean[]{true,true,true,true,false,false,false,false,false,false}, 
				new Boolean[]{b4[0],b4[1],b4[2],b4[3],b4[4],b4[5],b4[6],b4[7],b4[8],b4[9]}
				);

		assertArrayEquals(
				new Boolean[]{true,true,false,false,false,false,false,false,false,false}, 
				new Boolean[]{b5[0],b5[1],b5[2],b5[3],b5[4],b5[5],b5[6],b5[7],b5[8],b5[9]}
				);

		assertArrayEquals(
				new Boolean[]{false,false,false,false,false,false,false,false,false,false}, 
				new Boolean[]{b6[0],b6[1],b6[2],b6[3],b6[4],b6[5],b6[6],b6[7],b6[8],b6[9]}
				);

		
	}

	@Test
	public void testGetReferenceSequenceByIndex() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetReferenceSequenceById() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetMaxFreqMask() {
		fail("Not yet implemented");
	}

}
