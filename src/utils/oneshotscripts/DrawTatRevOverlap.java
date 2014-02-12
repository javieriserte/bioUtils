package utils.oneshotscripts;

import graphics.profile.PngWriter;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import utils.mutualinformation.misticmod.MI_Position;
import utils.mutualinformation.misticmod.MI_PositionLineParser;

/**
 * This class draws a Mutual Information matrix to
 * specifically show the regions that proteins Tat and Rev from HIV overlap.
 * 
 * @author javier
 *
 */
public class DrawTatRevOverlap {
	
	private final static int LABELWIDTH = 40;
	private final static int SQUAREWIDTH = 15;
	// Width and height oh each position.

	public static void main(String[] args) {
	
		int rev_ini = 1230;
		int rev_end = 1296;
		int tat_ini = 1297;
		int tat_end = 1351;
//		double cutoff = 6.5;
		double cutoff = 10;
		
		String mimatrix_pathfile = "/home/javier/Dropbox/Posdoc/HIV.Segundo.Analisis/mi_mf_cl/mi_cl_lc_data";
		String ref_file = "/home/javier/Dropbox/Posdoc/HIV.Segundo.Analisis/mi_mf_cl/solap.tat.rev/ref_seq_EF158040";
		String outfileimage = "/home/javier/Dropbox/Posdoc/HIV.Segundo.Analisis/mi_mf_cl/solap.tat.rev/mi_map_10.png";
		
		char[] tat_ref = load_ref_file(ref_file, tat_ini,tat_end);
		char[] rev_ref = load_ref_file(ref_file, rev_ini,rev_end);
		
		boolean[][] highMIValuesMatrix = loadMIMatrix(mimatrix_pathfile,rev_ini,rev_end,tat_ini,tat_end,cutoff);
		
		BufferedImage miMatrix = createMIMatrixImage(highMIValuesMatrix);
		
		miMatrix = decorateMIMatrix(miMatrix, rev_ini,rev_end,tat_ini,tat_end, rev_ref, tat_ref);
		
		PngWriter png = new PngWriter();
		
		try {
			png.write(new File(outfileimage), miMatrix);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	
	/**
	 * Adds labels and other decorations to a MI image matrix.
	 * @param miMatrix
	 * @param rev_ini
	 * @param rev_end
	 * @param tat_ini
	 * @param tat_end
	 * @param rev_ref
	 * @param tat_ref
	 * @return
	 */
	private static BufferedImage decorateMIMatrix(BufferedImage miMatrix, int rev_ini, int rev_end, int tat_ini, int tat_end, char[] rev_ref, char[] tat_ref) {

		int width = LABELWIDTH + miMatrix.getWidth()+10;
		int height = LABELWIDTH + miMatrix.getHeight()+10;
		
		BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
		 
		Graphics2D graphics = (Graphics2D) newImage.getGraphics();
		
		graphics.setColor(Color.white);
		graphics.fillRect(0, 0, width, height);
		graphics.drawImage(miMatrix, LABELWIDTH, LABELWIDTH, null);
		graphics.setColor(Color.black);
		graphics.setFont(new Font("Verdana", 1, 10));
		
		graphics.drawRect(LABELWIDTH, LABELWIDTH, miMatrix.getWidth(), miMatrix.getHeight());
		
		
		for (int i = 0; i < rev_ref.length ; i++) {

			int charWidth = graphics.getFontMetrics().stringWidth(String.valueOf(rev_ref[i]));
			int charHeight = graphics.getFontMetrics().getHeight();
			graphics.drawString(String.valueOf(rev_ref[i]), LABELWIDTH + i * (SQUAREWIDTH+1) + (SQUAREWIDTH-charWidth)/2,  (LABELWIDTH+charHeight)/2);
			
		}
		
		for (int j = 0 ; j < tat_ref.length ; j++) {
			
			int charWidth = graphics.getFontMetrics().stringWidth(String.valueOf(tat_ref[j]));
			int charHeight = graphics.getFontMetrics().getHeight();
			graphics.drawString(String.valueOf(tat_ref[j]), (LABELWIDTH-charWidth)/2, LABELWIDTH + j * (SQUAREWIDTH+1) + (SQUAREWIDTH+charHeight)/2 );

		}
		
		return newImage;
		
		
	}

	/**
	 * Creates an image view of the MI array.
	 * Values greater then the cutoff are shown as black squares. 
	 * 
	 * @param highMIValuesMatrix
	 * @return
	 */
	private static BufferedImage createMIMatrixImage( boolean[][] highMIValuesMatrix) {
		
		int hPos = highMIValuesMatrix.length;
		// number of horizontal positions
		int vPos = highMIValuesMatrix[0].length;
		// number of vertical positions		
		
		BufferedImage image = new BufferedImage(hPos*SQUAREWIDTH + hPos-1, vPos*SQUAREWIDTH+ vPos-1, BufferedImage.TYPE_INT_RGB);
		
		Graphics2D graphics = (Graphics2D) image.getGraphics();
		
		graphics.setColor(Color.lightGray);
		graphics.fillRect(0, 0, image.getWidth(), image.getHeight());
		
		for (int i = 0; i < hPos; i++) {
			
			for (int j = 0; j < vPos; j++) {
				
				Color c = highMIValuesMatrix[i][j]?Color.black:Color.white;
				graphics.setColor(c);
				graphics.fillRect(i * (SQUAREWIDTH+1) , j * (SQUAREWIDTH+1), SQUAREWIDTH, SQUAREWIDTH);
				
			}
			
		}
		
		
		return image;
		
	}

	/**
	 * Generates a matrix from MI values for Rev and Tat.
	 * Matrix is boolean, true indicates that a given pair has a MI value 
	 * greater than the cutoff.
	 * Index are expected to start in One.
	 * Return matrix indexes starts in Zero.
	 * 
	 * @param mimatrix_pathfile
	 * @param rev_ini
	 * @param rev_end
	 * @param tat_ini
	 * @param tat_end
	 * @param cutoff
	 * @return
	 */
	private static boolean[][] loadMIMatrix(String mimatrix_pathfile, int rev_ini, int rev_end, int tat_ini, int tat_end, double cutoff) {
		
		boolean[][]  mi  = new boolean[rev_end-rev_ini+1][tat_end-tat_ini+1]; 
		
		for (int c=0; c<rev_end-rev_ini ; c++) {

			for (int cc=0; cc<tat_end-tat_ini ; cc++) {
				
				mi[c][cc] = false;
				
			}
			
		}
		
		MI_PositionLineParser parser = new MI_PositionLineParser();

		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(mimatrix_pathfile)));
			
			String currentline;
			
			while ((currentline = br.readLine())!= null) {
				
				MI_Position position = parser.parse(currentline);
				
				boolean posOneIsInTat = (position.getPos1()>=tat_ini && position.getPos1()<=tat_end);
				boolean posOneIsInRev = (position.getPos1()>=rev_ini && position.getPos1()<=rev_end);
				boolean posTwoIsInTat = (position.getPos2()>=tat_ini && position.getPos2()<=tat_end);
				boolean posTwoIsInRev = (position.getPos2()>=rev_ini && position.getPos2()<=rev_end);
				
				if (( posOneIsInTat && posTwoIsInRev ) || ( posOneIsInRev && posTwoIsInTat ) ) {
					
					if (position.getMi() >= cutoff) {
						// Pos1 is assumed to be Rev, because Rev is before in order in the alignment
						// Therefore, Pos2 is assumed to be Tat, because Tat is after Rev in the alignment.
						mi[position.getPos1() - rev_ini][position.getPos2() - tat_ini] = true;
						
					}
					
				}
				
			}
			
			br.close();
			
			return mi;
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		return null;
		
	}

	/** 
	 * Read the a ref_file that contains the chars of a reference sequence
	 * of the input alignment used in the MI calculation. I.E. a single row 
	 * of the gap strip alignment. A subset of this chars are extracted given 
	 * the first and the last positions. The subset is intended to correspond to 
	 * a single protein in a multiple protein alignment.  
	 * 
	 *   
	 * @param ref_file a file contains chars in a single line (no fasta).
	 * @param ini first position to extract reference chars. One is the first position.
	 * @param end last position to extract reference chars. One is the first position.
	 * @return
	 */
	private static char[] load_ref_file(String ref_file, int ini, int end) {
		
		try {
			
			BufferedReader br = new BufferedReader(new FileReader(new File(ref_file)));
			
			String line = br.readLine();
			
			br.close();
			
			line = line.substring(ini-1, end);
			
			return line.toCharArray(); 
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		return null;
		
	}
	
}
