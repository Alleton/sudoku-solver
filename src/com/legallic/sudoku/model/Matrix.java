package com.legallic.sudoku.model;

public class Matrix {

	private Cell[][] cells;
	private int matrixSize = 9;

	public Matrix() {
		cells = new Cell[matrixSize][matrixSize];
	}
	
	public Cell[][] getCells() {
		return cells;
	}

	public void setCells(Cell[][] cells) {
		this.cells = cells;
	}

	public int getMatrixSize() {
		return matrixSize;
	}

	public void setMatrixSize(int size) {
		this.matrixSize = size;
	}
	
	@Override
	public Object clone(){
		Matrix clone = new Matrix();
		clone.setMatrixSize(matrixSize);
		clone.setCells(new Cell[clone.getMatrixSize()][clone.getMatrixSize()]);
		for(int rowIndex=0; rowIndex < this.getMatrixSize(); rowIndex++){
			for(int colIndex=0; colIndex < this.getMatrixSize(); colIndex++){
				clone.getCells()[rowIndex][colIndex] = (Cell) this.getCells()[rowIndex][colIndex].clone();
			}
		}
		return clone;
	}
	
	/**
	 * Display matrix in ASCII ART...
	 * @param showPossibleValues
	 * @return
	 */
	public String matrixToString(boolean showPossibleValues){
		StringBuilder builder = new StringBuilder();
		builder.append("\r\n");	
		for (int i = 0; i < this.getMatrixSize(); i++) {
			builder.append(" (" + i +")");
		}		
		builder.append("\r\n");		
		
		for (int i = 0; i < this.getMatrixSize(); i++) {
			builder.append("  v ");
		}		
		builder.append("\r\n");			
		
		for (int i = 0; i < this.getMatrixSize() / 3; i++) {
			builder.append("+-----------");
		}		
		builder.append("+\r\n");
		for(int rowIndex=0; rowIndex < this.getMatrixSize(); rowIndex++){
			builder.append("|");
			for(int colIndex=0; colIndex < this.getMatrixSize(); colIndex++){
				Cell cell = this.cells[rowIndex][colIndex];
				String cellDisplay = "";
				if(cell.isFound()){
					cellDisplay = "" + cell.getDefinitiveValue();
				}
				else{
					if(showPossibleValues){
						boolean[] possibleValues = cell.getPossibleValues();
						cellDisplay = "(";
						for (int i = 0; i < possibleValues.length; i++) {
							if(possibleValues[i]){
								cellDisplay += i + ",";
							}
						}
						cellDisplay += ")";
					}
					else{
						cellDisplay = " ";
					}
				}
				builder.append(" " + cellDisplay);
				if((colIndex + 1) % 3 == 0){
					builder.append(" |");
				}
				else{
					builder.append("  ");
				}
			}
			builder.append("\r\n");
			if((rowIndex +1) % 3 ==0){
				for (int i = 0; i < this.getMatrixSize() / 3; i++) {
					builder.append("+-----------");
				}
				builder.append("+\r\n");
			}
		}
		return builder.toString();		
	}
	
	@Override
	public String toString() {
		return matrixToString(false);
	}
	
}
