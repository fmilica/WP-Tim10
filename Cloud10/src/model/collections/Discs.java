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
	
	public Discs(String filePath) throws JsonIOException, JsonSyntaxException, FileNotFoundException {
		String sep = File.separator;
		Gson gson = new Gson();
		HashMap<String, Disc> discs = gson.fromJson(new FileReader(filePath + sep+ "data"+ sep + "discs.json"), new TypeToken<HashMap<String, Disc>>(){}.getType());
		this.discsMap = discs;
	}

	public Discs(HashMap<String, Disc> discsMap) {
		this.discsMap = discsMap;
	}

	public HashMap<String, Disc> getDiscsMap() {
		return discsMap;
	}
	public void setDiscsMap(HashMap<String, Disc> discsMap) {
		this.discsMap = discsMap;
	}
}
