package com.example.ringcon.sql;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.ringcon.structure.Rule;

public class SQLiteAdapter {

	private DBHelper dbHelper;

	final private static String[] PROJECTION = {Rule.KEY_ID, Rule.KEY_STARTDATE, Rule.KEY_ENDDATE,
				Rule.KEY_ACTIVE, Rule.KEY_WEEKDAYS, Rule.KEY_MODE };

	public SQLiteAdapter(Context context) {
		dbHelper = new DBHelper(context);
	}

	public long addRule(Rule rule) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		long result = db.insert(DBHelper.TABLE_NAME, null, rule.getCV());
		db.close();
		return result;
	}

	public boolean removeRule(long id) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		int result = db.delete(DBHelper.TABLE_NAME, Rule.KEY_ID + "=" + id, null);
		db.close();
		return result > 0;
	}

	public boolean editRule(long id, Rule rule) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		int result = db.update(DBHelper.TABLE_NAME, rule.getCV(), Rule.KEY_ID + "=" + id, null);
		db.close();
		return result > 0;
	}

	public Rule getRule(long id) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		Cursor cursor = db.query(DBHelper.TABLE_NAME, PROJECTION , Rule.KEY_ID + "=" + id, null, null, null, null, null);
		if(cursor.getCount()==0) {
			return null;
		}

		if (!cursor.isFirst()) {
			cursor.moveToFirst();
		}
		int itemId = cursor.getInt(cursor.getColumnIndexOrThrow(Rule.KEY_ID));
		long startTime = cursor.getLong(cursor.getColumnIndexOrThrow(Rule.KEY_STARTDATE));
		long endTime = cursor.getLong(cursor.getColumnIndexOrThrow(Rule.KEY_ENDDATE));
		boolean isActive = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndexOrThrow(Rule.KEY_ACTIVE)));
		int weekDays = cursor.getInt(cursor.getColumnIndexOrThrow(Rule.KEY_WEEKDAYS));
		int mode = cursor.getInt(cursor.getColumnIndexOrThrow(Rule.KEY_MODE));

		Rule rule = new Rule(itemId, startTime, endTime, weekDays, mode, isActive);

		cursor.close();
		db.close();

		return rule;
	}

	public ArrayList<Rule> getRules() {
		ArrayList<Rule> ruleList = new ArrayList<Rule>();
		SQLiteDatabase db = dbHelper.getWritableDatabase();

		Cursor cursor = db.query(
			DBHelper.TABLE_NAME, // The table to query
			PROJECTION ,		// The columns to return
			null,				// The columns for the WHERE clause
			null,				// The values for the WHERE clause
			null,				// don't group the rows
			null,
			null,// don't filter by row groups
			null				// The sort order
			);
		if(cursor.getCount()==0) {
			return new ArrayList<Rule>();
		}

		if (!cursor.isBeforeFirst()) {
			cursor.moveToFirst();
			cursor.moveToPrevious();
		}

		while (cursor.moveToNext()) {
			int itemId = cursor.getInt(cursor.getColumnIndexOrThrow(Rule.KEY_ID));
			long startTime = cursor.getLong(cursor.getColumnIndexOrThrow(Rule.KEY_STARTDATE));
			long endTime = cursor.getLong(cursor.getColumnIndexOrThrow(Rule.KEY_ENDDATE));
			boolean isActive = Boolean.parseBoolean(cursor.getString(cursor.getColumnIndexOrThrow(Rule.KEY_ACTIVE)));
			int weekDays = cursor.getInt(cursor.getColumnIndexOrThrow(Rule.KEY_WEEKDAYS));
			int mode = cursor.getInt(cursor.getColumnIndexOrThrow(Rule.KEY_MODE));

//			Date startDate = new Date();
//			Date endDate = new Date(endTime);

			Log.d("itemId", ""+itemId);
			Log.d("startTime", ""+startTime);
			Log.d("endTime", ""+endTime);
			Log.d("isActive", ""+isActive);
			Log.d("weekDays", ""+weekDays);
			
			Rule rule = new Rule(itemId, startTime, endTime, weekDays, mode, isActive);
			ruleList.add(rule);
		}
		cursor.close();
		db.close();

		return ruleList;
	}

}
