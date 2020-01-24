package model;

import java.util.Collection;

public class VirtuelnaMasina extends Resurs {

	private Kategorija kategorija;
	private int brojJezgara;
	private int RAM;
	private int GPU;
	private Collection<Disk> diskovi;
	private Collection<Aktivnost> aktivnosti;
	
	public VirtuelnaMasina() {}
	
	public VirtuelnaMasina(Kategorija kategorija, int brojJezgara, int rAM, int gPU, Collection<Disk> diskovi,
			Collection<Aktivnost> aktivnosti) {
		this.kategorija = kategorija;
		this.brojJezgara = brojJezgara;
		RAM = rAM;
		GPU = gPU;
		this.diskovi = diskovi;
		this.aktivnosti = aktivnosti;
	}

	public Kategorija getKategorija() {
		return kategorija;
	}
	public void setKategorija(Kategorija kategorija) {
		this.kategorija = kategorija;
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
	public Collection<Disk> getDiskovi() {
		return diskovi;
	}
	public void setDiskovi(Collection<Disk> diskovi) {
		this.diskovi = diskovi;
	}
	public Collection<Aktivnost> getAktivnosti() {
		return aktivnosti;
	}
	public void setAktivnosti(Collection<Aktivnost> aktivnosti) {
		this.aktivnosti = aktivnosti;
	}

	@Override
	public String toString() {
		return "VirtuelnaMasina [kategorija=" + kategorija + ", brojJezgara=" + brojJezgara + ", RAM=" + RAM + ", GPU="
				+ GPU + ", diskovi=" + diskovi + ", aktivnosti=" + aktivnosti + "]";
	}
}
