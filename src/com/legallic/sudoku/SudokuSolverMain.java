package com.legallic.sudoku;

import com.legallic.sudoku.exception.SudokuException;
import com.legallic.sudoku.model.Matrix;
import com.legallic.sudoku.service.Solver;

public class SudokuSolverMain {
	
	
	private static int[][] toSolveFull = {
			{1,9,2,8,3,7,4,6,5},
			{3,7,5,4,5,9,1,8,2},
			{4,8,6,5,2,1,7,3,9},
			{5,3,9,6,1,4,2,7,8},
			{2,4,1,9,7,8,6,5,3},
			{8,6,7,2,5,3,9,1,4},
			{6,2,8,1,4,5,3,9,7},
			{9,1,3,7,8,2,5,4,6},
			{7,5,4,3,9,6,8,2,1}
	};
	
	private static int[][] toSolvePart = {
		{1,9,0,0,3,7,4,6,5},
		{3,7,5,4,5,9,1,8,2},
		{4,8,6,5,2,1,7,3,9},
		{5,3,9,6,1,4,2,7,8},
		{2,4,1,9,7,8,6,5,3},
		{8,6,7,2,5,3,9,1,4},
		{6,2,8,1,4,5,3,9,7},
		{9,1,3,7,8,2,5,4,6},
		{7,5,4,3,9,6,8,2,0}
	};	
	
	private static int[][] toto1 = {
		{0,0,1,4,0,5,2,0,0},
		{2,0,0,7,1,6,0,0,8},
		{6,0,0,0,0,0,0,0,3},
		{0,0,3,0,6,0,9,0,0},
		{0,0,0,9,0,7,0,0,0},
		{0,0,7,0,5,0,4,0,0},
		{7,0,0,0,0,0,0,0,9},
		{3,0,0,6,4,9,0,0,5},
		{0,0,9,1,0,8,3,0,0}
	};	
	
	private static int[][] lemondeDifficile = {
		{1,9,0,0,3,0,0,0,5},
		{0,0,5,0,0,9,0,0,2},
		{0,0,0,0,0,1,7,3,0},
		{0,0,0,0,0,4,0,7,8},
		{0,0,1,9,0,8,6,0,0},
		{8,6,0,2,0,0,0,0,0},
		{0,2,8,1,0,0,0,0,0},
		{9,0,0,7,0,0,5,0,0},
		{7,0,0,0,9,0,0,2,1}
	};	
	
	private static int[][] lemondeTresDifficile = {
		{0,0,0,9,0,1,7,0,3},
		{0,0,0,0,0,8,0,1,0},
		{0,0,0,0,7,5,9,0,0},
		{5,8,0,3,0,0,1,2,0},
		{4,0,0,0,0,0,0,0,5},
		{0,2,7,0,0,9,0,4,6},
		{0,0,1,8,9,0,0,0,0},
		{0,3,0,7,0,0,0,0,0},
		{9,0,8,5,0,4,0,0,0}
	};	
	
	private static int[][] lemondedudimanche = {
		{0,9,3,0,0,0,0,0,7},
		{0,6,0,3,2,0,0,8,0},
		{8,2,0,7,0,0,0,1,5},
		{0,0,0,2,0,0,0,0,0},
		{0,8,0,4,3,1,0,5,0},
		{0,0,0,0,0,6,0,0,0},
		{2,5,0,0,0,3,0,4,6},
		{0,1,0,0,4,8,0,3,0},
		{3,0,0,0,0,0,5,7,0}
	};		
	
	private static int[][] extreme = {
		{3,0,0,0,1,0,0,0,9},
		{0,8,0,7,0,4,0,1,0},
		{0,0,5,0,0,0,4,0,0},
		{0,3,0,6,0,2,0,4,0},
		{6,0,0,0,0,0,0,0,3},
		{0,7,0,9,0,3,0,5,0},
		{0,0,3,0,0,0,5,0,0},
		{0,2,0,1,0,7,0,8,0},
		{8,0,0,0,2,0,0,0,6}
	};		
	
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		Matrix matrix = Initializer.initialize(extreme);
		Solver solver = new Solver(matrix);
		System.out.println("###############################################");
		System.out.println("Tentative de résolution de la matrice suivante");
		System.out.println("###############################################");
		System.out.println(matrix);
		try {
			System.out.println("Ai-je réussi ? : " + solver.solve());
			System.out.println(solver.getMatrix());
			System.out.println(" En " + (System.currentTimeMillis() - startTime) + " ms !!!!!!!!!!!!! ");	
		} catch (SudokuException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}
}
