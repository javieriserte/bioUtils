package fileformats.readers.pir;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pair.Pair;
import fileformats.readers.AlignmentReadingResult;
import fileformats.readers.FormattedAlignmentReader;
import fileformats.readers.rules.AlignmentRule;
import fileformats.readers.rules.BlankAlignmentRule;
import fileformats.readers.rules.ExceptionWhileReadingRule;

/**
 * Reads alignments in Pir format.
 * 
 * A sequence alignment in Pir format consist of:
 * <ol>
 *  <li>One or more blocks of sequence data. Each block consists of:
 *     <li>One header line starting with:</li>
 *      <ol>
 *       <li>a '>' symbol</li>
 *       <li>a two letter code</li>
 *       <li>a ';' symbol
 *       <li>a text field with a sequence id, until the next ';' symbol or end of line</li>
 *       <li>zero or more fields separated by ';'.</li>
 *     <li>One description textual line</li>
 *     <li>One or more lines with sequence symbols, can be separated by white spaces or not. Ending with a '*' symbol.</li>
 *     <li>Text about the sequence.
 *  </ol> 
 *  Some rules about representing sequences:
 *  <ol>
 *  	<li>Case doesn't matter.</li>
 *  	<li>Sequence symbols should be from a valid alphabet.</li>
 *  	<li>Gaps are represented using hyphens ("-").</li>
 *  </ol>
 * Note:If a header data line is not well formed, then 
 * that entire sequence block will be ignored.  
 * @author Javier iserte
 *
 */
public class PirFormattedAlignmentReader implements FormattedAlignmentReader {

	////////////////////////////
	// Class Constants
	private static final String FIRST_LINE_REGEX = "^>[^;]+;([^;]+).*$";
	private static final String SEQUENCE_LINE_REGEX = "^([^>][^*]+)$";
	private static final String ENDIND_SEQUENCE_LINE_REGEX = "^([^>][^*]*)\\*.*$";
	/////////////////////////////
	
	/////////////////////////////
	// Instance Variables
	private AlignmentReadingResult result;
	private List<Pair<String, String>> resultAlignment;
	/////////////////////////////
	
	//////////////////////////////
	// Constructor
	public PirFormattedAlignmentReader() {
		super();
	}
	// End of contructors
	//////////////////////////////

