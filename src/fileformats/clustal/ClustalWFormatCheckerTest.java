package fileformats.clustal;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.StringReader;

import org.junit.Test;

public class ClustalWFormatCheckerTest extends ClustalWFormatChecker {

	@Test
	public void testComplainsFormat() {
		
		String clustalText =
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
	
		ClustalWFormatChecker a = new ClustalWFormatChecker();
		
		assertEquals(true , a.complainsFormat(new BufferedReader(new StringReader(clustalText))));;
		
		
	}

}
