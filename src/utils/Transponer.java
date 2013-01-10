
package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

public class Transponer {
	List<String[]> datos = new Vector<String[]>();
	
		
	public void readFile(String filePath) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filePath));

		String linea; 
		do {
			linea = br.readLine();
			if (linea != null) this.datos.add(linea.split("\t"));
		} while (linea!="" && (linea != null));
	}
	
	public void writeFile(String filePath) throws IOException {
		

		String result ="";
		
		for (int x = 0;x < this.datos.get(0).length; x++) {
			
			for (int y = 0;y < this.datos.size(); y++) {
				
				result = result + this.datos.get(y)[x] + "\t"; 

			}
			result = result + "\r\n";
		}
		
		BufferedWriter out = new BufferedWriter(new FileWriter(filePath));
		
		out.write(result);
		out.flush();
		out.close();
		
	}
	
	public static void main (String[] args) throws IOException {
		
		Transponer t = new Transponer();
		
		t.readFile("C:\\paupWin32\\solmati\\data.txt");
		t.writeFile("C:\\paupWin32\\solmati\\data.out.txt");
		
		
	}
	
	
}
