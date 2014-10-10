package com.legallic.sudoku.model;

public class Coordinate {

	private int rowIndex;
	private int colIndex;
	
	public Coordinate() {
	}
	
	public Coordinate(int rowIndex, int colIndex) {
		super();
		this.rowIndex = rowIndex;
		this.colIndex = colIndex;
	}
	public int getRowIndex() {
		return rowIndex;
	}
	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}
	public int getColIndex() {
		return colIndex;
	}
	public void setColIndex(int colIndex) {
		this.colIndex = colIndex;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Coordinate){
			return (((Coordinate) obj).rowIndex == this.rowIndex) && (((Coordinate) obj).getColIndex() == this.colIndex);
		}
		else
			return false;
	}
	
	@Override
	public String toString() {
		return "(" + rowIndex + "," + colIndex + ")";
	}
}
