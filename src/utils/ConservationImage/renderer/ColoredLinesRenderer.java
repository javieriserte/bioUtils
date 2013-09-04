package utils.ConservationImage.renderer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import utils.ConservationImage.color.ColoringStrategy;

public class ColoredLinesRenderer implements Renderer {
	
	//////////////////////
	// Instance variables
	private DrawingLayoutLines layout;
	
	
	//////////////////////
	// Public Interface
	public ColoredLinesRenderer(DrawingLayoutLines layout) {
		super();
		this.layout = layout;
	}
	
	

	public ColoredLinesRenderer() {
		super();
		this.layout = new DrawingLayoutLines();
		
	}

	
	@Override
	public void setLayout(DrawingLayout layout) {
		
		this.layout = (DrawingLayoutLines) layout;
		
	}

	/**
	 * Return the deafult layout fot this renderer
	 * 
	 */
	@Override
	public DrawingLayout getDefaultLayout() {
		
		return new DrawingLayoutLines();
		
	}
	
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
		

		int paddingV = this.layout.getPaddingV();
		int paddingW = this.layout.getPaddingW();
		int lineHeight = this.layout.getLineHeight();
		int space_1 = this.layout.getSpace_1();
		int space_2 = this.layout.getSpace_2();
		int rulerHeight = this.layout.getRulerHeight();
		int rulerLinesVspace = this.layout.getRulerNumbersVspace();
		int rulerNumbersVspace = this.layout.getRulerNumbersVspace();
		int barsPerLine = this.layout.getBarsPerLine();
		
		int barsToPrint = data.length-windowLen+1;
		
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
		xPos++; 
		
		for (int x=windowLen-1;x<data.length;x++,xPos++) {
			windowValue = windowValue + (data[x] - data[xPos]) / windowLen;
			int yPos = ((xPos) / barsPerLine) * ( lineHeight + space_1 + space_2 + rulerHeight ); 
			setColorOnGraphic(g,xPos % barsPerLine,color.getColor(windowValue),yPos,lineHeight, paddingV, paddingW);
			
		}
		
		////////////////////////////////
		
		// Draw Black borders
				
		g.setColor(Color.BLACK);
		
		for (int row = 0; row< ((data.length-1) / barsPerLine) + 1;row++ ) {
			
			int last_x = Math.min(barsPerLine, data.length - windowLen + 1 - row * barsPerLine );
			
			int yPos = row * ( lineHeight + space_1 + space_2 + rulerHeight );
			
			g.drawRect(paddingW-1, yPos +paddingV -1, last_x+2, lineHeight+2);
			
		}
		
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
	
	////////////////////
	// Private Methods
	
	private void 				setColorOnGraphic					(Graphics2D g, int xPos, Color color, int yPos, int lineHeight, int paddingV, int paddingW) {
		g.setColor(color);
//		GradientPaint paint = new GradientPaint(paddingW+xPos,paddingV+ yPos, color, paddingW+xPos, paddingV+ yPos + lineHeight, Color.BLACK);
//		g.setPaint(paint);
		g.drawLine(paddingW+xPos,paddingV+ yPos, paddingW+xPos, paddingV+ yPos + lineHeight);
		
	}



	
	
}
