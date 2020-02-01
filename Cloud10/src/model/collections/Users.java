package model.collections;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import model.Organisation;
import model.User;
import model.enums.RoleType;

public class Users {

	private HashMap<String, User> usersMap;
	
	public Users() {}
	
	public Users(String filePath) {
		String sep = File.separator;
		Gson gson = new Gson();
		HashMap<String, User> users;
		try {
			users = gson.fromJson(new FileReader(filePath + sep+ "data"+ sep + "users.json"), new TypeToken<HashMap<String, User>>(){}.getType());
			this.usersMap = users;
		} catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public Users(String filePath,User u) {
		String sep = File.separator;
		Gson gson = new Gson();
		HashMap<String, User> us = new HashMap<String, User>();
		try {
			us = gson.fromJson(new FileReader(filePath + sep+ "data"+ sep + "users.json"), new TypeToken<HashMap<String, User>>(){}.getType());
		} catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
			e.printStackTrace();
		}
		HashMap<String, User> users = new HashMap<String, User>();
		try {
			users = gson.fromJson(new FileReader(filePath + sep+ "data"+ sep + "users.json"), new TypeToken<HashMap<String, User>>(){}.getType());
		} catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
			e.printStackTrace();
		}
		for (User user : us.values()) {
			if(user.getOrganisation().getName().equals(u.getOrganisation().getName())) {
				users.put(user.getEmail(), user);
			}
		}
		this.usersMap = users;
	}

	public Users(HashMap<String, User> userMap) {
		this.usersMap = userMap;
	}
	public HashMap<String, User> getUsersMap() {
		return usersMap;
	}
	public void setUsersMap(HashMap<String, User> usersMap) {
		this.usersMap = usersMap;
	}
	
	public void addUser(User u) {
		usersMap.put(u.getEmail(), u);
	}
	
	public void setOrgForUser(String user, Organisation organisation) {
		if(usersMap.get(user).getRole() != RoleType.SuperAdmin) {
			usersMap.get(user).setOrganisation(organisation);
		}
	}
	
	public boolean checkUser(User u) {
		if(usersMap.containsKey(u.getEmail())) {
			return true;
		}
		return false;
	}
	
	public void setUserValues(User u) {
		// mapa je
		// mozes da ga getujes samo, ne moras da prolazis
		User user = usersMap.get(u.getEmail());
		user.setPassword(u.getPassword());
		user.setName(u.getName());
		user.setSurname(u.getSurname());
		user.setRole(u.getRole());
		// ja bih bar rekla da moze ovako
		/*
		for (User user : usersMap.values()) {
			if(user.getEmail().equals(u.getEmail())) {
				user.setPassword(u.getPassword());
				user.setName(u.getName());
				user.setSurname(u.getSurname());
				user.setRole(u.getRole());
			}
		}*/
	}
	
	public void removeUser(User u) {
		usersMap.remove(u.getEmail());
	}
	
	public void changeUser(User current, User modified) {
		for (User u : usersMap.values()) {
			if(u.getEmail().equals(current.getEmail())) {
				u = new User(modified);
			}
		}
	}
	
	public boolean userChanged(User u) {
		User old = getUsersMap().get(u.getEmail());
		if(!old.getOrganisation().getName().equals(u.getOrganisation().getName())) {
			return true;
		}
		return false;
	}
	
	public void print() {
		System.out.println("ispisujem ucitane usere");
		for (User u : usersMap.values()) {
			System.out.println(u);
			if(u.getRole() != RoleType.SuperAdmin) {
				System.out.println(u+" "+u.getOrganisation().getDescription());
			}
		}
	}
}
