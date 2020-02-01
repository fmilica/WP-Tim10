package model.wrappers;

import java.util.Collection;

import model.Activity;
import model.Category;

public class VirtualMachineWrapper {

	private String name;
	private String organisation;
	private Category category;
	private int coreNum;
	private int ram;
	private int gpu;
	private Collection<String> discs;
	private Collection<Activity> activities;
	private String oldName;
	private boolean status;
	private Collection<String> newDiscs;
	
	public VirtualMachineWrapper() {}
	
	public VirtualMachineWrapper(String name, String organisation, Category category, int coreNum, int ram, int gpu,
			Collection<String> discs, Collection<Activity> activities, String oldName, boolean status,
			Collection<String> newDiscs) {
		this.name = name;
		this.organisation = organisation;
		this.category = category;
		this.coreNum = coreNum;
		this.ram = ram;
		this.gpu = gpu;
		this.discs = discs;
		this.activities = activities;
		this.oldName = oldName;
		this.status = status;
		this.newDiscs = newDiscs;
	}

	@Override
	public String toString() {
		return "VirtualMachineWrapper [name=" + name + ", organisation=" + organisation + ", category=" + category
				+ ", coreNum=" + coreNum + ", ram=" + ram + ", gpu=" + gpu + ", discs=" + discs + ", activities="
				+ activities + ", oldName=" + oldName + ", status=" + status + ", newDiscs=" + newDiscs + "]";
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getOrganisation() {
		return organisation;
	}
	public void setOrganisation(String organisation) {
		this.organisation = organisation;
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
	public int getRam() {
		return ram;
	}
	public void setRam(int ram) {
		this.ram = ram;
	}
	public int getGpu() {
		return gpu;
	}
	public void setGpu(int gpu) {
		this.gpu = gpu;
	}
	public Collection<String> getDiscs() {
		return discs;
	}
	public void setDiscs(Collection<String> discs) {
		this.discs = discs;
	}
	public Collection<Activity> getActivities() {
		return activities;
	}
	public void setActivities(Collection<Activity> activities) {
		this.activities = activities;
	}
	public String getOldName() {
		return oldName;
	}
	public void setOldName(String oldName) {
		this.oldName = oldName;
	}
	public Collection<String> getNewDiscs() {
		return newDiscs;
	}
	public void setNewDiscs(Collection<String> newDiscs) {
		this.newDiscs = newDiscs;
	}
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
}
