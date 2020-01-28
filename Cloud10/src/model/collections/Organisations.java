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

public class Organisations {

	private HashMap<String, Organisation> organisationsMap;
	
	public Organisations() {}
	
	public Organisations(String filePath) {
		String sep = File.separator;
		Gson gson = new Gson();
		HashMap<String, Organisation> organisations;
		try {
			organisations = gson.fromJson(new FileReader(filePath + sep+ "data"+ sep + "organisations.json"), new TypeToken<HashMap<String, Organisation>>(){}.getType());
			this.organisationsMap = organisations;
		} catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
			e.printStackTrace();
		}
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
