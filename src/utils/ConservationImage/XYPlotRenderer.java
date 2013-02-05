package utils.ConservationImage;

//import java.awt.BasicStroke;
import java.awt.Color;
//import java.awt.Font;
import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class XYPlotRenderer implements Renderer {

	@Override
	public BufferedImage render(ColoringStrategy color, double[] data, int windowLen) {
		
	
		
		
//	    PaddingW                                    PaddingW
//      /--/				                            /--/
//		---------------------------------------------------- /
//		|                   (   BODY   )                   | | PaddingV
//		|                                                  | /                      
//		|   /\      /\          /\      /\/\/\    /\  /\/  |     /         
//		|  /  \  /\/  \        /  \  /\/      \  /  \/     |     | PlotHeight
//		|      \/      \/\/\/\/    \/          \/          |     /        /		
//		|                                                  |              |  Space_1
//		|  |----|----|----|----|----|----|----|----|----|  |  Ruler    /  /            
//		|                                                  |           | PaddingV
//		----------------------------------------------------           / 
//
//         /--------------------------------------------/
//                        lineWidth		
		

		int paddingV = 30;
		int paddingW = 50;
		int plotHeight = 200;
		int space_1 = 10;
		int rulerHeight = 35;
		int rulerLinesVspace = 20;
		int rulerNumbersVspace = 15;
		int lineWidth = data.length - windowLen+1;
		
		int imageW = 2 * paddingW + lineWidth;
		int imageH = 2 * paddingV + plotHeight + space_1 + rulerHeight;
		
		BufferedImage bi = new BufferedImage(imageW, imageH, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = bi.createGraphics();

		// Draw Background
		
		g.setColor(Color.white);
		g.fillRect(0, 0, bi.getWidth(), bi.getHeight());


		// process first element
		double windowValue =0;
		for (int i =0; i<windowLen;i++) {
			windowValue = windowValue + data[i];
		}
		
		int xPos=0;
		windowValue = windowValue/ windowLen; 

		// Process all the elements
		for (int x=windowLen;x<data.length;x++,xPos++) {
			double newWindowValue = windowValue + (data[x] - data[xPos]) / windowLen;
			drawSingleLineOnPlot(g, windowValue, newWindowValue, xPos, color , plotHeight, paddingV, paddingW);
			windowValue = newWindowValue;
			
		}
		
		// Set Color for Line Drawing
		g.setColor(Color.BLACK);
		
		drawRuler(paddingV, paddingW, plotHeight, space_1, rulerLinesVspace, rulerNumbersVspace, lineWidth, g);
		
		return bi;
		
	}


	private void drawRuler(int paddingV, int paddingW, int plotHeight,
			int space_1, int rulerLinesVspace, int rulerNumbersVspace,
			int lineWidth, Graphics2D g) {

		int minRuleDiv = 10;
		int maxRuleDiv = 100;
		int midRuleDiv = 50;
		
		
		g.setStroke(new BasicStroke(2));
		
		// Draw Ruler
		
		// Draw Ruler Base Lines
		
		g.drawLine(  paddingW , 
					     paddingV + (plotHeight + space_1 + rulerLinesVspace ) , 
					     paddingW + lineWidth, 
					     paddingV + (plotHeight + space_1 + rulerLinesVspace));

		// Draw Horizontal Ruler Marks
		g.setStroke(new BasicStroke(1));
		
		// Draw Minor Marks
		for (int i=0;i<lineWidth;i=i+minRuleDiv) {
			int xPosR = paddingW + i;

			int yPosR = (paddingV + plotHeight + space_1 + rulerLinesVspace); 
			
			g.drawLine(xPosR, yPosR - 5, xPosR, yPosR);
		}
		
		// Draw Middle Marks
		for (int i=0;i<lineWidth;i=i+midRuleDiv) {
			
			int xPosR = paddingW + i;

			int yPosR = (paddingV + plotHeight + space_1 + rulerLinesVspace); 
			
			g.drawLine(xPosR, yPosR - 10, xPosR, yPosR);
			
		}
		
		// Draw Last Large Mark
		
		g.setStroke(new BasicStroke(2));
		
		g.drawLine(  paddingW + lineWidth, 
			         paddingV + (plotHeight + space_1 + rulerLinesVspace)  - 20, 
			         paddingW + lineWidth, 
			         paddingV + (plotHeight + space_1 + rulerLinesVspace) );

		// Draw Large Marks & Texts
		
		g.setFont(new Font("Verdana", Font.BOLD, 18));
		
		for (int i=0;i<lineWidth;i=i+maxRuleDiv) {

			int xPosR = paddingW +i;
		
			int yPosR = (paddingV + plotHeight + space_1 + rulerLinesVspace ); 
			
			g.drawLine(xPosR, yPosR - 20, xPosR, yPosR);
			
			String numberString = String.valueOf(i);
			
			int h= g.getFontMetrics().bytesWidth(numberString.getBytes(), 0, numberString.length());
			
			g.drawString( numberString, xPosR - (h/2) , yPosR + rulerNumbersVspace);
			
		}
	}


	// Private Methods
	

	private void drawSingleLineOnPlot(Graphics2D g, double lastValue, double newValue, int xPos, ColoringStrategy color, int plotHeight, int yTop, int xLeft) {

		int x1 = xLeft + xPos;
		int x2 = xLeft + xPos+1;
		int y1 = (int) (yTop + plotHeight * (1 - lastValue));
		int y2 = (int) (yTop + plotHeight * (1 - newValue));
		GradientPaint paint = new GradientPaint(x1,y1, color.getColor(lastValue), x2, y2, color.getColor(lastValue));
		g.setPaint(paint);
		g.setStroke(new BasicStroke(3));
		g.drawLine(x1,y1, x2, y2);
		
	}

}
