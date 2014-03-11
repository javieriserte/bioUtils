package fileformats.readers.phylip;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.StringReader;

import org.junit.Before;
import org.junit.Test;

import fileformats.readers.AlignmentReadingResult;

public class PhylipFormatReaderTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testRead() {
		
		String alstr01 = 
				" 3 384\n"+
				"CYS1_DICDI   -----MKVIL LFVLAVFTVF VSS------- --------RG IPPEEQ---- --------SQ \n"+
				"ALEU_HORVU   MAHARVLLLA LAVLATAAVA VASSSSFADS NPIRPVTDRA ASTLESAVLG ALGRTRHALR \n"+
				"CATH_HUMAN   ------MWAT LPLLCAGAWL LGV------- -PVCGAAELS VNSLEK---- --------FH \n"+
				"\n"+
				"             FLEFQDKFNK KY-SHEEYLE RFEIFKSNLG KIEELNLIAI NHKADTKFGV NKFADLSSDE \n"+
				"             FARFAVRYGK SYESAAEVRR RFRIFSESLE EVRSTN---- RKGLPYRLGI NRFSDMSWEE \n"+
				"             FKSWMSKHRK TY-STEEYHH RLQTFASNWR KINAHN---- NGNHTFKMAL NQFSDMSFAE \n"+
				"\n"+
				"             FKNYYLNNKE AIFTDDLPVA DYLDDEFINS IPTAFDWRTR G-AVTPVKNQ GQCGSCWSFS \n"+
				"             FQATRL-GAA QTCSATLAGN HLMRDA--AA LPETKDWRED G-IVSPVKNQ AHCGSCWTFS \n"+
				"             IKHKYLWSEP QNCSAT--KS NYLRGT--GP YPPSVDWRKK GNFVSPVKNQ GACGSCWTFS \n"+
				"\n"+
				"             TTGNVEGQHF ISQNKLVSLS EQNLVDCDHE CMEYEGEEAC DEGCNGGLQP NAYNYIIKNG \n"+
				"             TTGALEAAYT QATGKNISLS EQQLVDCAGG FNNF------ --GCNGGLPS QAFEYIKYNG \n"+
				"             TTGALESAIA IATGKMLSLA EQQLVDCAQD FNNY------ --GCQGGLPS QAFEYILYNK \n"+
				"\n"+
				"             GIQTESSYPY TAETGTQCNF NSANIGAKIS NFTMIP-KNE TVMAGYIVST GPLAIAADAV \n"+
				"             GIDTEESYPY KGVNGV-CHY KAENAAVQVL DSVNITLNAE DELKNAVGLV RPVSVAFQVI \n"+
				"             GIMGEDTYPY QGKDGY-CKF QPGKAIGFVK DVANITIYDE EAMVEAVALY NPVSFAFEVT \n"+
				"\n"+
				"             E-WQFYIGGV F-DIPCN--P NSLDHGILIV GYSAKNTIFR KNMPYWIVKN SWGADWGEQG \n"+
				"             DGFRQYKSGV YTSDHCGTTP DDVNHAVLAV GYGVENGV-- ---PYWLIKN SWGADWGDNG \n"+
				"             QDFMMYRTGI YSSTSCHKTP DKVNHAVLAV GYGEKNGI-- ---PYWIVKN SWGPQWGMNG \n"+
				"\n"+
				"             YIYLRRGKNT CGVSNFVSTS II-- \n"+
				"             YFKMEMGKNM CAIATCASYP VVAA \n"+
				"             YFLIERGKNM CGLAACASYP IPLV\n";
		
		PhylipFormattedAlignmentReader reader = new PhylipFormattedAlignmentReader();
		
		
		AlignmentReadingResult result = reader.read(new BufferedReader(new StringReader(alstr01)));
		
		assertTrue(result.successfulRead());
		
		assertNotNull(result.getAlignment());
		
		assertNull(result.getFault());
		
		assertEquals(3, result.getAlignment().size());
		
		
		
	}

}
