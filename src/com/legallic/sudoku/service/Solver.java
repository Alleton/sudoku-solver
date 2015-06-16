package com.legallic.sudoku.service;

import java.util.ArrayList;
import java.util.List;

import com.legallic.sudoku.exception.SudokuException;
import com.legallic.sudoku.exception.SudokuNoPossibleValueException;
import com.legallic.sudoku.model.Cell;
import com.legallic.sudoku.model.Coordinate;
import com.legallic.sudoku.model.Matrix;
import com.legallic.sudoku.model.SearchResult;

public class Solver {
	
	private Matrix matrix;
	private int totalFound = 0;
	
	/**
	 * Initialize a solver with a matrix
	 * @param matrix
	 */
	public Solver(Matrix matrix) {
		this.matrix = matrix;
	}

	/**
	 * Launch solving process on the matrix
	 * @return
	 * @throws SudokuException
	 */
	public boolean solve() throws SudokuException{
		
		boolean isSolved = false; 
		int currentIteration = 0;
		
		// Non heuristic and heuristic methods
		while(!isSolved){
			
			System.out.println("#### ITERATION " + currentIteration + " ####");
			
			// First pass
			isSolved = solveNoHeuristic();
			if(isSolved) break;
			
			// Second pass (heuristic) if not solved with first pass
			if(!isSolved){
				boolean modified = solveFirstHeuristic();
				
				// if not modifications made with heuristic, then get out of this step
				if(!modified){
					break;
				}
			}
			currentIteration++;
		}
		
		// Now, we can only make hypothesys if still not solved..
		if(!isSolved && !solveWithHypothesys()){
			solve();
		}
		
		// We can only reach this step if solved...
		return true;
	}
	
	private boolean solveWithHypothesys() throws SudokuException{
		System.out.println("");
		System.out.println("******* Nouvelle hypothese ******");
		System.out.println("");
		Cell cellForHypothesys = null;
		int lastMinValue = matrix.getMatrixSize();
		for(int rowIndex=0; rowIndex < matrix.getMatrixSize(); rowIndex++){
			for(int colIndex=0; colIndex < matrix.getMatrixSize(); colIndex++){
				Cell cell = matrix.getCells()[rowIndex][colIndex];
				int numberOfPossibleValue = numberOfPossibleValues(cell);
				if(numberOfPossibleValue == 1){
					throw new SudokuException();
				}
				if(numberOfPossibleValue > 1 && numberOfPossibleValue <= lastMinValue){
					cellForHypothesys = cell;
					lastMinValue = numberOfPossibleValue;
				}
			}
		}

		int hypothesysValue = firstPossibleValue(cellForHypothesys);
		System.out.println("Nous allons faire une hypothese sur la cellule (" + cellForHypothesys.getRowIndex() 
				+ "," + cellForHypothesys.getColIndex() + ") avec la valeur " 
				+ hypothesysValue
				);
		
		Matrix hypothesysMatrix = (Matrix) matrix.clone();
		Solver hypothesysSolver = new Solver(hypothesysMatrix);
		hypothesysSolver.setCellIsFound(hypothesysMatrix.getCells()[cellForHypothesys.getRowIndex()][cellForHypothesys.getColIndex()], hypothesysValue);
		boolean hypothesysSolved = false;
		try{
			hypothesysSolved = hypothesysSolver.solve();
		}
		catch(SudokuNoPossibleValueException noPossibleValueException){
			// hypothesysSolved reste à false
		}
		if(!hypothesysSolved){
			System.out.println("");
			System.out.println("******* Hypothese non verifiee :( ******");
			System.out.println("******* On supprime cette valeur possible et on recommence ! ******");
			System.out.println("");			
			//on définit à false la possibilité que cette valeur soit bonne
			cellForHypothesys.getPossibleValues()[hypothesysValue] = false;
		}
		else{
			System.out.println("");
			System.out.println("******* Hypothese verifiee ! ******");
			System.out.println("");					
			this.matrix = hypothesysMatrix;
		}
		return hypothesysSolved;
	}
	
