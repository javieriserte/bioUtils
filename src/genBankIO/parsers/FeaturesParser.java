package genBankIO.parsers;

import genBankIO.elements.Feature;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Parses the features part of a GenBank record.<br>
 * 
 * Each feature consist in a feature key or name, a location and qualifiers.
 * The feature key begins in column 6 and may be no more than 15
 * characters in length. The location begins in column 22. Feature
 * qualifiers begin on subsequent lines at column 22. Location,
 * qualifier, and continuation lines may extend from column 22 to 80.
 * 
 * @author Javier Iserte (jiserte@unq.edu.ar)
 *
 */
public class FeaturesParser {
	/**
	 * Parse an string with the features part of the record.
	 * 
	 * @param in
	 * @return a <code>List<code> of <code>Feature</code> objects. 
	 */
	public List<Feature> parse(String in) {
		BufferedReader br = new BufferedReader(new StringReader(deinterleaveFeatures(in)));
		List<Feature> feat = new ArrayList<Feature>();
		
		
		try {
			
			while(br.ready()) {
				
				String line = br.readLine();
				
				if (line==null) break;
				
				String featName = this.getFeatureName(line); 
				
				if (!featName.equals("")) {
					// Is A Feature ...
					feat.add(new Feature(featName,line.substring(21).trim()));
				} else {
					// or is a Qualifier.
					int eqpos = line.indexOf('=');
					String q ;
					String v ;
					if(eqpos >=0) {
						q = line.substring(0,eqpos).trim();
						v = this.dequote(line.substring(eqpos+1));						
					} else {
						q = line.trim();
						v = "";						
					}
					if (q.length()>1) {
						q = q.substring(1);
					}
					if (q.toUpperCase().equals("TRANSLATION")) {
						v = v.replaceAll(" ", "");
					
					}
					if (!q.equals("")) {
						if (feat.size()>0) {
							feat.get(feat.size()-1).addQualifier(q, v);
						}
					}
					
				}
			}
			
		} catch (IOException e) {
			
			e.printStackTrace();
			
		}

		return feat;
		
	}

	// Private Methods

	/**
	 * Removes quoting chars from the beginning or the end of a String.
	 * @param string
	 * @return
	 */
	private String dequote(String string) {
		int from = 0;
		int to = string.length();
		
		if (string.startsWith("\"")) from++;
		if (string.endsWith("\"")) to--;
		return string.substring(from, to);
	}

	/**
	 * Prepares the input string to be parsed.
	 * Removes blank lines, join all the qualifier data in one line.
	 * 
	 * @param in the input features string.
	 * @return a string modified in order to be more easily parsed.
	 */
	private String deinterleaveFeatures(String in) {
		BufferedReader br = new BufferedReader(new StringReader(in));
		StringBuilder out = new StringBuilder();
		
		try {
			
			br.readLine(); // Removes the first "FEATURES" field.
			
			
			while(br.ready()) {
				
				String line = br.readLine();
				
				if (line==null) break;
				
				if (!line.trim().equals("")) {
					// Skips blank lines
					if (this.continuesAPreviousQualifier(line)) {
						if (out.length()!=0) {
							out.append(" " + line.substring(21));
						}
					} else {
						if (out.length()!=0) {out.append("\r\n");}
						out.append(line);
					}
				}
				
			}
			
		} catch (IOException e) {
			
			e.printStackTrace();
			
		}
		
		
		return out.toString();
	}
	
	/**
	 * Returns the feature key name if there is one.
	 * 
	 * @param line a <code>String</code> with the line being parsed.
	 * @return a <code>String</code> with the name of the Feature.
	 */
	private String getFeatureName(String line) {
		if (line.length()>21) {
		return line.substring(0,21).trim().toUpperCase();
		} else {
			return line.trim();
		}
	}

	/**
	 * Checks if the current line is likely to continue the qualifier 
	 * data from the previous line.
	 * 
	 * There is a known case were the method do not behave as expected. 
	 * When the first line being parsed do not have a feature name (a bad 
	 * formed genbank record), this method informs that this first line 
	 * continues a previous qualifier.   
	 *  
	 * @param line a <code>String</code> with the line being parsed.
	 * @return <b>True</b> if the line appears to continue the previous, <b>False</b> otherwise.
	 */
	private boolean continuesAPreviousQualifier(String line) {
		if (line.length()>21) {
		return (this.getFeatureName(line).equals("")) && (line.charAt(21)!='/');
		} else {
			return false;
		}
	}
	

	
}

