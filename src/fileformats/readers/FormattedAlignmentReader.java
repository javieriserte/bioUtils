package fileformats.readers;

import java.io.BufferedReader;

/**
 * An itenrface for classes that read sequence alignment.
 * 
 * @author Javier
 *
 */
public interface FormattedAlignmentReader {

	public AlignmentReadingResult read(BufferedReader in);
	
	public String alignmentFormatName();
}
