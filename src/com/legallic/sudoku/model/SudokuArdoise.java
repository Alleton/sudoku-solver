package com.legallic.sudoku.model;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton; 
import javax.swing.JCheckBox;
import java.awt.*;
//import java.awt.event.*;
import javax.swing.*;

import com.legallic.sudoku.model.Matrix;
import com.legallic.sudoku.service.Solver;

public class SudokuArdoise extends JFrame  {
    static final long serialVersionUID = 1;
    
 // Attributs de la classe
    private JPanel      _panel;
 	private JLabel		_labVal, _labRes;
 	private JTextField	_txtVal, _txtRes;
 	private JCheckBox	_euro, _franc;
 	private JButton	_conv;
 	
    private boolean possedeDisque = true;
    private int MatSize ;
    private Matrix matrix;
    
    public SudokuArdoise(int MatrixSize) {
	
    	Container cont = getContentPane();
    	setBackground(Color.lightGray);
	setPreferredSize(new Dimension(250,250 ));
	this.MatSize = MatrixSize;
	//this.MatSize = matrix.getMatrixSize();
	
	
	Matrix matrix = new Matrix(); 
//getMatrixSize() ;
	cont.setPreferredSize(new Dimension(MatSize * 80, MatSize * 80 ));
	
		} 

    public void SetSize (int MatrixSize){
    	this.setPreferredSize(new Dimension(MatrixSize * 80, MatrixSize * 80 ));
    	//this.setSize(MatrixSize * 50, MatrixSize * 50 ) ;
    }
    
    public void setPossedeDisque(boolean possedeDisque) {
    	this.possedeDisque = possedeDisque;
    }
    
    public void affiche (Graphics g, int row , int col , int chiffre ){
    	//g.haut.setVisible(true);
    	g.setColor(Color.BLACK);	
    	g.drawString(String.valueOf(chiffre), row * 40 + 21, col * 40 + 21);
    	
    }
    
    
    public void dessiner(Graphics g) {
    	
	g.setColor(Color.BLACK);	
	
	for(int rowIndex=0; rowIndex < this.MatSize ; rowIndex++){
		for(int colIndex=0; colIndex < this.MatSize ; colIndex++){
			g.drawRect(rowIndex * 40 + 1, colIndex * 40 + 1,  40 , 40 ) ;
		}
		// On ajoute le panel haut en haut de la feuille
		// et celui du bas en dessous
		//this.haut.setLayout(new GridLayout(3,2));
		//this._conv.setVisible(true) ;
		
		
	}
	
	// Afficher les chiifres
	// matrix Matrix getMatrix() 
   }
    
    public void paintComponent(Graphics g) {
	//super.paintComponent(g);
	if (possedeDisque) dessiner(g);
	affiche ( g, 1 , 1 , 5 );
    }
}


