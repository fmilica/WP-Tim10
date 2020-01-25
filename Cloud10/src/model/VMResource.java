package model;

public abstract class VMResource {

	private String name;
	
	@Override
	public String toString() {
		return "VMResource [name=" + name + "]";
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
