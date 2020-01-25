package model;

import java.util.Date;

public class Activity {

	private Date onTime;
	private Date offTime;
	
	public Activity() {}

	public Activity(Date onTime, Date offTime) {
		this.onTime = onTime;
		this.offTime = offTime;
	}

	@Override
	public String toString() {
		return "Activity [onTime=" + onTime + ", offTime=" + offTime + "]";
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
