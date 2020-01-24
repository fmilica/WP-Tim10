package model;

public abstract class Resurs {

	private String ime;

	public String getIme() {
		return ime;
	}
	public void setIme(String ime) {
		this.ime = ime;
	}
	@Override
	public String toString() {
		return "Resurs [ime=" + ime + "]";
	}	
}
