package com.legallic.sudoku.model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import com.legallic.sudoku.Initializer;
import com.legallic.sudoku.SudokuGetPropertyValues;
import com.legallic.sudoku.exception.SudokuException;
import com.legallic.sudoku.model.Matrix;
import com.legallic.sudoku.service.Solver;


import java.io.*;
//import 

public class  Controler implements ActionListener { 
    Modele modele;
    VueSudoku vue;
    Solver solver;
    Matrix matrix ;
    
    public Controler(Modele modele, VueSudoku vue) {
	this.modele = modele;
	this.vue = vue;
    }
    
    public void actionPerformed(ActionEvent e) {
	if (e.getSource() == vue.trace) modele.setExiste(true);
	//else if (e.getSource() == vue.efface)modele.setExiste(false);
	

	// Traitement pour que les 2 cases à cocher
	// ne soit pas cochées en même temps :

	// Cas ou c'est la case euro qui a généré l'action
	// La case franc prend la valeur inverse de euro
	if (e.getSource() == vue._euro){
		vue._franc.setSelected(!vue._euro.isSelected());
		 modele.setExiste(true);
		 System.out.println("******* vue._euro ******");
	}

	// Cas ou c'est la case franc qui a généré l'action
	// La case euro prend la valeur inverse de franc
	if (e.getSource() == vue._franc) {
		vue._euro.setSelected(!vue._franc.isSelected());
    	modele.setExiste(true);
	    System.out.println("******* vue._franc ******");
	}
	
	
	if (e.getSource() == vue.trace) {
		vue.MajGrille() ;
		
		vue._euro.setSelected(!vue._franc.isSelected());
    	modele.setExiste(true);
	    System.out.println("******* vue.trace ******");
	}
    
	if (e.getSource() == vue.load) {
		System.out.println("******* vue.load ******");
		//SimpleFileChooser sfc = new SimpleFileChooser();
		SudokuGetPropertyValues properties = new SudokuGetPropertyValues();		
		String SudokuFolder = properties.getPropValue("SudokuFolder") ;
		JFileChooser chooser = new JFileChooser(SudokuFolder);
	        chooser.setMultiSelectionEnabled(false);
	        int option = chooser.showOpenDialog(vue.SudokuPanel);
	        if (option == JFileChooser.APPROVE_OPTION) {
	          File sf = chooser.getSelectedFile();
	          System.out.println("******* vue.load " + sf.getName()+ "******");
	          matrix = Initializer.initialize(SudokuFolder + "/" +  sf.getName());
	          
	          
	  		System.out.println("###############################################");
	  		System.out.println("Tentative de resolution de la matrice suivante");
	  		System.out.println("###############################################");
	  		System.out.println(matrix);
	  		vue.setMatrix(matrix);
	  		vue.MajGrille() ;

	        }
	        else {
	          //statusbar.setText("You canceled.");
	          System.out.println("******* vue.load " + "cancelled " + "******");
	        }
    	
		modele.setExiste(true);
	    System.out.println("******* vue.load ******");
	}
	 if (e.getSource() == vue.go) {
			System.out.println("******* vue.go ******");
	  		try {
	  			long startTime = System.currentTimeMillis();
	  			Solver solver = new Solver(matrix);
				System.out.println("Ai-je reussi ? : " + solver.solve());
				System.out.println(solver.getMatrix());
				System.out.println(" En " + (System.currentTimeMillis() - startTime) + " ms !!!!!!!!!!!!! ");
				vue.setMatrix(solver.getMatrix());
				vue.MajGrille() ;
			} catch (SudokuException ex) {
				System.err.println(ex.getMessage());
				ex.printStackTrace();
			}
	 }
	
    }
    public void setMatrix(Matrix matrix) {
		this.matrix = matrix;
	}
}
