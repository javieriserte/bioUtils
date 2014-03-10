package fileformats.readers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import fileformats.readers.clustal.ClustalFormattedAlignmentReader;
import fileformats.readers.fasta.FastaFormattedAlignmentReader;
import fileformats.readers.nexus.NexusFormattedAlignmentReader;
import fileformats.readers.phylip.PhylipFormattedAlignmentReader;
import fileformats.readers.pir.PirFormattedAlignmentReader;
import fileformats.readers.rules.ExceptionWhileReadingRule;

/**
 * Tries to read a input data from an alignment formatted in any of the standard 
 * alignment formats. At the moment Fasta, Nexus, Pir, Phylip and Clustal are supported. 
 * 
 * @author javier iserte.
 *
 */
public class GenericAlignmentReader {
	
	///////////////////////////////////
	// Class constants
	private static int BUFFER_LENGTH_FOR_MARKING = 10000000;
	// End of class constants
	///////////////////////////////////

	///////////////////////////////////
	// Public Interface
	public AlignmentReadingResult read(File infile) {
		try {
			
			return this.read(new BufferedReader(new FileReader(infile)));
			
		} catch (FileNotFoundException e) {
			
			return this.getExeceptionReadingResult(e); 
			
		}
		
	}
	
	public AlignmentReadingResult read(String input) {
		
		List<AlignmentReadingResult> results = new ArrayList<AlignmentReadingResult>();
		
		List<FormattedAlignmentReader> readers = getReaderList();
		
		for (FormattedAlignmentReader formattedAlignmentReader : readers) {
			
			AlignmentReadingResult result = formattedAlignmentReader.read(new BufferedReader(new StringReader(input)));
			
			if (result.successfulRead()) {
				
				results.clear();
				
				results.add(result);
				
				break;
				
			} else {
				
				results.add(result);
				
			}
			
		}
		
		return this.selectBestResult(results);
		
	}


	public AlignmentReadingResult read(BufferedReader in) {
		
		if (in.markSupported()) {
			
			return this.selectBestResult(this.attemptToReadFromMarkedBuffer(in));
			
		} else {
			
			try {
				
				return this.read(getEntireBufferContent(in));
				
			} catch (IOException e) {
				
				return this.getExeceptionReadingResult(e);
				
			}
			
		}
		
	}
	// End of Public interface
	///////////////////////////////////////////
	
	private List<AlignmentReadingResult> attemptToReadFromMarkedBuffer( BufferedReader in) {
		
		List<AlignmentReadingResult> results = new ArrayList<AlignmentReadingResult>();

		try {
			
			in.mark(GenericAlignmentReader.BUFFER_LENGTH_FOR_MARKING);

			List<FormattedAlignmentReader> readers = getReaderList();
			
			for (FormattedAlignmentReader formattedAlignmentReader : readers) {

				in.reset();
				
				AlignmentReadingResult result = formattedAlignmentReader.read(in);
				
				if (result.successfulRead()) {

					results.clear();
					
					results.add(result);
					
					return results;
					
				} else {
					
					results.add(result);
					
				}
				
			}
			
			return results;
		
		} catch (IOException e) {
			
			results.add(getExeceptionReadingResult(e));
			
			return  results;
			
		}
	}

	private String getEntireBufferContent(BufferedReader in) throws IOException {
		
		StringBuilder entireContentBuilder = new StringBuilder();
		
		String currentline = null;
		
		while ((currentline=in.readLine())!=null) {
			
			entireContentBuilder.append(currentline);
			
		}
		return entireContentBuilder.toString();
	}

	private AlignmentReadingResult getExeceptionReadingResult(IOException e) {
		AlignmentReadingResult result = new AlignmentReadingResult();
		
		result.setUnmetRule(new ExceptionWhileReadingRule(e.getMessage()));
		
		result.getUnmetRule().setWrongLineNumber(0);
		
		result.getUnmetRule().setWrongLineContent("");
		return result;
	}
	
	private List<FormattedAlignmentReader> getReaderList() {
		
		List<FormattedAlignmentReader> readers = new ArrayList<FormattedAlignmentReader>();
		
		readers.add(new FastaFormattedAlignmentReader());
		
		readers.add(new ClustalFormattedAlignmentReader());
		
		readers.add(new PhylipFormattedAlignmentReader());
		
		readers.add(new NexusFormattedAlignmentReader());
		
		readers.add(new PirFormattedAlignmentReader());
		
		return readers;
	}
	
	private AlignmentReadingResult selectBestResult( List<AlignmentReadingResult> results) {
		
		AlignmentReadingResult deepestResult = null;
		
		int deepestLine = 0;
		
		for (AlignmentReadingResult alignmentReadingResult : results) {
			
			if (alignmentReadingResult.successfulRead()) {
				
				return alignmentReadingResult;
				
			} else {
				
				if (alignmentReadingResult.getUnmetRule().getWrongLineNumber()>=deepestLine) {
				
					deepestResult = alignmentReadingResult;
					
				}
				
			}
			
		}
		
	return deepestResult;
	
	}
	
}
