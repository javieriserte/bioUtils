package seqManipulation.gapstripper.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import seqManipulation.gapstripper.CutOffEvaluator;
import seqManipulation.gapstripper.LowerEqualCutOffEvaluator;

public class LowerEqualCutOffEvaluatorTest {

	@Test
	public void testPassCutoff() {
		
		double[] values = new double[]{10,5,3,1,0.9,0.5,0.1,0.01,0.0001};
		Boolean[] pass  = new Boolean[values.length];
		
		CutOffEvaluator eval;
		
		eval = new LowerEqualCutOffEvaluator(1);
		int counter=0;
		for (double d : values) { pass[counter++] = eval.passCutoff(d); }
		
		assertArrayEquals(pass, new Boolean[]{false,false,false,true,true,true,true,true,true});

		eval = new LowerEqualCutOffEvaluator(10);
		counter=0;
		for (double d : values) { pass[counter++] = eval.passCutoff(d); }
		
		assertArrayEquals(pass, new Boolean[]{true,true,true,true,true,true,true,true,true});
		
		eval = new LowerEqualCutOffEvaluator(0.01);
		counter=0;
		for (double d : values) { pass[counter++] = eval.passCutoff(d); }
		
		assertArrayEquals(pass, new Boolean[]{false,false,false,false,false,false,false,true,true});
		
		eval = new LowerEqualCutOffEvaluator(0.000001);
		counter=0;
		for (double d : values) { pass[counter++] = eval.passCutoff(d); }
		
		assertArrayEquals(pass, new Boolean[]{false,false,false,false,false,false,false,false,false});

	}

}
