package genBankIO.parsers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import genBankIO.elements.Origin;

/**
 * Parses the origin part of a GenBank record.
 * 
 * @author Javier Iserte (jiserte@unq.edu.ar)
 *
 */
public class OriginParser {
	
	/**
	 * Parses a <code>String</code> with the sequence data 
	 * of a GenBank record.
	 * 
	 * @param in the input <code>String</code> to be parsed.s
	 * @return a <code>Origin</code> object with the sequence data.
	 */
	public Origin parse(String in) {
		BufferedReader br = new BufferedReader(new StringReader(in));
		StringBuilder out = new StringBuilder();
		
		try {
			while(br.ready()) {
				String line;
				try {
					
					line = br.readLine();
					
					if(line == null) break;
					
					out.append(line.replaceAll("[0-9 ]", ""));
					
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new Origin(out.toString());
	}

}
