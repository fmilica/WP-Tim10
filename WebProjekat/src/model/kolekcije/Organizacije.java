package model.kolekcije;

import java.util.HashMap;

import model.Organizacija;

public class Organizacije {

	private HashMap<String, Organizacija> organizacije;

	public Organizacije() {}
	
	public Organizacije(HashMap<String, Organizacija> organizacije) {
		this.organizacije = organizacije;
	}

	public HashMap<String, Organizacija> getOrganizacije() {
		return organizacije;
	}

	public void setOrganizacije(HashMap<String, Organizacija> organizacije) {
		this.organizacije = organizacije;
	}
}