	private boolean solveNoHeuristic() throws SudokuException{
			
		boolean modified = true;
		int numberOfPass = 0;
		
		// Premiere passe sans hypotheses
		while(modified){
			modified = false;
			
			System.out.println("- Recherche des valeurs possibles");
			totalFound = 0;
			for(int rowIndex=0; rowIndex < matrix.getMatrixSize(); rowIndex++){
				for(int colIndex=0; colIndex < matrix.getMatrixSize(); colIndex++){
					// ON analyse la cellule
					Cell cell = matrix.getCells()[rowIndex][colIndex];
					// Si la cellule n'a pas encore de valeur
					if(!cell.isFound()){
						// On cherche une valeur possible
						SearchResult result = searchPossibleValues(matrix.getCells()[rowIndex][colIndex], rowIndex, colIndex);
						modified = modified || result.isModified();
					}
					if(cell.isFound()){
						totalFound++;
						if(totalFound == matrix.getMatrixSize() * matrix.getMatrixSize()){
							System.out.println(matrix);
							return true;
						}
					}
				}
			}
			System.out.println(matrix);
			
			System.out.println("- Recherche des valeurs necessaires");
			totalFound = 0;
			for(int rowIndex=0; rowIndex < matrix.getMatrixSize(); rowIndex++){
				for(int colIndex=0; colIndex < matrix.getMatrixSize(); colIndex++){
					// ON analyse la cellule
					Cell cell = matrix.getCells()[rowIndex][colIndex];
					// Si la cellule n'a pas encore de valeur
					if(!cell.isFound()){
						// On cherche une valeur possible
						SearchResult result = searchNecessaryValues(matrix.getCells()[rowIndex][colIndex], rowIndex, colIndex);
						modified = modified || result.isModified();
					}
					if(cell.isFound()){
						totalFound++;
						if(totalFound == matrix.getMatrixSize() * matrix.getMatrixSize()){
							return true;
						}
					}
				}
			}			
			System.out.println(matrix);
			System.out.println("Pass number : " + ++numberOfPass);
			System.out.println("");
		}
		return false;
	}
	
	/**
	 * Si sur une ligne/col/square il existe seulement deux possibilité pour un chiffre, on teste si d'autres chiffres ont les mêmes deux seules possibilités.
	 * Si c'est le cas, on supprime les autres pour ces deux cases possibles
	 * @return true si des valeurs possibles ont été modifiées
	 */
	private boolean solveFirstHeuristic(){

		System.out.print("- Recherche heuristique : ");

		boolean modified = false;

		SearchResult searchResult;
		
		// Squares
		for(int squareIndex = 0; squareIndex < matrix.getMatrixSize(); squareIndex++){
			//System.out.println("Recherche pour le square " + squareIndex);
			searchResult = searchHeuristic(getSquareTopCoordinate(squareIndex), getSquareEndCoordinate(squareIndex));
			if(searchResult.isModified()){
				//System.out.println("Couple trouvé++++++");
				modified = true;
			}
		}
		
		// rows
		for(int rowIndex=0; rowIndex < matrix.getMatrixSize(); rowIndex++){
			searchResult = searchHeuristic(new Coordinate(rowIndex, 0), new Coordinate(rowIndex, matrix.getMatrixSize() -1));
			if(searchResult.isModified()){
				//System.out.println("Couple trouvé++++++");
				modified = true;
			}			
		}

		// columns
		for(int colIndex=0; colIndex < matrix.getMatrixSize(); colIndex++){
			searchResult = searchHeuristic(new Coordinate(0, matrix.getMatrixSize()), new Coordinate(matrix.getMatrixSize() -1, colIndex));
			if(searchResult.isModified()){
				//System.out.println("Couple trouvé++++++");
				modified = true;
			}	
		}
		if(modified)
			System.out.println("La recherche heuristique a permis de reduire le champs des possibles...");
		else
			System.out.println("La recherche n'a rien donnee...");
		return modified;
	}
	
