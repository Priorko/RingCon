package com.example.ringcon.sql;

import com.example.ringcon.structure.Rule;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

	public static final String TABLE_NAME = "rules";
	
	private static final String CREATE_TABLE_RULES = "create table rules ("
			+ Rule.KEY_ID + " integer primary key autoincrement, " 
			+ Rule.KEY_STARTDATE + " LONG, "
			+ Rule.KEY_ENDDATE + " LONG, "
			// repeating : int; => Mon = 1, Tue = 2, Wed = 4, Thu = 8 ... 
			// so if repeating = 17, that is Friday & Monday (16 + 1);
			+ Rule.KEY_WEEKDAYS + " INTEGER, "
			+ Rule.KEY_ACTIVE + " BOOL" + ");";
	
	public static final String DB_NAME = "ringcon";
	public static final int DB_VERSION = 1;

	public DBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_RULES);
	}

	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}
}
