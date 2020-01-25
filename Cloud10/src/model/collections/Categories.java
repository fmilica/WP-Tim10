package model.collections;

import java.util.HashMap;

import model.Category;

public class Categories {

	private HashMap<String, Category> categoriesMap;
	
	public Categories() {}
	
	public Categories(String filePath) {}

	public Categories(HashMap<String, Category> categoriesMap) {
		this.categoriesMap = categoriesMap;
	}
	
	public HashMap<String, Category> getCategoriesMap() {
		return categoriesMap;
	}
	public void setCategoriesMap(HashMap<String, Category> categoriesMap) {
		this.categoriesMap = categoriesMap;
	}
}
