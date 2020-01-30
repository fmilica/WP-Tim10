package model.collections;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import model.Category;
import model.wrappers.CategoryWrapper;

public class Categories {

	private HashMap<String, Category> categoriesMap;
	
	public Categories() {}
	
	public Categories(String filePath) {
		String sep = File.separator;
		Gson gson = new Gson();
		HashMap<String, Category> categories;
		try {
			categories = gson.fromJson(new FileReader(filePath + sep+ "data"+ sep + "categories.json"), new TypeToken<HashMap<String, Category>>(){}.getType());
			this.categoriesMap = categories;
		} catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
			e.printStackTrace();
		}	
	}

	public Categories(HashMap<String, Category> categoriesMap) {
		this.categoriesMap = categoriesMap;
	}
	
	public HashMap<String, Category> getCategoriesMap() {
		return categoriesMap;
	}
	public void setCategoriesMap(HashMap<String, Category> categoriesMap) {
		this.categoriesMap = categoriesMap;
	}
	
	public void addItem(Category cat) {
		categoriesMap.put(cat.getName(), cat);
	}
	
	public void change(CategoryWrapper cw) {
		for (Category cat : categoriesMap.values()) {
			if(cat.getName().equals(cw.getOldName())) {
				cat.setName(cw.getName());
				cat.setCoreNum(cw.getCoreNum());
				cat.setRAM(cw.getRam());
				cat.setGPU(cw.getGpu());
			}
		}
	}
	
	public void remove(Category c) {
		categoriesMap.remove(c.getName());
	}
}
