package model.wrappers;

public class BillTypeWrapper {
	private String name;
	private Double price;
	
	public BillTypeWrapper() {}

	public BillTypeWrapper(String name) {
		this.name = name;
		this.price = 0.0; // hehe ksenija hehe
	}
	
	public BillTypeWrapper(String name, Double price) {
		this.name = name;
		this.price = price;
	}

	@Override
	public String toString() {
		return "BillWrapper [name=" + name + ", price=" + price + "]";
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
}
