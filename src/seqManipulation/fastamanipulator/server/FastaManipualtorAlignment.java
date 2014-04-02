package seqManipulation.fastamanipulator.server;

import java.util.List;

import pair.Pair;

public class FastaManipualtorAlignment {

	//////////////////////////////
	// Private Instance Variables
	private int id;
	
	private String name;
	
	private List<Pair<String,String>> alignment;
	
	//////////////////////////////
	// Constructor
	public FastaManipualtorAlignment(int id, String name, List<Pair<String, String>> alignment) {
		super();
		this.id = id;
		this.name = name;
		this.alignment = alignment;
	}

	//////////////////////////////
	// Getters and Setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Pair<String, String>> getAlignment() {
		return alignment;
	}

	public void setAlignment(List<Pair<String, String>> alignment) {
		this.alignment = alignment;
	}
	
	

}
