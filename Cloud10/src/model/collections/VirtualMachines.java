package model.collections;

import java.util.HashMap;

import model.VirtualMachine;

public class VirtualMachines {

	private HashMap<String, VirtualMachine> virtualMachinesMap;
	
	public VirtualMachines() {}
	
	public VirtualMachines(String filePath) {}

	public VirtualMachines(HashMap<String, VirtualMachine> virtualMachinesMap) {
		this.virtualMachinesMap = virtualMachinesMap;
	}

	public HashMap<String, VirtualMachine> getVirtualMachinesMap() {
		return virtualMachinesMap;
	}
	public void setVirtualMachinesMap(HashMap<String, VirtualMachine> virtualMachinesMap) {
		this.virtualMachinesMap = virtualMachinesMap;
	}
}
