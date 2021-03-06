package com.example.ringcon.utils;

import java.util.ArrayList;

import com.example.ringcon.R;

import android.content.Context;

public class DateUtils {
	public DateUtils(Context ctx) {
		DAYS_OF_WEEK = ctx.getResources().getStringArray(R.array.days);
	}

//	private String[] DAYS_OF_WEEK = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri",
//			"Sat" };
	private String[] DAYS_OF_WEEK;
	// Resources res=getResources();
	// private static String[] DAYS_OF_WEEK =res.getStringArray(R.array.days);
	private static final int SECOND = 1000;
	private static final int MINUTE = 60 * SECOND;
	private static final int HOUR = 60 * MINUTE;

	public String getDays(int days) {
		if (days == 0) {
			return new String();
		}
		for (int i = DAYS_OF_WEEK.length - 1; i >= 0; i--) {
			if (Math.pow(2, i) <= days) {
				days -= Math.pow(2, i);
				return getDays(days) + (days == 0 ? "" : ", ")
						+ DAYS_OF_WEEK[i];
			}
		}
		return null;
	}

	public static ArrayList<Integer> getWeekDays(int days) {
		ArrayList<Integer> weekDays = new ArrayList<Integer>();

		if (days == 0) {
			return new ArrayList<Integer>();
		}
		for (int i = 6; i >= 0; i--) {
			if (Math.pow(2, i) <= days) {
				days -= Math.pow(2, i);
				weekDays.add(i + 1);
			}
		}
		return weekDays;
	}

	public String getTime(long millis) {
		StringBuffer text = new StringBuffer("");

		if (millis > HOUR) {
			text.append(millis / HOUR).append(":");
			millis %= HOUR;
		}
		if (millis >= MINUTE) {
			long minutes = millis / MINUTE;
			if (minutes < 10)
				text.append("0");
			text.append(minutes);
		}
		if (millis < MINUTE)
			text.append("00");
		return text.toString();
	}

	public static int getHours(long millis) {
		if (millis > HOUR)
			return (int) millis / HOUR;
		return 0;
	}

	public static int getMinutes(long millis) {
		millis %= HOUR;
		if (millis > MINUTE)
			return (int) millis / MINUTE;
		return 0;
	}
}
