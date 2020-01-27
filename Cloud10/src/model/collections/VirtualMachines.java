package model.collections;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import model.VirtualMachine;

public class VirtualMachines {

	private HashMap<String, VirtualMachine> virtualMachinesMap;
	
	public VirtualMachines() {}
	
	public VirtualMachines(String filePath) {
		String sep = File.separator;
		Gson gson = new GsonBuilder().setDateFormat("dd-MM-yyyy HH:mm").create();
		HashMap<String, VirtualMachine> vms;
		try {
			vms = gson.fromJson(new FileReader(filePath + sep+ "data"+ sep + "virtualMachines.json"), new TypeToken<HashMap<String, VirtualMachine>>(){}.getType());
			this.virtualMachinesMap = vms;
		} catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public VirtualMachines(HashMap<String, VirtualMachine> virtualMachinesMap) {
		this.virtualMachinesMap = virtualMachinesMap;
	}

	// funkcije za rad sa mapom
	
	// provera da li postoji VM sa unetim imenom
	public boolean checkVMName(String name) {
		if(virtualMachinesMap.containsKey(name)) {
			return false;
		}
		return true;
	}
	
	// dodavanje virtuelne masine
	public void addVM(VirtualMachine vm) {
		virtualMachinesMap.put(vm.getName(), vm);
	}
	
	// ---
	
	
	public HashMap<String, VirtualMachine> getVirtualMachinesMap() {
		return virtualMachinesMap;
	}
	public void setVirtualMachinesMap(HashMap<String, VirtualMachine> virtualMachinesMap) {
		this.virtualMachinesMap = virtualMachinesMap;
	}
}
