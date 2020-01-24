package model.kolekcije;

import java.util.HashMap;

import model.Korisnik;

public class Korisnici {

	private HashMap<String, Korisnik> korisnici;

	public Korisnici() {}
	
	public Korisnici(HashMap<String, Korisnik> korisnici) {
		this.korisnici = korisnici;
	}

	public HashMap<String, Korisnik> getKorisnici() {
		return korisnici;
	}

	public void setKorisnici(HashMap<String, Korisnik> korisnici) {
		this.korisnici = korisnici;
	}
}
