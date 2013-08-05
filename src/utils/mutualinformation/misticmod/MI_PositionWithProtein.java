package utils.mutualinformation.misticmod;

public class MI_PositionWithProtein extends MI_Position {

	int prot1;

	int prot2;
	
	public MI_PositionWithProtein(int pos1, int pos2, char aa1, char aa2, 	Double mi) {

		super(pos1, pos2, aa1, aa2, mi);
		
	}

	public int getProt1() {
		return prot1;
	}

	public void setProt1(int prot1) {
		this.prot1 = prot1;
	}

	public int getProt2() {
		return prot2;
	}

	public void setProt2(int prot2) {
		this.prot2 = prot2;
	}
	
	

}
