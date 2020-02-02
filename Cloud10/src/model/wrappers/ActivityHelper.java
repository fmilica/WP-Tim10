package model.wrappers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ActivityHelper {
	static public DateFormat formater = new SimpleDateFormat("dd.MM.yyyy HH:mm");
	private String on;
	private Date onDate;
	private String off;
	private Date offDate;
	
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
		try {
			this.onDate = formater.parse(on);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	public String getOff() {
		return off;
	}
	public void setOff(String off) {
		this.off = off;
		try {
			this.offDate = formater.parse(off);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	public Date getOnDate() {
		return onDate;
	}
	public void setOnDate(Date onDate) {
		this.onDate = onDate;
	}
	public Date getOffDate() {
		return offDate;
	}
	public void setOffDate(Date offDate) {
		this.offDate = offDate;
	}
}
