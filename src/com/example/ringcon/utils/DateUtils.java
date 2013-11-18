package com.example.ringcon.utils;

import java.util.ArrayList;

public class DateUtils {

	private static final String[] DAYS_OF_WEEK = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
	
	public static String getDays(int days) {
		if (days == 0) {
			return new String();
		}
		for (int i = DAYS_OF_WEEK.length + 1; i >= 0 ; i++) {
			if (Math.pow(2, i) > days) {
				days -= Math.pow(2, i - 1);
				return getDays(days) + (days == 0 ? "" : ", " )+ DAYS_OF_WEEK[i - 1];
			}
		}
		return null;
	}

	public static ArrayList<Integer> getWeekDays(int days) {
		ArrayList<Integer> weekDays = new ArrayList<Integer>();

		if (days == 0) {
			return null;
		}
		for (int i = 6; i >= 0; i--) {
			if (Math.pow(2, i) <= days) {
				days -= Math.pow(2, i);
				weekDays.add(i + 1);
			}
		}
		return weekDays;
	}
}
