package distanceMatrix;




public class DistanceMatrix {
	private double data[];
	private String names[];
	private int numberOfOTUS;
	private String title="Matriz de Distancias";
	private String description="";

	
	// CONSTRUCTOR
	/**
	 * Constructor a partir de un numero de 'operational taxonomic unit' (OTU) dado.
	 * @param numberOfSpecies cantidad de OTUs
	 */
	public DistanceMatrix(int numberOfOTU) {
		super();
		int positions = numberOfOTU*(numberOfOTU+1)/2;
		this.numberOfOTUS = numberOfOTU;
		this.setData(new double[positions]);
		this.names = new String[numberOfOTU];
	}

	// GETTERS & SETTERS
	
	public String getTitle() {
		return this.title;
	}
	
	public void setTitle(String text) {
		this.title = text;
	}
	
	public int getNumberOfOTU(){
		return this.numberOfOTUS;
	}

	public void setNumberOfOTU(int numberOfOTUS){
		this.numberOfOTUS = numberOfOTUS;
	}
	
	protected double[] getData() {
		return data;
	}

	protected void setData(double[] data) {
		this.data = data;
	}
 
	protected String[] getNames() {
		return names;
	}

	protected void setNames(String[] names) {
		this.names = names;
	}
	// METODOS DE INSTANCIA
	
	public String getDescription() {
		return this.description;
	}
	
	public void setName(String name, int atPos) {
		this.names[atPos] = name;
	}
		
	public void addDescriptionLine(String lineText) {
		this.description = this.description.concat(lineText+"\n");
	}

	public void clearDescription() {
		this.description="";
	}
	
	public void setValue(int posX, int posY, double value) {
		int pos =0;
		try {
			pos = calculateRealPos(posX,posY);
		} catch (MatrixOutOfBoundsException e) {
			System.out.println("posición X    :"+ posX);
			System.out.println("posición Y    :"+ posY);
			System.out.println("Numero de OTU :"+ this.getNumberOfOTU());
			e.printStackTrace();
		} catch (MatrixDIagonalAccessException e) {
			// Si caigo en la diagonal, el valor es siempre cero!
		}
		this.getData()[pos]=value;
	}

	public double getValue(int posX, int posY) {
		int pos =0;
		try {
			pos = calculateRealPos(posX,posY);
		} catch (MatrixOutOfBoundsException e) {
			System.out.println("posición X    :"+ posX);
			System.out.println("posición Y    :"+ posY);
			System.out.println("Numero de OTU :"+ this.getNumberOfOTU());
			e.printStackTrace();
		} catch (MatrixDIagonalAccessException e) {
			return 0;
		}
		return this.getData()[pos];
	}
	
	public void printToFile(DistanceMatrixWriter writer, String filepath){
		writer.printToFile(this, filepath);
	}
	
	/**
	 * Exporta los datos de dos matrices de distancia encolumnados para poder graficar la correlación entre ellos.
	 * Asume que las matrices contienen los mismo OTUs y en la misma posición.
	 * 
	 * @param otherMatrix La otra Matriz con la que se compara.
	 * @return String que es el texto encolumanado de los valores de la primer matriz y el correspondiente valor de la sengunda matriz.
	 */
	public String MatrixCorrelationWith(DistanceMatrix otherMatrix) {
		String result="";
		
		for (int i=0; i<this.getNumberOfOTU()-1;i++) {
			for (int j=i+1; j<this.getNumberOfOTU();j++) {
				result = result + this.getValue(i, j) + "\t" + otherMatrix.getValue(i, j)+ "\n";
			}
		}
		return result;
	}
	
	
	// MÉTODOS PROTEGIDOS Y PRIVADOS
	
	protected double searchMax(){
		// Se asume que no hay distancias negativas
		double maxValue=0;
		for (int x=0;x<this.getNumberOfOTU()*(this.getNumberOfOTU()+1)/2;x++) {if (this.data[x]>maxValue) maxValue = this.data[x] ;}
		return maxValue;
	}
	
	protected int calculateRealPos(int posX, int posY) throws MatrixOutOfBoundsException, MatrixDIagonalAccessException {
		int realPos=0;
		if (posX>=this.getNumberOfOTU()||posY>=this.getNumberOfOTU()){throw new MatrixOutOfBoundsException();}
		if (posX==posY){throw new MatrixDIagonalAccessException();}
		if (posX>posY) {int tmp = posY; posY=posX; posX=tmp;}
		realPos = (posY-1)*posY/2+posX;
		return realPos;
	}
}
