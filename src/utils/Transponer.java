
package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.List;
import java.util.Vector;

public class Transponer {
	List<String[]> datos = new Vector<String[]>();
	
		
	public void read(InputStream in) throws IOException {

		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		
		String linea = null; 
		
		while ((linea = br.readLine())!=null){
			
			if (linea != null) this.datos.add(linea.split("\t"));
		
		};
		
		br.close();
	}
	
	public void writeFile(PrintStream out) throws IOException {
		

		String result ="";
		
		for (int x = 0;x < this.datos.get(0).length; x++) {
			
			for (int y = 0;y < this.datos.size(); y++) {
				
				result = result + this.datos.get(y)[x] + "\t"; 

			}
			result = result + "\r\n";
		}
		
		out.println(result);
		out.flush();
		out.close();
		
	}
	
	public static void main (String[] args) throws IOException {
		
		Transponer t = new Transponer();
		
		t.read(System.in);
		t.writeFile(System.out);
		
		
	}
	
	
}