	/**
	 * Parcours la zone pour y trouver deux possibilite maximum pour un chiffre
	 * @param startCoordinate
	 * @param endCoordinate
	 * @return
	 */
	private  SearchResult searchHeuristic(Coordinate startCoordinate, Coordinate endCoordinate){
		
		boolean modified = false;
		
		// on boucle sur les chiffres pour trouver deux possibilites
		for (int i = 1; i < matrix.getMatrixSize(); i++) {
			//System.out.println("Recherche des premiers couples pour la valeur " + i);
			List<Coordinate> firstPossibleSet = computeNumberOfPossible(startCoordinate, endCoordinate, i);
			
			// Si on a deux possibilites et que ce sont les memes
			if(firstPossibleSet.size() == 2){
				//System.out.println("Premier couple trouve : recherche des deuxiemes couples pour la valeur " + i);
				for (int j = i + 1; j <= matrix.getMatrixSize(); j++) {
					List<Coordinate> secondPossibleSet = computeNumberOfPossible(startCoordinate, endCoordinate, j);
					if(secondPossibleSet.size() == 2){
						// Si ce sont les memes cases, alors on les tagge
						if(firstPossibleSet.get(0).equals(secondPossibleSet.get(0)) && 
							firstPossibleSet.get(1).equals(secondPossibleSet.get(1))
								){
							modified = modified || tagCell(firstPossibleSet.get(0), i, j);
							modified = modified || tagCell(firstPossibleSet.get(1), i, j);
							//System.out.println("Got it ! Two possibles : " + firstPossibleSet.get(0) + " " + firstPossibleSet.get(1) + " " + i + " " + j );
						}
					}
				}
			}
		}
		return new SearchResult(false,modified);
	}
	
	/**
	 * Conserve uniquement les deux valeurs possibles passées en paramètre pour la cellule passée en paramètre
	 * @param cellCoordinate
	 * @param firstPossibleValue
	 * @param secondPossibleValue
	 * @return true si un changement a été effectué dans la liste des valeurs possible pour cette cellule
	 */
	private boolean tagCell(Coordinate cellCoordinate, int firstPossibleValue, int secondPossibleValue){
		boolean modified = false;
		Cell cell = matrix.getCells()[cellCoordinate.getRowIndex()][cellCoordinate.getColIndex()];
		boolean[] possibleValues = cell.getPossibleValues();
		for (int i = 1; i < possibleValues.length; i++) {
			if(i != firstPossibleValue && i != secondPossibleValue && possibleValues[i]){
				possibleValues[i] = false;
				modified = true;
			}
		}
		return modified;
	}
	
	/**
	 * Determine une liste de coordonnée de cellules qui ont comme possible la valeur target
	 * @param startCoordinate
	 * @param endCoordinate
	 * @param targetValue
	 * @return
	 */
	private List<Coordinate> computeNumberOfPossible(Coordinate startCoordinate, Coordinate endCoordinate, int targetValue){
		List<Coordinate> possibleCoordinates = new ArrayList<Coordinate>();
		for(int rowIndex=startCoordinate.getRowIndex(); rowIndex <= endCoordinate.getRowIndex(); rowIndex++){
			for(int colIndex=startCoordinate.getColIndex(); colIndex <= endCoordinate.getColIndex(); colIndex++){
				if(matrix.getCells()[rowIndex][colIndex].getPossibleValues()[targetValue]) {
					possibleCoordinates.add(new Coordinate(rowIndex,colIndex));
				}
			}
		}
		return possibleCoordinates;
	}
	
