package fileformats.readers.nexus;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import org.junit.Test;

import fileformats.readers.AlignmentReadingResult;

public class NexusFormattedAlignmentReaderTest extends NexusFormattedAlignmentReader {

	@Test
	public void testRead() {
		
		String aln_01 = 
				"#NEXUS\n"+
						"BEGIN OTHERBLOCK;\n"+
						"	NOTHINGHERE\n"+
						"END;\n"+
						"\n"+
						"BEGIN ANOTHERBLOCK;\n"+
						"END;\n"+
						"\n"+
						"BEGIN DATA;\n"+
						"        Dimensions NTax=10 NChar=705;\n"+
						"        Format DataType=DNA Interleave=yes Gap=- Missing=?;\n"+
						"        Matrix\n"+
						"Cow     ATGGC ATATC CCATA CAACT AGGAT TCCAA GATGC AACAT CACCA ATCAT AGAAG AACTA\n"+
						"Carp    ATGGCACACCCAACGCAACTAGGTTTCAAGGACGCGGCCATACCCGTTATAGAGGAACTT\n"+
						"Chicken ATGGCCAACCACTCCCAACTAGGCTTTCAAGACGCCTCATCCCCCATCATAGAAGAGCTC\n"+
						"Human   ATGGCACATGCAGCGCAAGTAGGTCTACAAGACGCTACTTCCCCTATCATAGAAGAGCTT\n"+
						"Loach   ATGGCACATCCCACACAATTAGGATTCCAAGACGCGGCCTCACCCGTAATAGAAGAACTT\n"+
						"Mouse   ATGGCCTACCCATTCCAACTTGGTCTACAAGACGCCACATCCCCTATTATAGAAGAGCTA\n"+
						"Rat     ATGGCTTACCCATTTCAACTTGGCTTACAAGACGCTACATCACCTATCATAGAAGAACTT\n"+
						"Seal    ATGGCATACCCCCTACAAATAGGCCTACAAGATGCAACCTCTCCCATTATAGAGGAGTTA\n"+
						"Whale   ATGGCATATCCATTCCAACTAGGTTTCCAAGATGCAGCATCACCCATCATAGAAGAGCTC\n"+
						"Frog    ATGGCACACCCATCACAATTAGGTTTTCAAGACGCAGCCTCTCCAATTATAGAAGAATTA\n"+
						"\n"+
						"\n"+
						"Cow     AAATGATCTGCGTCAATATTA---------------------TAA\n"+
						"Carp    AACTGATCCTCATTAATACTAGAAGACGCCTCGCTAGGAAGCTAA\n"+
						"Chicken GCCTGATCCTCACTA------------------CTGTCATCTTAA\n"+
						"Human   ATA---------------------GGGCCCGTATTTACCCTATAG\n"+
						"Loach   AACTGGTCCACCCTTATACTAAAAGACGCCTCACTAGGAAGCTAA\n"+
						"Mouse   AACTGATCTGCTTCAATAATT---------------------TAA\n"+
						"Rat     AACTGATCAGCTTCTATAATT---------------------TAA\n"+
						"Seal    AAATGATCTACCTCAATGCTT---------------------TAA\n"+
						"Whale   AAATGATCTGTATCAATACTA---------------------TAA\n"+
						"Frog    AACTGATCTTCATCAATACTA---GAAGCATCACTA------AGA\n"+
						";\n";

		String aln_02 = 
				"#NEXUS\n"+
						"BEGIN OTHERBLOCK;\n"+
						"	NOTHINGHERE\n"+
						"END;\n"+
						"\n"+
						"BEGIN ANOTHERBLOCK;\n"+
						"END;\n"+
						"\n"+
						"BEGIN DATA;\n"+
						"        Dimensions NTax=10 NChar=705;\n"+
						"        Format DataType=DNA Interleave=yes Gap=- Missing=?;\n"+
						"        Matrix\n"+
						"[Cow     ATGGC ATATC CCATA CAACT AGGAT TCCAA GATGC AACAT CACCA ATCAT AGAAG AACTA]\n"+
						"Carp    ATGGCACACCCAACGCAACTAGGTTTCAAGGACGCGGCCATACCCGTTATAGAGGAACTT\n"+
						"\n"+
						"[Cow     AAATGATCTGCGTCAATATTA---------------------TAA]\n"+
						"Carp    AACTGATCTTCATCAATACTA---GAAGCATCACTA------AGA\n"+
						";\n";
		
		BufferedReader in_01 = new BufferedReader(new StringReader(aln_01));
		BufferedReader in_02 = new BufferedReader(new StringReader(aln_02));

		NexusFormattedAlignmentReader reader = new NexusFormattedAlignmentReader();
		
		AlignmentReadingResult result_01 = reader.read(in_01);
		AlignmentReadingResult result_02 = reader.read(in_02);
		
		assertTrue(result_01.successfulRead());
		assertNotNull(result_01.getAlignment());
		assertNull(result_01.getFault());
		assertEquals(10,result_01.getAlignment().size());

		assertTrue(result_02.successfulRead());
		assertNotNull(result_02.getAlignment());
		assertNull(result_02.getFault());
		assertEquals(1,result_02.getAlignment().size());

	}
	
	@Test
	public void testUncomment() {
		
		String noComment = "no comments";
		String simpleComment = "[no ]comments";
		String difficultComment = "][no ][comments";
		String manyComments = "[no ]much[comments] here";
		String multilineComment = "no much [comments \n"+" found]here";

		NexusFormattedAlignmentReader reader = new NexusFormattedAlignmentReader();
		
		try {
			String noCommentResult = reader.removeComments(new BufferedReader(new StringReader(noComment))).readLine();
			assertEquals(noComment,noCommentResult);
			
			String simpleCommentResult = reader.removeComments(new BufferedReader(new StringReader(simpleComment))).readLine();
			assertEquals("comments",simpleCommentResult);
			
			String difficultCommentResult = reader.removeComments(new BufferedReader(new StringReader(difficultComment))).readLine();
			assertEquals("]",difficultCommentResult);

			String manyCommentsResult = reader.removeComments(new BufferedReader(new StringReader(manyComments))).readLine();
			assertEquals("much here",manyCommentsResult);

			String multilineCommentResult = reader.removeComments(new BufferedReader(new StringReader(multilineComment))).readLine();
			assertEquals("no much here",multilineCommentResult);
			
		} catch (IOException e) {
			
			e.printStackTrace();
			
		}
		
		
	
	}

}
