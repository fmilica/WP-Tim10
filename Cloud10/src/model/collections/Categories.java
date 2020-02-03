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
	
	// mozda je bolje raditi u servisnoj klasi
	public void change(CategoryWrapper cw) {
		if (!cw.getOldName().equals(cw.getName())) {
			// promeni ime
			// brisemo sa starim imenom iz mape
			if (categoriesMap.containsKey(cw.getOldName())) {
				Category c = categoriesMap.get(cw.getOldName());
				categoriesMap.remove(cw.getOldName());
				c.setCoreNum(cw.getCoreNum());
				c.setGPU(cw.getGpu());
				c.setRAM(cw.getRam());
				c.setName(cw.getName());
				categoriesMap.put(cw.getName(), c);
			}/*
			else {
				trebalo bi vratiti error tip 400, bad request, mora postojati staro ime
			}*/
		}
		// isto je staro i novo ime
		// menjamo vrednosti
		// ako postoji u mapi
		if (categoriesMap.containsKey(cw.getName())) {
			Category c = categoriesMap.get(cw.getName());
			c.setCoreNum(cw.getCoreNum());
			c.setGPU(cw.getGpu());
			c.setRAM(cw.getRam());
		}
		/*
		else {
			isti error kao gore
		}*/
		/*
		for (Category cat : categoriesMap.values()) {
			if(cat.getName().equals(cw.getOldName())) {
				cat.setName(cw.getName());
				cat.setCoreNum(cw.getCoreNum());
				cat.setRAM(cw.getRam());
				cat.setGPU(cw.getGpu());
			}
		}*/
	}
	
	public void remove(Category c) {
		categoriesMap.remove(c.getName());
	}
	
	public void writeCategories(String filePath) {
		String sep = File.separator;
		ObjectMapper mapper = new ObjectMapper();
		try {
            mapper.writeValue(new File(filePath + sep + "data" + sep + "categories.json"), categoriesMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
}
