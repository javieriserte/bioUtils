package utils.mutualinformation.mimatrixviewer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * Component that shows a zoom-in portion of a MI matrix.
 * 
 * @author Javier Iserte
 *
 */
public class ZoomPanel extends JScrollPane {

	////////////////////////////////
	// Class Constants
	private static final long serialVersionUID = -5691536429438522274L;
	private static final int CELL_SIZE = 5;
	private static final int CELL_SEP = 1;
	private static final int HEADER_SIZE = 15;
	private static final int FONT_SIZE = 10;
	private static final String FONT_NAME = "ARIAL";
	////////////////////////////////
	
	////////////////////////////////
	// Components
	private JPanel imagePanel;
	////////////////////////////////

	////////////////////////////////
	// Instance Variables
	private ZoomMatrixColoringStrategy coloringStrategy;
	private BufferedImage image;
	private double[][] subMatrix;
	private char[] aaSeqHor;
	private char[] aaSeqVer;
	////////////////////////////////
	
	////////////////////////////////
	// Constructor
	public ZoomPanel() {
		super();
	}
	////////////////////////////////
	
	////////////////////////////////
	// Public Interface
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (this.getImage()==null) {
			this.createImage();
		}
		g.drawImage(this.getImage(), 0, 0,null);
	}
	/**
	 * This methods erases the current image setting it to null.
	 * This should be used to indicate that a new image must be generated.
	 */
	public void resetImage() {
		this.setImage(null);
	}
	/**
	 * Precalculates the size of an image if it is draw used current settings
	 * and changes the size of the viewport to accomodate it. 
	 */
	public void acomodateSize() {
		int hSize = this.getSubMatrix()[0].length * (ZoomPanel.CELL_SEP+ZoomPanel.CELL_SIZE)+ZoomPanel.CELL_SEP + ZoomPanel.HEADER_SIZE;
		int vSize = this.getSubMatrix().length* (ZoomPanel.CELL_SEP+ZoomPanel.CELL_SIZE)+ZoomPanel.CELL_SEP + ZoomPanel.HEADER_SIZE;
		
		Dimension size = new Dimension(hSize,vSize);
		
		this.getImagePanel().setPreferredSize(size);
		this.getImagePanel().setSize(size);
	}
	/**
	 * Use this method to tell this component that draw a matrix with 
	 * given data.
	 * @param subMatrix
	 */
	public void renderImage(double[][] subMatrix, char[] aaSeqHor, char[] aaSeqVer) {
		this.setSubMatrix(subMatrix);
		this.resetImage();
		this.acomodateSize();
		this.getImagePanel().updateUI();
	}
	////////////////////////////////

	////////////////////////////////
	// Getters and setters
	public ZoomMatrixColoringStrategy getColoringStrategy() {
		return coloringStrategy;
	}

	public void setColoringStrategy(ZoomMatrixColoringStrategy coloringStrategy) {
		this.coloringStrategy = coloringStrategy;
	}

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}
	protected JPanel getImagePanel() {
		return imagePanel;
	}

	protected void setImagePanel(JPanel imagePanel) {
		this.imagePanel = imagePanel;
	}
	public double[][] getSubMatrix() {
		return subMatrix;
	}

	public void setSubMatrix(double[][] subMatrix) {
		this.subMatrix = subMatrix;
	}
	protected char[] getAaSeqHor() {
		return aaSeqHor;
	}

	protected void setAaSeqHor(char[] aaSeqHor) {
		this.aaSeqHor = aaSeqHor;
	}

	protected char[] getAaSeqVer() {
		return aaSeqVer;
	}

	protected void setAaSeqVer(char[] aaSeqVer) {
		this.aaSeqVer = aaSeqVer;
	}
	///////////////////////////////

	///////////////////////////////
	// Protected and private methods
	protected void createImage() {
		int hSize = this.getSubMatrix()[0].length * (ZoomPanel.CELL_SIZE + ZoomPanel.CELL_SEP) + ZoomPanel.CELL_SEP + ZoomPanel.HEADER_SIZE;
		int vSize = this.getSubMatrix().length * (ZoomPanel.CELL_SIZE + ZoomPanel.CELL_SEP) + ZoomPanel.CELL_SEP + ZoomPanel.HEADER_SIZE;
		this.setImage(new BufferedImage(hSize, vSize, BufferedImage.TYPE_INT_RGB));
		
		////////////////////////////
		// Draw cells
		BufferedImage cells = new BufferedImage(hSize - ZoomPanel.HEADER_SIZE, hSize - ZoomPanel.HEADER_SIZE, BufferedImage.TYPE_INT_RGB);
		Graphics2D cellsGraphics = (Graphics2D) cells.getGraphics();
		cellsGraphics.fillRect(0, 0, cells.getWidth(), cells.getHeight());
		cellsGraphics.setColor(Color.black);
		for (int i = 0; i< this.getSubMatrix()[0].length; i++ ) {
			for (int j = 0; j< this.getSubMatrix().length; j++ ) {
				Color color = this.getColoringStrategy().getColor(this.subMatrix[i+1][j+1]);
				cellsGraphics.setColor(color);
				cellsGraphics.fillRect(
						i*(ZoomPanel.CELL_SIZE+ZoomPanel.CELL_SEP) + ZoomPanel.CELL_SEP, 
						i*(ZoomPanel.CELL_SIZE+ZoomPanel.CELL_SEP) + ZoomPanel.CELL_SEP,
						ZoomPanel.CELL_SIZE, ZoomPanel.CELL_SIZE);
				
			}
		}
		this.getImage().getGraphics().drawImage(cells, ZoomPanel.HEADER_SIZE, ZoomPanel.HEADER_SIZE, null);
		////////////////////////////
		
		////////////////////////////
		// Draw Grid
		// No nothing really.
		// Grid is background not recolored
		////////////////////////////
		
		////////////////////////////
		// Draw Headers
		BufferedImage horHeader = new BufferedImage(cells.getWidth(), ZoomPanel.HEADER_SIZE, BufferedImage.TYPE_INT_RGB);
		BufferedImage verHeader = new BufferedImage(ZoomPanel.HEADER_SIZE, cells.getHeight(),BufferedImage.TYPE_INT_RGB);
		Graphics2D hhGraphics = (Graphics2D) horHeader.getGraphics();
		Graphics2D vhGraphics = (Graphics2D) verHeader.getGraphics();
		hhGraphics.setColor(Color.white);
		hhGraphics.fillRect(0, 0, horHeader.getWidth(), horHeader.getHeight());
		vhGraphics.setColor(Color.white);
		vhGraphics.fillRect(0, 0, horHeader.getWidth(), horHeader.getHeight());
		
		hhGraphics.setFont(new Font(ZoomPanel.FONT_NAME, 1, ZoomPanel.FONT_SIZE));
		hhGraphics.setColor(Color.black);
		
		for (int i = 0; i < this.getSubMatrix()[0].length; i++) {
			
			String currentChar = String.valueOf(this.getAaSeqHor()[i]);
			Rectangle2D textBounds = hhGraphics.getFont().createGlyphVector(hhGraphics.getFontRenderContext(),currentChar).getVisualBounds();
			int textAdv = (int)(ZoomPanel.CELL_SIZE - textBounds.getWidth() ) / 2;
			int textHeight = (int) (ZoomPanel.CELL_SIZE - textBounds.getHeight()) / 2;
			hhGraphics.drawString( currentChar, (int) (i * (ZoomPanel.CELL_SIZE+ZoomPanel.CELL_SEP) + textAdv - textBounds.getMinX()), (int) (ZoomPanel.CELL_SIZE - textHeight - textBounds.getMinY()));
			
		}
		
		for (int i = 0; i < this.getSubMatrix().length; i++) {
			
			String currentChar = String.valueOf(this.getAaSeqVer()[i]);
			Rectangle2D textBounds = vhGraphics.getFont().createGlyphVector(vhGraphics.getFontRenderContext(),currentChar).getVisualBounds();
			int textAdv = (int)(ZoomPanel.CELL_SIZE - textBounds.getWidth() ) / 2;
			int textHeight = (int) (ZoomPanel.CELL_SIZE - textBounds.getHeight()) / 2;
			vhGraphics.drawString( currentChar, (int) (textAdv - textBounds.getMinX()), (int) (ZoomPanel.CELL_SIZE - textHeight + textBounds.getMinY() + i * (ZoomPanel.CELL_SIZE+ZoomPanel.CELL_SEP)));
			
		}
		////////////////////////////
	}
	///////////////////////////////
}
