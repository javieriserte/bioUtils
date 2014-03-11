package fileformats.readers.fasta;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.StringReader;

import org.junit.Test;

import fileformats.readers.AlignmentReadingResult;

public class FastaFormattedAlignmentReaderTest extends
		FastaFormattedAlignmentReader {

	@Test
	public void testRead() {
		
		String aln_01 = 
				">seq01\n"+
		        "ACTACGACTGGACATCGACACGATCGACTTACGACG\n"+
				"CTCTCTCAGAGGACTATCAGCATCTGACTGACTACT\n"+
				">seq02\n"+
		        "ACTACGACTGGACATCGACACGATCGACTTACGACG\n"+
				"CTCTCTCAGAGGACTATCAGCATCTGACTGACTACT\n"+
				">seq03\n"+
		        "ACTACGACTGGACATCGACACGATCGACTTACGACG\n"+
				"CTCTCTCAGAGGACTATCAGCATCTGACTGACTACT\n";
		
		String aln_02 = 
				"aseq01\n"+
		        "ACTACGACTGGACATCGACACGATCGACTTACGACG\n"+
				"CTCTCTCAGAGGACTATCAGCATCTGACTGACTACT\n"+
				">seq02\n"+
		        "ACTACGACTGGACATCGACACGATCGACTTACGACG\n"+
				"CTCTCTCAGAGGACTATCAGCATCTGACTGACTACT\n"+
				">seq03\n"+
		        "ACTACGACTGGACATCGACACGATCGACTTACGACG\n"+
				"CTCTCTCAGAGGACTATCAGCATCTGACTGACTACT\n";

		String aln_03 = 
				">seq01\n"+
		        "ACTACGACTGGACATCGACACGATCGACTTACGACG\n"+
				"CTCTCTCAGAGGACTATCAGCATCTGACTGACTACT\n"+
				">seq02\n"+
		        "ACTACGACTGGACATCGACACGATCGACTTACGACG\n"+
				"CTCTCTCAGAGGACTATCAGCATCTGACTGACTACT\n"+
				">seq02\n"+
		        "ACTACAACTGGACATCGACACGATCGACTTACGACG\n"+
				"CTCTCTCAGAGGACTATCAGCATCTGACTGACTACT\n";		
		
		BufferedReader in_01 = new BufferedReader(new StringReader(aln_01));
		BufferedReader in_02 = new BufferedReader(new StringReader(aln_02));
		BufferedReader in_03 = new BufferedReader(new StringReader(aln_03));
		

		FastaFormattedAlignmentReader reader = new FastaFormattedAlignmentReader();
		
		AlignmentReadingResult result_01 = reader.read(in_01);

		assertTrue(result_01.successfulRead());
		assertNull(result_01.getFault());
		assertNotNull(result_01.getAlignment());
		assertEquals(3, result_01.getAlignment().size());
		
		AlignmentReadingResult result_02 = reader.read(in_02);

		assertFalse(result_02.successfulRead());
		assertNotNull(result_02.getFault());
		assertNull(result_02.getAlignment());
		assertEquals(new FastaDescriptionLineFault().getClass(), result_02.getFault().getClass());
		
		AlignmentReadingResult result_03 = reader.read(in_03);

		assertFalse(result_03.successfulRead());
		assertNotNull(result_03.getFault());
		assertNull(result_03.getAlignment());
		assertEquals(new FastaDuplicateDescriptionFault().getClass(), result_03.getFault().getClass());
		
		System.err.println(result_03.getFault().getDefaultMessage());
	}

}
