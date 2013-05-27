package seqManipulation.orf.seqsource;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import fileformats.fastaIO.Pair;

public class TextSequenceSource extends SequenceSource {

	public TextSequenceSource(BufferedReader in) {
		super(in);
	}

	@Override
	public List<Pair<String, String>> getSequences() {
		List<Pair<String,String>> sequences = new ArrayList<Pair<String,String>>();

		try {
			String line = "";
			
			int counter = 1;
			
			while ((line = in.readLine())!= null) {
				
				Pair<String, String> pair = new Pair<String, String>("Sequence:"+counter, line.trim());

				sequences.add(pair);
				
			}
			
			return sequences;

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
		
	}

}
