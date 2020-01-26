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

public class Organisations {

	private HashMap<String, Organisation> organisationsMap;
	
	public Organisations() {}
	
	public Organisations(String filePath) throws JsonIOException, JsonSyntaxException, FileNotFoundException {
		String sep = File.separator;
		Gson gson = new Gson();
		HashMap<String, Organisation> organisations = gson.fromJson(new FileReader(filePath + sep+ "data"+ sep + "organisations.json"), new TypeToken<HashMap<String, Organisation>>(){}.getType());
		this.organisationsMap = organisations;
	}

	public Organisations(HashMap<String, Organisation> organisationsMap) {
		this.organisationsMap = organisationsMap;
	}

	public HashMap<String, Organisation> getOrganisationsMap() {
		return organisationsMap;
	}
	public void setOrganisationsMap(HashMap<String, Organisation> organisationsMap) {
		this.organisationsMap = organisationsMap;
	}
}
