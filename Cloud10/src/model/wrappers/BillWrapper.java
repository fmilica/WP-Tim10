package model.wrappers;

import java.util.ArrayList;

public class BillWrapper {

	private ArrayList<BillTypeWrapper> vms;
	private ArrayList<BillTypeWrapper> discs;
	private double total;
	
	public BillWrapper() {
		this.vms = new ArrayList<BillTypeWrapper>();
		this.discs = new ArrayList<BillTypeWrapper>();
		this.total = 0;
	}

	public BillWrapper(ArrayList<BillTypeWrapper> vms, ArrayList<BillTypeWrapper> discs, double total) {
		this.vms = vms;
		this.discs = discs;
		this.total = total;
	}

	@Override
	public String toString() {
		return "BillWrapper [vms=" + vms + ", discs=" + discs + ", total=" + total + "]";
	}

	public ArrayList<BillTypeWrapper> getVms() {
		return vms;
	}
	public void setVms(ArrayList<BillTypeWrapper> vms) {
		this.vms = vms;
	}
	public ArrayList<BillTypeWrapper> getDiscs() {
		return discs;
	}
	public void setDiscs(ArrayList<BillTypeWrapper> discs) {
		this.discs = discs;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
}
