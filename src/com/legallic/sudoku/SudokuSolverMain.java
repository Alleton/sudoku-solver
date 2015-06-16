package com.legallic.sudoku;




import com.legallic.sudoku.model.Matrix;
import com.legallic.sudoku.model.VueSudoku;
import com.legallic.sudoku.model.Modele;
import com.legallic.sudoku.model.Controler;

public class SudokuSolverMain {
	
	
	public static void main(String[] args)  {
		//long startTime = System.currentTimeMillis();
		
		SudokuGetPropertyValues properties = new SudokuGetPropertyValues();		
		System.out.println (properties.getPropValue("SudokuFile"));

		String SudokuFile = properties.getPropValue("SudokuFile") ;
		String SudokuFolder = properties.getPropValue("SudokuFolder") ;
		System.out.println (properties.getPropValue("SudokuFolder"));
		
		
		Matrix matrix = Initializer.initialize(SudokuFolder + "/" + SudokuFile);
		//Solver solver = new Solver(matrix);
		System.out.println("###############################################");
		System.out.println("Tentative de resolution de la matrice suivante");
		System.out.println("###############################################");
		System.out.println(matrix);
	
		Modele  modele = new Modele();
		
		VueSudoku vue = new VueSudoku(modele , matrix);
		
		Controler Controler =  new Controler(modele, vue);
		Controler.setMatrix(matrix);
		
		vue.trace.addActionListener(Controler);
		vue.go.addActionListener(Controler);
		vue.load.addActionListener(Controler);
		vue._conv.addActionListener(Controler);
		vue._euro.addActionListener(Controler);
		vue._franc.addActionListener(Controler);
		
		// Affiche la position de depart
		vue.MajGrille() ;



		
	}
}