	/**
	 * Pour chaque case non trouvée, on recherche pour chacune des valeurs possibles de la cellule passée en paramètre, 
	 * si elle n'est pas également possible pas déjà ailleurs (ligne / colonne ou carré)
	 * @param cell
	 * @param rowIndex
	 * @param colIndex
	 * @return
	 * @throws SudokuException
	 */
	private SearchResult searchNecessaryValues(Cell analyzedCell, int rowIndex, int colIndex) throws SudokuException{
		boolean[] possibleValues = analyzedCell.getPossibleValues();		
		boolean iIsANecessaryValue;
		boolean modified = false;

		// Recherche les valeurs possibles détectée précedement
		for (int i = 1; i < possibleValues.length; i++) {
			
			// Si i est une valeur possible
			if(possibleValues[i]){
				
				boolean samePossibleInSameSquare = false;
				iIsANecessaryValue = true;				
				// On analyse la ligne
				for(int currentColIndex=0; currentColIndex < matrix.getMatrixSize(); currentColIndex++){
					Cell currentCell = matrix.getCells()[rowIndex][currentColIndex];
					// Si on est sur la cellule actuellement étudié, elle n'est pas trouvée, donc écartée ici
					if(!currentCell.isFound() && currentColIndex != analyzedCell.getColIndex()){
						// Si i est une valeur possible de la cellule courante, on regarde si elles sont dans le meme square
						if(currentCell.getPossibleValues()[i]){
							iIsANecessaryValue = false;
							if(currentCell.getSquareIndex() == analyzedCell.getSquareIndex()){
								samePossibleInSameSquare = true;
							}
							else{
								samePossibleInSameSquare = false;
								break;
							}
						}
					}
				}	
				if(iIsANecessaryValue){
					setCellIsFound(analyzedCell, i);
					return new SearchResult(true, true);
				}
				if(samePossibleInSameSquare){
					modified = unsetPossibleValueForSquare(i, analyzedCell.getSquareIndex(), rowIndex, -1);
				}

				samePossibleInSameSquare = false;
				iIsANecessaryValue = true;
				// On analyse la colonne
				for(int currentRowIndex=0; currentRowIndex < matrix.getMatrixSize(); currentRowIndex++){
					Cell currentCell = matrix.getCells()[currentRowIndex][colIndex];
					// Si on est sur la cellule actuellement étudié, elle n'est pas trouvée, donc écartée ici
					if(!currentCell.isFound() && currentRowIndex != analyzedCell.getRowIndex()){
						// Si i est une valeur possible de la cellule courante, alors on va pas plus loin
						if(currentCell.getPossibleValues()[i]){
							iIsANecessaryValue = false;
							if(currentCell.getSquareIndex() == analyzedCell.getSquareIndex()){
								samePossibleInSameSquare = true;
							}
							else{
								samePossibleInSameSquare = false;
								break;
							}
						}
					}
				}	
				if(iIsANecessaryValue){
					setCellIsFound(analyzedCell, i);
					return new SearchResult(true, true);
				}
				if(samePossibleInSameSquare){
					modified = unsetPossibleValueForSquare(i, analyzedCell.getSquareIndex(), -1, colIndex);
				}				
			
				// Selon JF, analyser la ligne et la colonne induit d'avoir analysé le carré (mais moi je suis dubitatif)				
			}
		}
		return new SearchResult(false, modified);
	}

	
	
	
	/** 
	 * searchPossibleValues in alll 3 zones
	 * @param cell
	 * @param rowIndex
	 * @param colIndex
	 * @return
	 * @throws SudokuException
	 */
	
	
	private SearchResult searchPossibleValues(Cell cell, int rowIndex, int colIndex) throws SudokuException{
		boolean modified=false;
		SearchResult searchResult = searchValueRow(cell, rowIndex);
		if(searchResult.isFound()){
			return searchResult;
		}
		modified = searchResult.isModified();
		searchResult = searchValueColumn(cell, colIndex);
		modified = modified && searchResult.isModified();
		if(searchResult.isFound()){
			return new SearchResult(true, modified);
		}
		searchResult = searchValueSquare(cell);
		if(searchResult.isFound()){
			return new SearchResult(true, modified);
		}		
		else 
			return new SearchResult(false, modified && searchResult.isModified());
	}
	
