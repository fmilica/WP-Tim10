package model;

import model.enums.DiscType;

public class Disc extends VMResource {

	private DiscType type;
	private int capacity;
	private String vm;
	
	public Disc() {}

	public Disc(String name, DiscType type, int capacity) {
		super(name);
		this.type = type;
		this.capacity = capacity;
	}
	
	public Disc(String name, String organisation, 
			DiscType type, int capacity, String vm) {
		super(name, organisation);
		this.type = type;
		this.capacity = capacity;
		this.vm = vm;
	}

	@Override
	public String toString() {
		return "Disc [type=" + type + ", capacity=" + capacity + ", vm=" + vm + ", name=" + name + ", organisation="
				+ organisation + "]";
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
	
	public boolean hasNull() {
		if(this.name == null || this.name.trim().length() == 0) {
			return true;
		}
		else if(this.organisation == null || this.organisation.trim().length() == 0) {
			return true;
		}
		/*else if(this.type == null) {
			return true;
		}*/
		else if(this.capacity <= 0) {
			return true;
		}
		else if(this.vm == null || this.vm.trim().length() == 0) {
			return true;
		}
		return false;
	}
}
