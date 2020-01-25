package model;

public class Category {

	private String name;
	private int coreNum;
	private int RAM;
	private int GPU;
	
	public Category() {}

	public Category(String name, int coreNum, int rAM, int gPU) {
		this.name = name;
		this.coreNum = coreNum;
		RAM = rAM;
		GPU = gPU;
	}
	
	@Override
	public String toString() {
		return "Category [name=" + name + ", coreNum=" + coreNum + ", RAM=" + RAM + ", GPU=" + GPU + "]";
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
	public int getRAM() {
		return RAM;
	}
	public void setRAM(int rAM) {
		RAM = rAM;
	}
	public int getGPU() {
		return GPU;
	}
	public void setGPU(int gPU) {
		GPU = gPU;
	}
}
