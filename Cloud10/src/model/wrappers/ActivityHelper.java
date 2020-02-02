package model.wrappers;

public class ActivityHelper {
	private String on;
	private String off;
	
	public ActivityHelper() {}

	public ActivityHelper(String on, String off) {
		this.on = on;
		this.off = off;
	}

	@Override
	public String toString() {
		return "ActivityHelper [on=" + on + ", off=" + off + "]";
	}

	public String getOn() {
		return on;
	}
	public void setOn(String on) {
		this.on = on;
	}
	public String getOff() {
		return off;
	}
	public void setOff(String off) {
		this.off = off;
	}
}
