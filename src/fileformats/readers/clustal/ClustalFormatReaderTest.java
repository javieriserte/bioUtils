package fileformats.readers.clustal;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.StringReader;

import org.junit.Test;

import fileformats.readers.AlignmentReadingResult;
import fileformats.readers.rules.BlankAlignmentRule;

public class ClustalFormatReaderTest extends ClustalFormattedAlignmentReader {

	@Test
	public void testComplainsFormat() {
		
		String clustalText01 =
						"CLUSTAL W (1.82) multiple sequence alignment\n"+
						"\n"+
				        "FOSB_MOUSE      MFQAFPGDYDSGSRCSSSPSAESQYLSSVDSFGSPPTAAASQECAGLGEMPGSFVPTVTA 60\n"+
						"FOSB_HUMAN      MFQAFPGDYDSGSRCSSSPSAESQYLSSVDSFGSPPTAAASQECAGLGEMPGSFVPTVTA 60\n"+
						"                ************************************************************\n"+
						"\n"+
						"FOSB_MOUSE      ITTSQDLQWLVQPTLISSMAQSQGQPLASQPPAVDPYDMPGTSYSTPGLSAYSTGGASGS 120\n"+
						"FOSB_HUMAN      ITTSQDLQWLVQPTLISSMAQSQGQPLASQPPVVDPYDMPGTSYSTPGMSGYSSGGASGS 120\n"+
						"                ********************************.***************:*.**:******\n"+
						"\n"+
						"FOSB_MOUSE      GGPSTSTTTSGPVSARPARARPRRPREETLTPEEEEKRRVRRERNKLAAAKCRNRRRELT 180\n"+
						"FOSB_HUMAN      GGPSTSGTTSGPGPARPARARPRRPREETLTPEEEEKRRVRRERNKLAAAKCRNRRRELT 180\n"+
						"                ****** ***** .**********************************************\n"+
						"\n"+
						"FOSB_MOUSE      DRLQAETDQLEEEKAELESEIAELQKEKERLEFVLVAHKPGCKIPYEEGPGPGPLAEVRD 240\n"+
						"FOSB_HUMAN      DRLQAETDQLEEEKAELESEIAELQKEKERLEFVLVAHKPGCKIPYEEGPGPGPLAEVRD 240\n"+
						"                ************************************************************\n"+
						"\n"+
						"FOSB_MOUSE      LPGSTSAKEDGFGWLLPPPPPPPLPFQSSRDAPPNLTASLFTHSEVQVLGDPFPVVSPSY 300\n"+
						"FOSB_HUMAN      LPGSAPAKEDGFSWLLPPPPPPPLPFQTSQDAPPNLTASLFTHSEVQVLGDPFPVVNPSY 300\n"+
						"                ****:.******.**************:*:**************************.***\n"+
						"\n"+
						"FOSB_MOUSE      TSSFVLTCPEVSAFAGAQRTSGSEQPSDPLNSPSLLAL 338\n"+
						"FOSB_HUMAN      TSSFVLTCPEVSAFAGAQRTSGSDQPSDPLNSPSLLAL 338\n"+
						"                ***********************:**************";
	
		String clustalText02 = "";
		
		String clustalText03 =
				"CLUSTAL W (1.82) multiple sequence alignment\n"+
				"\n"+
		        "FOSB_MOUSE      MFQAFPGDYDSGSRCSSSPSAESQYLSSVDSFGSPPTAAASQECAGLGEMPGSFVPTVTA 60\n"+
				"FOSB_HUMAN      MFQAFPGDYDSGSRCSSSPSAESQYLSSVDSFGSPPTAAASQECAGLGEMPGSFVPTVTA 60\n"+
				"                ************************************************************\n"+
				"\n"+
				"FOSB_MOUSE      ITTSQDLQWLVQPTLISSMAQSQGQPLASQPPAVDPYDMPGTSYSTPGLSAYSTGGASGS 120\n"+
				"FOSB_HUMAN      ITTSQDLQWLVQPTLISSMAQSQGQPLASQPPVVDPYDMPGTSYSTPGMSGYSSGGASGS 120\n"+
				"                ********************************.***************:*.**:******\n"+
				"\n"+
				"dsakljfha sfkahglkj ashglkjafghafljkgh fgjkhafdgl kjahglakjfgh alfdgkjhasdf gljfhg\n"+
				"ajfgh dfgjkhafljgh  asdfjkhljkhljasf  lkdhfjkhdsf l kjsdhflkjadfh lajsdfh    180\n"+
				"********************** ***** .**********************************************\n"+
				"\n"+
				"FOSB_MOUSE      DRLQAETDQLEEEKAELESEIAELQKEKERLEFVLVAHKPGCKIPYEEGPGPGPLAEVRD 240\n"+
				"FOSB_HUMAN      DRLQAETDQLEEEKAELESEIAELQKEKERLEFVLVAHKPGCKIPYEEGPGPGPLAEVRD 240\n"+
				"                ************************************************************\n"+
				"\n"+
				"FOSB_MOUSE      LPGSTSAKEDGFGWLLPPPPPPPLPFQSSRDAPPNLTASLFTHSEVQVLGDPFPVVSPSY 300\n"+
				"FOSB_HUMAN      LPGSAPAKEDGFSWLLPPPPPPPLPFQTSQDAPPNLTASLFTHSEVQVLGDPFPVVNPSY 300\n"+
				"                ****:.******.**************:*:**************************.***\n"+
				"\n"+
				"FOSB_MOUSE      TSSFVLTCPEVSAFAGAQRTSGSEQPSDPLNSPSLLAL 338\n"+
				"FOSB_HUMAN      TSSFVLTCPEVSAFAGAQRTSGSDQPSDPLNSPSLLAL 338\n"+
				"                ***********************:**************";
		
		ClustalFormattedAlignmentReader a = new ClustalFormattedAlignmentReader();
		
		AlignmentReadingResult al1 = a.read(new BufferedReader(new StringReader(clustalText01)));
		AlignmentReadingResult al2 = a.read(new BufferedReader(new StringReader(clustalText02)));
		AlignmentReadingResult al3 = a.read(new BufferedReader(new StringReader(clustalText03)));
		
		
		
		assertEquals(true , al1.successfulRead());
		
		assertNotNull(al1.getAlignment());
		
		assertEquals(2,al1.getAlignment().size());
		
		assertEquals("FOSB_MOUSE",al1.getAlignment().get(0).getFirst());
		
		assertEquals("MFQAFPGDYDSGSRCSSSPSAESQYLSSVDSFGSPPTAAASQECAGLGEMPGSFVPT"+
		             "VTAITTSQDLQWLVQPTLISSMAQSQGQPLASQPPAVDPYDMPGTSYSTPGLSAYST"+
				     "GGASGSGGPSTSTTTSGPVSARPARARPRRPREETLTPEEEEKRRVRRERNKLAAAK"+
		             "CRNRRRELTDRLQAETDQLEEEKAELESEIAELQKEKERLEFVLVAHKPGCKIPYEE"+
				     "GPGPGPLAEVRDLPGSTSAKEDGFGWLLPPPPPPPLPFQSSRDAPPNLTASLFTHSE"+
		             "VQVLGDPFPVVSPSYTSSFVLTCPEVSAFAGAQRTSGSEQPSDPLNSPSLLAL",
		             al1.getAlignment().get(0).getSecond());
		
		
	
		assertEquals(false, al2.successfulRead());
		
		assertNull(al2.getAlignment());
		
		assertNotNull(al2.getUnmetRule());
		
		assertEquals((new BlankAlignmentRule()).getClass(), al2.getUnmetRule().getClass());
		
	
		
		assertEquals(false, al3.successfulRead());
		
		assertNull(al3.getAlignment());
		
		assertNotNull(al3.getUnmetRule());
		
		assertEquals((new SequenceOrConservationClustalRule()).getClass(), al3.getUnmetRule().getClass());
		
		assertEquals(11,al3.getUnmetRule().getWrongLineNumber());
		
		
	}

}
