package utils.mutualinformation.misticmod.onepixelmap;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.List;

import utils.mutualinformation.misticmod.datastructures.MI_PositionWithProtein;
import utils.mutualinformation.misticmod.onepixelmap.themes.MatrixColoringTheme;

/**
 * This class creates an image representing a matrix of MI data. Uses one pixel
 * per pair of residues.
 * 
 * @author javier
 *
 */
public class MIOnePixelMap {
	
	////////////////////////////////////////////////////////////////////////////
	// Public interface
	public BufferedImage getImage(
			List<Integer> lengths, 
			List<String> names, 
			List<MI_PositionWithProtein> positions, 
			int matrixWidth, 
			double maxMIValue, 
			double minMIValue, 
			MatrixColoringTheme colorer) {
		
		int widthOfSepataringColumns = lengths.size()- 1;
		
		BufferedImage bi = new BufferedImage(matrixWidth+widthOfSepataringColumns, matrixWidth + widthOfSepataringColumns, BufferedImage.TYPE_INT_RGB);
		
		bi.getGraphics().setColor(Color.black);
		
		bi.getGraphics().fillRect(0, 0, matrixWidth + widthOfSepataringColumns , matrixWidth + matrixWidth + widthOfSepataringColumns);
		
		this.drawMI_Points(positions, maxMIValue, minMIValue, bi, colorer);
		
		return drawProteinRegions(lengths, names, matrixWidth, bi, colorer);
	}
	////////////////////////////////////////////////////////////////////////////

	////////////////////////////////////////////////////////////////////////////
	// Private methods
	private BufferedImage drawProteinRegions(List<Integer> lengths, List<String> names, int max, BufferedImage bi, MatrixColoringTheme colorer) {

		BufferedImage bli = new BufferedImage(max + 50 + lengths.size()-1, max+50 + lengths.size()-1, BufferedImage.TYPE_INT_RGB);
		
		Graphics2D graphics = (Graphics2D) bli.getGraphics();
		
		graphics.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
		
		graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
		int offset = 50;
		
		Color[] colors = new Color[]{new Color(255, 0, 0, 150), new Color(0 , 255, 0, 150)}; 
		
		int i =0;
		
		int region_counter = 0;
		
		int markerRegionWidth = 50;
		
		for (int len : lengths) {
		
			graphics.setColor(Color.white);
			
			graphics.fillRect(offset + region_counter , 0 , len, markerRegionWidth);	
			
			graphics.fillRect(0 ,offset+region_counter , markerRegionWidth , len);
			
			graphics.setColor(colors[i]);
			
			graphics.fillRect(offset + region_counter , 0 , len, markerRegionWidth);	
			
			graphics.fillRect(0 ,offset+region_counter , markerRegionWidth , len);

			offset = offset + len;
			
			region_counter = region_counter+1;
			
			i=1-i;
			
		}
		
		if (names.size()==lengths.size()) {
		
			offset = 50;
			
			region_counter = 0;
			
			graphics.setFont(new Font("Arial", 1, 45));
			
			graphics.setColor(new Color(50,50,170));
			
			for (int len : lengths) {
				
				Rectangle2D textBounds = graphics.getFont().createGlyphVector(graphics.getFontRenderContext(), names.get(region_counter).trim()).getVisualBounds();
				
				int textAdv = (int)(len - textBounds.getWidth() ) / 2; 
				
				int textHeight = (int) (markerRegionWidth + textBounds.getHeight())/2;
				
				graphics.drawString( names.get(region_counter).trim(), (int) (offset + region_counter + textAdv - textBounds.getMinX()/2), textHeight );
				
				AffineTransform transformOrig = graphics.getTransform();
				
				graphics.setColor(new Color(50,50,170));
				
				graphics.rotate(Math.PI/2,markerRegionWidth/2,markerRegionWidth/2);
				
				graphics.drawString( names.get(region_counter).trim(), (int) (offset + region_counter + textAdv - textBounds.getMinX()/2), textHeight );
				
				graphics.setTransform(transformOrig);
				
				offset = offset + len;
				
				region_counter = region_counter + 1;
				
			}
		
		}
		graphics.drawImage(bi, null, 50, 50);
		
		return bli;
	}

	private void drawMI_Points(List<MI_PositionWithProtein> positions, double maxMIValue, double minMIValue, BufferedImage bi, MatrixColoringTheme colorer) {
		
		Graphics2D g = (Graphics2D) bi.getGraphics();
		
		g.setColor(colorer.getSeparatingLineColor());
		
		g.fillRect(0, 0, bi.getWidth(), bi.getHeight());
		
		for (MI_PositionWithProtein mi_PositionWithProtein : positions) {
			
			int rgb = colorer.getRGBColor(mi_PositionWithProtein.getMi());
			
			int x = mi_PositionWithProtein.getPos1()-1 + mi_PositionWithProtein.getProtein_1();
			
			int y = mi_PositionWithProtein.getPos2()-1  + mi_PositionWithProtein.getProtein_2();
			
			bi.setRGB(x, y, rgb);
			
			bi.setRGB(y, x, rgb);
			
		}
		
	}
	// End of private methods
	////////////////////////////////////////////////////////////////////////////

}
