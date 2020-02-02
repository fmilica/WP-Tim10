package model;

import java.io.File;
import java.util.Collection;
import java.util.StringTokenizer;

public class Organisation {

	private String name;
	private String description;
	private String logo;
	private Collection<String> users;
	private Collection<String> resources; 
	
	public Organisation() {}
	
	public Organisation(String name) {
		this.name = name;
	}

	public Organisation(String name, String description, String logo, Collection<String> users,
			Collection<String> resources) {
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
		if(logo == null || logo.trim().length() == 0) {
			this.logo = "";
		}
		else {
			String sep = File.separator;
			StringTokenizer st = new StringTokenizer(logo, sep);
			String file = "";
			while(st.hasMoreTokens()) {
				file = st.nextToken();
			}
			String path = "images/png/";
			st = new StringTokenizer(file, ".");
			String name = st.nextToken();
			String ext = st.nextToken();
			if(ext.equals("jpg") || ext.equals("png") || ext.equals("gif")) {
				path += name + "." + ext;
				this.logo = path;
				System.out.println(this.logo);
			}
			else {
				this.logo = "";
			}
		}
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
	public Collection<String> getResources() {
		return resources;
	}
	public void setResources(Collection<String> resources) {
		this.resources = resources;
	}
	public void addResource(String resource) {
		this.resources.add(resource);
	}
	
	public boolean hasNull() {
		if(this.name == null || this.name.trim().length() == 0) {
			return true;
		}
		else if(this.description == null || this.description.trim().length() == 0) {
			return true;
		}
		//logo moze da bude null ili prazan kao i liste
		return false;
	}
}
