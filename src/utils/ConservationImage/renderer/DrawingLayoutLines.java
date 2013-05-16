package utils.ConservationImage.renderer;

public class DrawingLayoutLines implements DrawingLayout {
	
//    PaddingH                                    PaddingH
//  /--/				                            /--/
//	---------------------------------------------------- /
//	|                   (   BODY   )                   | | PaddingV
//	|  ||||||||||||||||||||||||||||||||||||||||||||||  | /            /
//	|                                                  |              |  Space_1
//	|  |----|----|----|----|----|----|----|----|----|  |  Ruler       /            /
//	|                                                  |                           |  Space_2
//	|  ||||||||||||||||||||||||||||||||||||||||||||||  |                           /
//	|                                                  |
//	|  |----|----|----|----|----|----|----|----|----|  |
//	|                                                  |
//	|  ||||||||||||||||||||||||||||||||||||||||||||||  |
//	|                                                  |
//	|  |----|----|----|----|----|----|----|----|----|  |
//	|                                                  |
//	|  ||||||||||||||||||||||||||||||||||||||||||||||  |
//	|                                                  |
//	|  |----|----|----|----|----|----|----|----|----|  |
//	|                                                  |
//	|  ||||||||||||||||||||||||||||||||||||||||||||||  |
//	|                                                  |
//	|  |----|----|----|----|----|----|----|----|----|  |		
//	|                                                  |
//	|  ||||||||||||||||||||||||||||||||||||||||||||||  |
//	|                                                  |
//	|  |----|----|----|----|----|----|----|----|----|  | /
//	|                                                  | | PaddingV
//	---------------------------------------------------- / 
	
	
	/////////////////////////
	// Instance Variables
	
	int paddingV;
	int paddingW;
	int lineHeight;
	int space_1;
	int space_2;
	int rulerHeight;
	int rulerLinesVspace;
	int rulerNumbersVspace;
	int barsPerLine;

	////////////////////////////
	// Construcutor

	/**
	 * Creates a new layout with the gicen values.
	 * 
	 * @param paddingV
	 * @param paddingW
	 * @param lineHeight
	 * @param space_1
	 * @param space_2
	 * @param rulerHeight
	 * @param rulerLinesVspace
	 * @param rulerNumbersVspace
	 * @param barsPerLine
	 */
	public DrawingLayoutLines(int paddingV, int paddingW, int lineHeight,
			int space_1, int space_2, int rulerHeight, int rulerLinesVspace,
			int rulerNumbersVspace, int barsPerLine) {
		super();
		this.paddingV = paddingV;
		this.paddingW = paddingW;
		this.lineHeight = lineHeight;
		this.space_1 = space_1;
		this.space_2 = space_2;
		this.rulerHeight = rulerHeight;
		this.rulerLinesVspace = rulerLinesVspace;
		this.rulerNumbersVspace = rulerNumbersVspace;
		this.barsPerLine = barsPerLine;
	}
	
	/**
	 * creates a default layout.
	 */
    public DrawingLayoutLines() {
		super();

		this.paddingV = 30;
		this.paddingW = 50;
		this.lineHeight = 50;
		this.space_1 = 10;
		this.space_2 = 30;
		this.rulerHeight = 35;
		this.rulerLinesVspace = 20;
		this.rulerNumbersVspace = 15;
		this.barsPerLine = 500;
    }

	/////////////////////////////////
	// Getters and setters
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



	public int getLineHeight() {
		return lineHeight;
	}



	public void setLineHeight(int lineHeight) {
		this.lineHeight = lineHeight;
	}



	public int getSpace_1() {
		return space_1;
	}



	public void setSpace_1(int space_1) {
		this.space_1 = space_1;
	}



	public int getSpace_2() {
		return space_2;
	}



	public void setSpace_2(int space_2) {
		this.space_2 = space_2;
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



	public int getBarsPerLine() {
		return barsPerLine;
	}

	public void setBarsPerLine(int barsPerLine) {
		this.barsPerLine = barsPerLine;
	}
	
	
}
