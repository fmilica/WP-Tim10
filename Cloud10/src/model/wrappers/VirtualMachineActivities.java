package model.wrappers;

import java.util.ArrayList;

public class VirtualMachineActivities {
	
	private String vmName;
	private ArrayList<ActivityHelper> activities;
	
	public VirtualMachineActivities() {}

	public VirtualMachineActivities(String vmName, ArrayList<ActivityHelper> activities) {
		this.vmName = vmName;
		this.activities = activities;
	}

	@Override
	public String toString() {
		return "VirtualMachineActivities [vmName=" + vmName + ", activities=" + activities + "]";
	}

	public String getVmName() {
		return vmName;
	}
	public void setVmName(String vmName) {
		this.vmName = vmName;
	}
	public ArrayList<ActivityHelper> getActivities() {
		return activities;
	}
	public void setActivities(ArrayList<ActivityHelper> activities) {
		this.activities = activities;
	}
}
