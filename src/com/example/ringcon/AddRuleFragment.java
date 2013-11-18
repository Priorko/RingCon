package com.example.ringcon;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import structure.Rule;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import com.example.ringcon.sql.DBHelper;

public class AddRuleFragment extends DialogFragment {

	ScrollView containerView;
	DBHelper mDbHelper;
	TimePicker startTimePicker;
	TimePicker endTimePicker;
	
	ToggleButton monTb;
	ToggleButton tueTb;
	ToggleButton wedTb;
	ToggleButton thuTb;
	ToggleButton friTb;
	ToggleButton satTb;
	ToggleButton sunTb;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
//		setRetainInstance(true);
//		setStyle(DialogFragment.STYLE_NO_TITLE, R.style.SoleDialog);
		
		super.onCreate(savedInstanceState);
		mDbHelper = new DBHelper(getActivity());
	}

	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		containerView = (ScrollView) inflater.inflate(R.layout.dailog_add_rule, null);
		
		startTimePicker = (TimePicker) containerView.findViewById(R.id.startTime);
		endTimePicker = (TimePicker) containerView.findViewById(R.id.endTime);
		
		monTb = (ToggleButton) containerView.findViewById(R.id.tbtnMon);
		tueTb = (ToggleButton) containerView.findViewById(R.id.tbtnTue);
		wedTb = (ToggleButton) containerView.findViewById(R.id.tbtnWed);
		thuTb = (ToggleButton) containerView.findViewById(R.id.tbtnThu);
		friTb = (ToggleButton) containerView.findViewById(R.id.tbtnFri);
		satTb = (ToggleButton) containerView.findViewById(R.id.tbtnSat);
		sunTb = (ToggleButton) containerView.findViewById(R.id.tbtnSun);
		
		startTimePicker.setIs24HourView(true);
		endTimePicker.setIs24HourView(true);

		((Button) containerView.findViewById(R.id.addRule)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String days = "";
				if(monTb.isChecked())
					days += "mon,";
				if(tueTb.isChecked())
					days += "tue,";
				if(wedTb.isChecked())
					days += "wed,";
				if(thuTb.isChecked())
					days += "thu,";
				if(friTb.isChecked())
					days += "fri,";
				if(satTb.isChecked())
					days += "sat,";
				if(sunTb.isChecked())
					days += "sun,";
				if(days.length()>0)
					days = days.substring(0, days.length()-1);
				Log.d("DAYS", days);
				SQLiteDatabase db = mDbHelper.getWritableDatabase();
				
				SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm"); 
				Date startDate = new Date(0, 0, 0, startTimePicker.getCurrentHour(), startTimePicker.getCurrentMinute());
				Date endDate = new Date(0, 0, 0, endTimePicker.getCurrentHour(), endTimePicker.getCurrentMinute());
				ContentValues values = new ContentValues();
				values.put(Rule.KEY_STARTDATE, dateFormat.format(startDate));
				values.put(Rule.KEY_ENDDATE, dateFormat.format(endDate));
				values.put(Rule.KEY_ACTIVE, true);
				values.put(Rule.KEY_WEEKDAYS, days);
				
				db.insert(DBHelper.TABLE_NAME, null, values);
			}
		});
		return containerView;
	}

}
