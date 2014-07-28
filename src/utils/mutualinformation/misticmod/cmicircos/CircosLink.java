package utils.mutualinformation.misticmod.cmicircos;

public class CircosLink {

	String chr1;
	String chr2;
	
	int pos1_chr1;
	int pos2_chr1;
	
	int pos1_chr2;
	int pos2_chr2;
	
	
	public CircosLink(String chr1, String chr2, int pos1_chr1, int pos2_chr1,
			int pos1_chr2, int pos2_chr2) {
		super();
		this.chr1 = chr1;
		this.chr2 = chr2;
		this.pos1_chr1 = pos1_chr1;
		this.pos2_chr1 = pos2_chr1;
		this.pos1_chr2 = pos1_chr2;
		this.pos2_chr2 = pos2_chr2;
	}


	@Override
	public String toString() {
		
		return chr1 +"\t" +String.valueOf(pos1_chr1) + "\t" + String.valueOf(pos2_chr1) + "\t" +
				chr2 +"\t" +String.valueOf(pos1_chr2) + "\t" + String.valueOf(pos2_chr2);
		
	}

	
	
	
}
