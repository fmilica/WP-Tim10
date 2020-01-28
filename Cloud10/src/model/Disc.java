package model;

import model.enums.DiscType;

public class Disc extends VMResource {

	private DiscType type;
	private int capacity;
	private String vm;
	
	public Disc() {}

	public Disc(String name, DiscType type, int capacity, String vm) {
		super(name);
		this.type = type;
		this.capacity = capacity;
		this.vm = vm;
	}
	
	@Override
	public String toString() {
		return "Disc [name=" + name + ", capacity=" + capacity + ", VM=" + vm + "]";
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
	public String getVm() {
		return vm;
	}
	public void setVm(String vm) {
		this.vm = vm;
	}
}
