package com.legallic.sudoku;

import com.legallic.sudoku.model.Cell;

import com.legallic.sudoku.model.Matrix;
import java.util.Arrays; 
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;



public class Initializer {


	public static Matrix initialize(String SudokuFile){
		Matrix matrix = new Matrix();
		
		 try {
			// location of user dir
			 System.out.println(System.getProperty("user.dir"));
		    // open file input stream
			 BufferedReader reader = new BufferedReader(new FileReader(SudokuFile));

			// read file line by line
			String line = null;
			String splitBy = ",";  
				
			Scanner scanner = null;
			int rowIndex = 0;
			int colIndex = 0;
					
			while ((line = reader.readLine()) != null) {
				// test for empty string BEFORE reading 1st char  :) 
				if ( line.trim().length() != 0  && line.charAt(0) != '#'   )   {
					// read line 
					scanner = new Scanner(line);
					scanner.useDelimiter(splitBy);
					while (scanner.hasNext()) {
						
						int value = (Integer.parseInt(scanner.next()));
						// Create Cell and initialize it
						Cell cell = new Cell();
						cell.setColIndex(colIndex);
						cell.setRowIndex(rowIndex);
						
						cell.setDefinitiveValue(value);
						if(value > 0){
							cell.setFound(true);
						}
						boolean[] allIsPossible = new boolean[matrix.getMatrixSize() + 1];
						// 	TODO : initialize in a better way
						/*for (int i = 1; i < allIsPossible.length; i++) {
							allIsPossible[i] = !cell.isFound();
						}*/
						Arrays.fill(allIsPossible,!cell.isFound() ) ;
						
						cell.setPossibleValues(allIsPossible);
						cell.setSquareIndex(colIndex / 3 + (rowIndex / 3 ) * 3);
						matrix.getCells()[rowIndex][colIndex] = cell;
						
						colIndex++;
					}
					// colonne full try next row
					colIndex = 0;
					rowIndex++;
					System.out.println( "rowIndex" + rowIndex ) ;
				}
			}
				//close reader
		       
			reader.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				throw new IllegalArgumentException("Unable to load " + SudokuFile, e);
				//e.printStackTrace();
			}
		         		
		return matrix;
	}
	
	
}
