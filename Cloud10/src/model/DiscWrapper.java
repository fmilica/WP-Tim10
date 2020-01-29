package model;

public class DiscWrapper {

	private Disc disc;
	private String oldName;
	
	public DiscWrapper() {}

	public DiscWrapper(Disc disc, String oldName) {
		this.disc = disc;
		this.oldName = oldName;
	}

	@Override
	public String toString() {
		return "DiscWrapper [disc=" + disc + ", oldName=" + oldName + "]";
	}

	public Disc getDisc() {
		return disc;
	}
	public void setDisc(Disc disc) {
		this.disc = disc;
	}
	public String getOldName() {
		return oldName;
	}
	public void setOldName(String oldName) {
		this.oldName = oldName;
	}
}
