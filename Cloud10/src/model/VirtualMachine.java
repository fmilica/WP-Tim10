package model;

import java.util.Collection;

public class VirtualMachine {

	private Category category;
	private int coreNum;
	private int RAM;
	private int GPU;
	private Collection<Disc> discs;
	private Collection<Activity> activities;
	
	public VirtualMachine() {}

	public VirtualMachine(Category category, int coreNum, int rAM, int gPU, Collection<Disc> discs,
			Collection<Activity> activities) {
		this.category = category;
		this.coreNum = coreNum;
		RAM = rAM;
		GPU = gPU;
		this.discs = discs;
		this.activities = activities;
	}

	@Override
	public String toString() {
		return "VirtualMachine [category=" + category + ", coreNum=" + coreNum + ", RAM=" + RAM + ", GPU=" + GPU + "]";
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
		return RAM;
	}
	public void setRAM(int rAM) {
		RAM = rAM;
	}
	public int getGPU() {
		return GPU;
	}
	public void setGPU(int gPU) {
		GPU = gPU;
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
