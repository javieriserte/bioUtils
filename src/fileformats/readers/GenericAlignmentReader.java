package fileformats.readers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fileformats.readers.clustal.ClustalFormattedAlignmentReader;
import fileformats.readers.fasta.FastaFormattedAlignmentReader;
import fileformats.readers.faults.ExceptionWhileReadingFault;
import fileformats.readers.nexus.NexusFormattedAlignmentReader;
import fileformats.readers.phylip.PhylipFormattedAlignmentReader;
import fileformats.readers.pir.PirFormattedAlignmentReader;

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
	public List<AlignmentReadingResult> read(File infile) {
		try {
			
			return this.read(new BufferedReader(new FileReader(infile)));
			
		} catch (FileNotFoundException e) {
			
			return this.getExeceptionReadingResult(e); 
			
		}
		
	}
	
	public List<AlignmentReadingResult> read(String input) {
		
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
		
		return this.sortResultByReadingDepth(results);
		
	}


	public List<AlignmentReadingResult> read(BufferedReader in) {
		
		if (in.markSupported()) {
			
			return this.sortResultByReadingDepth(this.attemptToReadFromMarkedBuffer(in));
			
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
	
	////////////////////////////////////////////
	// Private and protected methods
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
			
			return  getExeceptionReadingResult(e);
			
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

	private List<AlignmentReadingResult> getExeceptionReadingResult(IOException e) {
		
		ArrayList<AlignmentReadingResult> listResult = new ArrayList<AlignmentReadingResult>();
		
		AlignmentReadingResult result = new AlignmentReadingResult();
		
		result.setFault(new ExceptionWhileReadingFault(e.getMessage()));
		
		result.getFault().setWrongLineNumber(0);
		
		result.getFault().setWrongLineContent("");
		
		result.getFault().setFaultProducerReader(null);
		
		listResult.add(result);
		
		return listResult;
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
	
	private List<AlignmentReadingResult> sortResultByReadingDepth( List<AlignmentReadingResult> results) {
		
		Collections.sort(results, Collections.reverseOrder(new AlignmentReadingResultDepthComparator()));
		
		return results;
	
	}
	
	// End of private and protected methods
	///////////////////////////////////////////
	
}
