package com.example.ringcon;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.ringcon.sql.SQLiteAdapter;
import com.example.ringcon.structure.Rule;

public class MainActivity extends FragmentActivity {

	public final static String ADD_RULE = "add_rule";
	public final static String EDIT_RULE = "edit_rule";
	ListView ruleLv;
	SilenceManagerReceiver silanceManager;
	SQLiteAdapter sqliteAdapter;
	RulesAdapter ruleAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		sqliteAdapter = new SQLiteAdapter(this);
		silanceManager = new SilenceManagerReceiver();
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
		mContent.show(getSupportFragmentManager(), ADD_RULE);

	}

	public void refreshList() {
		final ArrayList<Rule> ruleList = new SQLiteAdapter(this).getRules();
		RulesAdapter ruleAdapter = new RulesAdapter(this, ruleList);
		ruleAdapter = new RulesAdapter(this, ruleList);
		ruleLv.setAdapter(ruleAdapter);
		ruleLv.setEnabled(true);
		ruleLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, final View view, 
					int position, long id) {
				AddRuleFragment mContent = new AddRuleFragment();
				Rule rule = (Rule)view.getTag();
				Bundle b = new Bundle();
				b.putSerializable("rule", rule);
				mContent.setArguments(b);
				mContent.show(getSupportFragmentManager(), EDIT_RULE);
			}
		});
	}
	
	public void onDelete(View v) {
		Rule rule = (Rule) v.getTag();
		silanceManager.cancelAlarm(this.getApplicationContext(), rule);
		new SQLiteAdapter(this).removeRule(rule.getId());
		refreshList();
	}

//	public void onRemoveRule(View v) {
//		Rule rule = (Rule) v.getTag();
//		silanceManager.cancelAlarm(this.getApplicationContext(), rule);
//		boolean oo = sqliteAdapter.removeRule(rule.getId());
//		refreshList();
//		
//	}

	public void setRule(Rule rule) {
		if (silanceManager != null) {
			silanceManager.setRule(this.getApplicationContext(), rule);
		}
	}
}
