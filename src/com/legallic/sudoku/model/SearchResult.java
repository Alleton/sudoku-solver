package com.legallic.sudoku.model;

public class SearchResult {

	private boolean found = false;
	private boolean modified = false;

	public SearchResult() {
	}
	
	public SearchResult(boolean found, boolean modified) {
		this.found = found;
		this.modified = modified;
	}
	
	public boolean isFound() {
		return found;
	}
	public void setFound(boolean found) {
		this.found = found;
	}
	public boolean isModified() {
		return modified;
	}
	public void setModified(boolean modified) {
		this.modified = modified;
	}
}
