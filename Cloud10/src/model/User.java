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

	public User(String email, String password, String name, String surname, Organisation organisation, RoleType role) {
		this.email = email;
		this.password = password;
		this.name = name;
		this.surname = surname;
		this.organisation = organisation;
		this.role = role;
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
}
