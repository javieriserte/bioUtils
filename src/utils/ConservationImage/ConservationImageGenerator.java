package utils.ConservationImage;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.TrayIcon.MessageType;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.sun.image.codec.jpeg.*;

import fastaIO.FastaMultipleReader;
import fastaIO.Pair;

public class ConservationImageGenerator {

	////////////////////
	// Instance Variable
	private double[] 			data;
	private String 				bases								= "ACTG";
	private String			 	amino 								= "ACDEFGHIKLMNPQRSTVWY";

	
	//////////////
	// Constructor 
	
	public 						ConservationImageGenerator			(double[] data) {
		this.data = data;
	}
	
	public 						ConservationImageGenerator			() {
		this.data = null;
	}

	
	///////////////////
	// Public Interface
	
	@SuppressWarnings("restriction")
	public void 				printImage							(File outfile, ColoringStrategy color, int windowSize ) throws ImageFormatException, IOException {
		
		// notice that createGraphics returns a g2d object directly, no cast!

		BufferedImage bi = this.render(color,this.data,windowSize);
		
//		 save the image

		this.exportJPG(outfile, bi);
	}

	@SuppressWarnings("restriction")
	private void 				exportJPG							(File outfile, BufferedImage bi) throws FileNotFoundException, IOException {
		FileOutputStream out = new FileOutputStream(outfile);
		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
		JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bi);
		param.setQuality(1.0f, false);
		encoder.setJPEGEncodeParam(param);
		encoder.encode(bi);
	}
	
	public BufferedImage 		render								(ColoringStrategy color, double[] data, int windowLen) {
		BufferedImage bi = new BufferedImage(data.length-windowLen+1, 100, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = bi.createGraphics();

		
		g.setColor(Color.white);
		g.fillRect(0, 0, bi.getWidth(), bi.getHeight());

		
		double windowValue =0;
		for (int i =0; i<windowLen;i++) {
			windowValue = windowValue + data[i];
		}
		
		// process first element
		int xPos=0;
		windowValue = windowValue/ windowLen; 
		setColorOnGraphic(g,xPos,color.getColor(windowValue));
		
		//
		for (int x=windowLen;x<data.length;x++,xPos++) {
			windowValue = windowValue + (data[x] - data[xPos]) / windowLen; 
			setColorOnGraphic(g,xPos,color.getColor(windowValue));
			
		}
		
		
		g.setColor(Color.BLACK);
		
		int minRuleDiv = 100;
		int maxRuleDiv = 1000;
		int midRuleDiv = 500;
		
		
		
		g.setStroke(new BasicStroke(4));
		
		g.drawLine(0, 80, bi.getWidth(), 80);
		for (int i=0;i<data.length-windowLen+1;i=i+minRuleDiv) {
			g.drawLine(i, 75, i, 80);
		}

		for (int i=0;i<data.length-windowLen+1;i=i+midRuleDiv) {
			g.drawLine(i, 70, i, 80);
		}

		g.setFont(new Font("Verdana", Font.BOLD, 18));
		for (int i=0;i<data.length-windowLen+1;i=i+maxRuleDiv) {
			g.drawLine(i, 60, i, 80);
			String numberString = String.valueOf(i); 
			int h= g.getFontMetrics().bytesWidth(numberString.getBytes(), 0, numberString.length());
			g.drawString( numberString, i - (h/2) , 98);
		}
		

		
		return bi;
	}
	
	
	//////////////////
	// Private Methods
	
	private void 				setColorOnGraphic					(Graphics2D g, int xPos, Color color) {
		g.setColor(color);
		g.drawLine(xPos, 0, xPos, 50);
	}

	private double[]			getDataFromClustal					(String line) {
		double[] data = new double[line.length()];
		
		for (int i = 0; i < data.length; i++) {
			switch (line.charAt(i)) {
			case '*': data[i] = 1; 	 	break;

			case ':': data[i] = 0.5; 	break;

			case '.': data[i] = 0.25; 	break;

			case ' ': data[i] = 0; 		break;
				
			default:   					break;
			}
		}
		
		return data;
	}
	
	private double[]			getDataFromInformationContent		(File inputFastaAlignment, boolean isDNA) {
		// From "Crooks, Gavin,E. et al, WebLogo: a Aequence Logo Generator. 2004"
		// R_seq = S_max - S_obs = Log_2(N) - ( - SUM(n=1,N) p_n * Log_2(p_n) )

		FastaMultipleReader fmr = new FastaMultipleReader();
		List<Pair<String, String>> alin = null;

		double S_max;
		double[][] p;
		double[] data;
		int N;
		int Len;
		int n_seqs=0;

		
		try {
			alin = fmr.readFile(inputFastaAlignment);
			n_seqs = alin.size();
		} catch (FileNotFoundException e) {
			System.out.println("hubo un error al leer el alineamiento");
			e.printStackTrace();
		}
		
		Len = alin.get(0).getSecond().length();
		if (isDNA) N=4; else N=20;
		p = new double[Len][N];
		S_max = Math.log(N)/Math.log(2);
		
		
		if (isDNA) p = getFreqForDNA(p,alin,n_seqs); else p = getFreqForAmino(p,alin,n_seqs);

		data = new double[Len];
		
		for (int i = 0; i < data.length; i++) {
			
			double s_obs=0;

			for (int j = 0; j < N; j++) {
				if (p[i][j]!= 0 ) { 
					s_obs = s_obs +  p[i][j] *  Math.log(p[i][j]) / Math.log(2);
				}
			}
			 
			data[i] = (S_max + s_obs) / S_max;
		}

		return data;
	}	
	
	private double[][] 			getFreqForAmino						(double[][] p ,List<Pair<String, String>> alin, int n_seqs) {

		return getFreq(p , alin, n_seqs, this.amino);
		
	}
	
	private double[][] 			getFreqForDNA						(double[][] p ,List<Pair<String, String>> alin, int n_seqs) {

		return getFreq(p , alin, n_seqs, this.bases);
		
	}
	
	private double[][] 			getFreq								(double[][] p ,List<Pair<String, String>> alin, int n_seqs, String chars) {

		// Initialize array
		
		for (int i=0;i<p.length;i++) {
			for (int j = 0; j < p[i].length; j++) {
				p[i][j]=0;
			} 
		}
		
		// count caracters in each postition
		
		for (int i = 0; i<p.length; i++) {
			//iterate over each position
			
			for (int j = 0; j < n_seqs; j++) {
				//iterate over each sequence

				char c = Character.toUpperCase(alin.get(j).getSecond().charAt(i));
				
				try {
					p[i][chars.lastIndexOf(c)]++;	
				} catch (Exception e) {
					System.out.println("el caracter es: " +  c + " y el �ndice es: " + chars.lastIndexOf(c));
				}
				
				
			} 
			
		}
		
		// calculate frequencies
		
		for (int i=0;i<p.length;i++) {
			for (int j = 0; j < p[i].length; j++) {
				p[i][j]=p[i][j]/n_seqs;
			} 
		}
				
		return p;
	}
	
	
	///////////////////
	// GETTERS & SETTER
	
	public double[] 			getData								() {
		return data;
	}

	public void 				setData								(double[] data) {
		this.data = data;
	}
	
	
	//////////////////
	// Executable Main
	
	@SuppressWarnings("restriction")
	public static void 			main								(String[] args) {
		ConservationImageGenerator cig = new ConservationImageGenerator();

		int responseInt=0;
		String response= null; 
		responseInt = JOptionPane.showOptionDialog(null, "Crear Imagen desde Clustal? Elegir 'No' implica crear la imagen a pritr del alineamiento usando el contenido informativo", "Elegir m�todo", JOptionPane.YES_NO_OPTION ,JOptionPane.QUESTION_MESSAGE, null, null, null);
		
		if (responseInt== JOptionPane.YES_NO_OPTION) {
			response = JOptionPane.showInputDialog(null, "Escribir la secuencia de simbolos de conservaci�n de clustal",  "Clustal", JOptionPane.QUESTION_MESSAGE);
			cig.setData( cig.getDataFromClustal(response) );	
			System.out.println( cig.getDataFromClustal(response) );
			
		} else {
			response = JOptionPane.showInputDialog(null, "Escribir la ruta del alineamiento", "Buscar Archivo",   JOptionPane.QUESTION_MESSAGE);
			
			responseInt = JOptionPane.showOptionDialog(null, "Es DNA ? Elegir No implica que son proteinas", "DNA O PROTEINA", JOptionPane.YES_NO_OPTION ,JOptionPane.QUESTION_MESSAGE, null, null, null);
			
			cig.setData( cig.getDataFromInformationContent(new File(response), responseInt == JOptionPane.YES_OPTION) );
		}
		
		response = JOptionPane.showInputDialog(null, "Escriba el tama�o de la ventana que quiere usar", "Elegir Ventana", JOptionPane.QUESTION_MESSAGE);
		
		int windowSize = Integer.parseInt(response);
		
		response = JOptionPane.showInputDialog(null, "Escribir la ruta del archivo de salida",  "Archivo de Salida", JOptionPane.QUESTION_MESSAGE);
		
		
		try {
			cig.printImage(new File(response), new RedBlueColorringStrategy(),windowSize );
		} catch (ImageFormatException e) {
			System.out.println("Hubo Un error con el formato de la imagen");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Hubo Un error con el archivo de salida");
			e.printStackTrace();
		}
	}

}
