package fastaIO;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Vector;


public class FastaWriter {

	///////////////////////
	// Public Interface
	public void writeFile(String outputpath, String sequence, String description) {
		this.writeFile(outputpath, sequence, description,false);
	}
	public void writeFile(File out, String sequence, String description) {
		this.writeFile(out, sequence, description,true);
	}
	public void writeFile(String outputpath, String sequence, String description, boolean append) {
		File out = new File(outputpath);
		this.writeFile(out, sequence, description,append);
	}
	public void writeFile(File out, String sequence, String description, boolean append) {
		try {
			this.print(new FileOutputStream(out,append), sequence, description);
		} catch (FileNotFoundException e) {
			System.err.println("An error ocurred while writing the file");
		}
	}
	public void writeFile(File out, List<Pair<String,String>> seqs,  boolean append) {
		try {
			this.print(new FileOutputStream(out,append), seqs);
		} catch (FileNotFoundException e) {
			System.err.println("An error ocurred while writing the file");
		}
	}
	public void print (OutputStream out, List<Pair<String,String>> seqs) {
		this.print(new PrintWriter(out),seqs);
	}
	public void print (OutputStream out, String sequence, String description ) {
		this.print(new PrintWriter(out),sequence,description);
	}
	public void print (PrintWriter out,  String sequence, String description ) {
		List<Pair<String,String>> seqs = new Vector<Pair<String,String>>();
		seqs.add(new Pair<String, String>(description, sequence));
		this.print(out, seqs);
	}
	public void print (PrintWriter out,  List<Pair<String,String>> seqs ) {
		for (Pair<String, String> pair : seqs) {
			out.println(">" + pair.getFirst());
			out.println(pair.getSecond());
		} 
		out.close();
	}
}
