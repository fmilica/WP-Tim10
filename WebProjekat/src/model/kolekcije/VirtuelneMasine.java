package model.kolekcije;

import java.util.HashMap;

import model.VirtuelnaMasina;

public class VirtuelneMasine {

	private HashMap<String, VirtuelnaMasina> virtuelneMasine;

	public VirtuelneMasine() {}

	public VirtuelneMasine(HashMap<String, VirtuelnaMasina> virtuelneMasine) {
		this.virtuelneMasine = virtuelneMasine;
	}
	
	public HashMap<String, VirtuelnaMasina> getVirtuelneMasine() {
		return virtuelneMasine;
	}
	public void setVirtuelneMasine(HashMap<String, VirtuelnaMasina> virtuelneMasine) {
		this.virtuelneMasine = virtuelneMasine;
	}
}