	////////////////////////////////
	// Public Interface
	/**
	 * Attempts to read an alignment with Pir format.
	 * Success or not, a AlignmentReadingResult object is returned.
	 * 
	 *  @see AlignmentReadingResult
	 */
	@Override
	public AlignmentReadingResult read(BufferedReader in) {
		
		int lineCounter = 0;
		
		this.setResult(new AlignmentReadingResult());
		this.setResultAlignment(new ArrayList<Pair<String,String>>());
		
		Pair<String,StringBuilder> currentSequenceBuilder = null;
		
		try {
			
			String currentLine = null;

			Pattern patternFirstLine = Pattern.compile(PirFormattedAlignmentReader.FIRST_LINE_REGEX);
			Pattern patternSequenceLine = Pattern.compile(PirFormattedAlignmentReader.SEQUENCE_LINE_REGEX);
			Pattern patternEndingSequenceLine = Pattern.compile(PirFormattedAlignmentReader.ENDIND_SEQUENCE_LINE_REGEX);
			
			Matcher matcherFirstLine ;
			Matcher matcherSequenceLine ;
			Matcher matcherEndingSequenceLine ;
			
			boolean nextIsFirstLine = true;			
			boolean nextIsDescritionLine = false;
			boolean nextIsSequenceLine = false;
			
			LineReadingLoop:
			while( (currentLine = in.readLine()) != null) {
				
				lineCounter++;
				
				boolean lineMatchEmptyLine = currentLine.trim().equals("");

				if (!lineMatchEmptyLine) {
					
					///////////////////////////////////////////
					// Try to read the first line of a sequence block
					if (nextIsFirstLine) {
						
						matcherFirstLine = patternFirstLine.matcher(currentLine);
						
						if(matcherFirstLine.matches()) {
						
							StringBuilder currentStringBuilder = new StringBuilder();
						
							currentSequenceBuilder = new Pair<String,StringBuilder>(matcherFirstLine.group(1).trim(),currentStringBuilder);
						
							nextIsFirstLine = false;			

							nextIsDescritionLine = true;
						
							continue LineReadingLoop;
						
						} 
						
					}
					////////////////////////////////////////////
					
					///////////////////////////////////////////
					// Try to read the description line of a sequence block
					// and it is ignored
					if (nextIsDescritionLine) {
						
						nextIsDescritionLine = false;
						nextIsSequenceLine = true;

						continue LineReadingLoop;
						
					}
					///////////////////////////////////////////
					
					///////////////////////////////////////////
					// Try to read a sequence line of a sequence block
					if (nextIsSequenceLine) {
						
						matcherSequenceLine = patternSequenceLine.matcher(currentLine);
						matcherEndingSequenceLine = patternEndingSequenceLine.matcher(currentLine);
						
						boolean lineMatchNonEndingSequenceLine = matcherSequenceLine.matches();
						boolean lineMatchEndingSequenceLine = matcherEndingSequenceLine.matches();
						
						if(lineMatchNonEndingSequenceLine) {
						
							String sequence = matcherSequenceLine.group(1).replaceAll("\\s", "");
							
							currentSequenceBuilder.getSecond().append(sequence);
						
							continue LineReadingLoop;
						
						} else 
						if (lineMatchEndingSequenceLine) {
						
							String sequence = matcherEndingSequenceLine.group(1).replaceAll("\\s", "");
							
							currentSequenceBuilder.getSecond().append(sequence);
							
							this.getResultAlignment().add(new Pair<String, String>(currentSequenceBuilder.getFirst(), currentSequenceBuilder.getSecond().toString()));
							
							nextIsSequenceLine = false;
							
							nextIsFirstLine = true;
						
							continue LineReadingLoop;
						
						} else {
							
							return this.getResultForUnmetRule(new PirSequenceLineRule(), lineCounter, currentLine);
							
						}
						
					}
					////////////////////////////////////
					
				}
				
			}
			////////////////////////////////////
			// Reach end of input data
			// with no errors.
			if (this.getResultAlignment().size()==0) {
				
				return getResultForUnmetRule(new BlankAlignmentRule(), lineCounter,"");
				
			} else { 
			
				this.getResult().setAlignment(this.getResultAlignment());
								
				return this.getResult();
			
			}
			////////////////////////////////////
			
		} catch (IOException e) {
			
			return this.getResultForUnmetRule(new ExceptionWhileReadingRule(e.getMessage()),lineCounter,"");
			
		}
		
	}
	@Override
	public String alignmentFormatName() {
		return "Pir/NBFR";
	}
	// End of Public Interface
	/////////////////////////////////////
	
	
	/////////////////////////////////////
	// Private Methods
	private AlignmentReadingResult getResultForUnmetRule(AlignmentRule unmetRule, int lineNumber, String lineContent) {
		
		unmetRule.setWrongLineNumber(lineNumber);
		
		unmetRule.setWrongLineContent(lineContent);

		this.getResult().setUnmetRule(unmetRule);
		
		return this.getResult();
	}
	// End of private methods
	//////////////////////////////////////

	////////////////////////////////////////////
	// Getters and Setters
	private List<Pair<String, String>> getResultAlignment() {
		return resultAlignment;
	}
	private void setResultAlignment(List<Pair<String, String>> resultAlignment) {
		this.resultAlignment = resultAlignment;
	}
	private AlignmentReadingResult getResult() {
		return result;
	}
	private void setResult(AlignmentReadingResult alignmentReadingResult) {
		this.result = alignmentReadingResult;
	}
	// End of Getters and Setters
	///////////////////////////////////////////

}
