package utils.mutualinformation.mimatrixviewer;


public class DataContainer {

	private MI_Matrix data;
	private String title;

	private int[] lengths;
	private String[] proteinNames;
	
	
	
	
	public DataContainer(MI_Matrix data, String title, int[] lengths, String[] proteinNames) {
		super();
		this.data = data;
		this.title = title;
		this.lengths = lengths;
		this.proteinNames = proteinNames;
	}
	
	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}
	public MI_Matrix getData() {
		return data;
	}
	public void setData(MI_Matrix data) {
		this.data = data;
	}
	public int[] getLengths() {
		return lengths;
	}
	public void setLengths(int[] lengths) {
		this.lengths = lengths;
	}
	public String[] getProteinNames() {
		return proteinNames;
	}
	public void setProteinNames(String[] proteinNames) {
		this.proteinNames = proteinNames;
	}
	
	
	
}
