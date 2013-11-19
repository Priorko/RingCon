package com.example.ringcon;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ListView;

import com.example.ringcon.sql.SQLiteAdapter;
import com.example.ringcon.structure.Rule;

public class MainActivity extends FragmentActivity {

	ListView ruleLv;
	SQLiteAdapter sqliteAdapter;
	SilanceManagerReceiver silanceManager;
	RulesAdapter ruleAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		sqliteAdapter = new SQLiteAdapter(this);
		silanceManager = new SilanceManagerReceiver();
		ruleLv = (ListView) findViewById(R.id.ruleLv);
		
		refreshList();
	}

	public void onAddRule(View v) {
		
//		Context context = this.getApplicationContext();
//
//    	if (silanceManager != null) {
//    		Rule rule = new Rule(1384797600, 1384798900, 63, true);
//    		rule.setId(12);
//    		silanceManager.setRule(context, rule);
//    	} else {
//    		Toast.makeText(context, "Crap! Can't do this :(", Toast.LENGTH_SHORT).show();
//    	}
		AddRuleFragment mContent = new AddRuleFragment();
		mContent.show(getSupportFragmentManager(), "add_rule");
	}

	public void refreshList() {
		ArrayList<Rule> ruleList = sqliteAdapter.getRules();
		ruleAdapter = new RulesAdapter(this, ruleList);
		ruleLv.setAdapter(ruleAdapter);
	}

	public void onRemoveRule(View v) {
		Rule rule = (Rule) v.getTag();
		silanceManager.cancelAlarm(this.getApplicationContext(), rule);
		boolean oo = sqliteAdapter.removeRule(rule.getId());
		refreshList();
		
	}

	public void setRule(Rule rule) {
		if (silanceManager != null) {
			silanceManager.setRule(this.getApplicationContext(), rule);
		}
	}
}
