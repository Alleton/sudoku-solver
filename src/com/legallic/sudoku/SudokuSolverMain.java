package com.legallic.sudoku;



import com.legallic.sudoku.exception.SudokuException;
import com.legallic.sudoku.model.Matrix;
import com.legallic.sudoku.service.Solver;

public class SudokuSolverMain {
	
	
	public static void main(String[] args)  {
		long startTime = System.currentTimeMillis();
		
		SudokuGetPropertyValues properties = new SudokuGetPropertyValues();		
		System.out.println (properties.getPropValue("SudokuFile"));

		String SudokuFile = properties.getPropValue("SudokuFile") ;
		String SudokuFolder = properties.getPropValue("SudokuFolder") ;

		
		
		Matrix matrix = Initializer.initialize(SudokuFolder + "/" + SudokuFile);
		Solver solver = new Solver(matrix);
		System.out.println("###############################################");
		System.out.println("Tentative de resolution de la matrice suivante");
		System.out.println("###############################################");
		System.out.println(matrix);
		
		

		try {
			System.out.println("Ai-je reussi ? : " + solver.solve());
			System.out.println(solver.getMatrix());
			System.out.println(" En " + (System.currentTimeMillis() - startTime) + " ms !!!!!!!!!!!!! ");	
		} catch (SudokuException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}

		
	}
}
