package com.example.ringcon;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ringcon.sql.SQLiteAdapter;
import com.example.ringcon.structure.Rule;
import com.example.ringcon.utils.DateUtils;

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
		view.setTag(rule);
		DateUtils utils = new DateUtils(ctx);
		((TextView) view.findViewById(R.id.startDate)).setText(utils.getTime(rule.getStartDate()));
		((TextView) view.findViewById(R.id.finishDate)).setText(utils.getTime(rule.getFinishDate()));
		String sDays = utils.getDays(rule.getWeekdays());
		((TextView) view.findViewById(R.id.weekDays)).setText(sDays.length() == 0 ? 
				ctx.getResources().getString(R.string.no_days) : sDays);
		((ImageView)view.findViewById(R.id.removeRule)).setTag(rule);
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
			Rule rule = getRule((Integer) buttonView.getTag());
			rule.setActive(isChecked);
			new SQLiteAdapter(ctx).editRule(rule.getId(), rule);

			if (RulesAdapter.this.ctx != null && MainActivity.class.isInstance(RulesAdapter.this.ctx)) {
				if (rule.getId() >= 0) {
					rule.setId(rule.getId());
					((MainActivity)RulesAdapter.this.ctx).setRule(rule);
				}
			}
		}
	};

}

