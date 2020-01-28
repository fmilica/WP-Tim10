package model;

public class VMResource {

	protected String name;
	
	public VMResource() {}
	
	public VMResource(String name) {
		this.name = name;
	}
	
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
