package model.collections;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import model.Disc;

public class Discs {

	private HashMap<String, Disc> discsMap;
	
	public Discs() {}
	
	public Discs(String filePath) {
		String sep = File.separator;
		Gson gson = new Gson();
		HashMap<String, Disc> discs;
		try {
			discs = gson.fromJson(new FileReader(filePath + sep+ "data"+ sep + "discs.json"), new TypeToken<HashMap<String, Disc>>(){}.getType());
			this.discsMap = discs;
		} catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public Discs(HashMap<String, Disc> discsMap) {
		this.discsMap = discsMap;
	}

	// funkcije za rad sa mapom
	
	// provera da li postoji disk sa unetim imenom
	public boolean checkDiscName(String name) {
		if(discsMap.containsKey(name)) {
			return false;
		}
		return true;
	}
	
	// dodavanje diska
	public void addDisc(Disc d) {
		discsMap.put(d.getName(), d);
	}
	
	// brisanje diska
	public void deleteDisc(String name) {
		discsMap.remove(name);
	}
	
	// ---
	
	public HashMap<String, Disc> getDiscsMap() {
		return discsMap;
	}
	public void setDiscsMap(HashMap<String, Disc> discsMap) {
		this.discsMap = discsMap;
	}
}
