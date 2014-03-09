package fileformats.readers.rules;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for objects that represents rules unmet by 
 * a line in a sequence alignment.
 * 
 * @author Javier
 *
 */
public abstract class AlignmentRule {
	
	///////////////////////////////////
	// Instance Variables
	private String message;
	private int wrongLineNumber;
	private String wrongLineContent;
	

	/**
	 * Returns a message destined to the user with a description 
	 * of the error un the current line.
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
		
		result.add("Error found on line: "+ this.getWrongLineNumber());
		result.add(this.getWrongLineContent());
		result.add(this.getMessage());
		
		return result;
		
	}
	
	

}
