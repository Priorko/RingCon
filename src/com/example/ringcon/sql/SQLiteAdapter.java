package com.example.ringcon.sql;

import java.util.ArrayList;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.ringcon.structure.Rule;

public class SQLiteAdapter {

	private DBHelper dbHelper;

	private Context mContext;

	public SQLiteAdapter(Context context) {
		mContext = context;
		dbHelper = new DBHelper(context);
	}

	public boolean AddRule(Rule rule) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		return db.insert(DBHelper.TABLE_NAME, null, rule.getCV()) >= 0;
	}

	public boolean removeRule(int id) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		return db.delete(DBHelper.TABLE_NAME, Rule.KEY_ID + "=" + id, null) >= 0;
	}

	public boolean editRule(int id, Rule rule) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		return db.update(DBHelper.TABLE_NAME, rule.getCV(), Rule.KEY_ID + "=" + rule.getId(), null) >= 0;
	}

	public ArrayList<Rule> getRules() {
		
		
		return null;
	}

}