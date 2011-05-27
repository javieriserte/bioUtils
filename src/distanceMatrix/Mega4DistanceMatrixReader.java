package distanceMatrix;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

/**
 * Clase que permite leer archivos de matrices de distancia en formato Mega.
 * 
 * @author Javi
 *
 */
public class Mega4DistanceMatrixReader {
	protected String title="";
	protected String description="";
	protected DistanceMatrix myMatrix=null;
	
	public static void main(String[] arg) {
		// Solo para testear
		
		
		Mega4DistanceMatrixReader mr = new Mega4DistanceMatrixReader();

		DistanceMatrix m = mr.read("C:\\javier\\Investigacion\\KstringJava\\02 Filogenias 16S\\04 Distancias\\Streptococcus.16s.Select.dist.meg");
		DistanceMatrix m2 = mr.read("C:\\javier\\Investigacion\\InterNucleotideDistance\\genomes\\Streptococcus\\Streptococcus.out.meg");
		System.out.println(m.MatrixCorrelationWith(m2));
	}
	/**
	 * Método que permite leer un archivo en formato mega.
	 * 
	 * @param filepath es el path del archivo que se quiere leer
	 * @return una matriz de distancias si la lectura fue exitosa.
	 */
	
	public DistanceMatrix read(String filepath) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(filepath));

			br = processMegaToken(br);
			br = processTitle(br);
			br = processFormat(br);
			br = processDescription(br);
			br = processTaxaName(br);
			br = processMatrixData(br);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this.myMatrix;
		
	}
	
	// MÉTODOS PRIVADOS O PROTEGIDOS
	protected BufferedReader processMegaToken(BufferedReader content) throws IOException, IncorrectMegaFormatException {
		// ver si empieza con "#mega"
		
		boolean exit = true;
		while (exit) {
			if (!content.ready()) {throw new IncorrectMegaFormatException(); }
			String line = content.readLine();
			exit = !line.toUpperCase().startsWith("#MEGA");
			
		}
		return content;
	}

	protected BufferedReader processTitle(BufferedReader content) throws IOException, IncorrectMegaFormatException {
		// ver si empieza con "title:" y guardar el título
		
		String line = content.readLine();
		if (line.toUpperCase().startsWith("!TITLE:")) {
			this.title = line.substring("!TITLE:".length());
			return content;
		} else {
			throw new IncorrectMegaFormatException("!Title tag not present in current Mega File");
		}
		

	}

	protected BufferedReader processFormat(BufferedReader content) throws IOException, IncorrectMegaFormatException {
		// ver si empieza con "!format:" y guardar el título
		
		String line = content.readLine();
		if (line.toUpperCase().startsWith("!FORMAT")) {
			return content;
		} else {
			throw new IncorrectMegaFormatException("!Format tag not present in current Mega File");
		}
		

	}
	
	protected BufferedReader processDescription(BufferedReader content) throws IncorrectMegaFormatException, IOException {

		// ver si empieza con "description"
		// guardar todas las lineas no vacias como descripcion

		String line = content.readLine();
		if (line.toUpperCase().startsWith("!DESCRIPTION")) {
			line = content.readLine();
			while(!line.trim().isEmpty()) {
				this.description = this.description.concat(line+"\n"); 
				line = content.readLine();
			}
			return content;
		} else {
			throw new IncorrectMegaFormatException();
		}
		

	}

	protected BufferedReader processTaxaName(BufferedReader content) throws IOException, IncorrectMegaFormatException {

		// ver cada linea si es de la siguiente forma "[numero] #nombre"
		// hacer esto con cada línea hasta llegar a una línea vacia
		List<String> names = new Vector<String>();
		String line = content.readLine();
		while(!line.trim().isEmpty()) {
			 String n = line.substring(line.indexOf(']')+1).trim();
			 names.add(n.substring(1));
			 line = content.readLine();
		}
		this.myMatrix = new DistanceMatrix(names.size());
		this.myMatrix.setTitle(this.title)                ; 
		this.myMatrix.addDescriptionLine(this.description); 
			
		for (int i=0;i<names.size();i++) {
			this.myMatrix.setName(names.get(i),i );
		}
		return content;
		
	}
	
	protected BufferedReader processMatrixData(BufferedReader content) throws IOException {
		// separar todos los números de la matriz, tener en cuenta las posiciones de la diagonal están vacias
		// cuando un número está encerrado entre corchetes deben descartarse....
		// ubicar los datos de en la matriz
		
		String line = content.readLine();
		line = content.readLine();
		int row =0;
		while(line!=null && !line.trim().isEmpty()) {
			 String n = line.substring(line.indexOf(']')+1).trim();

			 n = n.replace('[', ' ');
			 n = n.replace(']', ' ');
			 n = n.replaceAll("\\s+", " "); 
			 
			 String[] values = n.split(" ");
			 for (int i = 0;i<row;i++) {
				 if (i!=row) {
					 this.myMatrix.setValue(i, row, Double.parseDouble(values[i])); 
				 }
			 }
			 row++;
			 line = content.readLine();
		}
		return content;
	}
	
}
