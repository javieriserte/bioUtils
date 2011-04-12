package distanceMatrix;

import java.text.DecimalFormat;

public class PhylipDistanceMatrixWriter extends DistanceMatrixWriter {
	private String[] truncatedNames;
	
	
	//CONSTRUCTORES
	public PhylipDistanceMatrixWriter() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PhylipDistanceMatrixWriter(int decimalPlaces) {
		super(decimalPlaces);
		// TODO Auto-generated constructor stub
	}
	
	
	//METODOS DE INSTANCIA
	/**
	 * Crea un String con el formato de archivo de la matriz de distancia de phylip.<br>
	 * 
	 * <b>Ejemplo:</b>
	 * <pre>
	 *     14
	 *  Mouse     
	 *  Bovine      1.7043
	 *  Lemur       2.0235  1.1901
	 *  Tarsier     2.1378  1.3287  1.2905
	 *  Squir Monk  1.5232  1.2423  1.3199  1.7878
	 *  Jpn Macaq   1.8261  1.2508  1.3887  1.3137  1.0642
	 *  Rhesus Mac  1.9182  1.2536  1.4658  1.3788  1.1124  0.1022
	 *  Crab-E.Mac  2.0039  1.3066  1.4826  1.3826  0.9832  0.2061  0.2681
	 *  BarbMacaq   1.9431  1.2827  1.4502  1.4543  1.0629  0.3895  0.3930  0.3665
	 *  Gibbon      1.9663  1.3296  1.8708  1.6683  0.9228  0.8035  0.7109  0.8132  0.7858
	 *  Orang       2.0593  1.2005  1.5356  1.6606  1.0681  0.7239  0.7290  0.7894  0.7140  0.7095
	 *  Gorilla     1.6664  1.3460  1.4577  1.5935  0.9127  0.7278  0.7412  0.8763  0.7966  0.5959  0.4604
	 *  Chimp       1.7320  1.3757  1.7803  1.7119  1.0635  0.7899  0.8742  0.8868  0.8288  0.6213  0.5065  0.3502
	 *  Human       1.7101  1.3956  1.6661  1.7599  1.0557  0.6933  0.7118  0.7589  0.8542  0.5612  0.4700  0.3097  0.2712
	 *  </pre>
	 *  <b>Descripción:</b>
	 *  Primera línea: Cantidad de OTUs. Cinco digitos ajustados a la derecha.
	 *  Segunda a n-esima línea: Contiene dos bloques. El primero tiene 10 caracteres para identificar una secuencia. 
	 *  El segundo es el conjunto de distancias. Cada distancia comienza dos dos caracteres de separación, 
	 *  luego son seis para el número.(aparentemente una cifra significativa y cuatro decimales).
	 */
	@Override
	protected String prepareOutputFile() {
		String output="";
		this.truncateNames();
		output = addFirstLine(output);
		for (int index=0;index<this.getDistances().getNumberOfOTU();index++) {
			output = output + this.addDistancesLine(index, 1, 4);
		}
		
		return output;
	}
	
	protected void truncateNames() {
		DecimalFormat df = new DecimalFormat("00");
		this.truncatedNames = new String[this.getDistances().getNumberOfOTU()]; 
		for (int pos=0;pos<this.getDistances().getNumberOfOTU();pos++) {
			String name = this.getDistances().getNames()[pos];
			if (name.length()>8) {
				name = name.substring(0, 8);
			} else {
				StringBuilder sb = new StringBuilder("00000000");
				name = sb.insert(0, name).toString();
			}
			this.truncatedNames[pos] = name + df.format(pos);
		}
	}
	
	protected String addName(String string,int pos) {
		return string + this.truncatedNames[pos];
	}
	
	protected String addDistancesLine(int index, int dig,int dec) {
		String line ="";
		line = this.addName(line, index);
	 	for (int pos=0;pos<index;pos++) {
	 		String num;
			try {
				num = "  " + PrintedNumber.formatedFloat(this.getDistances().getValue(index, pos), dig, dec);
			} catch (ImposibleNumberRepresentationException e) {
				StringBuilder sb =  new StringBuilder(dig+pos+1);
				for  (int x=0;x<sb.capacity();x++) sb.append("*");
				num = sb.toString();
			}
	 		line =  line + num;
	 	}
	 	return line + "\n";
	}
	
	protected String addFirstLine(String string){
		String n="";
		try {
			n = PrintedNumber.formatedFloat(this.getDistances().getNumberOfOTU(), 5, 0).toString();	
		} catch (Exception e) {
			n = "*****";
		}
		return string.concat(n).concat("\n");
		
	}
}
