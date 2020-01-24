package model;

import java.util.Collection;

public class Organizacija {

	private String ime;
	private String opis;
	private String logo;
	private Collection<String> korisnici;
	private Collection<Resurs> resursi;
	
	public Organizacija() {}
	
	public Organizacija(String ime, String opis, String logo, Collection<String> korisnici,
			Collection<Resurs> resursi) {
		this.ime = ime;
		this.opis = opis;
		this.logo = logo;
		this.korisnici = korisnici;
		this.resursi = resursi;
	}
	
	public String getIme() {
		return ime;
	}
	public void setIme(String ime) {
		this.ime = ime;
	}
	public String getOpis() {
		return opis;
	}
	public void setOpis(String opis) {
		this.opis = opis;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public Collection<String> getKorisnici() {
		return korisnici;
	}
	public void setKorisnici(Collection<String> korisnici) {
		this.korisnici = korisnici;
	}
	public Collection<Resurs> getResursi() {
		return resursi;
	}
	public void setResursi(Collection<Resurs> resursi) {
		this.resursi = resursi;
	}

	@Override
	public String toString() {
		return "Organizacija [ime=" + ime + ", opis=" + opis + ", logo=" + logo + ", korisnici=" + korisnici
				+ ", resursi=" + resursi + "]";
	}
}
