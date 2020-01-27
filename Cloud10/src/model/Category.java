package model;

public class Category {

	private String name;
	private int coreNum;
	private int ram;
	private int gpu;
	
	public Category() {}
	
	public Category(String name) {
		this.name = name;
	}
	
	public Category(Category c) {
		this.name = c.name;
		this.coreNum = c.coreNum;
		this.ram = c.ram;
		this.gpu = c.gpu;
	}

	public Category(String name, int coreNum, int ram, int gpu) {
		this.name = name;
		this.coreNum = coreNum;
		this.ram = ram;
		this.gpu = gpu;
	}
	
	@Override
	public String toString() {
		return "Category [name=" + name + ", coreNum=" + coreNum + ", RAM=" + ram + ", GPU=" + gpu + "]";
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
		return ram;
	}
	public void setRAM(int ram) {
		this.ram = ram;
	}
	public int getGPU() {
		return gpu;
	}
	public void setGPU(int gpu) {
		this.gpu = gpu;
	}
}
