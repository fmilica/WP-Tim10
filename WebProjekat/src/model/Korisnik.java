package model;

import model.enums.Uloga;

public class Korisnik {

	private String email;
	private String lozinka;
	private String ime;
	private String prezime;
	private Organizacija organizacija;
	private Uloga uloga;
	
	public Korisnik() {}
	
	public Korisnik(String email, String lozinka) {
		this.email = email;
		this.lozinka =lozinka;
	}
	
	public Korisnik(String email, String lozinka, String ime, String prezime, Organizacija organizacija, Uloga uloga) {
		this.email = email;
		this.lozinka = lozinka;
		this.ime = ime;
		this.prezime = prezime;
		this.organizacija = organizacija;
		this.uloga = uloga;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLozinka() {
		return lozinka;
	}

	public void setLozinka(String lozinka) {
		this.lozinka = lozinka;
	}

	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public String getPrezime() {
		return prezime;
	}

	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}

	public Organizacija getOrganizacija() {
		return organizacija;
	}

	public void setOrganizacija(Organizacija organizacija) {
		this.organizacija = organizacija;
	}

	public Uloga getUloga() {
		return uloga;
	}

	public void setUloga(Uloga uloga) {
		this.uloga = uloga;
	}

	@Override
	public String toString() {
		return "Korisnik [email=" + email + ", lozinka=" + lozinka + ", ime=" + ime + ", prezime=" + prezime
				+ ", organizacija=" + organizacija + ", uloga=" + uloga + "]";
	}
	
	
}
