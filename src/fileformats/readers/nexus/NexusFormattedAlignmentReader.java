package fileformats.readers.nexus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pair.Pair;
import fileformats.readers.AlignmentReadingResult;
import fileformats.readers.FormattedAlignmentReader;
import fileformats.readers.rules.AlignmentRule;
import fileformats.readers.rules.BlankAlignmentRule;
import fileformats.readers.rules.ExceptionWhileReadingRule;

/**
 * Attempts to read a Nexus formatted alignment.
 * 
 * A Nexus alignment consists of:
 * <ol>
 * 	<li> The first line starts with '#NEXUS' string </li>
 *  <li> Comments enclosed in '[' and ']'.
 *  <li> Several blocks, each block begins with 'BEGIN block_name; and finishes with END;'
 *  <li> For sequence the block name is 'data'. A 'data' block contains:
 *   <ol>
 *    <li> a 'Dimensions' field like 'Dimensions ntax=4 nchar=15;';</li>
 *    <li> a 'Format' field like 'Format datatype=dna symbols="ACTG" missing=? gap=-;';<li>
      <li> a 'Matrix' field with sequence data lines organized in blocks. 
           A sequence data block consists of lines with:
       <ol>
        <li> an Id field with no spaces.
        <li> a sequence fields that can be separated by spaces.
       </ol> 
      <li> a ';' symbol for ending the matrix field.
     </ol>
 * </ol>
 * 
 * @author javier
 *
 */
public class NexusFormattedAlignmentReader implements FormattedAlignmentReader {
	////////////////////////////
	// Class Constants
	private static final String FIRST_LINE_REGEX = "^\\s*#NEXUS\\s*$";
	private static final String SEQUENCE_LINE_REGEX = "^\\s*([^\\s]+)\\s+(.+);*\\s*$";
	private static final String BEGIN_BLOCK = "^\\s*BEGIN\\s+([^\\s]+)\\s*;\\s*$";
	private static final String END_BLOCK = "^\\s*END;\\s*$";
	private static final String DIMENSIONS_IN_DATA_BLOCK = "^\\s*Dimensions\\s+.+$";
	private static final String FORMAT_IN_DATA_BLOCK = "^\\s*Format\\s+.+$";
	private static final String MATRIX_IN_DATA_BLOCK = "^\\s*Matrix\\s*$";
	// End of Class Constants
	/////////////////////////////
	
	/////////////////////////////
	// Instance Variables
	private AlignmentReadingResult result;
	private Map<String,StringBuilder> alignmentBuilder;
	// End of Instance Variables
	////////////////////////////
	
