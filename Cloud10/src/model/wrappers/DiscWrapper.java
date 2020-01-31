package model.wrappers;

import model.enums.DiscType;

public class DiscWrapper {

	private String newName;
	private DiscType type;
	private int capacity;
	private String organisation;
	private String vm;
	private String oldName;
	
	public DiscWrapper() {}

	public DiscWrapper(String newName, DiscType type, int capacity, String organisation, String vm, String oldName) {
		this.newName = newName;
		this.type = type;
		this.capacity = capacity;
		this.organisation = organisation;
		this.vm = vm;
		this.oldName = oldName;
	}

	@Override
	public String toString() {
		return "DiscWrapper [newName=" + newName + ", type=" + type + ", capacity=" + capacity + ", organisation="
				+ organisation + ", vm=" + vm + ", oldName=" + oldName + "]";
	}

	public String getNewName() {
		return newName;
	}
	public void setNewName(String newName) {
		this.newName = newName;
	}
	public DiscType getType() {
		return type;
	}
	public void setType(DiscType type) {
		this.type = type;
	}
	public int getCapacity() {
		return capacity;
	}
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	public String getOrganisation() {
		return organisation;
	}
	public void setOrganisation(String organisation) {
		this.organisation = organisation;
	}
	public String getVm() {
		return vm;
	}
	public void setVm(String vm) {
		this.vm = vm;
	}

	public String getOldName() {
		return oldName;
	}
	public void setOldName(String oldName) {
		this.oldName = oldName;
	}
}
