package fileformats.readers.pir;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.StringReader;

import org.junit.Test;

import fileformats.readers.AlignmentReadingResult;
import fileformats.readers.faults.BlankAlignmentFault;

public class PirReaderTest extends PirFormattedAlignmentReader {

	@Test
	public void testRead() {
		
		String alignment_01 = 
				">P1;CRAB_ANAPL\n"+
				"ALPHA CRYSTALLIN B CHAIN (ALPHA(B)-CRYSTALLIN).\n"+
				"  MDITIHNPLI RRPLFSWLAP SRIFDQIFGE HLQESELLPA SPSLSPFLMR \n"+
				"  SPIFRMPSWL ETGLSEMRLE KDKFSVNLDV KHFSPEELKV KVLGDMVEIH \n"+
				"  GKHEERQDEH GFIAREFNRK YRIPADVDPL TITSSLSLDG VLTVSAPRKQ \n"+
				"  SDVPERSIPI TREEKPAIAG AQRK*\n"+
				"\n"+
				">P1;CRAB_BOVIN\n"+
				"ALPHA CRYSTALLIN B CHAIN (ALPHA(B)-CRYSTALLIN).\n"+
				"  MDIAIHHPWI RRPFFPFHSP SRLFDQFFGE HLLESDLFPA STSLSPFYLR \n"+
				"  PPSFLRAPSW IDTGLSEMRL EKDRFSVNLD VKHFSPEELK VKVLGDVIEV \n"+
				"  HGKHEERQDE HGFISREFHR KYRIPADVDP LAITSSLSSD GVLTVNGPRK \n"+
				"  QASGPERTIP ITREEKPAVT AAPKK*\n"+
				"\n"+
				">P1;CRAB_CHICK\n"+
				"ALPHA CRYSTALLIN B CHAIN (ALPHA(B)-CRYSTALLIN).\n"+
				"  MDITIHNPLV RRPLFSWLTP SRIFDQIFGE HLQESELLPT SPSLSPFLMR \n"+
				"  SPFFRMPSWL ETGLSEMRLE KDKFSVNLDV KHFSPEELKV KVLGDMIEIH \n"+
				"  GKHEERQDEH GFIAREFSRK YRIPADVDPL TITSSLSLDG VLTVSAPRKQ \n"+
				"  SDVPERSIPI TREEKPAIAG SQRK*\n";
		
		String alignment_02 = "";
		
		String alignment_03 = 
				">P1CRAB_ANAPL\n"+
				"ALPHA CRYSTALLIN B CHAIN (ALPHA(B)-CRYSTALLIN).\n"+
				"  MDITIHNPLI RRPLFSWLAP SRIFDQIFGE HLQESELLPA SPSLSPFLMR \n"+
				"  SPIFRMPSWL ETGLSEMRLE KDKFSVNLDV KHFSPEELKV KVLGDMVEIH \n"+
				"  GKHEERQDEH GFIAREFNRK YRIPADVDPL TITSSLSLDG VLTVSAPRKQ \n"+
				"  SDVPERSIPI TREEKPAIAG AQRK*\n"+
				"\n"+
				">P1;CRAB_BOVIN\n"+
				"ALPHA CRYSTALLIN B CHAIN (ALPHA(B)-CRYSTALLIN).\n"+
				"  MDIAIHHPWI RRPFFPFHSP SRLFDQFFGE HLLESDLFPA STSLSPFYLR \n"+
				"  PPSFLRAPSW IDTGLSEMRL EKDRFSVNLD VKHFSPEELK VKVLGDVIEV \n"+
				"  HGKHEERQDE HGFISREFHR KYRIPADVDP LAITSSLSSD GVLTVNGPRK \n"+
				"  QASGPERTIP ITREEKPAVT AAPKK*\n"+
				"\n"+
				">P1;CRAB_CHICK\n"+
				"ALPHA CRYSTALLIN B CHAIN (ALPHA(B)-CRYSTALLIN).\n"+
				"  MDITIHNPLV RRPLFSWLTP SRIFDQIFGE HLQESELLPT SPSLSPFLMR \n"+
				"  SPFFRMPSWL ETGLSEMRLE KDKFSVNLDV KHFSPEELKV KVLGDMIEIH \n"+
				"  GKHEERQDEH GFIAREFSRK YRIPADVDPL TITSSLSLDG VLTVSAPRKQ \n"+
				"  SDVPERSIPI TREEKPAIAG SQRK*\n";
		
		BufferedReader in01 = new BufferedReader(new StringReader(alignment_01));
		BufferedReader in02 = new BufferedReader(new StringReader(alignment_02));
		BufferedReader in03 = new BufferedReader(new StringReader(alignment_03));
		
		PirFormattedAlignmentReader reader = new PirFormattedAlignmentReader();
		
		AlignmentReadingResult result01 = reader.read(in01);
		AlignmentReadingResult result02 = reader.read(in02);
		AlignmentReadingResult result03 = reader.read(in03);
		
		assertTrue(result01.successfulRead());
		assertNull(result01.getFault());
		assertNotNull(result01.getAlignment());
		assertEquals(3,result01.getAlignment().size());
		assertEquals(174,result01.getAlignment().get(0).getSecond().length());
		assertEquals("CRAB_ANAPL",result01.getAlignment().get(0).getFirst());
		
		assertFalse(result02.successfulRead());
		assertNotNull(result02.getFault());
		assertNull(result02.getAlignment());
		assertEquals((new BlankAlignmentFault()).getClass(),result02.getFault().getClass());
		
		assertTrue(result03.successfulRead());
		assertNull(result03.getFault());
		assertNotNull(result03.getAlignment());
		assertEquals(2,result03.getAlignment().size());
		assertEquals(175,result03.getAlignment().get(0).getSecond().length());
		assertEquals("CRAB_BOVIN",result03.getAlignment().get(0).getFirst());
		
	}

}
