package fileformats.readers.phylip;

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
 * A class for object readting Phylip format.
 * Phylip format has two flavors, interleaved and sequential.
 * 
 * A sequence alignment in Phylip format consists of:<br>
 * <ol>
 *  <li>The first line in the file contains two numbers separated by spaces: number of sequence and length of alignment. In that order.</li>
 *  <li>One or more empty lines.</li>
 *  <li>One or more blocks of sequence data. Each block consists of one line for each sequence in the alignment. Each line consists of:</li>
 *      <ol>
 *       <li>The sequence name with length of 10 characters, only in the first block</li>
 *       <li>Zero or more white spaces</li>
 *       <li>sequence symbol, can be separated by white spaces or not</li>
 *      </ol>
 *  </ol> 
 *  Some rules about representing sequences:
 *  <ol>
 *  	<li>Case doesn't matter.</li>
 *  	<li>Sequence symbols should be from a valid alphabet.</li>
 *  	<li>Gaps are represented using hyphens ("-").</li>
 *  </ol>
 * @author Javier Iserte
 *
 */
public class PhylipFormattedAlignmentReader implements FormattedAlignmentReader {

	////////////////////////////
	// Class Constants
	private static final String FIRST_LINE_REGEX = "^\\s*([0-9]+)\\s+([0-9]+)\\s*$";
	private static final String FIRST_BLOCK_LINE_REGEX = "^(.{10})\\s*(.+)$";
	private static final String COMMON_BLOCK_LINE_REGEX = "^\\s{10}\\s*(.+)$";
	/////////////////////////////
	
	/////////////////////////////
	// Instance Variables
	private AlignmentReadingResult result;
	private List<Pair<String, StringBuilder>> resultAlignmentBuilder;
	private List<Pair<String, String>> resultAlignment;
	private AlignmentRule unmetRule;
	/////////////////////////////
	
	//////////////////////////////
	// Constructor
	public PhylipFormattedAlignmentReader() {
		super();
		this.setResult(new AlignmentReadingResult());
		this.setResultAlignmentBuilder(new ArrayList<Pair<String,StringBuilder>>());
		this.setResultAlignment(new ArrayList<Pair<String,String>>());
	}
	//////////////////////////////
	
	@Override
	public AlignmentReadingResult read(BufferedReader in) {
		
		int numberOfSequences= 0;
		
		int lineCounter = 0;
		
		int sequenceLineCounter=0;
		
		try {
			
			////////////////////////////////
			// Read the first line of data
			// and process it in a different way
			String currentLine = null;

			Pattern pattern = Pattern.compile(PhylipFormattedAlignmentReader.FIRST_LINE_REGEX);
			
			Matcher matcher ;
			
			if ( (currentLine = in.readLine()) == null) {
				
				return this.getResultForUnmetRule(new BlankAlignmentRule(),lineCounter,"");
				
			} else  {
				
				lineCounter++;
				matcher = pattern.matcher(currentLine );
				if (matcher.matches() ) {
			
					numberOfSequences = Integer.valueOf(matcher.group(1));
				
				} else {
				
					return this.getResultForUnmetRule(new PhylipHeaderRule(),lineCounter,currentLine);
				
				}
				
			}
			
			////////////////////////////////////
			
			
			////////////////////////////////////
			// Read the first sequence block
			while (sequenceLineCounter <numberOfSequences && (currentLine=in.readLine())!=null) {
				
				lineCounter++;
				
				boolean lineMatchesBlackLine = currentLine.trim().equals("");

				if (!lineMatchesBlackLine) {
					
					pattern = Pattern.compile(PhylipFormattedAlignmentReader.FIRST_BLOCK_LINE_REGEX);
					
					matcher = pattern.matcher(currentLine);
					
					boolean lineMatchesSequence = matcher.matches();
					
					if (lineMatchesSequence) {
						
						sequenceLineCounter++;
						
						String description = matcher.group(1).trim();
						
						if (!description.equals("")) {
							
							StringBuilder sequence = new StringBuilder();
						
							sequence.append(matcher.group(2).replaceAll("\\s", ""));
						
							this.getResultAlignmentBuilder().add(new Pair<String, StringBuilder>(description, sequence));
						
						} else {
							
							return this.getResultForUnmetRule(new FirstBlockLinePhylipRule(),lineCounter,currentLine);
							
						}
						
						
					} else {
						
						return this.getResultForUnmetRule(new FirstBlockLinePhylipRule(),lineCounter,currentLine);
						
					}
					
				}
				
			}
			/////////////////////////////
			
			
			////////////////////////////////////
			// Read the rest of sequence blocks
			while (sequenceLineCounter >=numberOfSequences && (currentLine=in.readLine())!=null) {
				
				lineCounter++;
				
				boolean lineMatchesBlackLine = currentLine.trim().equals("");

				if (!lineMatchesBlackLine) {
					
					pattern = Pattern.compile(PhylipFormattedAlignmentReader.COMMON_BLOCK_LINE_REGEX);
					
					matcher = pattern.matcher(currentLine);
					
					boolean lineMatchesSequence = matcher.matches();
					
					if (lineMatchesSequence) {
						
						sequenceLineCounter++;
						
						StringBuilder sequence = new StringBuilder();
						
						sequence.append(matcher.group(1).replaceAll("\\s", ""));
						
						this.getResultAlignmentBuilder().get( (sequenceLineCounter-1)%3).getSecond().append(sequence);
						
					} else {
						
						return this.getResultForUnmetRule(new CommonBlockLinePhylipRule(),lineCounter,currentLine);
						
					}
					
				}
				
			}
			////////////////////////////////////
			
			////////////////////////////////////
			// Reach end of input data
			
			if (sequenceLineCounter==0) {
				
				return getResultForUnmetRule(new BlankAlignmentRule(), lineCounter,"");
				
			} else { 
			
				for (Pair<String, StringBuilder> pair : this.getResultAlignmentBuilder()) {
					
					this.getResultAlignment().add(new Pair<String, String>(pair.getFirst(), pair.getSecond().toString()));
					
				}
				
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
		return "Phylip";
	}
	
	private AlignmentReadingResult getResultForUnmetRule(AlignmentRule unmetRule, int lineNumber, String lineContent) {
		
		unmetRule.setWrongLineNumber(lineNumber);
		
		unmetRule.setWrongLineContent(lineContent);

		this.getResult().setUnmetRule(unmetRule);
		
		return this.getResult();
	}
	



	///////////////////////////////////
	// Getters and Setters
	protected AlignmentReadingResult getResult() {
		return result;
	}

	protected void setResult(AlignmentReadingResult result) {
		this.result = result;
	}

	protected List<Pair<String, StringBuilder>> getResultAlignmentBuilder() {
		return resultAlignmentBuilder;
	}

	protected void setResultAlignmentBuilder(
			List<Pair<String, StringBuilder>> resultAlignmentBuilder) {
		this.resultAlignmentBuilder = resultAlignmentBuilder;
	}

	protected List<Pair<String, String>> getResultAlignment() {
		return resultAlignment;
	}

	protected void setResultAlignment(List<Pair<String, String>> resultAlignment) {
		this.resultAlignment = resultAlignment;
	}

	protected AlignmentRule getUnmetRule() {
		return unmetRule;
	}

	protected void setUnmetRule(AlignmentRule unmetRule) {
		this.unmetRule = unmetRule;
	}

}

