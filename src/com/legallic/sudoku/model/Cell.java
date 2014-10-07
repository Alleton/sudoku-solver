package com.legallic.sudoku.model;

public class Cell {

	private int colIndex;
	private int rowIndex;
	private int squareIndex;
	private int definitiveValue;
	private boolean[] possibleValues;
	private boolean found;
		
	public int getColIndex() {
		return colIndex;
	}
	public void setColIndex(int x) {
		this.colIndex = x;
	}
	public int getRowIndex() {
		return rowIndex;
	}
	public void setRowIndex(int y) {
		this.rowIndex = y;
	}
	public int getSquareIndex() {
		return squareIndex;
	}
	public void setSquareIndex(int squareIndex) {
		this.squareIndex = squareIndex;
	}
	public int getDefinitiveValue() {
		return definitiveValue;
	}
	public void setDefinitiveValue(int value) {
		this.definitiveValue = value;
	}
	public boolean[] getPossibleValues() {
		return possibleValues;
	}
	public void setPossibleValues(boolean[] possibleValues) {
		this.possibleValues = possibleValues;
	}
	public boolean isFound() {
		return found;
	}
	public void setFound(boolean found) {
		this.found = found;
	}
	
	@Override
	protected Object clone(){
		Cell clone = new Cell();
		clone.setColIndex(colIndex);
		clone.setRowIndex(rowIndex);
		clone.setSquareIndex(squareIndex);
		clone.setDefinitiveValue(definitiveValue);
		clone.setPossibleValues(possibleValues.clone());
		clone.setFound(found);
		return clone;
	}
	
	@Override
	public String toString() {
		return "" + this.definitiveValue;
	}
	
}
