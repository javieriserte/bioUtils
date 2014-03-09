package fileformats.readers.clustal;

import java.util.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pair.Pair;
import fileformats.readers.AlignmentReadingResult;
import fileformats.readers.FormattedAlignmentReader;
import fileformats.readers.rules.AlignmentRule;
import fileformats.readers.rules.BlankAlignmentRule;
import fileformats.readers.rules.ExceptionWhileReadingRule;

/**
 * Checks Clustal format.
 * <br>
 * Clustal format:<br>
 * <ol>
 *  <li>The first line in the file must start with the words "CLUSTAL W" or "CLUSTALW". Other information in the first line is ignored.</li>
 *  <li>One or more empty lines.</li>
 *  <li>One or more blocks of sequence data. Each block consists of:</li>
 *      <ol>
 *      <li>One line for each sequence in the alignment. Each line consists of:</li>
 *          <ol>
 *          <li>the sequence name</li>
 *          <li>white space</li>
 *          <li>up to 60 sequence symbols.</li>
 *          <li>optional - white space followed by a cumulative count of residues for the sequences</li>
 *          </ol>
 *      <li>A line showing the degree of conservation for the columns of the alignment in this block.
 *      One or more empty lines.</li>
 *      </ol>
 *  </ol> 
 *  Some rules about representing sequences:
 *  <ol>
 *  	<li>Case doesn't matter.</li>
 *  	<li>Sequence symbols should be from a valid alphabet.</li>
 *  	<li>Gaps are represented using hyphens ("-").</li>
 *  	<li>The characters used to represent the degree of conservation are</li>
 *          <ol>
 *          <li>*   -- all residues or nucleotides in that column are identical</li>
 *          <li>:   -- conserved substitutions have been observed</li>
 *          <li>.   -- semi-conserved substitutions have been observed</li>
 *          <li>    -- no match.</li>
 *          </ol>
 * 	 </ol>
 * @author javier
 *
 */
public class ClustalFormatReader implements FormattedAlignmentReader {

	private static final String SEQUENCE_LINE_REGEX = "^([^\\s]+)\\s+([^\\s]{1,60})(\\s[0-9]+)*$";
	private static final String CONSERVATION_LINE_REGEX =  "^[\\s:.*]+$";
	
	@Override
	public AlignmentReadingResult read(BufferedReader in) {

		AlignmentReadingResult result = new AlignmentReadingResult();
		LinkedHashMap<String,StringBuilder> resultAlignmentMap = new LinkedHashMap<>();
		List<Pair<String, String>> resultAlignmentPairList = new ArrayList<Pair<String,String>>();
		
		int lineCounter = 0;
		
		try {
			
			int numberOfSequenceLines = 0;
			String currentLine = null;
			

			////////////////////////////////
			// Read the first line of data
			// and process it in a different way
			if ( (currentLine = in.readLine()) == null || !currentLine.matches("^CLUSTAL.+$") ) {
				
				return getErrorOnFirstLineResult(result, lineCounter, currentLine);
				
			}
			
			lineCounter++;
			////////////////////////////////


			////////////////////////////////
			// Read of the other Lines in data
			// lines can contain:
			//  - descrition , sequence and number data.  or
			//  - descrition and sequence data
			//  - conservation data
			//  - blank line 
			// conservation data lines and blank lines are ignored.
			// sequence order or size is not checked.
			Pattern paternSequenceLine = Pattern.compile(ClustalFormatReader.SEQUENCE_LINE_REGEX);
			
			Pattern paternConservationLine = Pattern.compile(ClustalFormatReader.CONSERVATION_LINE_REGEX);
			
			while ((currentLine = in.readLine()) != null) {
			
				lineCounter++;
				
				boolean lineMatchesEmptyLine = currentLine.trim().equals(""); 
				
				if (!lineMatchesEmptyLine) {
					
					Matcher matcherSequenceLine = paternSequenceLine.matcher(currentLine);
					
					Matcher matcherConservationLine = paternConservationLine.matcher(currentLine);
					
					boolean lineMatchesSequenceLine = matcherSequenceLine.matches();
						
					boolean lineMatchesConservationLine = matcherConservationLine.matches(); 
				
					if (lineMatchesSequenceLine){
						
						numberOfSequenceLines ++;
						
						addFragmentToSequence(resultAlignmentMap, matcherSequenceLine);
						
					} else if (!lineMatchesConservationLine) {
						
						return getResultForUnrecongnizedLine(result, lineCounter, currentLine);
						
					}

				}
			
			}
			//////////////////////////////

			////////////////////////////////////////////
			// Reach end of data without any error.
			if (numberOfSequenceLines==0) {
				
				return getResultForEmptyAlignment(result, lineCounter);
				
			} else { 
			
				return maptoListSequences(result, resultAlignmentMap, resultAlignmentPairList);
			
			}
			////////////////////////////////////////////

			
		} catch (IOException e) {
			
			return getResultForException(result, e);
			
		}

	}

