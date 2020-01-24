package model;

public class Kategorija {

	private String ime;
	private int brojJezgara;
	private int RAM;
	private int GPU;
	
	public Kategorija() {}
	
	public Kategorija(String ime, int brojJezgara, int rAM, int gPU) {
		this.ime = ime;
		this.brojJezgara = brojJezgara;
		RAM = rAM;
		GPU = gPU;
	}

	@Override
	public String toString() {
		return "Kategorija [ime=" + ime + ", brojJezgara=" + brojJezgara + ", RAM=" + RAM + ", GPU=" + GPU + "]";
	}

	public String getIme() {
		return ime;
	}
	public void setIme(String ime) {
		this.ime = ime;
	}
	public int getBrojJezgara() {
		return brojJezgara;
	}
	public void setBrojJezgara(int brojJezgara) {
		this.brojJezgara = brojJezgara;
	}
	public int getRAM() {
		return RAM;
	}
	public void setRAM(int rAM) {
		RAM = rAM;
	}
	public int getGPU() {
		return GPU;
	}
	public void setGPU(int gPU) {
		GPU = gPU;
	}
}
