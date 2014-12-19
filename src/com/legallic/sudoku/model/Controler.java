package com.legallic.sudoku.model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class  Controler implements ActionListener { 
    Modele modele;
    VueSudoku vue;
    
    public Controler(Modele modele, VueSudoku vue) {
	this.modele = modele;
	this.vue = vue;
    }
    
    public void actionPerformed(ActionEvent e) {
	if (e.getSource() == vue.trace) modele.setExiste(true);
	else if (e.getSource() == vue.efface)modele.setExiste(false);
	

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
    }
}