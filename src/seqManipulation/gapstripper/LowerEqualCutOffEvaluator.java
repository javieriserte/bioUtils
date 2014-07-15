package seqManipulation.gapstripper;

public class LowerEqualCutOffEvaluator extends CutOffEvaluator {


	////////////////////////////////////////////////////////////////////////////
	// Constructor
	/**
	 * Creates a new LowerEqualCutOffEvaluator with the given value
	 * @param cutoff
	 */
	public LowerEqualCutOffEvaluator(double cutoff) {
		super(cutoff);
	}
	////////////////////////////////////////////////////////////////////////////

	////////////////////////////////////////////////////////////////////////////
	// Public interface
	/**
	 * Evaluates the given value against the cut-off value.
	 * 
	 * @param value is the value to compare against the cutoff
	 * @return true if the given value is lower or equal to the cutoff value
	 */
	@Override
	public boolean passCutoff(double value) {
		return value<=this.getCutoff();
	}
	////////////////////////////////////////////////////////////////////////////


}
