package com.example.ringcon;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import structure.Rule;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class RulesAdapter extends BaseAdapter {
	Context ctx;
	LayoutInflater lInflater;
	ArrayList<Rule> objects;

	RulesAdapter(Context context, ArrayList<Rule> rules) {
		ctx = context;
		objects = rules;
		lInflater = (LayoutInflater) ctx
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return objects.size();
	}

	@Override
	public Object getItem(int position) {
		return objects.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			view = lInflater.inflate(R.layout.list_item, parent, false);
		}

		Rule rule = getRule(position);

		SimpleDateFormat simpleDate = new SimpleDateFormat("HH:mm");
		

		((TextView) view.findViewById(R.id.startDate)).setText(simpleDate
				.format(rule.getStartDate()));
		((TextView) view.findViewById(R.id.finishDate)).setText(simpleDate
				.format(rule.getFinishDate()));
		CheckBox cbBuy = (CheckBox) view.findViewById(R.id.isActive);
		cbBuy.setOnCheckedChangeListener(myCheckChangList);
		cbBuy.setTag(position);
		cbBuy.setChecked(rule.isActive());
		return view;
	}

	Rule getRule(int position) {
		return ((Rule) getItem(position));
	}
	
	OnCheckedChangeListener myCheckChangList = new OnCheckedChangeListener() {
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			getRule((Integer) buttonView.getTag()).setActive(isChecked);
		}
	};

}