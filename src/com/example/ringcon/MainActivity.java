package com.example.ringcon;

import java.util.ArrayList;
import java.util.Arrays;

import structure.Rule;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import java.util.Date;
import android.view.View;
import android.widget.ListView;

public class MainActivity extends FragmentActivity {

	Rule[] rules ={new Rule(new Date(), new Date(), true),new Rule(new Date(), new Date(), true),new Rule(new Date(), new Date(), true),};
	ListView ruleList;
	RulesAdapter ruleAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		ArrayList<Rule> assetList = new ArrayList<Rule>(Arrays.asList(rules));
		ruleAdapter = new RulesAdapter(this, assetList);

		ruleList = (ListView) findViewById(R.id.ruleList);
		ruleList.setAdapter(ruleAdapter);

	}

	public void onAddRule(View v) {
		AddRuleFragment mContent = new AddRuleFragment();
		mContent.show(getSupportFragmentManager(), "add_rule");
	}
}
