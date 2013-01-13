package genBankIO.elements;

import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Stores the data of the header of a GenBank record.<br>
 * 
 * To the date of writing this code the following fields are part 
 * of the GenBank release notes:<br>
 * <pre>
 * LOCUS, LENGTH, MOLTYPE, DIVISION, DATE, DEFINITION, 
 * ACCESSION, VERSION, KEYWORDS, SOURCE, ORGANISM, 
 * PROJECT, DBLINK, SEGMENT, COMMENT, BASE COUNT, 
 * CONTIG, NID, REFERENCE.
 * </pre>
 * Is expected that a GenBank contains only one of each of this 
 * fields (except REFERENCE) and each one has an own Getter and Setter.
 * Because can be multiples REFERENCE fields, with many sub fields, 
 * the is special groups of the methods to work with them.<br>
 * If other fields appears in a GenBank record, they are stores, 
 * but there is not particular getter and setter for them, 
 * instead there are a few methods to store and retrieve the fields by name.
 * 
 * @author Javier Iserte (jiserte@unq.edu.ar)
 *
 */
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
	private List<String> otherFieldNames;
	private List<String> otherFieldValues;
	private List<Reference> references;	
	


	// Constructor
	/**
	 * Creates and empty GenBankHeader object.
	 */
	public GenBankHeader() {
		super();
		this.references = new ArrayList<Reference>();
	}

	// Public Interface 
	
	/**
	 * Adds a new Reference to the header.
	 * 
	 * @param reference is a <code>Reference</code> object to be stored.
	 *  
	 */
	public void addReference(Reference reference) {
		if (reference!= null) this.references.add(reference);
	}
	
	/**
	 * Retrieve the number of <code>Reference</code> objects stored in the header.
	 * 
	 * @return a <code>int</code> with the number of references.
	 */
	public int getNumberOfReferences() {
		return this.references.size();
	}

	/**
	 * Get a given <code>Reference</code> by its index number.
	 * 
	 * @return a <code>Reference</code> object.
	 */
	public Reference getReference(int index) {
		if (index >= 0 && index<this.references.size()) return this.references.get(index);
		return null;
	}
	
	/**
	 * Store a single field value given the field name.<br>
	 * For REFERENCE fields creates a new empty <code>Reference</code> object
	 * and only stores the value given in the same line of the GenBank record.
	 * For the reference subfields, the value are added to the last stored <code>Reference</code>. 
	 * 
	 * @param fieldname is a <code>String</code> with the field name.
	 * @param value is a <code>String</code> with the field value.
	 */
	public void setFieldFromString(String fieldname, String value) {
		Method m = getSetterOrGetterMethod(fieldname,false,false);
		
		if (m!=null) {
			try {
				m.invoke(this, value);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			return;
		}
		
  		if (fieldname.equals("REFERENCE")) {
  			this.addReference(new Reference());
  			this.getReference(this.getNumberOfReferences()-1).setValue(value);
  			return;
  			};
  			
  		m = getSetterOrGetterMethod(fieldname,false,true);

  		if (m!=null) {
  			if (this.references.size()>0) {
  				try {
					m.invoke(this.getReference(this.getNumberOfReferences()-1), value);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
				return;
  	  		}	
  		}
  		
		// If new Fields are added later in the GenBank specifications by NCBI, here will be stored.
		if (this.otherFieldNames == null) {
			this.otherFieldNames = new ArrayList<String>();
			this.otherFieldValues = new ArrayList<String>();
		}
		this.otherFieldNames.add(fieldname);
		this.otherFieldValues.add(value);

	}
	
	/**
	 * Creates the appropiate getter o setter method for the fields 
	 * of the <code>GenBankHeader</code> object.
	 * 
	 * 
	 * @param name is a <code>String</code> with the name of the field. 
	 * @param getter <b>True</b> if the method to be created is a getter. <b>False</b> otherwise.
	 * @param referencesubfields <b>True</b> if the method to be created is for the <code>Reference</code> 
	 * subfields. <b>False</b> if the method is for the <code>GenBankHeader</code> fields.
	 * @return a <code>Method</code> if all the parameters are OK, null otherwise.
	 */
	private Method getSetterOrGetterMethod(String name, boolean getter, boolean referencesubfields) {
		
		if ( !(getter&&referencesubfields)) {
		
			try {
				String methodName;
				if (referencesubfields) {
					methodName = this.getMethodNameForReferenceSubFields(name);
				} else {
					methodName = this.getMethodNameForFields(name);
				}
				if (!methodName.equals("")) {
					String prefix;
					Class<? extends Object> dest;
					if (getter) {prefix = "get";} else {prefix = "set";};
					if (referencesubfields) dest = Reference.class; else dest = GenBankHeader.class;
					return dest.getMethod(prefix+methodName, String.class);
				}
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * Gets the core name of the getter and setter methods for GenBankHeader object.
	 * 
	 * @param fieldname is a <code>String</code> with the name of the header field.
	 * @return <code>String</code> a String with the core name of the getter o setter.
	 */
	private String getMethodNameForFields(String fieldname) {
		if (fieldname.equals("DEFINITION")) {return "Definition";};
		if (fieldname.equals("ACCESSION")) {return "Accession";};
		if (fieldname.equals("VERSION")) {return "Accession";};
		if (fieldname.equals("NID")) {return "Nid";};
		if (fieldname.equals("PROJECT")) {return "Project";};
		if (fieldname.equals("DBLINK")) {return "Dblink";};
		if (fieldname.equals("KEYWORDS")) {return "Keywords";};
		if (fieldname.equals("SEGMENT")) {return "Comment";};
		if (fieldname.equals("SOURCE")) {return "Source";};
		if (fieldname.equals("ORGANISM")) {return "Organism";};
		if (fieldname.equals("COMMENT")) {return "Comment";};
		if (fieldname.equals("BASE COUNT")) {return "Basecount";};
		if (fieldname.equals("CONTIG")) {return "Contig";};
		return "";
	}
	
	/**
	 * Gets the core name of the getter and setter methods for Reference object.
	 * 
	 * @param fieldname is a <code>String</code> with the name of the Reference field.
	 * @return <code>String</code> a String with the core name of the getter o setter.
	 */
	private String getMethodNameForReferenceSubFields(String fieldname) {
	
		if (fieldname.equals("AUTHORS")) {return "Authors";};
		if (fieldname.equals("CONSRTM")) {return "Consortiums";};
		if (fieldname.equals("TITLE")) {return "Title";};
		if (fieldname.equals("JOURNAL")) {return "Journal";};
		if (fieldname.equals("MEDLINE")) {return "Medline";};
		if (fieldname.equals("PUBMED")) {return "Pubmed";};
		if (fieldname.equals("REMARK")) {return "Remark";};
		return "";
	}
	
	/**
	 * Retrieves a single field value given the field name.<br>
	 * For REFERENCE retrieves the number of References in the headers.
	 * For retrieve particular information of References, use:
	 * <code>getNumberOfReferences</code> and <code>getReference(index)
	 * </code> methods
	 * 
	 * @param fieldname is a <code>String</code> with the field name.
	 * @return a <code>String</code> with the value of the String.
	 */
	public String getFieldFromString(String fieldname) {

		Method m = getSetterOrGetterMethod(fieldname,true,false);
		if (m!=null) {
			try {
				return (String) m.invoke(this, fieldname);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		
  		if (fieldname.equals( "REFERENCE")) {return String.valueOf(this.getNumberOfReferences() + " References.");};
  		
  		// If new Fields are added later, here will be stored.
  			if (this.otherFieldNames != null) {
  				
  				int i = this.otherFieldNames.indexOf(fieldname);
  				if (i>=0) return this.otherFieldValues.get(i);
  			}
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
