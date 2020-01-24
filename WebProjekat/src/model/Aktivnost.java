package model;

import java.util.Date;

public class Aktivnost {

	private Date vremePaljenja;
	private Date vremeGasenja;

	public Aktivnost() {}
	
	public Aktivnost(Date vremePaljenja, Date vremeGasenja) {
		this.vremePaljenja = vremePaljenja;
		this.vremeGasenja = vremeGasenja;
	}
	
	public Date getVremePaljenja() {
		return vremePaljenja;
	}
	public void setVremePaljenja(Date vremePaljenja) {
		this.vremePaljenja = vremePaljenja;
	}
	public Date getVremeGasenja() {
		return vremeGasenja;
	}
	public void setVremeGasenja(Date vremeGasenja) {
		this.vremeGasenja = vremeGasenja;
	}

	@Override
	public String toString() {
		return "Aktivnost [vremePaljenja=" + vremePaljenja + ", vremeGasenja=" + vremeGasenja + "]";
	}
	
	
}
