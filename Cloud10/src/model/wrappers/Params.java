package model.wrappers;

public class Params {

	private String searchBy;
	private String coresLow;
	private String coresHigh;
	private String ramLow;
	private String ramHigh;
	private String gpuLow;
	private String gpuHigh;
	
	public Params() {}

	public Params(String searchBy, String coresLow, String coresHigh, String ramLow, String ramHigh, String gpuLow,
			String gpuHigh) {
		this.searchBy = searchBy;
		this.coresLow = coresLow;
		this.coresHigh = coresHigh;
		this.ramLow = ramLow;
		this.ramHigh = ramHigh;
		this.gpuLow = gpuLow;
		this.gpuHigh = gpuHigh;
	}

	@Override
	public String toString() {
		return "Params [searchBy=" + searchBy + ", coresLow=" + coresLow + ", coresHigh=" + coresHigh + ", ramLow="
				+ ramLow + ", ramHigh=" + ramHigh + ", gpuLow=" + gpuLow + ", gpuHigh=" + gpuHigh + "]";
	}

	public String getSearchBy() {
		return searchBy;
	}
	public void setSearchBy(String searchBy) {
		this.searchBy = searchBy;
	}
	public String getCoresLow() {
		return coresLow;
	}
	public void setCoresLow(String coresLow) {
		this.coresLow = coresLow;
	}
	public String getCoresHigh() {
		return coresHigh;
	}
	public void setCoresHigh(String coresHigh) {
		this.coresHigh = coresHigh;
	}
	public String getRamLow() {
		return ramLow;
	}
	public void setRamLow(String ramLow) {
		this.ramLow = ramLow;
	}
	public String getRamHigh() {
		return ramHigh;
	}
	public void setRamHigh(String ramHigh) {
		this.ramHigh = ramHigh;
	}
	public String getGpuLow() {
		return gpuLow;
	}
	public void setGpuLow(String gpuLow) {
		this.gpuLow = gpuLow;
	}
	public String getGpuHigh() {
		return gpuHigh;
	}
	public void setGpuHigh(String gpuHigh) {
		this.gpuHigh = gpuHigh;
	}
}
