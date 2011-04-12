package utils.histo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

public class ReadFileWriteHisto {

	public static void main(String[] args) {
		ReadFileWriteHisto i = new ReadFileWriteHisto();
		
		//System.out.println(i.readfile("C:\\Javier\\Dropbox\\My Dropbox\\Investigacion\\Sandra\\Matriz_Todos_N.txt"));
		List<Float> r = i.readfile("C:\\Javier\\Dropbox\\My Dropbox\\Investigacion\\Sandra\\Matriz_Todos.txt");
		Float max = i.getMax(r);
		int pos = (int) (max*100)+1;
		
		int[] casillas = new int[pos+1];
		for (int j=0;j<=pos;j++) {
			
			casillas[j]=0;
			
		}
		
		for (float j : r) {
			int v = (int) (j*100)+1;
			casillas[v]++; 
		}
		
		for (int j=0;j<=pos;j++) {
			
			System.out.println("j :" +  casillas[j]);
			
		}

		
	}
	
	public float getMax(List<Float> r) {
		float m = r.get(0);
		
		for (Float f : r) {
			m = Math.max(m, f);
		}
		return m;
	}
	
	public List<Float> readfile(String filepath) {
		
		File f = new File(filepath);
		List<Float> result = new Vector<Float>();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(f));
			while (br.ready()) {
				result.add(Float.valueOf(br.readLine()));
			}
			
						
		} catch (FileNotFoundException e) { e.printStackTrace(); 
		} catch (NumberFormatException e) {	e.printStackTrace();
		} catch (IOException e) { e.printStackTrace();}
		
		return result;
		
	}
	
	
}



