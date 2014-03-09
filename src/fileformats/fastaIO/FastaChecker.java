package fileformats.fastaIO;

import java.io.BufferedReader;
import java.io.IOException;

import fileformats.readers.FileFormatChecker;

public class FastaChecker implements FileFormatChecker{

	@Override
	public boolean complainsFormat(BufferedReader in) {
		
		try {
			while (in.ready()) {
				try {
					String line = in.readLine().trim();
					if (line != null && !line.equals("")) {
						return line.startsWith(">");
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		} catch (IOException e) {
			System.err.println("There was an error reading the data:" + e.getMessage());
		}
		
		return false;
	}

}
