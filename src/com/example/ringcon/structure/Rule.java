package com.example.ringcon.structure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import com.example.ringcon.utils.DateUtils;

import android.content.ContentValues;

public class Rule implements Serializable {

	private static final long serialVersionUID = 8997836577075119215L;

	public static final String KEY_STARTDATE = "startdate";
	public static final String KEY_ENDDATE = "enddate";
	public static final String KEY_ACTIVE = "active";
	public static final String KEY_WEEKDAYS = "weekdays";
	public static final String KEY_ID = "id";

	private int weekdays;
	private long startDate;
	private long finishDate;
	private int id;
	private boolean active;

	public Rule (long sDate, long fDate, int repeating, boolean isOn){
		startDate = sDate;
		finishDate = fDate;
		active = isOn;
		weekdays = repeating;
	}

	public Rule (Date sDate, Date fDate, int repeating, boolean isOn){
		setStartDate(sDate);
		setFinishDate(fDate);
		active = isOn;
		weekdays = repeating;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getWeekdays() {
		return weekdays;
	}

	public void setWeekdays(int weekdays) {
		this.weekdays = weekdays;
	}

	public long getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate.getTime();
	}

	public void setStartDate(long startDate) {
		this.startDate = startDate;
	}

	public long getFinishDate() {
		return finishDate;
	}

	public void setFinishDate(Date finishDate) {
		this.finishDate = finishDate.getTime();
	}

	public void setFinishDate(long finishDate) {
		this.finishDate = finishDate;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public ContentValues getCV() {
		ContentValues cv = new ContentValues();

		cv.put(KEY_ID, this.id);
		cv.put(KEY_STARTDATE, String.valueOf(this.startDate));
		cv.put(KEY_ENDDATE, String.valueOf(this.finishDate));
		cv.put(KEY_WEEKDAYS, String.valueOf(this.weekdays));
		cv.put(KEY_ACTIVE, String.valueOf(this.active));
		return cv;
	}

	public ArrayList<Integer> getArrayOfWeekDays() {
		return DateUtils.getWeekDays(this.weekdays);
	}

}
