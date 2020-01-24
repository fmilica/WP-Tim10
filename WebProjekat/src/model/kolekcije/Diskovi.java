package model.kolekcije;

import java.util.HashMap;

import model.Disk;

public class Diskovi {

	private HashMap<String, Disk> diskovi;
	
	public Diskovi() {}

	public Diskovi(HashMap<String, Disk> diskovi) {
		this.diskovi = diskovi;
	}

	public HashMap<String, Disk> getDiskovi() {
		return diskovi;
	}

	public void setDiskovi(HashMap<String, Disk> diskovi) {
		this.diskovi = diskovi;
	}
}
