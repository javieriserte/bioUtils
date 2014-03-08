package fileformats.clustal;

import java.io.BufferedReader;
import java.io.IOException;
import fileformats.FileFormatChecker;

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
public class ClustalWFormatChecker implements FileFormatChecker {

	@Override
	public boolean complainsFormat(BufferedReader in) {
		
		try {
			
			int numberOfSequenceLines = 0;
			String currentLine = null;
			
			// Rule First Line
			if ( (currentLine = in.readLine()) == null || !currentLine.matches("^CLUSTAL.+$") ) {
				return false;
			}

			// Other Lines
			while ((currentLine = in.readLine()) != null) {
			
				if ( ! currentLine.trim().equals("")) {

					if (currentLine==null || ! (currentLine.matches("^[^\\s]+\\s+[^\\s]{1,60}(\\s[0-9]+)*$") || currentLine.matches("^[\\s:.*]+$"))) {
					//if (currentLine==null || !currentLine.matches("^[^\\s]\\s+[^\\s]{1,60}(\\s[0-9]+)*$")) {
						
						return false;
						
					} else {
						numberOfSequenceLines ++;
					}

				}
			
			}

			// Reach End of stream without any error.
			return numberOfSequenceLines>=1;
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return false;
		
	
	}
	
}
