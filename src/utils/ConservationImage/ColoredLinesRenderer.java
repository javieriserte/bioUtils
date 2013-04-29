package utils.ConservationImage;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class ColoredLinesRenderer  implements Renderer {

	
	public BufferedImage 		render								(ColoringStrategy color, double[] data, int windowLen) {
	
		
		
//	    PaddingH                                    PaddingH
//      /--/				                            /--/
//		---------------------------------------------------- /
//		|                   (   BODY   )                   | | PaddingV
//		|  ||||||||||||||||||||||||||||||||||||||||||||||  | /            /
//		|                                                  |              |  Space_1
//		|  |----|----|----|----|----|----|----|----|----|  |  Ruler       /            /
//		|                                                  |                           |  Space_2
//		|  ||||||||||||||||||||||||||||||||||||||||||||||  |                           /
//		|                                                  |
//		|  |----|----|----|----|----|----|----|----|----|  |
//		|                                                  |
//		|  ||||||||||||||||||||||||||||||||||||||||||||||  |
//		|                                                  |
//		|  |----|----|----|----|----|----|----|----|----|  |
//		|                                                  |
//		|  ||||||||||||||||||||||||||||||||||||||||||||||  |
//		|                                                  |
//		|  |----|----|----|----|----|----|----|----|----|  |
//		|                                                  |
//		|  ||||||||||||||||||||||||||||||||||||||||||||||  |
//		|                                                  |
//		|  |----|----|----|----|----|----|----|----|----|  |		
//		|                                                  |
//		|  ||||||||||||||||||||||||||||||||||||||||||||||  |
//		|                                                  |
//		|  |----|----|----|----|----|----|----|----|----|  | /
//		|                                                  | | PaddingV
//		---------------------------------------------------- / 
		

		int paddingV = 30;
		int paddingW = 50;
		int barsToPrint = data.length-windowLen+1;
		int lineHeight = 50;
		int space_1 = 10;
		int space_2 = 30;
		int rulerHeight = 35;
		int rulerLinesVspace = 20;
		int rulerNumbersVspace = 15;
		int barsPerLine = 3500;
		// TODO permit that this values can be set by command line.
		//       maybe customizing a config file.
		
		int numberOfLines = ((barsToPrint-1) /  barsPerLine)+1;
		
		int imageW = 2 * paddingW + barsPerLine;
		int imageH = 2 * paddingV + numberOfLines * (lineHeight + space_1 +space_2 + rulerHeight) - space_2;
		
		
		BufferedImage bi = new BufferedImage(imageW, imageH, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = bi.createGraphics();

		
		g.setColor(Color.white);
		g.fillRect(0, 0, bi.getWidth(), bi.getHeight());


		// process first element
		double windowValue =0;
		for (int i =0; i<windowLen;i++) {
			windowValue = windowValue + data[i];
		}
		
		int xPos=0;
		windowValue = windowValue/ windowLen; 
		setColorOnGraphic(g,xPos ,color.getColor(windowValue),0,lineHeight, paddingV, paddingW);
		
		// Process the rest of the elements
		for (int x=windowLen;x<data.length;x++,xPos++) {
			windowValue = windowValue + (data[x] - data[xPos]) / windowLen;
			int yPos = ((xPos) / barsPerLine) * ( lineHeight + space_1 + space_2 + rulerHeight ); 
			setColorOnGraphic(g,xPos % barsPerLine,color.getColor(windowValue),yPos,lineHeight, paddingV, paddingW);
			
		}
		
		
		g.setColor(Color.BLACK);
		
		int minRuleDiv = 10;
		int maxRuleDiv = 100;
		int midRuleDiv = 50;
		
		
		g.setStroke(new BasicStroke(2));
		
		// Draw Ruler
		
		// Draw Ruler Base Lines
		
		for (int l = 0; l<numberOfLines;l++) {

			int bFrom = l * barsPerLine;
			int bTo = Math.min((l+1) * barsPerLine, barsToPrint);
			
			int xto = bTo - bFrom;
			
			g.drawLine(  paddingW , 
					     paddingV + (lineHeight + space_1 + rulerHeight+ space_2) * (l) + space_1 + lineHeight + rulerLinesVspace, 
					     paddingW + xto, 
					     paddingV + (lineHeight + space_1 + rulerHeight+ space_2) * (l) + space_1 + lineHeight + rulerLinesVspace);

		}
		
		
		g.setStroke(new BasicStroke(1));
		for (int i=0;i<barsToPrint;i=i+minRuleDiv) {
			int xPosR = (i % barsPerLine);
			int l = ( i / barsPerLine);
			int yPosR = (lineHeight + space_1 + rulerHeight+ space_2) * l + space_1 + lineHeight + rulerLinesVspace; 
			
			g.drawLine(paddingW + xPosR, paddingV + yPosR - 5, paddingW + xPosR, paddingV + yPosR);
		}

		for (int i=0;i<barsToPrint;i=i+midRuleDiv) {
			int xPosR = (i % barsPerLine);
			int l = ( i / barsPerLine);
			int yPosR = (lineHeight + space_1 + rulerHeight+ space_2) * l + space_1 + lineHeight + rulerLinesVspace; 
			
			g.drawLine(paddingW + xPosR, paddingV + yPosR - 10, paddingW + xPosR, paddingV + yPosR);
		}

		

		
		
		g.setFont(new Font("Verdana", Font.BOLD, 18));
		g.setStroke(new BasicStroke(2));
		

		for (int l = 0; l<numberOfLines;l++) {

			int bFrom = l * barsPerLine;
			int bTo = Math.min((l+1) * barsPerLine, barsToPrint);
			
			int xto = bTo - bFrom;
			
			g.drawLine(  paddingW + xto, 
					     paddingV + (lineHeight + space_1 + rulerHeight+ space_2) * l + space_1 + lineHeight + rulerLinesVspace - 20, 
					     paddingW + xto, 
					     paddingV + (lineHeight + space_1 + rulerHeight+ space_2) * l + space_1 + lineHeight + rulerLinesVspace);

		}
		
		
		for (int i=0;i<barsToPrint;i=i+maxRuleDiv) {
			int xPosR = (i % barsPerLine);
			int l = ( i / barsPerLine);
			int yPosR = (lineHeight + space_1 + rulerHeight+ space_2) * l + space_1 + lineHeight + rulerLinesVspace; 
			
			g.drawLine(paddingW + xPosR, paddingV + yPosR - 20, paddingW + xPosR, paddingV + yPosR);
			
			String numberString = String.valueOf(i); 
			int h= g.getFontMetrics().bytesWidth(numberString.getBytes(), 0, numberString.length());
			g.drawString( numberString, paddingW + xPosR - (h/2) , paddingV + yPosR + rulerNumbersVspace);
			
		}
		
		return bi;
		
	}
	
	// Private Methods
	
	private void 				setColorOnGraphic					(Graphics2D g, int xPos, Color color, int yPos, int lineHeight, int paddingV, int paddingW) {
		g.setColor(color);
//		GradientPaint paint = new GradientPaint(paddingW+xPos,paddingV+ yPos, color, paddingW+xPos, paddingV+ yPos + lineHeight, Color.BLACK);
//		g.setPaint(paint);
		g.drawLine(paddingW+xPos,paddingV+ yPos, paddingW+xPos, paddingV+ yPos + lineHeight);
		
	}
	
	
}
