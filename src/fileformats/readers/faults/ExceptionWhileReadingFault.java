package fileformats.readers.faults;

public class ExceptionWhileReadingFault extends AlignmentReadingFault {

	public ExceptionWhileReadingFault(String excetionMessage) {
		super();
		this.setMessage("A excetion ocurred while reading the input data: " + excetionMessage);
	}
	
	

}
