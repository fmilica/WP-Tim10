package model;

import java.util.ArrayList;
import java.util.Collection;

public class VirtualMachine extends VMResource {

	private Category category;
	private int coreNum;
	private int ram;
	private int gpu;
	private Collection<String> discs;
	private ArrayList<Activity> activities;
	
	public VirtualMachine() {}
	
	public VirtualMachine(String name, String organisation, 
			String categoryName, int coreNum, int ram, int gpu) {
		super(name, organisation);
		this.category = new Category(categoryName);
		this.coreNum = coreNum;
		this.ram = ram;
		this.gpu = gpu;
	}
	
	public VirtualMachine(String name, String organisation, Category category) {
		super(name, organisation);
		this.category = new Category(category);
		this.coreNum = category.getCoreNum();
		this.ram = category.getRAM();
		this.gpu = category.getGPU();
	}
	
	public VirtualMachine(String name, String organisation, 
			Category category, ArrayList<Activity> activities) {
		super(name, organisation);
		this.category = category;
		this.coreNum = category.getCoreNum();
		this.ram = category.getRAM();
		this.gpu = category.getGPU();
		this.activities = activities;
	}
	
	public VirtualMachine(String name, String organisation, 
			Category category, Collection<String> discs,
			ArrayList<Activity> activities) {
		super(name, organisation);
		this.category = category;
		this.coreNum = category.getCoreNum();
		ram = category.getRAM();
		gpu = category.getGPU();
		this.discs = discs;
		this.activities = activities;
	}

	@Override
	public String toString() {
		return "VirtualMachine [category=" + category + ", coreNum=" + coreNum + ", ram=" + ram + ", gpu=" + gpu
				+ ", discs=" + discs + ", activities=" + activities + ", name=" + name + ", organisation="
				+ organisation + "]";
	}

	// dodavanje diska virtuelnoj masini
	public void addDisc(String discName) {
		this.discs.add(discName);
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	public int getCoreNum() {
		return coreNum;
	}
	public void setCoreNum(int coreNum) {
		this.coreNum = coreNum;
	}
	public int getRAM() {
		return ram;
	}
	public void setRAM(int ram) {
		this.ram = ram;
	}
	public int getGPU() {
		return gpu;
	}
	public void setGPU(int gpu) {
		this.gpu = gpu;
	}
	public Collection<String> getDiscs() {
		return discs;
	}
	public void setDiscs(Collection<String> discs) {
		this.discs = discs;
	}
	public ArrayList<Activity> getActivities() {
		return activities;
	}
	public void setActivities(ArrayList<Activity> activities) {
		this.activities = activities;
	}
	
	public boolean hasNull() {
		if(this.category.hasNull()) {
			return true;
		}
		else if(this.name == null || this.name.trim().length() == 0) {
			return true;
		}
		else if(this.organisation == null || this.name.trim().length() == 0) {
			return true;
		}
		else if(this.coreNum <= 0) {
			return true;
		}
		else if(this.ram <= 0) {
			return true;
		}
		else if(this.gpu < 0) {
			return true;
		}
		return false;
	}
}