	private SearchResult searchValueColumn(Cell analyzedCell, int colIndex) throws SudokuException{
		SearchResult searchResult = new SearchResult();
		for(int rowIndex=0; rowIndex < matrix.getMatrixSize(); rowIndex++){
			Cell currentCell = matrix.getCells()[rowIndex][colIndex];
			// Si on est sur la cellule actuellement étudié, elle n'est pas trouvée, donc écartée ici
			if(currentCell.isFound() && analyzedCell.getPossibleValues()[currentCell.getDefinitiveValue()]){
				analyzedCell.getPossibleValues()[currentCell.getDefinitiveValue()] = false;
				searchResult.setModified(true);
			}
		}
		boolean valueFound = definitiveValueFound(analyzedCell);
		if(valueFound){
			searchResult.setFound(true);
		}
		return searchResult;
	}
	
	private SearchResult searchValueRow(Cell analyzedCell, int rowIndex) throws SudokuException{
		SearchResult searchResult = new SearchResult();
		for(int colIndex=0; colIndex < matrix.getMatrixSize(); colIndex++){
			Cell currentCell = matrix.getCells()[rowIndex][colIndex];
			// Si on est sur la cellule actuellement étudié, elle n'est pas trouvée, donc écartée ici
			if(currentCell.isFound() && analyzedCell.getPossibleValues()[currentCell.getDefinitiveValue()]){
				analyzedCell.getPossibleValues()[currentCell.getDefinitiveValue()] = false;
				searchResult.setModified(true);
			}
		}
		boolean valueFound = definitiveValueFound(analyzedCell);
		if(valueFound){
			searchResult.setFound(true);
		}
		return searchResult;		
	}
	
	private SearchResult searchValueSquare(Cell analyzedCell) throws SudokuException {
		SearchResult searchResult = new SearchResult();
		Coordinate squareTop = getSquareTop(analyzedCell);
		// TODO : dynamic matrix size
		for(int rowIndex = squareTop.getRowIndex(); rowIndex <= squareTop.getRowIndex() + 2; rowIndex++){
			for(int colIndex = squareTop.getColIndex(); colIndex <= squareTop.getColIndex() + 2; colIndex++){
				Cell currentCell = matrix.getCells()[rowIndex][colIndex];
				// Si on est sur la cellule actuellement étudié, elle n'est pas trouvée, donc écartée ici
				if(currentCell.isFound() && analyzedCell.getPossibleValues()[currentCell.getDefinitiveValue()]){
					analyzedCell.getPossibleValues()[currentCell.getDefinitiveValue()] = false;
					searchResult.setModified(true);		
				}
			}
		}
		boolean valueFound = definitiveValueFound(analyzedCell);
		if(valueFound){
			searchResult.setFound(true);
		}
		return searchResult;	
	}
	
	/**
	 * SquareTop = coin en haut à gauche
	 * @param cell
	 * @return
	 */
	private Coordinate getSquareTop(Cell cell){
		int squareRowIndex = (cell.getSquareIndex() / 3) * 3;
		int squareColIndex = (cell.getSquareIndex() % 3) * 3;
		return new Coordinate(squareRowIndex, squareColIndex);
	}
	
	private Coordinate getSquareTopCoordinate(int squareIndex){
		int squareRowIndex = (squareIndex / 3) * 3;
		int squareColIndex = (squareIndex % 3) * 3;
		return new Coordinate(squareRowIndex, squareColIndex);
	}
	
	private Coordinate getSquareEndCoordinate(int squareIndex){
		int squareRowIndex = (squareIndex / 3) * 3 + 2;
		int squareColIndex = (squareIndex % 3) * 3 + 2;
		return new Coordinate(squareRowIndex, squareColIndex);
	}	
	
