package seqManipulation.gapstripper;

import static org.junit.Assert.*;

import org.junit.Test;

public class AboveEqualCutOffEvaluatorTest {

	@Test
	public void testPassCutoff() {
		double[] values = new double[]{10,5,3,1,0.9,0.5,0.1,0.01,0.0001};
		Boolean[] pass  = new Boolean[values.length];
		
		CutOffEvaluator eval;
		
		eval = new AboveEqualCutOffEvaluator(1);
		int counter=0;
		for (double d : values) { pass[counter++] = eval.passCutoff(d); }
		
		assertArrayEquals(pass, new Boolean[]{true,true,true,true,false,false,false,false,false,});

		eval = new AboveEqualCutOffEvaluator(10);
		counter=0;
		for (double d : values) { pass[counter++] = eval.passCutoff(d); }
		
		assertArrayEquals(pass, new Boolean[]{true,false,false,false,false,false,false,false,false});
		
		eval = new AboveEqualCutOffEvaluator(0.01);
		counter=0;
		for (double d : values) { pass[counter++] = eval.passCutoff(d); }
		
		assertArrayEquals(pass, new Boolean[]{true,true,true,true,true,true,true,true,false});
		
		eval = new AboveEqualCutOffEvaluator(0.000001);
		counter=0;
		for (double d : values) { pass[counter++] = eval.passCutoff(d); }
		
		assertArrayEquals(pass, new Boolean[]{true,true,true,true,true,true,true,true,true});
	}

}
