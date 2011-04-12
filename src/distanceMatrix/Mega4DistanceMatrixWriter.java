package distanceMatrix;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class Mega4DistanceMatrixWriter extends DistanceMatrixWriter{
	
	
	// CONSTRUCTOR
	public Mega4DistanceMatrixWriter(int decimalplaces) {
		super(decimalplaces);
	}

	// MÉTODOS DE INSTANCIA PRIVADOS O PROTEGIDOS
	@Override
	protected String prepareOutputFile(){
		String output="";
		
		// ENCABEZADO
		output = this.addMegaToken(output);
		output = this.addTitleString(output);
		output = this.addFormatString(output);
		output = this.addDescriptionString(output);
		
		// LISTA DE OTUs
		int maxsize=((Integer)(this.getDistances().getNumberOfOTU()+1)).toString().trim().length(); 	// es el número mayor de OTUs
		double maxvalue=this.getDistances().searchMax();											 	// es el valor más grande de las distancias de la matriz
		int wideInt=(int) maxvalue/10 + 1;											 					// es la cantidad de dígitos que ocupa la parte entera de maxvalue
		
		StringBuilder format = new StringBuilder(wideInt +1+ this.getDecimalPlaces() ) ;     // 
		
		for(int x=0;x<wideInt;x++) format.append('0');
		format.append('.');
		for(int x=0;x<this.getDecimalPlaces();x++) format.append('0');
		
		DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
		simbolos.setDecimalSeparator('.');
		DecimalFormat df = new DecimalFormat(format.toString(),simbolos);

		StringBuilder sb = new StringBuilder(maxsize);for(int y=0;y<maxsize;y++)sb.append(' ');
		String trail = sb.toString();
		
		for (int x=0;x<this.getDistances().getNumberOfOTU();x++){
			String label = (String)(trail+(x+1));

			output = output.concat("["+label.substring((label.length()-maxsize))+ "] #"+this.getDistances().getNames()[x]+"\n");
		}
			output = output.concat("\n");
			
		wideInt = wideInt +1+ this.getDecimalPlaces();
		StringBuilder EmptyField = new StringBuilder(wideInt);for(int y=0;y<wideInt ;y++)EmptyField.append(' ');
		output = output.concat("["+trail+" ");			
		
		// TABLA DE DISTANCIAS
		for (int x=0;x<this.getDistances().getNumberOfOTU();x++){
			String field = (String)(EmptyField.toString() + (x+1));
			output = output.concat( " " + field.substring(field.length()-wideInt));
		}
		output = output.concat(" ]\n");
		
		for (int y=0;y<this.getDistances().getNumberOfOTU();y++){
			String label = (String)(trail+(y+1));
			output = output.concat("["+label.substring((label.length()-maxsize))+ "] ");
			for (int x=0;x<y;x++){
				output = output.concat(" "+df.format(this.getDistances().getValue(x, y)));
			}
			output = output.concat("\n");
		}
	    return output;
	}
	
	protected String addTitleString(String string) {
		return string.concat("!Title: "+this.getDistances().getTitle()+";\n");
	}

	protected String addMegaToken(String string) {
		return string.concat("#mega\n");
	}

	protected String addFormatString(String string) {
		return string.concat("!Format DataType=Distance DataFormat=LowerLeft NTaxa="+this.getDistances().getNumberOfOTU()+";\n");
	}
	
	protected String addDescriptionString(String string){
		// Reemplazo todos los caracteres prohibidos en la descripción por puntos.
		String description = this.getDistances().getDescription();
		description.replace(';', '.');
		description.replace('#', '.');
		description.replace('!', '.');
		return string.concat("!Description\n"+description+";\n\n");
	}
	
}
