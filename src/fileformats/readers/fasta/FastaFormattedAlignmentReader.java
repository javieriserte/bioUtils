package fileformats.readers.fasta;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fileformats.readers.AlignmentReadingResult;
import fileformats.readers.FormattedAlignmentReader;
import fileformats.readers.faults.AlignmentReadingFault;
import fileformats.readers.faults.BlankAlignmentFault;
import fileformats.readers.faults.ExceptionWhileReadingFault;
import pair.Pair;
/**
 * Reads alignments in fasta format.
 * 
 * A sequence alignment in fasta format consist of:
 * <ol>
 *  <li>One or more blocks of sequence data. Each block consists of:
 *     <li>One header line starting with:</li>
 *      <ol>
 *       <li>a '>' symbol</li>
 *       <li>a text field with a sequence id</li>
 *     <li>One or more lines with sequence symbols.</li>
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
public class FastaFormattedAlignmentReader implements FormattedAlignmentReader {

	////////////////////////////
	// Class Constants
	private static final String FIRST_LINE_REGEX = "^>(.+)\\s*$";
	private static final String SEQUENCE_LINE_REGEX = "^\\s*[^>]([a-zA-Z-.+*]+)\\s*$";
	// End Of class constants
	/////////////////////////////
	
	/////////////////////////////
	// Instance Variables
	private AlignmentReadingResult result;
	private List<Pair<String, String>> resultAlignment;
	// End of instance variables
	/////////////////////////////
	
	//////////////////////////////
	// Constructor
	public FastaFormattedAlignmentReader() {
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

		Map<String,StringBuilder> resultAlignmentMapBuilder = new LinkedHashMap<String, StringBuilder>();
		
		String lastId = null;
		
		try {
			
			String currentLine = null;

			Pattern patternFirstLine = Pattern.compile(FastaFormattedAlignmentReader.FIRST_LINE_REGEX);
			
			Pattern patternSequenceLine = Pattern.compile(FastaFormattedAlignmentReader.SEQUENCE_LINE_REGEX,Pattern.CASE_INSENSITIVE);	
			
			Matcher matcherFirstLine ;
			Matcher matecherSequenceLine ;
			
			LineReadingLoop:
			while( (currentLine = in.readLine()) != null) {
				
				lineCounter++;
				
				boolean lineMatchEmptyLine = currentLine.trim().equals("");

				if (!lineMatchEmptyLine) {
					
					///////////////////////////////////////////
					// Try to read the first line of a sequence block
					matcherFirstLine = patternFirstLine.matcher(currentLine);
						
					if(matcherFirstLine.matches()) {
						
						lastId = matcherFirstLine.group(1).trim();
						
						if (resultAlignmentMapBuilder.containsKey(lastId)) {
							
							return this.getResultForFault(new FastaDuplicateDescriptionFault(), lineCounter, currentLine);
							
						}
						
						resultAlignmentMapBuilder.put(lastId, new StringBuilder());
						
						continue LineReadingLoop;
					///////////////////////////////////////////
					} 
					
					///////////////////////////////////////////
					// Try to read a sequence line of a sequence block
					if (lastId!=null) {
						
						matecherSequenceLine = patternSequenceLine.matcher(currentLine);
						
						if (matecherSequenceLine.matches()) {

							resultAlignmentMapBuilder.get(lastId).append(currentLine.trim());
							
							continue LineReadingLoop;

						} else {
							
							return this.getResultForFault(new FastaSequenceLineFault(), lineCounter, currentLine);
							
						}
						
						
					} else {
						
						return this.getResultForFault(new FastaDescriptionLineFault(), lineCounter, currentLine);
						
					}
					
					////////////////////////////////////////////
						
				}
				
			}
			////////////////////////////////////
			// Reach end of input data
			// with no errors.
			if (resultAlignmentMapBuilder.size()==0) {
				
				return getResultForFault(new BlankAlignmentFault(), lineCounter,"");
				
			} else { 
			
				this.mapToListAlignment(resultAlignmentMapBuilder);
				
				this.getResult().setAlignment(this.getResultAlignment());
								
				return this.getResult();
			
			}
			////////////////////////////////////
			
		} catch (IOException e) {
			
			return this.getResultForFault(new ExceptionWhileReadingFault(e.getMessage()),lineCounter,"");
			
		}
		
	}
	@Override
	public String alignmentFormatName() {
		return "Fasta";
	}

	// End of Public Interface
	/////////////////////////////////////

	/////////////////////////////////////
	// Protected and private Methods
	private AlignmentReadingResult getResultForFault(AlignmentReadingFault fault, int lineNumber, String lineContent) {
		
		fault.setWrongLineNumber(lineNumber);
		
		fault.setWrongLineContent(lineContent);
		
		fault.setFaultProducerReader(this);

		this.getResult().setFault(fault);
		
		return this.getResult();
	}
	
	private void mapToListAlignment(Map<String,StringBuilder> alignmentBuilder) {
		
		List<Pair<String, String>> result = new ArrayList<Pair<String,String>>();
		
		for (String seqId : alignmentBuilder.keySet()) {
			
			String sequence = alignmentBuilder.get(seqId).toString();
			
			result.add(new Pair<String, String>(seqId, sequence));
			
		}
		
		this.setResultAlignment(result);;
		
	}
	// End of Protected and private methods
	/////////////////////////////////////
	
	/////////////////////////////////////
	// Getters And Setters
	protected AlignmentReadingResult getResult() {
		return result;
	}

	protected void setResult(AlignmentReadingResult result) {
		this.result = result;
	}
	
	protected List<Pair<String, String>> getResultAlignment() {
		return resultAlignment;
	}

	protected void setResultAlignment(List<Pair<String, String>> resultAlignment) {
		this.resultAlignment = resultAlignment;
	}
	// End of Getters And Setters
	//////////////////////////////////////



}
