package fileformats.genBankIO;

import java.io.BufferedReader;
import java.io.IOException;

import fileformats.FileFormatChecker;

public class GenBankFormatChecker implements FileFormatChecker {

	@Override
	public boolean complainsFormat(BufferedReader in) {

		try {
			while (in.ready()) {
				String line = in.readLine().trim();				
				if (line!=null && !line.equals("")) {
					return line.startsWith("LOCUS");
				}
			return false;
			
			}
		} catch (IOException e) {
			System.err.println("There was an error reading the data:" + e.getMessage());
		}
		
		return false;
		
	}

}
