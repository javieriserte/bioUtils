package genBankIO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import genBankIO.elements.Origin;

public class OriginParser {
	
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
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			

		return new Origin(out.toString());
	}

}
