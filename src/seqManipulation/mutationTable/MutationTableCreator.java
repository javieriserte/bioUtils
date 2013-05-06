package seqManipulation.mutationTable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.List;
import java.util.Vector;

import cmdGA.Parser;
import cmdGA.SingleOption;
import cmdGA.exceptions.IncorrectParameterTypeException;
import cmdGA.parameterType.InputStreamParameter;
import cmdGA.parameterType.PrintStreamParameter;

import fileformats.fastaIO.*;

public class MutationTableCreator {

	// VARIABLES DE CLASE
	
	// VARIABLES DE INTANCIA
	private List<Pair<String, String>> mySeqs;
	
	
	// MAIN EJECUTABLE
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
	
		// Step One : Create the parser
		Parser parser = new Parser();
		
		// Step Two : Create the options
		
		SingleOption infile = new SingleOption(parser, System.in, "-infile", InputStreamParameter.getParameter());
		SingleOption outfile = new SingleOption(parser, System.out, "-out", PrintStreamParameter.getParameter());
		
		// Step Three : Parse the command line
		
		try {
			parser.parseEx(args);
		} catch (IncorrectParameterTypeException e) {
			System.err.println("There was an error parsing the command line");
			System.exit(1);
		}
		
		BufferedReader in = new BufferedReader(new InputStreamReader( (InputStream) infile.getValue() ));
		
		FastaMultipleReader fmr;
		MutationTableCreator mtc = new MutationTableCreator();
				
		fmr = new FastaMultipleReader();
				
		mtc.setMySeqs(fmr.readBuffer(in));
				
		PrintStream out = new PrintStream((PrintStream)outfile.getValue());

		out.print(mtc.getTable('\t'));
		
	}
	
	// CONSTRUCTOR

	// M�TODOS DE CLASE P�BLICOS
	
	// M�TODOS DE INSTANCIA P�BLICOS
	
	public void addSeq(String description, String sequence) {

		if (!this.isInitilized()) {
			// Checks if instance variable mySeqs is not initialized
			this.setMySeqs(new Vector<Pair<String,String>>());
		}
		this.getMySeqs().add(new Pair<String, String>(description, sequence));
			// Adds a new sequence.
	}
	
	public int getNumberOfSequences() {
		return this.getMySeqs().size();
	}
	
	public String getDescOfSequence(int numberOrder) {
		return this.getMySeqs().get(numberOrder).getFirst();
	}
	
	public String getSequence(int numberOrder) {
		return this.getMySeqs().get(numberOrder).getSecond();
	}
	
	public boolean isEmpty(){
		return !this.isInitilized() || this.getMySeqs().isEmpty();
	}

	/**
	 * This method checks if all the sequences have the same lenght.
	 * @return
	 */
	public boolean verifySizeEquality() {
		boolean result = true;
		int initialSize;

		if (this.isEmpty()) return true;
		initialSize = this.getSequence(0).length();

		for(int c = 1;c<this.getNumberOfSequences();c++) {
			result = result && (initialSize==this.getSequence(c).length());
		}
		
		return result;
	}
	
	public String getTable(char separatorChar, List<Pair<String,String>> alignment) {
		
		this.setMySeqs(alignment);
		
		return this.getTable(separatorChar);
		
	}
	
	public String getTable(char separatorChar) {
	
		String result ="";
		
		if (!this.verifySizeEquality()) return "";
		
		Diferences data = this.getDiferences();
		
		String linea="";
		
		
		// First Line. with Sequences Descriptions.
		
		for(int j=0;j<this.getNumberOfSequences();j++){
			linea = linea + separatorChar + this.getDescOfSequence(j);
		}
		
		result = linea + System.getProperty("line.separator");;
		
		// From Second Line. with Sequences Descriptions.
		for (int i=0;i<data.getPos().length;i++) {
		
			linea = (Integer.toString(data.getPos()[i] + 1));
			
			for (int j=0;j<this.getNumberOfSequences();j++) {
				linea= linea + separatorChar + data.getSeqDiff()[j][i];
			}
			result = result + linea + System.getProperty("line.separator");
			
		}
		
		
		
		return result;
	}
	
	
	// GETTERS & SETTERS
	

	public void setMySeqs(List<Pair<String, String>> mySeqs) {
		this.mySeqs = mySeqs;
	}


	public List<Pair<String, String>> getMySeqs() {
		return mySeqs;
	}
	// M�TODOS DE CLASE PRIVADOS
	
	// M�TODOS DE INSTANCIA PRIVADOS

	private boolean isInitilized() {
		return (this.getMySeqs()!=null);
	}

	private Diferences getDiferences() {
		int Nseqs=this.getNumberOfSequences();
		char[][] resultSeq = new char[Nseqs][];
		Vector<Integer> resultPos = new Vector<Integer>();
		
		resultPos.toArray();
		
		int sequenceLength = this.getSequence(0).length();
		Diferences result = new Diferences();
		
		StringBuilder[] f = new StringBuilder[Nseqs];
		for (int i=0;i<Nseqs;i++) f[i] = new StringBuilder();
		
		for (int i=0;i<sequenceLength;i++) {
			if (!this.sameCharAt(i)) {
				
				for (int j=0;j<Nseqs;j++) {
					f[j].append(this.getSequence(j).charAt(i));
					
				}
				resultPos.add(i);
			}
		}

		for (int i=0;i<Nseqs;i++) resultSeq[i] = f[i].toString().toCharArray();

		
		Integer[] pos = new Integer[resultPos.size()];
		pos = resultPos.toArray(pos);
		result.setPos(pos);
		
		result.setSeqDiff(resultSeq);
		
		return result;
	}

	private boolean sameCharAt(int i) {
		boolean result  = true;
		
		char ini = Character.toUpperCase(this.getSequence(0).charAt(i));
		
		for (int j =1; j < this.getNumberOfSequences();j++) {
			result = result && (ini==Character.toUpperCase(this.getSequence(j).charAt(i)));
		}
		
		return result;
	}

	// CLASES AUXILIARES PRIVADAS
	
	private class Diferences {
		private char[][] seqDiff;
		private Integer[] pos;
		
		// GETTERS & SETTERS
		public void setSeqDiff(char[][] seqDiff) {
			this.seqDiff = seqDiff;
		}
		public char[][] getSeqDiff() {
			return seqDiff;
		}
		public void setPos(Integer[] pos) {
			this.pos = pos;
		}
		public Integer[] getPos() {
			return pos;
		}
		
	}
	
}