	private void unsetPossibleValue(Cell cell){
		
		// Remove all possible values for this cell
		for(int i = 0; i <= matrix.getMatrixSize(); i++){
			cell.getPossibleValues()[i] = false;
		}
		
		// Remove cell value from possible value for all cells in line/row/square 
		for (int i = 0; i < matrix.getMatrixSize(); i++) {
			for (int j = 0; j < matrix.getMatrixSize(); j++) {
				Cell currentCell = matrix.getCells()[i][j];
				if(i== cell.getRowIndex() 
					|| j==cell.getColIndex()
					|| matrix.getCells()[i][j].getSquareIndex() ==  cell.getSquareIndex()
					){
					currentCell.getPossibleValues()[cell.getDefinitiveValue()] = false;
				}
			}
		}
	}
	
	/**
	 * Remove any possible value that are not in the same line/col when it's necessary in this line/col
	 * @param possibleValueToRemove
	 * @param squareIndex
	 * @param rowIndex
	 * @param colIndex
	 */
	private boolean unsetPossibleValueForSquare(int possibleValueToRemove, int squareIndex, int rowIndex, int colIndex){
		boolean modified = false;
		for (int i = getSquareTopCoordinate(squareIndex).getRowIndex(); i <= getSquareEndCoordinate(squareIndex).getRowIndex(); i++) {
			for (int j = getSquareTopCoordinate(squareIndex).getColIndex(); j <= getSquareEndCoordinate(squareIndex).getColIndex(); j++) {
				Cell cell = matrix.getCells()[i][j];
				if((colIndex == -1 && cell.getRowIndex() != rowIndex)
						|| (rowIndex == -1 && cell.getColIndex() != colIndex)){
					if(cell.getPossibleValues()[possibleValueToRemove]){
						cell.getPossibleValues()[possibleValueToRemove] = false;
						modified = true;
					}
				}
			}
		}
		if(modified){
			/*System.out.println("unsetPossibleValueForSquare. "
				+ "Possible value = " + possibleValueToRemove
				+ ", SquareIndex = " + squareIndex
				+ ", rowIndex = " + rowIndex
				+ ", colIndex = " + colIndex);*/
		}
		return modified;
	}
		
	private boolean definitiveValueFound(Cell cell) throws SudokuException{
		if(cell.isFound()){
			return true;
		}
		boolean[] possibleValues = cell.getPossibleValues();
		int numberOfPossibleValue = 0; 
		int possibleValueLastIndex = 0;
		for (int i = 1; i < possibleValues.length; i++) {
			boolean possibleValue = possibleValues[i];
			if(possibleValue) {
				numberOfPossibleValue++;
				possibleValueLastIndex = i;
			}
		}
		if(numberOfPossibleValue == 0)
			throw new SudokuNoPossibleValueException(cell);
		if(numberOfPossibleValue == 1){
			setCellIsFound(cell, possibleValueLastIndex);
			return true;
		}
		return false;
	}
	
	private int numberOfPossibleValues(Cell cell){
		boolean[] possibleValues = cell.getPossibleValues();
		int numberOfPossibleValues = 0;
		for (int i = 1; i < possibleValues.length; i++) {
			if(possibleValues[i])
				numberOfPossibleValues++;
		}
		return numberOfPossibleValues;
	}
	
	private int firstPossibleValue(Cell cell) throws SudokuException{
		boolean[] possibleValues = cell.getPossibleValues();
		for (int i = 1; i < possibleValues.length; i++) {
			if(possibleValues[i])
				return i;
		}
		throw new SudokuException();
	}
	
	private int lastPossibleValue(Cell cell) throws SudokuException{
		boolean[] possibleValues = cell.getPossibleValues();
		for (int i = possibleValues.length - 1; i > 0; i--) {
			if(possibleValues[i])
				return i;
		}
		throw new SudokuException();
	}	
	
	public void setCellIsFound(Cell cell, int value){
		cell.setDefinitiveValue(value);
		cell.setFound(true);	
		unsetPossibleValue(cell);
		System.out.println(">>>>> Valeur " + value + " trouvee pour la cellule (" + 
				cell.getRowIndex() + "," + 
				cell.getColIndex() + ")");
	}

	public Matrix getMatrix() {
		return matrix;
	}

	public void setMatrix(Matrix matrix) {
		this.matrix = matrix;
	}
}
