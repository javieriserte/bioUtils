package fileformats.readers.faults;

import java.util.ArrayList;
import java.util.List;

import fileformats.readers.FormattedAlignmentReader;

/**
 * Class for objects that represents a specific kind of failure resulting
 * when a sequence reader tries to read a given line in a sequence alignment.
 * 
 * @author Javier Iserte
 *
 */
public abstract class AlignmentReadingFault {
	
	///////////////////////////////////
	// Instance Variables
	/**
	 * A simple human-readable test message with the description of the fault found.
	 */
	private String message;
	/**
	 * The number of the line in the input data where the fault was found.
	 */
	private int wrongLineNumber;
	/**
	 * The content of the line in the input data where the fault was found.
	 */
	private String wrongLineContent;
	/**
	 * The sequence alignment reader that generate that fault.
	 */
	private FormattedAlignmentReader faultProducerReader; 

	/**
	 * Returns a message destined to the user with a description 
	 * of the error on the current line.
	 * The message should include the info required for understanding 
	 * what is wrong, but should not content the line number ot the line number.
	 * These data is provided in <code>getWrongLineNumber</code> and 
	 * <code>getWringLineContent</code> methods.
	 * 
	 * @return a string with an error description.
	 * @see #getWrongLineNumber()
	 * @see #getWrongLineContent()
	 */
	public String getMessage() {
		return this.message;
	}
	
	/**
	 * Get the line in the input data where the error was found.
	 * @return a line number.
	 */
	public int getWrongLineNumber(){
		return this.wrongLineNumber;
	}
	
	/**
	 * Get the content of the line where the error was found.
	 * 
	 * @return a string with the line.
	 */
	public String getWrongLineContent() {
		return this.wrongLineContent;
	}
	
	protected void setMessage(String message) {
		this.message = message;
	}

	public void setWrongLineNumber(int wrongLineNumber) {
		this.wrongLineNumber = wrongLineNumber;
	}

	public void setWrongLineContent(String wrongLineContent) {
		this.wrongLineContent = wrongLineContent;
	}
	
	/**
	 * Get a standard message.
	 * @return
	 */
	public List<String> getDefaultMessage() {
		
		List<String> result = new ArrayList<>();
		
		result.add("Error found on line: "+ this.getWrongLineNumber() + ".");
		result.add("Raised by: "+ this.getFaultProducerReader().alignmentFormatName() + " parser.");
		result.add(this.getWrongLineContent());
		result.add(this.getMessage());
		
		return result;
		
	}

	/**
	 * Get the sequence alignment reader that generate that fault.
	 */
	public FormattedAlignmentReader getFaultProducerReader() {
		return faultProducerReader;
	}

	public void setFaultProducerReader(FormattedAlignmentReader faultyReader) {
		this.faultProducerReader = faultyReader;
	}
	
	

}
