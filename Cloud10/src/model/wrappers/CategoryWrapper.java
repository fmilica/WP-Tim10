package model.wrappers;

public class CategoryWrapper {
	private String oldName;
	private String name;
	private int coreNum;
	private int ram;
	private int gpu;
	
	public CategoryWrapper() {}

	public CategoryWrapper(String oldName, String name, int coreNum, int ram, int gpu) {
		super();
		this.oldName = oldName;
		this.name = name;
		this.coreNum = coreNum;
		this.ram = ram;
		this.gpu = gpu;
	}

	public String getOldName() {
		return oldName;
	}

	public void setOldName(String oldName) {
		this.oldName = oldName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	@Override
	public String toString() {
		return "CategoryWrapper [oldName=" + oldName + ", name=" + name + ", coreNum=" + coreNum + ", ram=" + ram
				+ ", gpu=" + gpu + "]";
	}
}
