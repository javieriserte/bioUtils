package utils.sudoku;

public class SudokuShuffler {

	private char[][] elements;
	private int columnsPerBlock;
	private int rowsPerBlock;
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SudokuShuffler s = new SudokuShuffler(5, 4);
		
		String s1 ="ABCDEFGHIJKLMNOPQRSTPQRSTABCDEFGHIJKLMNOKLMNOPQRSTABCDEFGHIJFGHIJKLMNOPQRSTABCDEEABCDJFGHIOKLMNTPQRSTPQRSEABCDJFGHIOKLMNOKLMNTPQRSEABCDJFGHIJFGHIOKLMNTPQRSEABCDDEABCIJFGHNOKLMSTPQRSTPQRDEABCIJFGHNOKLMNOKLMSTPQRDEABCIJFGHIJFGHNOKLMSTPQRDEABCCDEABHIJFGMNOKLRSTPQRSTPQCDEABHIJFGMNOKLMNOKLRSTPQCDEABHIJFGHIJFGMNOKLRSTPQCDEABBCDEAGHIJFLMNOKQRSTPQRSTPBCDEAGHIJFLMNOKLMNOKQRSTPBCDEAGHIJFGHIJFLMNOKQRSTPBCDEA";
		System.out.println(s1.length());
		
		int v=0;
		for (int i=0; i<20;i++){
			for (int j=0; j<20;j++){
				s.addValue(j, i, s1.charAt(v++));				
			}
		}
		
		System.out.println(s.toString());
		
		
		// swap column blocks

			int n = (int)(Math.random()* 10 + 5);

		 	for (int i=0;i<n;i++) {
			 	int c1 = (int)(Math.random()* s.columnBlocks());
			 	int c2 = (int)(Math.random()* s.columnBlocks());
			 	s.swapColumnBlock(c1,c2);
		 	}
			

		
		// swap row blocks
		 	
			n = (int)(Math.random()* 10 + 5);

		 	for (int i=0;i<n;i++) {
			 	int c1 = (int)(Math.random()* s.rowBlocks());
			 	int c2 = (int)(Math.random()* s.rowBlocks());
			 	s.swapRowBlock(c1,c2);
		 	}
		
		// swap columns

		 	n = (int)(Math.random()* 10 + 5);

		 	for (int i=0;i<n;i++) {
		 		int c = (int)(Math.random()* s.columnBlocks());
			 	int c1 = (int)(Math.random()* s.columnsPerBlock);
			 	int c2 = (int)(Math.random()* s.columnsPerBlock);
			 	s.swapColumn(c1,c2,c);
		 	}
			
		
		
		// swap rows
		 	for (int i=0;i<n;i++) {
		 		int c = (int)(Math.random()* s.rowBlocks());
			 	int c1 = (int)(Math.random()* s.rowsPerBlock);
			 	int c2 = (int)(Math.random()* s.rowsPerBlock);
			 	s.swapColumn(c1,c2,c);
		 	}
		

		
		String o = "ABCDEFGHIJKLMNOPQRST";
		String d = "VQYWDGCPNSERHIMTKALF";
		
		s.replaceElements(o.toCharArray(), d.toCharArray());
		
		s.eraseElementsSymmetrically(250);
		
