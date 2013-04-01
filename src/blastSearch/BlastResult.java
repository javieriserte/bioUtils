package blastSearch;

/**
 * This class parses and represents a blast result made with local blast with the option "-outfmt 6"
 * 
 * @author Javier Iserte
 *
 */
public class BlastResult {

	private String query;
	private String subject;
	private double idPercent;
	private int length;
	private int mismatch;
	private int gapopen;
	private int queryStart;
	private int queryEnd;
	private int subjectStart;
	private int subjectEnd;
	private double evalue;
	private double bitscore;
	
	
	// public interface
	
	public void parse(String outputline) {
		
		String data[] = outputline.split("\t");
		
		if (data.length == 12) {
			this.setQuery(data[0]);
			this.setSubject(data[1]);
			this.setIdPercent(Double.valueOf(data[2]));
			this.setLength(Integer.valueOf(data[3]));
			this.setMismatch(Integer.valueOf(data[4]));
			this.setGapopen(Integer.valueOf(data[5]));
			this.setQueryStart(Integer.valueOf(data[6]));
			this.setQueryEnd(Integer.valueOf(data[7]));
			this.setSubjectStart(Integer.valueOf(data[8]));
			this.setSubjectEnd(Integer.valueOf(data[9]));
			this.setEvalue(Double.valueOf(data[10]));
			this.setBitscore(Double.valueOf(data[11]));
		}
		
	}
	
	// Getters and Setters
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public double getIdPercent() {
		return idPercent;
	}
	public void setIdPercent(double idPercent) {
		this.idPercent = idPercent;
	}
	public int getLength() {
		return length;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public int getMismatch() {
		return mismatch;
	}
	public void setMismatch(int mismatch) {
		this.mismatch = mismatch;
	}
	public int getGapopen() {
		return gapopen;
	}
	public void setGapopen(int gapopen) {
		this.gapopen = gapopen;
	}
	public int getQueryStart() {
		return queryStart;
	}
	public void setQueryStart(int queryStart) {
		this.queryStart = queryStart;
	}
	public int getQueryEnd() {
		return queryEnd;
	}
	public void setQueryEnd(int queryEnd) {
		this.queryEnd = queryEnd;
	}
	public int getSubjectStart() {
		return subjectStart;
	}
	public void setSubjectStart(int subjectStart) {
		this.subjectStart = subjectStart;
	}
	public int getSubjectEnd() {
		return subjectEnd;
	}
	public void setSubjectEnd(int subjectEnd) {
		this.subjectEnd = subjectEnd;
	}
	public double getEvalue() {
		return evalue;
	}
	public void setEvalue(double evalue) {
		this.evalue = evalue;
	}
	public double getBitscore() {
		return bitscore;
	}
	public void setBitscore(double bitscore) {
		this.bitscore = bitscore;
	}
	
	@Override
	public String toString() {
		return getQuery() + "\t" +
		getSubject() + "\t" +
		getIdPercent() + "\t" +
		getLength() + "\t" +
		getMismatch() + "\t" +
		getGapopen() + "\t" +
		getQueryStart() + "\t" +
		getQueryEnd() + "\t" +
		getSubjectStart()+ "\t" +
		getSubjectEnd() + "\t" +
		getEvalue() + "\t" +
		getBitscore();
	}
	
}
