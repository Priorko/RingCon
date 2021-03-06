package com.example.ringcon;

import java.util.ArrayList;

import android.annotation.SuppressLint;
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
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import com.example.ringcon.sql.DBHelper;
import com.example.ringcon.sql.SQLiteAdapter;
import com.example.ringcon.structure.Rule;
import com.example.ringcon.utils.DateUtils;

public class AddRuleFragment extends DialogFragment {

	ScrollView containerView;
	DBHelper mDbHelper;
	TimePicker startTimePicker;
	TimePicker endTimePicker;
	Spinner sModeSpinner;
	LinearLayout dayContainer;
	ArrayList<ToggleButton> dayList;
	Rule mRule;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	//	setRetainInstance(true);
		setStyle(DialogFragment.STYLE_NO_TITLE, R.style.SoleDialog);
		
		super.onCreate(savedInstanceState);
		
	}

	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRule = null;
		if(getArguments() != null && getArguments().containsKey("rule"))
			mRule = (Rule)getArguments().getSerializable("rule");

		dayList = new ArrayList<ToggleButton>();
		
		containerView = (ScrollView) inflater.inflate(R.layout.dailog_rule, null);
		dayContainer = (LinearLayout) containerView.findViewById(R.id.dayContainer);
		startTimePicker = (TimePicker) containerView.findViewById(R.id.startTime);
		endTimePicker = (TimePicker) containerView.findViewById(R.id.endTime);
		sModeSpinner = (Spinner) containerView.findViewById(R.id.sModeSpinner);
		
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
		
		if(mRule != null)
			initializeData(mRule);

		((Button) containerView.findViewById(R.id.addRule)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int day = 0;
				for(int i=0; i<dayList.size(); i++){
					if(dayList.get(i).isChecked())
						day += (int)Math.pow(2, i);
				}
				
				long startTime = (startTimePicker.getCurrentHour()*60 + startTimePicker.getCurrentMinute())*60*1000;
				long endTime = (endTimePicker.getCurrentHour()*60 + endTimePicker.getCurrentMinute())*60*1000;
				int mode = 0;
				mode = sModeSpinner.getSelectedItemPosition();
				Rule rule = new Rule(startTime, endTime, day, mode, true);
				if(getTag().equals(MainActivity.ADD_RULE)){
					rule.setId(new SQLiteAdapter(getActivity()).addRule(rule));
					if (getActivity() != null && MainActivity.class.isInstance(getActivity())) {
						if (rule.getId() >= 0) {
							rule.setId(rule.getId());
							((MainActivity)getActivity()).setRule(rule);
						}
					}
				}
				else if(getTag().equals(MainActivity.EDIT_RULE)){
					new SQLiteAdapter(getActivity()).editRule(mRule.getId(), rule);
				}
				((MainActivity)getActivity()).refreshList();
				AddRuleFragment.this.dismiss();
			}
		});
		
		((Button) containerView.findViewById(R.id.cancel)).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				((MainActivity)getActivity()).refreshList();
				AddRuleFragment.this.dismiss();
			}
		});
		return containerView;
	}
	
	private void initializeData(Rule rule){
		startTimePicker.setCurrentHour(DateUtils.getHours(rule.getStartDate()));
		startTimePicker.setCurrentMinute(DateUtils.getMinutes(rule.getStartDate()));
		endTimePicker.setCurrentHour(DateUtils.getHours(rule.getFinishDate()));
		endTimePicker.setCurrentMinute(DateUtils.getMinutes(rule.getFinishDate()));
		sModeSpinner.setSelection(rule.getMode());
		ArrayList<Integer> weekDays = new DateUtils(getActivity().getApplicationContext()).getWeekDays(rule.getWeekdays());
		for(int i=0; i<weekDays.size(); i++){
			dayList.get(weekDays.get(i)-1).setChecked(true);
		}
	}
}
