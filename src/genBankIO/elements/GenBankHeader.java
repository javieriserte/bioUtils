package genBankIO.elements;

import java.util.ArrayList;
import java.util.List;

public class GenBankHeader {
	
	private String locus;
	private String length;
	private String molType;
	private String division;
	private String date;
	private String definition;
	private String accession;
	private String version;
	private String keywords;
	private String source;
	private String organism;
	private String project;
	private String dblink;
	private String segment;
	private String comment;
	private String basecount;
	private String contig;
	private String nid;
	
	private List<Reference> references;	
	


	// Constructor
	public GenBankHeader() {
		super();
		this.references = new ArrayList<Reference>();
	}

	// Public Interface 
	
	public void addReference(Reference reference) {
		this.references.add(reference);
	}
	
	public int getNumberOfReferences() {
		return this.references.size();
	}
	
	public Reference getReference(int index) {
		return this.references.get(index);
	}
	
	public void setFieldFromString(String fieldname, String value) {
		if (fieldname.equals( "DEFINITION")) {this.setDefinition(value);};
		if (fieldname.equals( "ACCESSION")) {this.setAccession(value);};
		if (fieldname.equals( "VERSION")) {this.setAccession(value);};
		if (fieldname.equals( "NID")) {this.setNid(value);};
		if (fieldname.equals( "PROJECT")) {this.setProject(value);};
		if (fieldname.equals( "DBLINK")) {this.setDblink(value);};
		if (fieldname.equals( "KEYWORDS")) {this.setKeywords(value);};
		if (fieldname.equals( "SEGMENT")) {this.setComment(value);};
		if (fieldname.equals( "SOURCE")) {this.setSource(value);};
		if (fieldname.equals( "ORGANISM")) {this.setOrganism(value);};
		if (fieldname.equals( "COMMENT")) {this.setComment(value);};
		if (fieldname.equals( "BASE COUNT")) {this.setBasecount(value);};
		if (fieldname.equals( "CONTIG")) {this.setContig(value);};
		
		
  		if (fieldname.equals( "REFERENCE")) {
  			this.addReference(new Reference());
  			this.getReference(this.getNumberOfReferences()-1).setValue(value);
  			};
  		
  		if (fieldname.equals( "AUTHORS")) {this.getReference(this.getNumberOfReferences()-1).setAuthors(value);};
  		if (fieldname.equals( "CONSRTM")) {this.getReference(this.getNumberOfReferences()-1).setConsortiums(value);};
  		if (fieldname.equals( "TITLE")) {this.getReference(this.getNumberOfReferences()-1).setTitle(value);};
  		if (fieldname.equals( "JOURNAL")) {this.getReference(this.getNumberOfReferences()-1).setJournal(value);};
  		if (fieldname.equals( "MEDLINE")) {this.getReference(this.getNumberOfReferences()-1).setMedline(value);};
  		if (fieldname.equals( "PUBMED")) {this.getReference(this.getNumberOfReferences()-1).setPubmed(value);};
  		if (fieldname.equals( "REMARK")) {this.getReference(this.getNumberOfReferences()-1).setRemark(value);};
	}
	
	public String getFieldFromString(String fieldname) {
		return "";
	}
	
	// Getters And Setters
	
	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getDblink() {
		return dblink;
	}

	public void setDblink(String dblink) {
		this.dblink = dblink;
	}

	public String getSegment() {
		return segment;
	}

	public void setSegment(String segment) {
		this.segment = segment;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getBasecount() {
		return basecount;
	}

	public void setBasecount(String basecount) {
		this.basecount = basecount;
	}

	public String getContig() {
		return contig;
	}

	public void setContig(String contig) {
		this.contig = contig;
	}
	
	public String getLocus() {
		return locus;
	}
	public void setLocus(String locus) {
		this.locus = locus;
	}
	public String getLength() {
		return length;
	}
	public void setLength(String length) {
		this.length = length;
	}
	public String getMolType() {
		return molType;
	}
	public String getDefinition() {
		return definition;
	}

	public void setDefinition(String definition) {
		this.definition = definition;
	}

	public String getAccession() {
		return accession;
	}

	public void setAccession(String accession) {
		this.accession = accession;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getOrganism() {
		return organism;
	}

	public void setOrganism(String organism) {
		this.organism = organism;
	}

	public void setMolType(String molType) {
		this.molType = molType;
	}
	public String getDivision() {
		return division;
	}
	public void setDivision(String division) {
		this.division = division;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
	public String getNid() {
		return nid;
	}

	public void setNid(String nid) {
		this.nid = nid;
	}

	
	

}