	@Override
	public AlignmentReadingResult read(BufferedReader in) {
		
		this.setAlignmentBuilder(new LinkedHashMap<String, StringBuilder>());
		
		this.setResult(new AlignmentReadingResult());
		
		BufferedReader inC = this.removeComments(in);
		
		int lineCounter= 0;
		
	try {
			
			String currentLine = null;

			Pattern patternFirstLine = Pattern.compile(NexusFormattedAlignmentReader.FIRST_LINE_REGEX,Pattern.CASE_INSENSITIVE);
			Pattern patternSequenceLine = Pattern.compile(NexusFormattedAlignmentReader.SEQUENCE_LINE_REGEX,Pattern.CASE_INSENSITIVE);
			Pattern patternBeginBlockLine = Pattern.compile(NexusFormattedAlignmentReader.BEGIN_BLOCK,Pattern.CASE_INSENSITIVE);
			Pattern patternEndBlockLine = Pattern.compile(NexusFormattedAlignmentReader.END_BLOCK,Pattern.CASE_INSENSITIVE);
			Pattern patternDimensionLine = Pattern.compile(NexusFormattedAlignmentReader.DIMENSIONS_IN_DATA_BLOCK,Pattern.CASE_INSENSITIVE);
			Pattern patternFormatLine = Pattern.compile(NexusFormattedAlignmentReader.FORMAT_IN_DATA_BLOCK,Pattern.CASE_INSENSITIVE);
			Pattern patternMatrixLine = Pattern.compile(NexusFormattedAlignmentReader.MATRIX_IN_DATA_BLOCK,Pattern.CASE_INSENSITIVE);
			
			Matcher matcherFirstLine ;
			Matcher matcherBeginBlockLine;
			Matcher matcherEndBlockLine;
			Matcher matcherSequenceLine ;
			Matcher matcherDimensionLine;
			Matcher matcherFormatLine;
			Matcher matcherMatrixLine;
			
			boolean nextIsFirstLine = true;			
			boolean nextIsBlockStartLine = false;
			boolean nextIsBlockEndLine = false;
			boolean nextIsIgnoredBlockLine = false;
			boolean nextIsDataHeaderBlockLine = false;
			boolean nextIsDataSequenceLine = false;
			
			LineReadingLoop:
			while( (currentLine = inC.readLine()) != null) {
				
			lineCounter++;
				
				boolean lineMatchEmptyLine = currentLine.trim().equals("");

				if (!lineMatchEmptyLine) {
					
					///////////////////////////////////////
					// Try to read the first line
					if (nextIsFirstLine) {
						
						matcherFirstLine = patternFirstLine.matcher(currentLine);
						
						boolean matchFirstLine =  matcherFirstLine.matches();
						
						if (matchFirstLine) {
							
							nextIsFirstLine = false;
							
							nextIsBlockStartLine = true;
							
							continue LineReadingLoop;
							
						} else {
							
							return this.getResultForUnmetRule(new NexusFirstLineRule(), lineCounter, currentLine);
							
						}
						
					}
					///////////////////////////////////////
					
					///////////////////////////////////////
					// Try to read a nexus block
					if (nextIsBlockStartLine) {
						
						matcherBeginBlockLine = patternBeginBlockLine.matcher(currentLine);
						
						boolean matchStartBlockLine =  matcherBeginBlockLine.matches();
						
						if ( matchStartBlockLine ) {
							
							String blockName = matcherBeginBlockLine.group(1);
							
							nextIsBlockStartLine = false;
							
							if (blockName.equalsIgnoreCase("DATA")) {
								
								nextIsDataHeaderBlockLine = true;
								
								continue LineReadingLoop;
								
							} else {
								
								nextIsIgnoredBlockLine =true;
								
								nextIsBlockEndLine = true;
								
								continue LineReadingLoop;
								
							}
							
						} else {
							
							return this.getResultForUnmetRule(new NexusBeginBlockRule(), lineCounter, currentLine);
							
						}
						
					}
					///////////////////////////////////////
					
					///////////////////////////////////////
					// Try to read end of block lines
					if (nextIsBlockEndLine) {
						
						matcherEndBlockLine = patternEndBlockLine.matcher(currentLine);
						
						if (matcherEndBlockLine.matches()) {
							
							nextIsIgnoredBlockLine = false;
							nextIsBlockEndLine = false;
							nextIsBlockStartLine = true;
							continue LineReadingLoop;
							
						}
					}
					///////////////////////////////////////
					
					///////////////////////////////////////
					// Try to read lines inside an ignored nexus block
					if (nextIsIgnoredBlockLine) {
						continue LineReadingLoop;
					}
					///////////////////////////////////////
					
					///////////////////////////////////////
					// Try to read lines of the header of 'data' block
					if (nextIsDataHeaderBlockLine) {
						
						matcherDimensionLine = patternDimensionLine.matcher(currentLine);
						matcherFormatLine = patternFormatLine.matcher(currentLine);
						matcherMatrixLine= patternMatrixLine.matcher(currentLine);
						
						if (matcherDimensionLine.matches() || matcherFormatLine.matches()) {
							
							continue LineReadingLoop;
							
						} else if (matcherMatrixLine.matches()) {
							
							nextIsDataSequenceLine = true;
							
							nextIsDataHeaderBlockLine = false;
							
							continue LineReadingLoop;
							
						} else {
							
							return this.getResultForUnmetRule(new NexusDataBlockHeaderRule(), lineCounter, currentLine);
							
						}
						
					}
					///////////////////////////////////////
					
					///////////////////////////////////////
					// Try to read a sequence line
					if (nextIsDataSequenceLine) {
						
						matcherSequenceLine = patternSequenceLine.matcher(currentLine);
						
						if (matcherSequenceLine.matches()) {
							
							String id = matcherSequenceLine.group(1);
							
							String sequence = matcherSequenceLine.group(2);
							
							this.addSequenceFragment(id,sequence);
							
							if (currentLine.contains(";")) {
								
								nextIsDataSequenceLine = false;
								
								nextIsBlockStartLine = true;
								
							}
							
						}
						
					}
					////////////////////////////////////
					
				}
			
			}
			
			//////////////////////////////////
			// Reach the end of the input data
			// without errors
			if (this.getAlignmentBuilder()==null || this.getAlignmentBuilder().size()==0) {
				
				return this.getResultForUnmetRule(new BlankAlignmentRule(), lineCounter, currentLine);
				
			} else {
				
				this.mapToListAlignment();

				return this.getResult();
				
			}
			//////////////////////////////////
			
		} catch (Exception e) {

			return this.getResultForUnmetRule(new ExceptionWhileReadingRule(e.getMessage()),lineCounter,"");

		}
		
	}
	
