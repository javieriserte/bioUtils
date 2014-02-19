package utils.mutualinformation.mimatrixviewer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class MIMatrixPane extends JScrollPane {
	
	///////////////////////////////
	// Class Variables
	private static final long serialVersionUID = 1L;
	
	//////////////////////////////
	//  Instance Variables
	private MI_Matrix matrix = null;
	// Mi data values
	private MatrixColoringStrategy color = null;
	// The way that each pair of residues is colored on map.
	private int numberOfProteins = 1;
	// How many proteins correspond to the map
	private int[] proteinLengths = null;
	// The length of each protein
	private String[] names = null;
	// The name of each protein
	private BufferedImage image = null;;
	// The generated map image
	private ImagePanel imagePane = null;
	// Component to store the image
	
	///////////////////////////////
	// Constructor
	public MIMatrixPane() {
		super();
		this.setImagePane(new ImagePanel());
		this.setViewportView(this.getImagePane());
		this.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		this.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		this.getImagePane().setOpaque(true);
		this.getImagePane().setDoubleBuffered(true);
		this.setOpaque(true);
		
	}

	////////////////////////////////
	// Public Interface
	public void resetImage() {
		this.setImage(null);
	}
	public void accomodateSize() {
		boolean showBands = this.getProteinLengths().length >0;
		int nprot = this.getProteinLengths().length;
		int size = this.getMatrix().getSize() + nprot + (showBands?30:0);
		Dimension matrixSize = new Dimension(size, size);
		this.getImagePane().setSize(matrixSize);
		this.getImagePane().setPreferredSize(matrixSize);
	}
	
	////////////////////////////////
	// Private and protected 
	// methods
	/**
	 * Creates the MI image map.
	 * This could be implemented as an strategy in order to draw the map in different ways.
	 * @return
	 */
	private BufferedImage createMapImage() {
		
		if (this.getMatrix() == null || this.getColor() == null) {
			return null;
		}
		
		boolean showNames = (this.getNames()!=null && this.getNames().length ==this.numberOfProteins);
		boolean showBands = this.getProteinLengths().length >0;
		int namesLabelWidth = showBands?30:0;
		
		BufferedImage image = new BufferedImage(namesLabelWidth+matrix.getSize()+this.numberOfProteins,namesLabelWidth+matrix.getSize()+this.numberOfProteins,BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = (Graphics2D) image.getGraphics();
		graphics.setColor(Color.white);
		graphics.fillRect(0, 0, image.getWidth(),image.getHeight());
		
		///////////////////////////
		// Draw Mi Points
		for (int i=0; i< this.getMatrix().getSize();i++) {
			for (int j=0; j< this.getMatrix().getSize();j++) {
				
				int px = this.translateCoordinate(i+1);
				
				int py = this.translateCoordinate(j+1);
				
				image.setRGB(namesLabelWidth+px, namesLabelWidth+py, this.getColor().getColor(matrix.getValue(i+1, j+1)).getRGB());
				
			}
		}
		
		/////////////////////////
		
		/////////////////////////
		// Draw Grid for proteins
		int protAcum = 0;
		for (int i = 0; i<this.getNumberOfProteins();i++) {
			protAcum += this.getProteinLengths()[i];
			image.getGraphics().setColor(Color.white);
			image.getGraphics().drawLine( namesLabelWidth +protAcum+i, namesLabelWidth + 0, namesLabelWidth+ protAcum+i, namesLabelWidth+ matrix.getSize()+this.getNumberOfProteins()-2);
			image.getGraphics().drawLine( namesLabelWidth+ 0, namesLabelWidth+ protAcum+i, namesLabelWidth+ matrix.getSize()+this.getNumberOfProteins()-2,namesLabelWidth+protAcum+i );
		}
		//////////////////////////
		
		//////////////////////////
		// Draw Text
		graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		
		graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
		Color[] colors = new Color[]{new Color(255, 0, 0, 150), new Color(0 , 255, 0, 150)}; 
		
		int i =0;
		
		int region_counter = 0;
		
		int markerRegionWidth = namesLabelWidth;
		
		int offset = namesLabelWidth;
		
		for (int len : this.getProteinLengths()) {
		
//			image.getGraphics().setColor(Color.white);
//			
//			image.getGraphics().fillRect(offset + region_counter , 0 , len, markerRegionWidth);	
//			
//			image.getGraphics().fillRect(0 ,offset+region_counter , markerRegionWidth , len);
			
			graphics.setColor(colors[i]);
			
			graphics.fillRect(offset + region_counter , 0 , len, markerRegionWidth);	
			
			graphics.fillRect(0 ,offset+region_counter , markerRegionWidth , len);

			offset = offset + len;
			
			region_counter = region_counter+1;
			
			i=1-i;
			
		}
//		
		if (showNames) {
		
			offset = namesLabelWidth;
			
			region_counter = 0;
			
			graphics.setFont(new Font("Arial", 1, 25));
			
			graphics.setColor(new Color(50,50,170));
			
			for (int len : this.getProteinLengths()) {
				
				Rectangle2D textBounds = graphics.getFont().createGlyphVector(((Graphics2D) image.getGraphics()).getFontRenderContext(), this.getNames()[region_counter].trim()).getVisualBounds();
				
				int textAdv = (int)(len - textBounds.getWidth() ) / 2; 
				
				int textHeight = (int) (markerRegionWidth + textBounds.getHeight())/2;
				
				graphics.drawString( this.getNames()[region_counter].trim(), (int) (offset + region_counter + textAdv - textBounds.getMinX()/2), textHeight );
				
				AffineTransform transformOrig = ((Graphics2D) image.getGraphics()).getTransform();
				
				graphics.setColor(new Color(50,50,170));
				
				graphics.rotate(Math.PI/2,markerRegionWidth/2,markerRegionWidth/2);
				
				graphics.drawString( this.getNames()[region_counter].trim(), (int) (offset + region_counter + textAdv - textBounds.getMinX()/2), textHeight );
				
				graphics.setTransform(transformOrig);
				
				offset = offset + len;
				
				region_counter = region_counter + 1;
				
			}
		
		}
		//////////////////////////
		this.getImagePane().setSize(image.getWidth(), image.getHeight());
		System.err.println(image.getWidth() + " x " +  image.getHeight());
		this.getImagePane().updateUI();
		return image;
	}

	/**
	 * Re-map position to draw, according to the protein the position belongs.
	 * 
	 * @param pos from 1 to size of matrix
	 * @return
	 */
	private int translateCoordinate(int pos) {
		
		int prot = 0;
		int acumSize = 0;
		do {
			acumSize += this.proteinLengths[prot];
			prot++;
		} while (pos > acumSize);
		
		return pos+prot-2;
	}

	/////////////////////////////////////
	// Getters and Setters
	public MI_Matrix getMatrix() {
		return matrix;
	}

	public void setMatrix(MI_Matrix matrix) {
		this.matrix = matrix;
		if (this.proteinLengths == null) {
			this.proteinLengths =  new int[]{this.matrix.getSize()};
		}
		this.accomodateSize();
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

	public void setNumberOfProteins(int numberOfProteins) {
		this.numberOfProteins = numberOfProteins;
	}

	public int[] getProteinLengths() {
		return proteinLengths;
	}

	public void setProteinLengths(int[] proteinLengths) {
		this.setNumberOfProteins(proteinLengths.length);
		this.proteinLengths = proteinLengths;
	}

	protected BufferedImage getImage() {
		return image;
	}

	protected void setImage(BufferedImage image) {
		this.image = image;
	}

	protected ImagePanel getImagePane() {
		return imagePane;
	}

	protected void setImagePane(ImagePanel imagePane) {
		this.imagePane = imagePane;
	}
	public String[] getNames() {
		return names;
	}

	public void setNames(String[] names) {
		this.names = names;
	}
	
	///////////////////////////////////////
	// Auxiliary Classes
	private class ImagePanel extends JPanel {

		private static final long serialVersionUID = -4046360075583779295L;

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			if (MIMatrixPane.this.matrix!=null) {
				if (MIMatrixPane.this.getImage()==null) {
					MIMatrixPane.this.setImage(MIMatrixPane.this.createMapImage());
				}
				g.drawImage(MIMatrixPane.this.getImage(), 0, 0, null);
			}
			
		}
	}


}
