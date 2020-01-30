package model.wrappers;

import model.enums.DiscType;

public class DiscWrapper {

	private String newName;
	private DiscType type;
	private int capacity;
	private String oldName;
	
	public DiscWrapper() {}

	public DiscWrapper(String newName, DiscType type, int capacity, String oldName) {
		this.newName = newName;
		this.type = type;
		this.capacity = capacity;
		this.oldName = oldName;
	}

	@Override
	public String toString() {
		return "DiscWrapper [newName=" + newName + ", type=" + type + ", capacity=" + capacity + ", oldName=" + oldName
				+ "]";
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
	public String getOldName() {
		return oldName;
	}
	public void setOldName(String oldName) {
		this.oldName = oldName;
	}
}