	@Override
	public String alignmentFormatName() {
		return "Nexus";
	}

	private void mapToListAlignment() {
		
		List<Pair<String, String>> result = new ArrayList<Pair<String,String>>();
		
		for (String seqId : this.getAlignmentBuilder().keySet()) {
			
			String sequence = this.getAlignmentBuilder().get(seqId).toString();
			
			result.add(new Pair<String, String>(seqId, sequence));
			
		}
		
		this.getResult().setAlignment(result);;
		
	}

	private void addSequenceFragment(String id, String sequence) {

		if (this.getAlignmentBuilder()==null) {
			
			this.setAlignmentBuilder(new LinkedHashMap<String, StringBuilder>());
			
		}
		
		if (!this.getAlignmentBuilder().containsKey(id)) {
			
			this.getAlignmentBuilder().put(id, new StringBuilder());
			
		}
		
		this.getAlignmentBuilder().get(id).append(sequence.replaceAll("\\s", ""));
		
	}

	protected BufferedReader removeComments(BufferedReader in) {
		char Opener = '[';
		char Closer = ']';

		boolean isComment=false;
		int c;
		StringBuilder result = new StringBuilder();
		
		try {
			
			while((c=in.read()) >= 0) {
				
				char currentchar = (char) c;
				
				isComment = isComment || (currentchar==Opener);
						
				if (!isComment) {
					result.append(currentchar);
				}
				
				isComment = isComment && !(currentchar==Closer);
				
			}
			
			return new BufferedReader (new StringReader(result.toString())); 
			
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
	}

	protected Map<String, StringBuilder> getAlignmentBuilder() {
		return alignmentBuilder;
	}

	protected void setAlignmentBuilder(Map<String, StringBuilder> alignmentBuilder) {
		this.alignmentBuilder = alignmentBuilder;
	}
	
	private AlignmentReadingResult getResult() {
		return result;
	}
	private void setResult(AlignmentReadingResult alignmentReadingResult) {
		this.result = alignmentReadingResult;
	}
	
	private AlignmentReadingResult getResultForUnmetRule(AlignmentRule unmetRule, int lineNumber, String lineContent) {
		
		unmetRule.setWrongLineNumber(lineNumber);
		
		unmetRule.setWrongLineContent(lineContent);

		this.getResult().setUnmetRule(unmetRule);
		
		return this.getResult();
	}

}
