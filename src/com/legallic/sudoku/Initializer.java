package com.legallic.sudoku;

import com.legallic.sudoku.model.Cell;
import com.legallic.sudoku.model.Matrix;

public class Initializer {

	public static Matrix initialize(int[][] sudoku){
		Matrix matrix = new Matrix();
		
		for(int rowIndex=0; rowIndex < matrix.getMatrixSize(); rowIndex++){
			for(int colIndex=0; colIndex < matrix.getMatrixSize(); colIndex++){
				Cell cell = new Cell();
				cell.setColIndex(colIndex);
				cell.setRowIndex(rowIndex);
				int value = sudoku[rowIndex][colIndex];
				cell.setDefinitiveValue(value);
				if(value > 0){
					cell.setFound(true);
				}
				boolean[] allIsPossible = new boolean[matrix.getMatrixSize() + 1];
				// TODO : initialiser de meilleur façon
				for (int i = 1; i < allIsPossible.length; i++) {
					allIsPossible[i] = !cell.isFound();
				}
				cell.setPossibleValues(allIsPossible);
				cell.setSquareIndex(colIndex / 3 + (rowIndex / 3 ) * 3);
				matrix.getCells()[rowIndex][colIndex] = cell;
			}
		}
		return matrix;
	}
}
