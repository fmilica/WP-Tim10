package model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Activity {
	static public DateFormat formater = new SimpleDateFormat("dd.mm.yyyy hh:mm");
	private Date onTime;
	private String on;
	private Date offTime;
	private String off;
	
	public Activity() {}

	public Activity(Date onTime, Date offTime) {
		this.onTime = onTime;
		this.on = formater.format(onTime);
		this.offTime = offTime;
		this.off = formater.format(offTime);
	}

	@Override
	public String toString() {
		return "Activity [onTime=" + on + ", offTime=" + off + "]";
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
	public Date getOnTime() {
		return onTime;
	}
	public void setOnTime(Date onTime) {
		this.onTime = onTime;
	}
	public Date getOffTime() {
		return offTime;
	}
	public void setOffTime(Date offTime) {
		this.offTime = offTime;
	}
}
