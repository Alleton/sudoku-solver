package com.legallic.sudoku.model;

//import java.awt.BorderLayout;
import java.awt.*;
//import java.awt.event.*;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.text.NumberFormat;
import javax.swing.text.NumberFormatter;



public class VueSudoku extends JFrame implements Observer {
	final static boolean RIGHT_TO_LEFT = false;
	static final long serialVersionUID = 1;
	Modele modele;
	private Matrix matrix;
	    
	    public JButton trace = new JButton("trace");
	    public JButton go = new JButton("go");
	    public JButton load = new JButton("load");
	    // Attributs de la classe
	    public JPanel      SudokuPanel;
	    private JPanel      GrillePanel ;
	   
	 	public JCheckBox	_euro, _franc;
	 	public JButton	_conv;
	 	NumberFormat numberFormat;
	 	JTextField [][] allField ; 
	 	JButton   [][] allButton;
	 	

	    public VueSudoku(Modele modele ,Matrix matrix) {
		this.modele = modele;
		setTitle("Sudoku");
		this.matrix = matrix ;
		
		
		
		
		System.out.println (this.matrix.toString());
		//SudokuPanel.setLayout(new GridBagLayout());
	    
		
		SudokuPanel = new JPanel();
		//Container cont = getContentPane();

		//container = new Container(SudokuPanel);
		
		
		setPreferredSize(new Dimension(450,450 ));
		
		modele.addObserver(this);

		SudokuPanel.add(trace);
		SudokuPanel.add(go);
		SudokuPanel.add(load);
		add(SudokuPanel, BorderLayout.NORTH);
		//add(ardoise, BorderLayout.CENTER);

		

		// Création des JCheckBox
		_euro 	= new JCheckBox("Euros");
		_franc 	= new JCheckBox("Francs");

		// Euro est cochée, franc ne l'est pas
		_euro.setSelected(false);
		_franc.setSelected(true);

		

		// Création du bouton
		_conv 	= new JButton("Conversion");


		// On ajoute les composants labels, textField et
		// checkBox au panel haut

		SudokuPanel.add(_euro);
		SudokuPanel.add(_franc);
		SudokuPanel.add(_conv);


				
				// new panel for grid
		GrillePanel = new JPanel(new BorderLayout()); //PREFERRED!

		GrillePanel.setLayout(new GridLayout(9,9));
		GrillePanel.setBackground(Color.ORANGE); 
		add(GrillePanel, BorderLayout.SOUTH);
		// 
		// drawRect(int x, int y, int l, int h) ;
		
		numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setMaximumFractionDigits(0);
        NumberFormatter formatter = new NumberFormatter(numberFormat);
        formatter.setAllowsInvalid(false);
        formatter.setCommitsOnValidEdit(true);//seems to be a no-op --
        
        allButton = new JButton [matrix.getMatrixSize()][matrix.getMatrixSize()];
        
		for(int colIndex=0; colIndex < matrix.getMatrixSize(); colIndex++){
			for(int rowIndex=0; rowIndex < matrix.getMatrixSize(); rowIndex++) {
				allButton[colIndex][rowIndex] = new JButton  (String.valueOf(0));
				GrillePanel.add ( allButton[colIndex][rowIndex]) ;
			}
		}
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocation(200,200);
		pack();
		setVisible(true);
	    }
		
	    
       public void MajGrille (){
    	// Mise a jour de toutes les cell de la grille   
   		for(int colIndex=0; colIndex < matrix.getMatrixSize(); colIndex++){
   			for(int rowIndex=0; rowIndex < matrix.getMatrixSize(); rowIndex++) {
   				//
   				MajCell (  colIndex ,  rowIndex) ;
   				//
   				//
   			}
   		}
       }
	    
       public void MajCell ( int colIndex , int rowIndex) {
    	// Mise a jour une  cell de la grille   
    	   Cell cell = matrix.getCells()[colIndex][rowIndex];
				if(!cell.isFound()){
					allButton[colIndex][rowIndex].setText ( " ") ;
				} else {
					allButton[colIndex][rowIndex].setText (String.valueOf(cell.getDefinitiveValue())) ;
				}
       }
       
	    public static void addComponentsToPane(Container pane) {
	        if (RIGHT_TO_LEFT) {
	            pane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
	        }
	 
	    pane.setLayout(new GridBagLayout());
	    //GridBagConstraints c = new GridBagConstraints();
	    }
	    
	    
	    public void LayoutEg1() {
	        setLayout(new GridBagLayout());
	        for (int i = 0; i < 9; i++) {
	            GridBagConstraints gbc = makeGbc(0, i);
	            JLabel label = new JLabel("Row Label " + (i + 1));
	            add(label, gbc);

	            JPanel panel = new JPanel();
	            panel.add(new JCheckBox("check box"));
	            panel.add(new JTextField(10));
	            panel.add(new JButton("Button"));
	            //panel.setBorder(BorderFactory.createEtchedBorder());
	            gbc = makeGbc(1, i);
	            add(panel, gbc);
	        }
	    }
	    
	    private GridBagConstraints makeGbc(int x, int y) {
	        GridBagConstraints gbc = new GridBagConstraints();
	        gbc.gridwidth = 1;
	        gbc.gridheight = 1;
	        gbc.gridx = x;
	        gbc.gridy = y;
	        gbc.weightx = x;
	        gbc.weighty = 1.0;
	        gbc.insets = new Insets(5, 5, 5, 5);
	        gbc.anchor = (x == 0) ? GridBagConstraints.LINE_START : GridBagConstraints.LINE_END;
	        gbc.fill = GridBagConstraints.HORIZONTAL;
	        return gbc;
	    }
	    
	    public void update(Observable o, Object arg) {
		 
	    	this.SudokuPanel.repaint() ;
	    	this.GrillePanel.repaint();
	    	
	    }

	    public void setMatrix ( Matrix matrix) {
	    	this.matrix = matrix ;
	    }
	    
	}

