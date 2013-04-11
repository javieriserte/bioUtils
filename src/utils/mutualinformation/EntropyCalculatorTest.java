package utils.mutualinformation;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class EntropyCalculatorTest extends EntropyCalculator {

	Character[] chars_tuples;
	Character[] chars_equals;
	Character[] chars_equals_gapped;
	Character[] chars_differents;
	Character[] dichars_a1;
	Character[] dichars_a2;
	
	@Before
	public void setUp() throws Exception {

	chars_equals  = new Character[]{'A','A','A','A','A','A','A','A'};
	
	chars_equals_gapped  = new Character[]{'A','A','A','A','-','-','-','-'};

	chars_tuples= new Character[]{'A','A','C','C','T','T','G','G'};
		
	chars_differents = new Character[]{'A','B','C','D','E','F','G','H'};
	
	dichars_a1 = new Character[]{'A','B','C','D','A','B','C','D','A','B','C','D','A','B','C','D'};
	
	dichars_a2 = new Character[]{'A','A','A','A','B','B','B','B','C','C','C','C','D','D','D','D'};
		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCalculateEntropyForDiCharacterArray() {
		

		double ent_dichars_a1_a2;	
		double ent_dichars_a1_a1;
		double ent_dichars_equals;
		
		try {
			ent_dichars_a1_a2 = EntropyCalculator.calculateEntropy(dichars_a1,dichars_a2, 4, true);
			ent_dichars_a1_a1 = EntropyCalculator.calculateEntropy(dichars_a1,dichars_a1, 4, true);
			ent_dichars_equals = EntropyCalculator.calculateEntropy(chars_equals,chars_equals, 4, true);
			
			assertEquals(2d, ent_dichars_a1_a2,0.001);
			assertEquals(1d, ent_dichars_a1_a1,0.001);
			assertEquals(0d, ent_dichars_equals,0.001);
			
			
		} catch (CharGroupSizeException e) {

			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testCalculateEntropyCharacterArrayIntBoolean() {
		
		double ent_equals = EntropyCalculator.calculateEntropy(chars_equals,4, true);
		double ent_equals_gappedOn = EntropyCalculator.calculateEntropy(chars_equals_gapped,4, true);
		double ent_equals_gappedOff = EntropyCalculator.calculateEntropy(chars_equals_gapped,4, false);
		double ent_different = EntropyCalculator.calculateEntropy(chars_differents, 8,true);
		double ent_tuples = EntropyCalculator.calculateEntropy(chars_tuples, 4,true);
		
		
		assertEquals(0d, ent_equals,0.001);
		assertEquals(1d/4, ent_equals_gappedOn,0.001);
		assertEquals(0d, ent_equals_gappedOff,0.001);
		assertEquals(1d, ent_different,0.001);
		assertEquals(1d, ent_tuples,0.001);
	}

	@Test
	public void testCalculateEntropyMapOfCharacterIntegerInt() {
		Map<Object, Double> freq_equals = EntropyCalculator.countCharacters(chars_equals);
		Map<Object, Double> freq_equals_gapped = EntropyCalculator.countCharacters(chars_equals_gapped);		
		Map<Object, Double> freq_tuples = EntropyCalculator.countCharacters(chars_tuples);
		Map<Object, Double> freq_different = EntropyCalculator.countCharacters(chars_differents);
		
		EntropyCalculator.convertToFrequencies(freq_equals, 8);
		EntropyCalculator.convertToFrequencies(freq_equals_gapped, 4);		
		EntropyCalculator.convertToFrequencies(freq_tuples, 8);
		EntropyCalculator.convertToFrequencies(freq_different, 8);
		
		double ent_equals = EntropyCalculator.calculateEntropy(freq_equals, 4);
		double ent_equals_gapped = EntropyCalculator.calculateEntropy(freq_equals_gapped, 4);		
		double ent_different = EntropyCalculator.calculateEntropy(freq_different, 8);
		double ent_tuples = EntropyCalculator.calculateEntropy(freq_tuples, 4);
		
		assertEquals(0d, ent_equals,0.001);
		assertEquals(0d, ent_equals_gapped,0.001);
		assertEquals(1d, ent_different,0.001);
		assertEquals(1d, ent_tuples,0.001);
		
	}

	@Test
	public void testConvertToFrequencies() {
		Map<Object, Double> freq_equals = EntropyCalculator.countCharacters(chars_equals);
		Map<Object, Double> freq_equals_gapped = EntropyCalculator.countCharacters(chars_equals_gapped);		
		Map<Object, Double> freq_tuples = EntropyCalculator.countCharacters(chars_tuples);
		Map<Object, Double> freq_different = EntropyCalculator.countCharacters(chars_differents);
		
		EntropyCalculator.convertToFrequencies(freq_equals, 8);
		EntropyCalculator.convertToFrequencies(freq_equals_gapped, 4);
		EntropyCalculator.convertToFrequencies(freq_tuples, 8);
		EntropyCalculator.convertToFrequencies(freq_different, 8);

		
		assertEquals(1, freq_equals.size(),0.001);
		assertTrue(freq_equals.containsKey('A'));
		assertFalse(freq_equals.containsKey('C'));
		assertEquals((double)1, (double)freq_equals.get('A'),0.001);
		
		assertEquals(1, freq_equals.size());
		assertTrue(freq_equals.containsKey('A'));
		assertFalse(freq_equals.containsKey('C'));
		assertEquals((double)1, (double)freq_equals.get('A'),0.001);
		
		assertEquals(4, freq_tuples.size());
		assertTrue(freq_tuples.containsKey('A'));
		assertTrue(freq_tuples.containsKey('C'));
		assertTrue(freq_tuples.containsKey('T'));
		assertTrue(freq_tuples.containsKey('G'));
		assertFalse(freq_tuples.containsKey('H'));
		assertEquals((double)(1d/4d), (double) freq_tuples.get('A'),0.001);
		assertEquals((double)(1d/4d), (double) freq_tuples.get('C'),0.001);
		assertEquals((double)(1d/4d), (double) freq_tuples.get('T'),0.001);
		assertEquals((double)(1d/4d), (double) freq_tuples.get('G'),0.001);
		

		assertEquals(8, freq_different.size());
		assertTrue(freq_different.containsKey('A'));
		assertTrue(freq_different.containsKey('B'));
		assertTrue(freq_different.containsKey('C'));
		assertTrue(freq_different.containsKey('D'));
		assertTrue(freq_different.containsKey('E'));
		assertTrue(freq_different.containsKey('F'));
		assertTrue(freq_different.containsKey('G'));
		assertTrue(freq_different.containsKey('H'));
		assertFalse(freq_different.containsKey('I'));
		assertEquals((double)(1d/8), (double) freq_different.get('A'),0.001);
		assertEquals((double)(1d/8), (double) freq_different.get('B'),0.001);
		assertEquals((double)(1d/8), (double) freq_different.get('C'),0.001);
		assertEquals((double)(1d/8), (double) freq_different.get('D'),0.001);
		assertEquals((double)(1d/8), (double) freq_different.get('E'),0.001);
		assertEquals((double)(1d/8), (double) freq_different.get('F'),0.001);
		assertEquals((double)(1d/8), (double) freq_different.get('G'),0.001);
		assertEquals((double)(1d/8), (double) freq_different.get('H'),0.001);

		
	}

	@Test
	public void testCountCharacters() {

		Map<Object, Double> freq_equals = EntropyCalculator.countCharacters(chars_equals);
		Map<Object, Double> freq_tuples = EntropyCalculator.countCharacters(chars_tuples);
		Map<Object, Double> freq_different = EntropyCalculator.countCharacters(chars_differents);
		Map<Object, Double> freq_equals_gapped = EntropyCalculator.countCharacters(chars_equals_gapped);
		
		assertEquals(2, freq_equals_gapped.size());
		assertTrue(freq_equals_gapped.containsKey('A'));
		assertTrue(freq_equals_gapped.containsKey('-'));
		assertFalse(freq_equals_gapped.containsKey('C'));
		assertEquals((double)4, (double)freq_equals_gapped.get('A'),0.001);
		assertEquals((double)4, (double)freq_equals_gapped.get('-'),0.001);

		
		assertEquals(1, freq_equals.size());
		assertTrue(freq_equals.containsKey('A'));
		assertFalse(freq_equals.containsKey('C'));
		assertEquals((double)8, (double)freq_equals.get('A'),0.001);
		
		assertEquals(4, freq_tuples.size());
		assertTrue(freq_tuples.containsKey('A'));
		assertTrue(freq_tuples.containsKey('C'));
		assertTrue(freq_tuples.containsKey('T'));
		assertTrue(freq_tuples.containsKey('G'));
		assertFalse(freq_tuples.containsKey('H'));
		assertEquals((double)2, (double) freq_tuples.get('A'),0.001);
		assertEquals((double)2, (double) freq_tuples.get('C'),0.001);
		assertEquals((double)2, (double) freq_tuples.get('T'),0.001);
		assertEquals((double)2, (double) freq_tuples.get('G'),0.001);
		

		assertEquals(8, freq_different.size());
		assertTrue(freq_different.containsKey('A'));
		assertTrue(freq_different.containsKey('B'));
		assertTrue(freq_different.containsKey('C'));
		assertTrue(freq_different.containsKey('D'));
		assertTrue(freq_different.containsKey('E'));
		assertTrue(freq_different.containsKey('F'));
		assertTrue(freq_different.containsKey('G'));
		assertTrue(freq_different.containsKey('H'));
		assertFalse(freq_different.containsKey('I'));
		assertEquals((double)1, (double) freq_different.get('A'),0.001);
		assertEquals((double)1, (double) freq_different.get('B'),0.001);
		assertEquals((double)1, (double) freq_different.get('C'),0.001);
		assertEquals((double)1, (double) freq_different.get('D'),0.001);
		assertEquals((double)1, (double) freq_different.get('E'),0.001);
		assertEquals((double)1, (double) freq_different.get('F'),0.001);
		assertEquals((double)1, (double) freq_different.get('G'),0.001);
		assertEquals((double)1, (double) freq_different.get('H'),0.001);
		
		
	}

}