	////////////////////////////////////
	// Private methods
	private AlignmentReadingResult getErrorOnFirstLineResult(AlignmentReadingResult result,
			int lineCounter, String currentLine) {
		AlignmentRule unmetRule;
		unmetRule = (currentLine == null)?new BlankAlignmentRule():new ClustalHeaderRule();
		
		unmetRule.setWrongLineNumber(lineCounter);
		
		unmetRule.setWrongLineContent(currentLine);
		
		result.setUnmetRule(unmetRule);
		
		return result;
	}
	
	private AlignmentReadingResult getResultForUnrecongnizedLine(
			AlignmentReadingResult result, int lineCounter, String currentLine) {
		AlignmentRule unmetRule;
		unmetRule = new SequenceOrConservationClustalRule();
		
		unmetRule.setWrongLineNumber(lineCounter);
		
		unmetRule.setWrongLineContent(currentLine);
		
		result.setUnmetRule(unmetRule);
		
		return result;
	}

	private void addFragmentToSequence(
			LinkedHashMap<String, StringBuilder> resultAlignmentMap,
			Matcher matcherSequenceLine) {
		String sequenceDescription = matcherSequenceLine.group(1);
		String sequence            = matcherSequenceLine.group(2);
		
		if (!resultAlignmentMap.containsKey(sequenceDescription)) {
			
			resultAlignmentMap.put(sequenceDescription, new StringBuilder());
			
		}
		
		resultAlignmentMap.get(sequenceDescription).append(sequence);
	}
	
	private AlignmentReadingResult getResultForEmptyAlignment(AlignmentReadingResult result, int lineCounter) {
		AlignmentRule unmetRule;
		
		unmetRule = new BlankAlignmentRule();
		
		unmetRule.setWrongLineNumber(lineCounter);
		
		unmetRule.setWrongLineContent("");
		
		result.setUnmetRule(unmetRule);
		
		return result;
	}



	private AlignmentReadingResult maptoListSequences(AlignmentReadingResult result,
			LinkedHashMap<String, StringBuilder> resultAlignmentMap,
			List<Pair<String, String>> resultAlignmentPairList) {
		for (String description : resultAlignmentMap.keySet()) {
			
			String sequence = resultAlignmentMap.get(description).toString();
			
			resultAlignmentPairList.add(new Pair<>(description,sequence));
			
		}
		
		result.setAlignment(resultAlignmentPairList);
		
		return result;
	}

	private AlignmentReadingResult getResultForException( AlignmentReadingResult result, IOException exception) {
		
		AlignmentRule unmetRule = new ExceptionWhileReadingRule(exception.getMessage());
		
		unmetRule.setWrongLineNumber(0);
		
		unmetRule.setWrongLineContent("");
		
		result.setUnmetRule(unmetRule);
		
		return result;
	}
	
}
