package fileformats;

import java.io.BufferedReader;

/**
 * Checks if a input data meets the requirements of a given file format.
 * 
 * @author Javier Iserte (jiserte@unq.edu.ar)
 *
 */
public interface FileFormatChecker {

	abstract public boolean complainsFormat(BufferedReader in);
	
}
