package utils.mutualinformation.misticmod;

public class CircosChromosome {
	
	String id;
	String label;
	int start;
	int end;
	String color;
	
	public CircosChromosome(String id, String label, int start, int end,
			String color) {
		super();
		this.id = id;
		this.label = label;
		this.start = start;
		this.end = end;
		this.color = color;
	}

	@Override
	public String toString() {
		
		return "chr - " + id + " " +label + " " + String.valueOf(this.start) + " " +String.valueOf(this.end) +" " + this.color;
		
	}
	

}
