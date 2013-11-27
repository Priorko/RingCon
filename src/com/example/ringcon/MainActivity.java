package com.example.ringcon;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ringcon.sql.SQLiteAdapter;
import com.example.ringcon.structure.Rule;

public class MainActivity extends ActionBarActivity {

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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
			case R.id.refresh:
				refreshList();
				break;
			case R.id.addRule:
				AddRuleFragment mContent = new AddRuleFragment();
				mContent.show(getSupportFragmentManager(), ADD_RULE);
				break;
		}
		return super.onOptionsItemSelected(item);
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
		
		TextView noRuleTv = (TextView) findViewById(R.id.noRuleTv);
		//noRuleTv.setText(Html.fromHtml(getResources().getString(R.string.no_rule)));
		if(ruleAdapter.getCount() == 0)
			noRuleTv.setVisibility(View.VISIBLE);
		else
			noRuleTv.setVisibility(View.INVISIBLE);
	}
	
	public void onDelete(View v) {
		Rule rule = (Rule) v.getTag();
		silanceManager.cancelAlarm(this.getApplicationContext(), rule);
		new SQLiteAdapter(this).removeRule(rule.getId());
		refreshList();
	}
	public void onUnset(View v) {
		Rule rule = (Rule) v.getTag();
		silanceManager.cancelAlarm(this.getApplicationContext(), rule);
		
		new SQLiteAdapter(this).removeRule(rule.getId());
		
		refreshList();
	}

	public void setRule(Rule rule) {
		if (silanceManager != null) {
			silanceManager.setRule(this.getApplicationContext(), rule);
		}
	}
}
