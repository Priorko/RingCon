package com.example.ringcon.sql;

import structure.Rule;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class SQLiteAdapter {

	private DBHelper dbHelper;

	private Context mContext;

	public SQLiteAdapter(Context context) {
		mContext = context;
		dbHelper = new DBHelper(context);
	}

	public boolean AddRule(Rule rule) {
		ContentValues cv = new ContentValues();

		SQLiteDatabase db = dbHelper.getWritableDatabase();

		cv.put(Rule.KEY_STARTDATE, "");
		cv.put(Rule.KEY_ENDDATE, "");
		cv.put(Rule.KEY_WEEKDAYS, "");
		cv.put(Rule.KEY_ACTIVE, "");
		long rowID = db.insert("mytable", null, cv);
		
		return rowID >= 0;
	}

	public boolean removeRule(int id) {
		return true;
	}

	public boolean editRule(int id, Rule rule) {
		return true;
	}

}