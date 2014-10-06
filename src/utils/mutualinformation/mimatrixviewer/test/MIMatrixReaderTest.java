package utils.mutualinformation.mimatrixviewer.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import utils.mutualinformation.mimatrixviewer.MIMatrixReader;
import utils.mutualinformation.mimatrixviewer.MI_Matrix;

public class MIMatrixReaderTest extends MIMatrixReader {

	MIMatrixReader reader;
	
	@Before
	public void setUp() throws Exception {
		
		this.reader = new MIMatrixReader();
		
	}

	@Test
	public void testReadFile() {
		
		String localPath = "src" + File.separatorChar +
				           "utils" + File.separatorChar +
				           "mutualinformation" + File.separatorChar +
				           "mimatrixviewer" + File.separatorChar +
				           "test"  + File.separatorChar +
				           "mi_data_test";
		
		File file = new File (localPath);
		
		System.out.println(file.getAbsolutePath());
		
		try {
			
			MI_Matrix matrix = this.reader.read(file);
			
			
			assertEquals(1891, matrix.count());
			
			assertEquals("MAQVSTQKTAHETSLNASGNSIIHYTNINYKDAASNSANRQDFTQGKFTEVKDIMVKSLALN",matrix.getReferenceSequenceAsString());
			
			assertEquals(8.934400,matrix.getZscoreValue(56,57),0.01);
			
			
		} catch (IOException e) {
			fail();
		}
		
	}

	@Test
	public void testReadZipped() {

		String localPath = "src" + File.separatorChar +
				"utils" + File.separatorChar +
				"mutualinformation" + File.separatorChar +
				"mimatrixviewer" + File.separatorChar +
				"test"  + File.separatorChar +
				"mi_data_test.zip";

		File file = new File (localPath);

		System.out.println(file.getAbsolutePath());

		MI_Matrix matrix = this.reader.readZipped(file);

		assertEquals(1891, matrix.count());

		assertEquals("MAQVSTQKTAHETSLNASGNSIIHYTNINYKDAASNSANRQDFTQGKFTEVKDIMVKSLALN",matrix.getReferenceSequenceAsString());

		assertEquals(8.934400,matrix.getZscoreValue(56,57),0.01);

	}

}
