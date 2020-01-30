package model;

public class VMResource {

	protected String name;
	protected String organisation;
	
	public VMResource() {}
	
	public VMResource(String name) {
		this.name = name;
	}
	
	public VMResource(String name, String organisation) {
		this.name = name;
		this.organisation = organisation;
	}
	
	@Override
	public String toString() {
		return "VMResource [name=" + name + ", organisation=" + organisation + "]";
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOrganisation() {
		return organisation;
	}
	public void setOrganisation(String organisation) {
		this.organisation = organisation;
	}
}
