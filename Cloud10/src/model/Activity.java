package model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Activity {
	static public DateFormat formater = new SimpleDateFormat("dd.MM.yyyy HH:mm");
	private Date onTime;
	private String on;
	private Date offTime;
	private String off;
	
	public Activity() {}

	public Activity(Date onTime) {
		this.onTime = onTime;
		this.on = formater.format(onTime);
	}
	
	public Activity(Date onTime, Date offTime) {
		this.onTime = onTime;
		this.on = formater.format(onTime);
		this.offTime = offTime;
		this.off = formater.format(offTime);
	}

	public Activity(String on) {
		this.on = on;
		try {
			this.onTime = formater.parse(on);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	public Activity(String on, String off) {
		this.on = on;
		try {
			this.onTime = formater.parse(on);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		this.off = off;
		try {
			this.offTime = formater.parse(off);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String toString() {
		return "Activity [onTime=" + on + ", offTime=" + off + "]";
	}

	public static boolean checkDate(String date) {
		try {
			formater.parse(date);
			return true;
		} catch (ParseException e) {
			return false;
		}
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
		this.on = formater.format(onTime);
	}
	public Date getOffTime() {
		return offTime;
	}
	public void setOffTime(Date offTime) {
		this.offTime = offTime;
		this.off = formater.format(offTime);
	}
}
