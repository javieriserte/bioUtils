package genBankIO.elements;

/**
 * 
 * Class to store data from a reference of a genbank sequence file.
 * @author Javier
 *
 */
public class Reference {
	private String value;
	private String authors;
	private String title;
	private String journal;
	private String pubmed;
	private String directSubmission;
	private String consortiums;
	private String medline;
	private String remark;
	
	public String getConsortiums() {
		return consortiums;
	}



	// Constructor

public Reference() {
		super();
	}

	// getters and setters
	
	public String getValue() {
		return value;
	}

	
	public String getAuthors() {
		return authors;
	}

	public void setValue(String value) {
		this.value = value;
	}



	public void setAuthors(String authors) {
		this.authors = authors;
	}



	public void setTitle(String title) {
		this.title = title;
	}



	public void setJournal(String journal) {
		this.journal = journal;
	}



	public void setPubmed(String pubmed) {
		this.pubmed = pubmed;
	}



	public void setDirectSubmission(String directSubmission) {
		this.directSubmission = directSubmission;
	}



	public void setConsortiums(String consortiums) {
		this.consortiums = consortiums;
	}



	public void setMedline(String medline) {
		this.medline = medline;
	}



	public void setRemark(String remark) {
		this.remark = remark;
	}



	public String getTitle() {
		return title;
	}

	public String getJournal() {
		return journal;
	}

	public String getPubmed() {
		return pubmed;
	}

	public String getDirectSubmission() {
		return directSubmission;
	}
	
	public String getMedline() {
		return medline;
	}

	public String getRemark() {
		return remark;
	}

}
