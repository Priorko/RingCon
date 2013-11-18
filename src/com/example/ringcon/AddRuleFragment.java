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

import com.example.ringcon.sql.DBHelper;

public class AddRuleFragment extends DialogFragment {

	ScrollView containerView;
	DBHelper mDbHelper;
	TimePicker startTimePicker;
	TimePicker endTimePicker;
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

		((Button) containerView.findViewById(R.id.addRule)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

//				Calendar startCalendar = new GregorianCalendar(0, 0, 0, startTimePicker.getCurrentHour(), startTimePicker.getCurrentMinute());
//				Calendar endCalendar = new GregorianCalendar(0, 0, 0, endTimePicker.getCurrentHour(), endTimePicker.getCurrentMinute());
				
				SQLiteDatabase db = mDbHelper.getWritableDatabase();
				
				SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm"); 
				Date startDate = new Date(0, 0, 0, startTimePicker.getCurrentHour(), startTimePicker.getCurrentMinute());
				Date endDate = new Date(0, 0, 0, startTimePicker.getCurrentHour(), endTimePicker.getCurrentMinute());
				ContentValues values = new ContentValues();
				Log.d("END DATE", dateFormat.format(endDate));
				values.put(Rule.KEY_STARTDATE, dateFormat.format(startDate));
				values.put(Rule.KEY_ENDDATE, dateFormat.format(endDate));
				values.put(Rule.KEY_ACTIVE, true);
				values.put(Rule.KEY_WEEKDAYS, "smth");
				
				db.insert(DBHelper.TABLE_NAME, null, values);
			}
		});
		return containerView;
	}

}
