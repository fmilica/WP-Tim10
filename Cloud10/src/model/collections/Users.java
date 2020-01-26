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

public class Users {

	private HashMap<String, User> usersMap;
	
	public Users() {}
	
	public Users(String filePath) throws JsonIOException, JsonSyntaxException, FileNotFoundException {
		String sep = File.separator;
		Gson gson = new Gson();
		HashMap<String, User> users = gson.fromJson(new FileReader(filePath + sep+ "data"+ sep + "users.json"), new TypeToken<HashMap<String, User>>(){}.getType());
		this.usersMap = users;
	}
	
	public Users(String filePath,User u) throws JsonIOException, JsonSyntaxException, FileNotFoundException {
		String sep = File.separator;
		Gson gson = new Gson();
		HashMap<String, User> us = gson.fromJson(new FileReader(filePath + sep+ "data"+ sep + "users.json"), new TypeToken<HashMap<String, User>>(){}.getType());
		HashMap<String, User> users = gson.fromJson(new FileReader(filePath + sep+ "data"+ sep + "users.json"), new TypeToken<HashMap<String, User>>(){}.getType());
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
		usersMap.get(user).setOrganisation(organisation);
	}
	
	public boolean checkUser(User u) {
		if(usersMap.containsKey(u.getEmail())) {
			return true;
		}
		return false;
	}
	
	public void setUserValues(User u) {
		for (User user : usersMap.values()) {
			if(user.getEmail().equals(u.getEmail())) {
				user.setPassword(u.getPassword());
				user.setName(u.getName());
				user.setSurname(u.getSurname());
				user.setRole(u.getRole());
			}
		}
	}
}
