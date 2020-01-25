package model.collections;

import java.util.HashMap;

import model.Disc;

public class Discs {

	private HashMap<String, Disc> discsMap;
	
	public Discs() {}
	
	public Discs(String filePath) {}

	public Discs(HashMap<String, Disc> discsMap) {
		this.discsMap = discsMap;
	}

	public HashMap<String, Disc> getDiscsMap() {
		return discsMap;
	}
	public void setDiscsMap(HashMap<String, Disc> discsMap) {
		this.discsMap = discsMap;
	}
}
