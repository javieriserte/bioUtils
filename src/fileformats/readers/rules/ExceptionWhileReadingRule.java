package fileformats.readers.rules;

public class ExceptionWhileReadingRule extends AlignmentRule {

	public ExceptionWhileReadingRule(String excetionMessage) {
		super();
		this.setMessage("A excetion ocurred while reading the input data: " + excetionMessage);
	}
	
	

}
