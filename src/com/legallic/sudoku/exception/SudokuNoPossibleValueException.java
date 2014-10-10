package com.legallic.sudoku.exception;

import com.legallic.sudoku.model.Cell;

public class SudokuNoPossibleValueException extends SudokuException {

	private Cell cell;
	
	public SudokuNoPossibleValueException(Cell cell) {
		this.cell = cell;
	}
	
	@Override
	public String getMessage() {
		return "Pas de valeur possible pour la cellule (" + (cell.getRowIndex() + 1) + ", " + (cell.getColIndex() + 1) + ")";
	}

	public Cell getCell() {
		return cell;
	}

	public void setCell(Cell cell) {
		this.cell = cell;
	}
}
