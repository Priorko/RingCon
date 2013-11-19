package com.example.ringcon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;

public class MainActivity extends FragmentActivity {

	ListView ruleLv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ruleLv = (ListView) findViewById(R.id.ruleLv);
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
		
		ArrayList<Rule> ruleList = new ArrayList<Rule>();
		cursor.moveToFirst();
		for(int i=0; i<cursor.getCount(); i++){
			long itemId = cursor.getLong(cursor.getColumnIndexOrThrow("id"));
			long startTime = cursor.getLong(cursor.getColumnIndexOrThrow(Rule.KEY_STARTDATE));
			long endTime = cursor.getLong(cursor.getColumnIndexOrThrow(Rule.KEY_ENDDATE));
			boolean isActive = (cursor.getInt(cursor.getColumnIndexOrThrow(Rule.KEY_ACTIVE)) == 1) ? true : false;
			int weekDays = cursor.getInt(cursor.getColumnIndexOrThrow(Rule.KEY_WEEKDAYS));
			
			Date startDate = new Date(startTime);
			Date endDate = new Date(endTime);
			
			ArrayList<Boolean> onDays = new ArrayList<Boolean>();
			while(weekDays > 0){
				if(weekDays % 2 == 0)
					onDays.add(false);
				else
					onDays.add(true);
				weekDays /= 2;
			}
			Log.d("itemId", ""+itemId);
			Log.d("startTime", ""+startDate);
			Log.d("endTime", ""+endDate);
			Log.d("isActive", ""+isActive);
			Log.d("weekDays", ""+weekDays);
			
			Rule rule = new Rule(startDate, endDate, isActive);
			rule.setWeekdays(onDays);
			ruleList.add(rule);
			
			cursor.moveToNext();
		}
		cursor.close();
		
		RulesAdapter ruleAdapter = new RulesAdapter(this, ruleList);
		ruleLv.setAdapter(ruleAdapter);
	}
	
	public void onAddRule(View v) {
		
		Context context = this.getApplicationContext();

    	if (silanceManager != null) {
    		Rule rule = new Rule(1384797600, 1384798900, 63, true);
    		rule.setId(12);
    		silanceManager.setRule(context, rule);
    	} else {
    		Toast.makeText(context, "Crap! Can't do this :(", Toast.LENGTH_SHORT).show();
    	}
//		AddRuleFragment mContent = new AddRuleFragment();
//		mContent.show(getSupportFragmentManager(), "add_rule");
	}
}
