package com.legallic.sudoku;

import com.legallic.sudoku.model.Cell;

import com.legallic.sudoku.model.Matrix;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Initializer {


	public static Matrix initialize(String SudokuFile){
		Matrix matrix = new Matrix();
		
		 try {
			 
		    // open file input stream
			 BufferedReader reader = new BufferedReader(new FileReader(SudokuFile));

			// read file line by line
			String line = null;
			String splitBy = ",";  
				
			Scanner scanner = null;
			int rowIndex = 0;
			int colIndex = 0;
					
			while ((line = reader.readLine()) != null) {
				char aChar = line.charAt(0);
				if ( aChar != '#' ) {
					
				
				// read line 
				scanner = new Scanner(line);
				scanner.useDelimiter(splitBy);
				while (scanner.hasNext()) {
					String strvalue = scanner.next() ;
						

						//int value = (Integer.parseInt(scanner.next()));
						int value = (Integer.parseInt(strvalue )) ;
						
						Cell cell = new Cell();
						cell.setColIndex(colIndex);
						cell.setRowIndex(rowIndex);
						
						//int value = sudoku[rowIndex][colIndex];
						
						
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
						
						colIndex++;
					
					
				}
					//empList.add(emp);
				
				colIndex = 0;
				rowIndex++;
				}
			}
				//close reader
		       
			reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		         		
		return matrix;
	}
	
	
}
