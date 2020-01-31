package model;

import model.enums.RoleType;

public class User {

	private String email;
	private String password;
	private String name;
	private String surname;
	private Organisation organisation;
	private RoleType role;
	
	public User() {}
	
	public User(String email, String password) {
		this.email = email;
		this.password = password;
	}
	
	public User(String email, String password, String name, String surname, String role) {
		this.email = email;
		this.password = password;
		this.name = name;
		this.surname = surname;
		this.organisation = new Organisation();
		if(role.equals("Admin")) {
			this.role = RoleType.Admin;
		}
		else {
			this.role = RoleType.User;
		}
	}
	
	public User(String email, String password, String name, String surname, String organisation, String role) {
		this.email = email;
		this.password = password;
		this.name = name;
		this.surname = surname;
		this.organisation = new Organisation(organisation);
		if(role.equals("Admin")) {
			this.role = RoleType.Admin;
		}
		else {
			this.role = RoleType.User;
		}
	}

	public User(String email, String password, String name, String surname, Organisation organisation, RoleType role) {
		this.email = email;
		this.password = password;
		this.name = name;
		this.surname = surname;
		this.organisation = organisation;
		this.role = role;
	}
	
	public User(User user) {
		this.email = user.email;
		this.password = user.password;
		this.name = user.name;
		this.surname = user.surname;
		this.organisation = user.organisation;
		this.role = user.role;
	}
	
	@Override
	public String toString() {
		return "User [email=" + email + ", password=" + password + ", name=" + name + ", surname=" + surname + "]";
	}

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public Organisation getOrganisation() {
		return organisation;
	}
	public void setOrganisation(Organisation organisation) {
		this.organisation = organisation;
	}
	public RoleType getRole() {
		return role;
	}
	public void setRole(RoleType role) {
		this.role = role;
	}
	
	public boolean hasNull() {
		if(this.email == null || this.email.trim().length() == 0) {
			return true;
		}
		else if(this.password == null || this.password.trim().length() == 0) {
			return true;
		}
		else if(this.name == null || this.name.trim().length() == 0) {
			return true;
		}
		else if(this.surname == null || this.surname.trim().length() == 0) {
			return true;
		}
		else if(this.organisation.getName() == null || this.organisation.getName().trim().length() == 0) {
			return true;
		}
		else if(this.role == null) {
			return true;
		}
		return false;
	}
}
