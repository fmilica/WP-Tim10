package model.collections;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

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

	public Users(HashMap<String, User> userMap) {
		this.usersMap = userMap;
	}
	public HashMap<String, User> getUsersMap() {
		return usersMap;
	}
	public void setUsersMap(HashMap<String, User> usersMap) {
		this.usersMap = usersMap;
	}
	
}
