package model;

import model.enums.Tip;

public class Disk {

	private String ime;
	private Tip tip;
	private int kapacitet;
	private String VM;

	public Disk() {}
	
	public Disk(String ime, Tip tip, int kapacitet, String vM) {
		this.ime = ime;
		this.tip = tip;
		this.kapacitet = kapacitet;
		VM = vM;
	}
	
	public String getIme() {
		return ime;
	}
	public void setIme(String ime) {
		this.ime = ime;
	}
	public Tip getTip() {
		return tip;
	}
	public void setTip(Tip tip) {
		this.tip = tip;
	}
	public int getKapacitet() {
		return kapacitet;
	}
	public void setKapacitet(int kapacitet) {
		this.kapacitet = kapacitet;
	}
	public String getVM() {
		return VM;
	}
	public void setVM(String vM) {
		VM = vM;
	}

	@Override
	public String toString() {
		return "Disk [ime=" + ime + ", tip=" + tip + ", kapacitet=" + kapacitet + ", VM=" + VM + "]";
	}	
	
	
}
