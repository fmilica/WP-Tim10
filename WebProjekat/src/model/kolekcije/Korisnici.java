package model.kolekcije;

import java.util.HashMap;

import model.Korisnik;
import model.Organizacija;
import model.enums.Uloga;

public class Korisnici {

	private HashMap<String, Korisnik> korisnici;

	public Korisnici() {
		Korisnik k = new Korisnik("sta@uns.ac.rs", "sad", "Eva", "Sef", new Organizacija(), Uloga.SuperAdmin);
		korisnici.put(k.getEmail(), k);
	}
	
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
