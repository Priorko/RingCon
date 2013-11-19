package com.example.ringcon;

import java.util.ArrayList;

import structure.Rule;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import com.example.ringcon.sql.DBHelper;

public class AddRuleFragment extends DialogFragment {

	ScrollView containerView;
	DBHelper mDbHelper;
	TimePicker startTimePicker;
	TimePicker endTimePicker;
	
	LinearLayout dayContainer;
	ArrayList<ToggleButton> dayList;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
//		setRetainInstance(true);
//		setStyle(DialogFragment.STYLE_NO_TITLE, R.style.SoleDialog);
		
		super.onCreate(savedInstanceState);
		
	}

	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		containerView = (ScrollView) inflater.inflate(R.layout.dailog_add_rule, null);
		dayContainer = (LinearLayout) containerView.findViewById(R.id.dayContainer);
		dayList = new ArrayList<ToggleButton>();
		
		startTimePicker = (TimePicker) containerView.findViewById(R.id.startTime);
		endTimePicker = (TimePicker) containerView.findViewById(R.id.endTime);
		
		String[] days = getResources().getStringArray(R.array.days);
		for(int i=0; i<days.length; i++){
			ToggleButton tb = new ToggleButton(getActivity());
			tb.setLayoutParams(new TableLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 1f));
			tb.setText(days[i]);
			tb.setTextOff(days[i]);
			tb.setTextOn(days[i]);
			tb.setPadding(2, 0, 2, 0);
			dayContainer.addView(tb);
			dayList.add(tb);
		}
		
		startTimePicker.setIs24HourView(true);
		endTimePicker.setIs24HourView(true);

		((Button) containerView.findViewById(R.id.addRule)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int day = 0;
				for(int i=0; i<dayList.size(); i++){
					if(dayList.get(i).isChecked())
						day += (int)Math.pow(2.0, i);
				}
				mDbHelper = new DBHelper(getActivity());
				SQLiteDatabase db = mDbHelper.getWritableDatabase();
				
				long startTime = (startTimePicker.getCurrentHour()*60 + startTimePicker.getCurrentMinute())*60*1000;
				long endTime = (endTimePicker.getCurrentHour()*60 + endTimePicker.getCurrentMinute())*60*1000;
				
				ContentValues values = new ContentValues();
				values.put(Rule.KEY_STARTDATE, startTime);
				values.put(Rule.KEY_ENDDATE, endTime);
				values.put(Rule.KEY_ACTIVE, true);
				values.put(Rule.KEY_WEEKDAYS, day);
				
				db.insert(DBHelper.TABLE_NAME, null, values);
				db.close();
				mDbHelper.close();
				dismiss();
			}
		});
		return containerView;
	}
}
