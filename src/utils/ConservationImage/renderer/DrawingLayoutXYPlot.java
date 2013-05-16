package utils.ConservationImage.renderer;

public class DrawingLayoutXYPlot implements DrawingLayout {

	
//    PaddingW                                    PaddingW
//  /--/				                            /--/
//	---------------------------------------------------- /
//	|                   (   BODY   )                   | | PaddingV
//	|                                                  | /                      
//	|   /\      /\          /\      /\/\/\    /\  /\/  |     /         
//	|  /  \  /\/  \        /  \  /\/      \  /  \/     |     | PlotHeight
//	|      \/      \/\/\/\/    \/          \/          |     /        /		
//	|                                                  |              |  Space_1
//	|  |----|----|----|----|----|----|----|----|----|  |  Ruler    /  /            
//	|                                                  |           | PaddingV
//	----------------------------------------------------           / 
//
//     /--------------------------------------------/
//   
	
	///////////////////////
	// Instance variables
	int paddingV;
	int paddingW;
	int plotHeight;
	int space_1;
	int rulerHeight;
	int rulerLinesVspace;
	int rulerNumbersVspace;
	
	
	/////////////////////////
	// Constructor
	/**
	 * Creates a new Drawig Layout with the given values
	 * 
	 * @param paddingV
	 * @param paddingW
	 * @param plotHeight
	 * @param space_1
	 * @param rulerHeight
	 * @param rulerLinesVspace
	 * @param rulerNumbersVspace
	 */
	public DrawingLayoutXYPlot(int paddingV, int paddingW, int plotHeight,
			int space_1, int rulerHeight, int rulerLinesVspace,
			int rulerNumbersVspace) {
		super();
		this.paddingV = paddingV;
		this.paddingW = paddingW;
		this.plotHeight = plotHeight;
		this.space_1 = space_1;
		this.rulerHeight = rulerHeight;
		this.rulerLinesVspace = rulerLinesVspace;
		this.rulerNumbersVspace = rulerNumbersVspace;
	}
	
	/**
	 * Creates a default layout for xy plots 
	 */
	public DrawingLayoutXYPlot() {
		super();
		this.paddingV = 30;
		this.paddingW = 50;
		this.plotHeight = 200;
		this.space_1 = 10;
		this.rulerHeight = 35;
		this.rulerLinesVspace = 20;
		this.rulerNumbersVspace = 15;
		
	}

	/////////////////////////
	// Getters and Setters

	public int getPaddingV() {
		return paddingV;
	}
	public void setPaddingV(int paddingV) {
		this.paddingV = paddingV;
	}
	public int getPaddingW() {
		return paddingW;
	}
	public void setPaddingW(int paddingW) {
		this.paddingW = paddingW;
	}
	public int getPlotHeight() {
		return plotHeight;
	}
	public void setPlotHeight(int plotHeight) {
		this.plotHeight = plotHeight;
	}
	public int getSpace_1() {
		return space_1;
	}
	public void setSpace_1(int space_1) {
		this.space_1 = space_1;
	}
	public int getRulerHeight() {
		return rulerHeight;
	}
	public void setRulerHeight(int rulerHeight) {
		this.rulerHeight = rulerHeight;
	}
	public int getRulerLinesVspace() {
		return rulerLinesVspace;
	}
	public void setRulerLinesVspace(int rulerLinesVspace) {
		this.rulerLinesVspace = rulerLinesVspace;
	}
	public int getRulerNumbersVspace() {
		return rulerNumbersVspace;
	}
	public void setRulerNumbersVspace(int rulerNumbersVspace) {
		this.rulerNumbersVspace = rulerNumbersVspace;
	}
	
	
	
}
