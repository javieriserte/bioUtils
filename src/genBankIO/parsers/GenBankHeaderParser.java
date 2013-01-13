package genBankIO.parsers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import genBankIO.elements.GenBankHeader;

/**
 * Parses the header of a GenBank record
 * 
 * @author Javier Iserte (jiserte@unq.edu.ar)
 *
 */
public class GenBankHeaderParser {
	
	/**
	 * Parses a String with the header part of a GenBank record.
	 * 
	 * @param in the input String with the GenBank header data.
	 * @return a <code>GenBankHeader</code> object with the data.
	 */
	public GenBankHeader parse(String in) {
		GenBankHeader gbh = new GenBankHeader();
		BufferedReader str = new BufferedReader(new StringReader(deInterleaveHeader(in)));

		try {
			String firstline = ""; 
			while (firstline.equals("")) { firstline = str.readLine(); }
			
			this.parseFirstLine(firstline.trim(), gbh);
				// the first line is parsed differently from the rest.
			
			while(str.ready()) {
				
				String currentline = str.readLine();
				
				if (currentline == null) break;
 				  // if the current line is null then goes out from the while loop.
				
				String field = this.getFieldName(currentline);
					// get the field name
				if (currentline.length()>12) gbh.setFieldFromString(field, currentline.substring(12));
				else gbh.setFieldFromString(field, "");
					// stores the data for this field
			}

		} catch (IOException e) { e.printStackTrace(); }
		
		return gbh;

	}

	// private Methods

	/**
	 * Retrieves the name of a fieldname is there is any.
	 * 
	 * @param currentline
	 * @return a <code>String</code> with the name of the field.
	 */
	private String getFieldName(String currentline) {
		if (currentline.length()>=12) return currentline.substring(0, 12).trim();
		return currentline.trim();
		
	}
	/**
	 * Parses the first line of a genbak header.<br>
	 * 
	 * According to Genbank release notes these are the format of the first line.
	 * <pre>
	 * Positions  Contents
     * ---------  --------
     * 01-05      'LOCUS'
     * 06-12      spaces
     * 13-28      Locus name
     * 29-29      space
     * 30-40      Length of sequence, right-justified
     * 41-41      space
     * 42-43      bp
     * 44-44      space
     * 45-47      spaces, ss- (single-stranded), ds- (double-stranded), or
     *            ms- (mixed-stranded)
     * 48-53      NA, DNA, RNA, tRNA (transfer RNA), rRNA (ribosomal RNA), 
     *            mRNA (messenger RNA), uRNA (small nuclear RNA).
     *            Left justified.
     * 54-55      space
     * 56-63      'linear' followed by two spaces, or 'circular'
     * 64-64      space
     * 65-67      The division code (see Section 3.3)
     * 68-68      space
     * 69-79      Date, in the form dd-MMM-yyyy (e.g., 15-MAR-1991)
     * </pre>
     * 
     * But is common to find record that do not complain this format. So a regular expression strategy was used.
     * 
     * @param firstline if the first line of the header of a genbank record
     * @param GenBankHeader the object were the results will be stored.
	 */
	private void parseFirstLine(String firstline, GenBankHeader gbh) {
		firstline = firstline.replaceAll(" BP ", " ");
		String[] ss = firstline.split(" +");
		if (ss.length ==6) {
		gbh.setLocus(ss[1]);
		gbh.setLength(ss[2]);
		gbh.setMolType(ss[3]);
		gbh.setDivision(ss[4]);
		gbh.setDate(ss[5]);
		}
	}
	
	/**
	 * Given a multiline Header from a genbank file, strips out 
	 * the line that starts with no keyword (the 0 to 12 first chars
	 * are spacers) and adds the content (from char 13 to the end 
	 * to the last line).<br> 
	 * <br>
	 * Example:<br>
	 * <br>
	 * 
	 * <pre>
	 * Input:
	 * 
	 * ...
	 * REFERENCE 2  (bases 1 to 5028)
     * AUTHORS   Roemer,T., Madden,K., Chang,J. and Snyder,M.
     * TITLE     Selection of axial growth sites in yeast requires Axl2p, a novel
     *           plasma membrane glycoprotein
     * JOURNAL   Genes Dev. 10 (7), 777-793 (1996)
     * ...
     * 
	 * Output:
	 * 
	 * ...
	 * REFERENCE 2  (bases 1 to 5028)
     * AUTHORS   Roemer,T., Madden,K., Chang,J. and Snyder,M.
     * TITLE     Selection of axial growth sites in yeast requires Axl2p, a novel plasma membrane glycoprotein
     * JOURNAL   Genes Dev. 10 (7), 777-793 (1996)
     * ...
     * </pre>
     * Is part of the parsing work flow.
	 * @param in the string corresponding to the genbank register.
	 * @return a String corresponding to the input data, but not interleaved.
	 */
	private String deInterleaveHeader(String in) {
		BufferedReader str = new BufferedReader(new StringReader(in));
		StringBuilder out = new StringBuilder();
     	try {
			while (str.ready()) {
				
				String currentLine = str.readLine();
				
				if (currentLine == null) break;
				if (!currentLine.trim().equals("")) {
					// Skip Blank Lines
					String field = this.getFieldName(currentLine);
					if (!field.equals("")) {
						if (out.length() != 0) {out.append("\r\n");}
						out.append(currentLine);
					} else {
						out.append(" ");		
						out.append(currentLine.substring(12));
					}
				} 
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return out.toString();
	}
	
}
