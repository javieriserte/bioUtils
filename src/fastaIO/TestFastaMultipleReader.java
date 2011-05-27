package fastaIO;

/**
 * Example of use of Pair class, and FastaReader.
 * 
 * @author Javier Iserte <jiserte@unq.edu.ar>
 *
 */
public class TestFastaMultipleReader {
	public static void main(String args[]) {

		Pair<Integer,Integer> pair_1 = new Pair<Integer,Integer>(2,2); 
		Pair<Integer,Integer> pair_2 = new Pair<Integer,Integer>(2,2);
		Pair<Integer,Integer> pair_3 = new Pair<Integer,Integer>(1,2);
		Pair<Integer,Float> pair_4 = new Pair<Integer,Float>(2,(float)2);
		
		System.out.println(pair_1.equals(pair_2));
		System.out.println(pair_1.equals(pair_3));
		System.out.println(pair_1.equals(pair_4));
		System.out.println(pair_4.equals(pair_1));
				
	}	
}
