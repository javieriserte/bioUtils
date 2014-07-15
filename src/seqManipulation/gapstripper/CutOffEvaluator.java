package seqManipulation.gapstripper;

/**
 * Abstract class to evaluate cut-off values.
 * Each method of profiling gives cut-off values that must be evaluated in 
 * different ways. Maximum frequency values must be lower than of equal to
 * the cut-off value to be considered to pass the test. Entropy values must be 
 * greater than or equal to the cut-off value to pass the test.
 * @author javier iserte
 *
 */
public abstract class CutOffEvaluator {
	
	////////////////////////////////////////////////////////////////////////////
	// Instance variables
	private double cutoff;
	////////////////////////////////////////////////////////////////////////////
	
	////////////////////////////////////////////////////////////////////////////
	// Constructor
	/**
	 * Creates a new CutOffEvaluator with the given value
	 * @param cutoff
	 */
	public CutOffEvaluator(double cutoff) {
		this.setCutoff(cutoff);
	}
	////////////////////////////////////////////////////////////////////////////

	////////////////////////////////////////////////////////////////////////////
	// Public interface
	/**
	 * Evaluates the given value against the cut-off value
	 * @param value
	 * @return
	 */
	public abstract boolean passCutoff(double value);
	/**
	 * @return the cutoff
	 */
	public double getCutoff() {
		return cutoff;
	}

	/**
	 * @param cutoff the cutoff to set
	 */
	public void setCutoff(double cutoff) {
		this.cutoff = cutoff;
	}
	////////////////////////////////////////////////////////////////////////////
	
}
