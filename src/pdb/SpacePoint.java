package pdb;

public abstract class SpacePoint {

	///////////////////////////
	// Public Constants
	public static final String ATOM = "ATOM";
	public static final String HETATOM = "HETATOM";
	
	///////////////////////////
	// Enumerations
	public static enum RecordName {
		Atom,
		HetAtom
	}
	
	///////////////////////////
	// Instance Variables
	private int              atomSerialNumber;
	private char[]           atomName;
	private char             alternateLocationIndicator;
	private char[]           residueName;
	private char             chainIdentifier;
	int                       residueSequenceNumber;
	char                      residueInsertionCode;
    double                    x;
    double                    y;
    double                    z;
    double                    occupancy;
    double                    temperatureFactor;
    char[]                    segmentIdentifier;   
    char[]                    elementSymbol;
    char[]                    charge;
    
    ///////////////////////////////////
    // Public Interface
    public double distanceTo(SpacePoint otherPoint) {
    	
    	return Math.pow(
    			(Math.pow((this.getX()-otherPoint.getX()),2) + 
    			 Math.pow((this.getY()-otherPoint.getY()),2) +
    			 Math.pow((this.getZ()-otherPoint.getZ()),2)   )
    			 , (1d/2) ) ;
    	
    }
    
    ///////////////////////////////////
    // Getters and Setters
	public abstract RecordName getType();
	
	public int getAtomSerialNumber() {
		return atomSerialNumber;
	}
	public void setAtomSerialNumber(int atomSerialNumber) {
		this.atomSerialNumber = atomSerialNumber;
	}
	public char[] getAtomName() {
		return atomName;
	}
	public void setAtomName(char[] atomName) {
		this.atomName = atomName;
	}
	public char getAlternateLocationIndicator() {
		return alternateLocationIndicator;
	}
	public void setAlternateLocationIndicator(char alternateLocationIndicator) {
		this.alternateLocationIndicator = alternateLocationIndicator;
	}
	public char[] getResidueName() {
		return residueName;
	}
	public void setResidueName(char[] residueName) {
		this.residueName = residueName;
	}
	public char getChainIdentifier() {
		return chainIdentifier;
	}
	public void setChainIdentifier(char chainIdentifier) {
		this.chainIdentifier = chainIdentifier;
	}
	public int getResidueSequenceNumber() {
		return residueSequenceNumber;
	}
	public void setResidueSequenceNumber(int residueSequenceNumber) {
		this.residueSequenceNumber = residueSequenceNumber;
	}
	public char getResidueInsertionCode() {
		return residueInsertionCode;
	}
	public void setResidueInsertionCode(char residueInsertionCode) {
		this.residueInsertionCode = residueInsertionCode;
	}
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public double getZ() {
		return z;
	}
	public void setZ(double z) {
		this.z = z;	
	    
	    
	    

	}
	public double getOccupancy() {
		return occupancy;
	}
	public void setOccupancy(double occupancy) {
		this.occupancy = occupancy;
	}
	public double getTemperatureFactor() {
		return temperatureFactor;
	}
	public void setTemperatureFactor(double temperatureFactor) {
		this.temperatureFactor = temperatureFactor;
	}
	public char[] getSegmentIdentifier() {
		return segmentIdentifier;
	}
	public void setSegmentIdentifier(char[] segmentIdentifier) {
		this.segmentIdentifier = segmentIdentifier;
	}
	public char[] getElementSymbol() {
		return elementSymbol;
	}
	public void setElementSymbol(char[] elementSymbol) {
		this.elementSymbol = elementSymbol;
	}
	public char[] getCharge() {
		return charge;
	}
	public void setCharge(char[] charge) {
		this.charge = charge;
	}
    
}


