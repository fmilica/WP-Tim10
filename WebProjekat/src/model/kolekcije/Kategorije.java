package model.kolekcije;

import java.util.HashMap;

import model.Kategorija;

public class Kategorije {

	private HashMap<String, Kategorija> kategorije;

	public Kategorije() {}
	
	public Kategorije(HashMap<String, Kategorija> kategorije) {
		this.kategorije = kategorije;
	}

	public HashMap<String, Kategorija> getKategorije() {
		return kategorije;
	}

	public void setKategorije(HashMap<String, Kategorija> kategorije) {
		this.kategorije = kategorije;
	}
}
