package utils.mutualinformation.mimatrixviewer;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class MIMatrixPane extends JPanel {

	///////////////////////////////
	// Class Variables
	private static final long serialVersionUID = 1L;
	
	//////////////////////////////
	//  Instance Variables
	private MI_Matrix matrix = null;
	// Mi data values
	private MatrixColoringStrategy color = null;
	// The way that each pair of reisudes is colored on map.
	private int numberOfProteins = 1;
	// How many proteins correspond to the map
	private int[] proteinLengths = null;
	// The length of each protein

	////////////////////////////////
	// Public Interface
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if (this.matrix!=null) {
			BufferedImage miMap = this.createMapImage();
			g.drawImage(miMap, 0, 0, null);
		}
		
	}
	
	private BufferedImage createMapImage() {
		
		if (this.getMatrix() == null || this.getColor() == null) {
			return null;
		}
		
		BufferedImage image = new BufferedImage(matrix.getSize(),matrix.getSize(),BufferedImage.TYPE_INT_RGB);
		
		for (int i=0; i< this.getMatrix().getSize();i++) {
			for (int j=0; j< this.getMatrix().getSize();j++) {
				
				image.setRGB(i, j, this.getColor().getColor(matrix.getValue(i+1, j+1)).getRGB());
				
			}
		}
		
		return image;
	}

	/////////////////////////////////////
	// Getters and Setters
	public MI_Matrix getMatrix() {
		return matrix;
	}

	public void setMatrix(MI_Matrix matrix) {
		this.matrix = matrix;
	}

	public MatrixColoringStrategy getColor() {
		return color;
	}

	public void setColor(MatrixColoringStrategy color) {
		this.color = color;
	}

	public int getNumberOfProteins() {
		return numberOfProteins;
	}

	private void setNumberOfProteins(int numberOfProteins) {
		this.numberOfProteins = numberOfProteins;
	}

	public int[] getProteinLengths() {
		return proteinLengths;
	}

	public void setProteinLengths(int[] proteinLengths) {
		this.setNumberOfProteins(proteinLengths.length);
		this.proteinLengths = proteinLengths;
	}

}
