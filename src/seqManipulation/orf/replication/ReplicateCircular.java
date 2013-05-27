package seqManipulation.orf.replication;

public class ReplicateCircular extends Replicate {

	@Override
	public Replicon attempToReplicateSequence(String sequence, int unitLength, int[] ATG, int[] stop) {
		
		int[] nATGs, nStop;
		
		
		if (unitLength % 3 == 0) {
			
			int by = 2;
			
			nATGs = replicateArray(sequence, ATG, by);
			
			nStop = replicateArray(sequence, stop, by);
			
			sequence = sequence + sequence;
			
		} else {
			
			int by = 4;
			
			nATGs = replicateArray(sequence, ATG, by);

			nStop = replicateArray(sequence, stop, by);
			
			sequence = sequence + sequence + sequence + sequence;
		}
		
		return new Replicon(nATGs, nStop, sequence);
		
	}
	
	/**
	 * Replicate the elements of and <code>int</code> array a given number of times.
	 * The replicated elements are put at the end of the array and its value is modified 
	 * too add the length of the sequence.
	 * 
	 * <pre>
	 * Example:
	 *         Initial Array = [1, 4 , 6 , 9]
	 *         sequence length = 10;
	 *         Replicate 3 times;
	 *         ...
	 *         Result Array = [1, 4 , 6 , 9, 1 + (10*1), 4 + (10*1), 6 + (10*1), 9 + (10*1), 1 + (10*2), 4 + (10*2), 6 + (10*2), 9 + (10*2)]
	 *         Result Array = [1, 4 , 6 , 9, 11, 14, 16, 19, 21, 24, 26, 29]
	 * 
	 * </pre>
	 * @param sequence is the sequence for which the array positions belongs. The positions are ATG or STOPs mark positions.
	 * @param array contain the positions of ATG and STOP marks from the sequence.
	 * @param by is the number of times that the array will be replicated. 
	 * @return a new array with elements of the first replicated
	 */
	protected static int[] replicateArray(String sequence, int[] array, int by) {
		int[] nArray = new int[by*array.length];
		for (int i=0 ; i < by*array.length  ; i++) nArray[i]  = array[i % array.length] + sequence.length() * ((int)(i / array.length));
		return nArray; 
	}

}
