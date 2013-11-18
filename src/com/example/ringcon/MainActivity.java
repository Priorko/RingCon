package com.example.ringcon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import structure.Rule;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.example.ringcon.sql.DBHelper;

public class MainActivity extends FragmentActivity {

	Rule[] rules = {new Rule(new Date(), new Date(), true),new Rule(new Date(), new Date(), true),new Rule(new Date(), new Date(), true)};
	ListView ruleList;
	RulesAdapter ruleAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		ArrayList<Rule> assetList = new ArrayList<Rule>(Arrays.asList(rules));
		ruleAdapter = new RulesAdapter(this, assetList);

		ruleList = (ListView) findViewById(R.id.ruleList);
		ruleList.setAdapter(ruleAdapter);

		getDataFromDB();
	}

	private void getDataFromDB(){
		DBHelper mDbHelper = new DBHelper(this);
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		String[] projection = {
			"id",
			Rule.KEY_STARTDATE,
			Rule.KEY_ENDDATE,
			Rule.KEY_ACTIVE,
			Rule.KEY_WEEKDAYS
		};
		Cursor cursor = db.query(
			mDbHelper.TABLE_NAME,  // The table to query
			projection ,		// The columns to return
			null,				// The columns for the WHERE clause
			null,				// The values for the WHERE clause
			null,				// don't group the rows
			null,				// don't filter by row groups
			null				// The sort order
			);
		if(cursor.getCount()==0)
			return;
		cursor.moveToFirst();
		for(int i=0; i<cursor.getCount(); i++){
			long itemId = cursor.getLong(cursor.getColumnIndexOrThrow("id"));
			long startTime = cursor.getLong(cursor.getColumnIndexOrThrow(Rule.KEY_STARTDATE));
			long endTime = cursor.getLong(cursor.getColumnIndexOrThrow(Rule.KEY_ENDDATE));
			boolean isActive = (cursor.getInt(cursor.getColumnIndexOrThrow(Rule.KEY_ACTIVE)) == 1) ? true : false;
			int weekDays = cursor.getInt(cursor.getColumnIndexOrThrow(Rule.KEY_WEEKDAYS));
			Log.d("itemId", ""+itemId);
			Log.d("startTime", ""+startTime);
			Log.d("endTime", ""+endTime);
			Log.d("isActive", ""+isActive);
			Log.d("weekDays", ""+weekDays);
			cursor.moveToNext();
		}
		cursor.close();
	}
	
	public void onAddRule(View v) {
		AddRuleFragment mContent = new AddRuleFragment();
		mContent.show(getSupportFragmentManager(), "add_rule");
	}
}
