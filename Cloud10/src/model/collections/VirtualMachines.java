package model.collections;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.ParseException;
import java.util.HashMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import model.Activity;
import model.VirtualMachine;

public class VirtualMachines {

	private HashMap<String, VirtualMachine> virtualMachinesMap;
	
	public VirtualMachines() {}
	
	public VirtualMachines(String filePath) {
		String sep = File.separator;
		Gson gson = new Gson();
		HashMap<String, VirtualMachine> vms;
		try {
			vms = gson.fromJson(new FileReader(filePath + sep+ "data"+ sep + "virtualMachines.json"), new TypeToken<HashMap<String, VirtualMachine>>(){}.getType());
			this.virtualMachinesMap = vms;
			for (VirtualMachine vm : vms.values()) {
				for(Activity a : vm.getActivities()) {
					try {
						a.setOnTime(Activity.formater.parse(a.getOn()));
						if (a.getOff() != null) {
							a.setOffTime(Activity.formater.parse(a.getOff()));	
						}
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public VirtualMachines(HashMap<String, VirtualMachine> virtualMachinesMap) {
		this.virtualMachinesMap = virtualMachinesMap;
	}

	// funkcije za rad sa mapom
	
	// provera da li postoji VM sa unetim imenom
	public boolean vmNameFree(String name) {
		if(virtualMachinesMap.containsKey(name)) {
			return false;
		}
		return true;
	}
	
	// dodavanje virtuelne masine
	public void addVM(VirtualMachine vm) {
		virtualMachinesMap.put(vm.getName(), vm);
	}
	
	// brisanje virtuelne masine
	public void deleteVm(String name) {
		virtualMachinesMap.remove(name);
	}
	// ---
	
	
	public HashMap<String, VirtualMachine> getVirtualMachinesMap() {
		return virtualMachinesMap;
	}
	public void setVirtualMachinesMap(HashMap<String, VirtualMachine> virtualMachinesMap) {
		this.virtualMachinesMap = virtualMachinesMap;
	}
	
	public void writeVMs(String filePath) {
		String sep = File.separator;
		ObjectMapper mapper = new ObjectMapper();
		try {
            mapper.writeValue(new File(filePath + sep + "data" + sep + "virtualMachines.json"), virtualMachinesMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
}
