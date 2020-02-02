package model.collections;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import model.Organisation;
import model.User;

public class Organisations {

	private HashMap<String, Organisation> organisationsMap;
	
	public Organisations() {
		organisationsMap = new HashMap<String, Organisation>();
	}
	
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
	
	public boolean checkOrg(String o) {
		if(organisationsMap.containsKey(o)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public void changeOrg(Organisation o) {
		for (Organisation org : organisationsMap.values()) {
			if(org.getName().equals(o.getName())) {
				org.setDescription(o.getDescription());
				org.setLogo(o.getLogo());
			}
		}
	}
	
	public void removeUser(User u) {
		for (Organisation o : organisationsMap.values()) {
			if(o.getName().equals(u.getOrganisation().getName())) {
				o.getUsers().remove(u.getEmail());
			}
		}
	}
	
	public void addItem(Organisation o) {
		organisationsMap.put(o.getName(), o);
	}
	
	public void writeOrganisations(String filePath) {
		String sep = File.separator;
		ObjectMapper mapper = new ObjectMapper();
		try {
            mapper.writeValue(new File(filePath + sep + "data" + sep + "organisations.json"), organisationsMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
}