		System.out.println(s.toString());
	}
	// CONSTRUCTOR
	
	public SudokuShuffler(int columnsPerBlock, int rowsPerBlock) {
		this.columnsPerBlock= columnsPerBlock;
		this.rowsPerBlock = rowsPerBlock;
		this.elements = new char[this.cellsPerRow()][this.cellsPerRow()];
	}
	
	// PUBLIC INTERFACE
	
	public void addValue(int column, int row, char value) {
		this.elements[column][row] = value;
	}
	
	public void swapColumnBlock(int columnBlock1, int columnBlock2) {
		if (columnBlock1<0||columnBlock1>=this.columnBlocks()) return;
		if (columnBlock2<0||columnBlock2>=this.columnBlocks()) return;
		if (columnBlock1==columnBlock2) return;
		
		for (int i=0; i< columnsPerBlock; i++) {
			//iterate over column
			for (int j =0; j< this.cellsPerRow(); j++ ) {
				//iterate over row
				
				this.swap(columnBlock1 * this.columnsPerBlock + i , j, columnBlock2 * this.columnsPerBlock + i, j);
			}
		}
		
	}
	
	public void swapRowBlock(int rowBlock1, int rowBlock2) {
		if (rowBlock1<0||rowBlock1>=this.rowBlocks()) return;
		if (rowBlock2<0||rowBlock2>=this.rowBlocks()) return;
		if (rowBlock1==rowBlock2) return;
		
		for (int i=0; i < rowsPerBlock; i++) {
			//iterate over row
			for (int j =0; j< this.cellsPerRow(); j++ ) {
				//iterate over column
				this.swap(j, rowBlock1 * this.rowsPerBlock + i ,j, rowBlock2 * this.rowsPerBlock + i);
			}
		}
	}

	public void swapColumn(int column1, int column2, int columnBlock) {
		if (columnBlock<0||columnBlock>=this.columnBlocks()) return ;
		if (column1<0||column1>=this.columnsPerBlock) return ;
		if (column2<0||column2>=this.columnsPerBlock) return ;
		
		for (int j =0; j< this.cellsPerRow(); j++ ) {
			//iterate over row
			this.swap(columnBlock * this.columnsPerBlock + column1 ,j, columnBlock * this.rowsPerBlock + column2,j);
		}
		
	}
	
	public void swapRow(int row1, int row2, int rowBlock) {
		if (rowBlock<0||rowBlock>=this.columnBlocks()) return ;
		if (row1<0||row2>=this.columnsPerBlock) return ;
		if (row1<0||row2>=this.columnsPerBlock) return ;
		
		for (int j =0; j< this.cellsPerRow(); j++ ) {
			//iterate over column
			this.swap(rowBlock * this.columnsPerBlock + row1 ,j, rowBlock * this.rowsPerBlock + row2,j);
		}
	}
	
	public void replaceElements(char[] from, char[] to) {
		char[][] newvalues = new char[this.cellsPerRow()][this.cellsPerRow()];

		for (int i=0; i<from.length; i++) {
			for (int j = 0; j< this.cellsPerRow(); j++ ) {
				for (int k = 0; k< this.cellsPerRow(); k++ ) {
					if(this.elements[j][k]==from[i]) newvalues[j][k] = to[i]; 
				}
			}
		}
		this.elements = newvalues;
	}

	public SudokuShuffler eraseElementsSymmetrically(int numberOfElements) {

		int hemi;
		int hidden;
		char[][] newvalues = null;
		SudokuShuffler newSud = null;
			
		// case numberOfElements is even
			// case grid is even, OK
			// case grid is Odd, Center is not erased

		// case numberOfElements is odd
			// case grid is Odd, OK, center is erased
			// case grid is even, BAD!!!!
		
		switch (numberOfElements%2) {
		case 0:
			// case numberOfElements is even
			
			
			
			hemi = (int)(Math.pow(this.cellsPerRow(), 2)-((int)Math.pow(this.cellsPerRow(), 2) % 2))/2;
			hidden = numberOfElements/2;
			if (hidden>hemi) return this;
			
			newvalues = erase(hemi, hidden);
			
			break;
			

		case 1:
			// case numberOfElements is odd
			switch ((int)Math.pow(this.cellsPerRow(), 2) % 2) {
			case 1:
				// case grid is Odd, OK, center is not known
				
				hemi = (int)(Math.pow(this.cellsPerRow(), 2)-((int)Math.pow(this.cellsPerRow(), 2) % 2))/2;
				if (numberOfElements-1>hemi) return this;
				hidden = (numberOfElements-1)/2;
				newvalues = erase(hemi, hidden);
				newvalues[(this.cellsPerRow()+1)/2][(this.cellsPerRow()+1)/2] = ' ';
				
				break;

			case 0:
				// case grid is even, BAD!!!!
				
				return this;
				
			default:
				

				break;
			}

			
			break;

		default:
			
			newSud = new SudokuShuffler(this.columnsPerBlock , this.columnsPerBlock);
			newSud.elements = newvalues;

		}
		return newSud;
		
	}

	
	public String toString() {
		StringBuilder result = new StringBuilder(1000);
		for ( int i = 0; i<this.cellsPerRow();i++) {
			for (int j = 0; j<this.cellsPerRow();j++) {
				
				result = result.append(this.elements[j][i]);
				
			}
			result = result.append("\r\n");
		}
		
		return result.toString();
	}
	

    // Private Methods
	
	private int columnBlocks() {
		return this.rowsPerBlock;
	}
	
	private int rowBlocks() {
		return this.columnsPerBlock;
	}
	
	private int cellsPerRow()  {
		return this.columnsPerBlock * this.rowsPerBlock;
	}

	private void swap(int c1, int r1, int c2, int r2) {
		char temp =this.elements[c1][r1] ;
		this.elements[c1][r1] = this.elements[c2][r2] ;
		this.elements[c2][r2] = temp ;
	}
	
	private char[][] erase(int hemi, int hidden) {
		int counter=0;
		char[][] newValues = new char[this.cellsPerRow()][this.cellsPerRow()];
		int shown = hemi - hidden;
		
		for (int i=0; i< this.cellsPerRow() && counter<hemi;i++ ) {
			for (int j=0; j< this.cellsPerRow()&& counter<hemi; j++, counter++) {
				
				double p = Math.random();
				
				if (p<= ((double)hidden/((double)shown+hidden)) ) {
					hidden--;
					newValues[i][j] = ' ';
					newValues[this.cellsPerRow() - i-1][this.cellsPerRow() - j-1] = ' ';
				} else {
					shown--;
					newValues[i][j] = this.elements[i][j];
					newValues[this.cellsPerRow() - i-1][this.cellsPerRow() - j-1] = this.elements[this.cellsPerRow() - i-1][this.cellsPerRow() - j-1];
				}
			}
		}
		return newValues;
	}
}
