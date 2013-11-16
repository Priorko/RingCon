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

	// кол-во элементов
	@Override
	public int getCount() {
		return objects.size();
	}

	// элемент по позиции
	@Override
	public Object getItem(int position) {
		return objects.get(position);
	}

	// id по позиции
	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// используем созданные, но не используемые view
		View view = convertView;
		if (view == null) {
			view = lInflater.inflate(R.layout.list_item, parent, false);
		}

		Rule r = getRule(position);

		SimpleDateFormat simpleDate = new SimpleDateFormat("dd/MM/yyyy");

		((TextView) view.findViewById(R.id.startDate)).setText(simpleDate
				.format(r.getStartDate()));
		((TextView) view.findViewById(R.id.finishDate)).setText(simpleDate
				.format(r.getFinishDate()));
		CheckBox cbBuy = (CheckBox) view.findViewById(R.id.isActive);
		cbBuy.setOnCheckedChangeListener(myCheckChangList);
		cbBuy.setTag(position);
		cbBuy.setChecked(r.isActive());
		return view;
	}

	// товар по позиции
	Rule getRule(int position) {
		return ((Rule) getItem(position));
	}

	// обработчик для чекбоксов
	OnCheckedChangeListener myCheckChangList = new OnCheckedChangeListener() {
		public void onCheckedChanged(CompoundButton buttonView,
				boolean isChecked) {
			// меняем данные товара (в корзине или нет)
			getRule((Integer) buttonView.getTag()).setActive(isChecked);
		}
	};

}