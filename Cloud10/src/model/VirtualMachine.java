package model;

import java.util.Collection;

public class VirtualMachine extends VMResource {

	private Category category;
	private int coreNum;
	private int ram;
	private int gpu;
	private Collection<Disc> discs;
	private Collection<Activity> activities;
	
	public VirtualMachine() {}

	public VirtualMachine(String name, Category category) {
		super(name);
		this.category = category;
		this.coreNum = category.getCoreNum();
		this.ram = category.getRAM();
		this.gpu = category.getGPU();
	}
	
	public VirtualMachine(String name, Category category, Collection<Activity> activities) {
		super(name);
		this.category = category;
		this.coreNum = category.getCoreNum();
		this.ram = category.getRAM();
		this.gpu = category.getGPU();
		this.activities = activities;
	}
	
	public VirtualMachine(String name, Category category, Collection<Disc> discs,
			Collection<Activity> activities) {
		super(name);
		this.category = category;
		this.coreNum = category.getCoreNum();
		ram = category.getRAM();
		gpu = category.getGPU();
		this.discs = discs;
		this.activities = activities;
	}

	@Override
	public String toString() {
		return "VirtualMachine [name=" + name + "category=" + category + ", coreNum=" + coreNum + ", RAM=" + ram + ", GPU=" + gpu + "]";
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
	public Collection<Disc> getDiscs() {
		return discs;
	}
	public void setDiscs(Collection<Disc> discs) {
		this.discs = discs;
	}
	public Collection<Activity> getActivities() {
		return activities;
	}
	public void setActivities(Collection<Activity> activities) {
		this.activities = activities;
	}
}
