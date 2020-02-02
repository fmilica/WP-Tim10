package model.wrappers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BillDates {
	static public DateFormat formater = new SimpleDateFormat("yyyy-MM-dd");
	private String start;
	private Date startDate;
	private String end;
	private Date endDate;
	
	public BillDates() {}

	public BillDates(String start, String end) {
		this.start = start;
		this.end = end;
	}

	@Override
	public String toString() {
		return "BillDates [start=" + start + ", end=" + end + "]";
	}

	public boolean formatDate() {
		try {
			this.startDate = formater.parse(this.start);
			this.endDate = formater.parse(this.end);
			return true;
		} catch (ParseException e) {
			return false;
		}
	}
	
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public String getEnd() {
		return end;
	}
	public void setEnd(String end) {
		this.end = end;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
}
