package model;

import model.enums.DiscType;

public class Disc extends VMResource {

	private String name;
	private DiscType type;
	private int capacity;
	private String VM;
	
	public Disc() {}

	public Disc(String name, DiscType type, int capacity, String vM) {
		this.name = name;
		this.type = type;
		this.capacity = capacity;
		VM = vM;
	}
	
	@Override
	public String toString() {
		return "Disc [name=" + name + ", capacity=" + capacity + ", VM=" + VM + "]";
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public DiscType getType() {
		return type;
	}
	public void setType(DiscType type) {
		this.type = type;
	}
	public int getCapacity() {
		return capacity;
	}
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	public String getVM() {
		return VM;
	}
	public void setVM(String vM) {
		VM = vM;
	}
}
