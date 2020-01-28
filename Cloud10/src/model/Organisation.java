package model;

import java.util.Collection;

public class Organisation {

	private String name;
	private String description;
	private String logo;
	private Collection<String> users;
	private Collection<VMResource> resources; 
	
	public Organisation() {}
	
	public Organisation(String name) {
		this.name = name;
	}

	public Organisation(String name, String description, String logo, Collection<String> users,
			Collection<VMResource> resources) {
		this.name = name;
		this.description = description;
		this.logo = logo;
		this.users = users;
		this.resources = resources;
	}

	@Override
	public String toString() {
		return "Organisation [name=" + name + ", description=" + description + ", logo=" + logo + "]";
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public Collection<String> getUsers() {
		return users;
	}
	public void setUsers(Collection<String> users) {
		this.users = users;
	}
	public void addUser(User user) {
		this.users.add(user.getEmail());
	}
	public Collection<VMResource> getResources() {
		return resources;
	}
	public void setResources(Collection<VMResource> resources) {
		this.resources = resources;
	}
}
